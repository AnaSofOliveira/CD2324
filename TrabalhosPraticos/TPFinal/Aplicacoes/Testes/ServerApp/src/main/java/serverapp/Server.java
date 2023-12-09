package serverapp;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.rabbitmq.client.*;
import geststubs.Categoria;
import geststubs.Ficheiro;
import geststubs.GestServiceGrpc;
import geststubs.Informacoes;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import serverapp.spreadMessages.SMessage;
import serverapp.spreadMessages.SMessageValues;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


public class Server extends GestServiceGrpc.GestServiceImplBase {

    // Server Configs
    private static int svcPort = 8500;


    // Spread Configs
    private static String spreadDeamonIP = "localhost";
    private static int spreadDeamonPort = 4803;
    private static SpreadConnection spreadCon;
    private static String spreadGroupName = "workers";


    // RabbitMQ Configs
    private static String rmqBrokerIP = "34.28.139.32"; /* TODO alterar IP*/
    private static String nome_exchange = "ex_" + UUID.randomUUID();
    private static String nome_queue = "queue_" + nome_exchange;
    private static String routing_key = "";
    private static Connection rmqCon;
    private static Channel rmqChannel;

    private HashMap<Long, String> filesToDownload = new HashMap<>();


    public static void main(String[] args) {
        try {
            if (args.length > 0){
                svcPort = Integer.parseInt(args[0]);
                spreadDeamonIP = args[1];
                rmqBrokerIP = args[2];
            }

            io.grpc.Server svc = ServerBuilder
                .forPort(svcPort)
                .addService(new Server())
                .build();
            svc.start();
            System.out.println("Server started, listening on " + svc);
            //Scanner scan = new Scanner(System.in);
            //scan.nextLine();

            connectToSpread();

            svc.awaitTermination();
            svc.shutdown();

        } catch (IOException | InterruptedException e) {
            System.out.println("Não foi possível iniciar o Servidor: " + e.getMessage());
        }catch (SpreadException e) {
            System.out.println("Não foi possível estabelecer a ligação com o Spread Deamon: " + e.getMessage());
        }

    }

    private static void closeRMQ() {
        try {
            rmqChannel.close();
            rmqCon.close();
        } catch (Exception e) {
            System.out.println("Erro ao encerrar canal RMQ.");
        }
    }

    private static void connectToSpread() throws UnknownHostException, SpreadException {
        spreadCon = new SpreadConnection();
        spreadCon.connect(InetAddress.getByName(spreadDeamonIP), spreadDeamonPort, "serverApp", false, true);
        System.out.println("connected to "+ spreadDeamonIP);
    }

    private static void createRabbitMQStructure() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rmqBrokerIP);
        factory.setPort(5672);
        rmqCon = factory.newConnection();
        rmqChannel = rmqCon.createChannel();

        rmqChannel.exchangeDeclare(nome_exchange, BuiltinExchangeType.FANOUT, true);
        rmqChannel.queueDeclare(nome_queue, true, false, false, null);
        rmqChannel.queueBind(nome_queue, nome_exchange, routing_key);
    }

    public static void deleteRabbitMQStructure() throws IOException {
        rmqChannel.queueUnbind(nome_exchange, nome_queue, routing_key);
        rmqChannel.queueDelete(nome_queue);
        rmqChannel.exchangeDelete(nome_exchange);
    }

    /**
     * Operações do Server
     **/
    @Override
    public void agruparVendas(Categoria request, StreamObserver<Informacoes> responseObserver) {
        System.out.println("Vou fazer a sintese. ");

        String tipo = request.getTipo().toLowerCase();
        if (tipo.equals("alimentar") || tipo.equals("casa")) {

            try {
                SMessage msg = new SMessage(0, SMessageValues.RESUME, nome_exchange, tipo);

                // Enviar mensagem em multicast para Spread Group
                SpreadMessage message = new SpreadMessage();
                message.addGroup(spreadGroupName);
                message.setData(new Gson().toJson(msg).getBytes());

                spreadCon.multicast(message);

                createRabbitMQStructure();

                // Aguardar resposta na queue associada a esse exchange
                connectToRabbitMQAsConsumer(responseObserver);

            } catch (Exception e) {
                System.out.println("Erro ao enviar a mensagem para o Spread Group: " + e.getMessage());
            }


        }else{
            responseObserver.onError(new Throwable("Tipo de Venda não suportado"));
        }
    }

    @Override
    public void obterFicheiro(Informacoes request, StreamObserver<Ficheiro> responseObserver) {

        String locFicheiro = this.filesToDownload.get(Long.parseLong(request.getHash()));
        String[] split = locFicheiro.split("/");
        String nomeFicheiro = split[split.length-1];

        System.out.println("Nome Ficheiro: " + locFicheiro);

        try (FileInputStream fileInputStream = new FileInputStream(locFicheiro)) {
            byte[] buffer = new byte[32 * 1024]; // 32 kB buffer size

            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                Ficheiro ficheiro = Ficheiro.newBuilder().setHash(request.getHash()).setNomeFicheiro(nomeFicheiro).setBlocos(ByteString.copyFrom(buffer, 0, bytesRead)).build();
                responseObserver.onNext(ficheiro);
            }
            responseObserver.onCompleted();

        } catch (IOException e) {
            System.out.println("Erro no envio da imagem para o cliente: " + e.getMessage());
        }
    }


    /**
     * Operações do RabbitMQ
     **/

    private void connectToRabbitMQAsConsumer(StreamObserver<Informacoes> responseObserver) {

        GestorMensagens gestorMensagens = new GestorMensagens(rmqChannel, responseObserver, this.filesToDownload);

        try {
            String consumerTag = rmqChannel.basicConsume(nome_queue, false, gestorMensagens, gestorMensagens);

        } catch (IOException e) {
            System.out.println("Erro ao consumir mensagem");
        }

    }


    /**
     * Operações do Spread
     **/
}
