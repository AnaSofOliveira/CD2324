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
    private static int svcPort;


    // Spread Configs
    private static String spreadDeamonIP;
    private static int spreadDeamonPort;
    private static SpreadConnection spreadCon;
    private final static String spreadGroupName = "workers";


    // RabbitMQ Configs
    private static String rmqBrokerIP;
    private static int rmqBrokerPort;
    private static String nome_exchange;
    private static String nome_queue;
    private final static String routing_key = "";
    private static Connection rmqCon;
    private static Channel rmqChannel;

    private final HashMap<Long, String> filesToDownload = new HashMap<>();

    public static void main(String[] args) {
        try {
            if (args.length > 0){
                svcPort = Integer.parseInt(args[0]);

                rmqBrokerIP = args[1];
                rmqBrokerPort = Integer.parseInt(args[2]);

                spreadDeamonIP = args[3];
                spreadDeamonPort = Integer.parseInt(args[4]);
            }

            UUID id = UUID.randomUUID();
            nome_exchange = "ex_" + id;
            nome_queue = "queue_" + id;

            io.grpc.Server svc = ServerBuilder
                .forPort(svcPort)
                .addService(new Server())
                .build();
            svc.start();
            System.out.println("Servidor iniciado e a aguardar pedidos");

            connectToSpread();

            svc.awaitTermination();
            svc.shutdown();

        } catch (IOException | InterruptedException e) {
            System.out.println("Não foi possível iniciar o Servidor: " + e.getMessage());
        }catch (SpreadException e) {
            System.out.println("Não foi possível estabelecer a ligação com o Spread Deamon: " + e.getMessage());
        }

    }


    /**
     * Operações do Servidor
     **/
    @Override
    public void agruparVendas(Categoria request, StreamObserver<Informacoes> responseObserver) {

        String tipo = request.getTipo().toLowerCase();
        if (tipo.equals("alimentar") || tipo.equals("casa")) {

            try {
                System.out.println("A enviar pedido de resumo para o Spread Group. ");
                SMessage msg = new SMessage(0, SMessageValues.RESUME, nome_exchange, tipo);

                SpreadMessage message = new SpreadMessage();
                message.addGroup(spreadGroupName);
                message.setData(new Gson().toJson(msg).getBytes());

                spreadCon.multicast(message);

                createRabbitMQStructure();

                System.out.println("A aguardar construção do resumo.");
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
        System.out.println("A obter ficheiro.");

        String locFicheiro = this.filesToDownload.get(Long.parseLong(request.getHash()));
        String[] split = locFicheiro.split("/");
        String nomeFicheiro = split[split.length-1];

        try (FileInputStream fileInputStream = new FileInputStream(locFicheiro)) {
            byte[] buffer = new byte[32 * 1024];

            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                Ficheiro ficheiro = Ficheiro.newBuilder().setHash(request.getHash()).setNomeFicheiro(nomeFicheiro).setBlocos(ByteString.copyFrom(buffer, 0, bytesRead)).build();
                System.out.println("A enviar ficheiro ao cliente.");
                responseObserver.onNext(ficheiro);
            }
            System.out.println("A enviar ficheiro ao cliente.");
            responseObserver.onCompleted();

        } catch (IOException e) {
            System.out.println("Erro no envio da imagem para o cliente: " + e.getMessage());
        }
    }


    /**
     * Operações do RabbitMQ
     **/
    private static void createRabbitMQStructure() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rmqBrokerIP);
        factory.setPort(rmqBrokerPort); // 5672
        rmqCon = factory.newConnection();
        rmqChannel = rmqCon.createChannel();

        rmqChannel.exchangeDeclare(nome_exchange, BuiltinExchangeType.FANOUT, true);
        rmqChannel.queueDeclare(nome_queue, true, false, false, null);
        rmqChannel.queueBind(nome_queue, nome_exchange, routing_key);
    }

    private void connectToRabbitMQAsConsumer(StreamObserver<Informacoes> responseObserver) {

        GestorMensagens gestorMensagens = new GestorMensagens(rmqChannel, nome_exchange, nome_queue, responseObserver, this.filesToDownload);

        try {
            rmqChannel.basicConsume(nome_queue, false, gestorMensagens, gestorMensagens);

        } catch (IOException e) {
            System.out.println("Erro ao consumir mensagem");
        }

    }


    /**
     * Operações do Spread
     **/
    private static void connectToSpread() throws UnknownHostException, SpreadException {
        spreadCon = new SpreadConnection();
        spreadCon.connect(InetAddress.getByName(spreadDeamonIP), spreadDeamonPort, "serverApp", false, true);
        System.out.println("Conectado ao Deamon Spread " + spreadDeamonIP + ":" + spreadDeamonPort);
    }

}
