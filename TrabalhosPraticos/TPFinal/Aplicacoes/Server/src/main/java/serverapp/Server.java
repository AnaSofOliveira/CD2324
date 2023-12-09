package serverapp;

import com.rabbitmq.client.*;
import gestorstubs.GestorServiceGrpc;
import gestorstubs.Path;
import gestorstubs.File;
import gestorstubs.Void;
import io.grpc.ManagedChannel;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Server extends GestorServiceGrpc.GestorServiceImplBase {

    // gRPC configurations
    private static int srvPort = 7500;
    private static String srvIP = "localhost";

    // RabbitMQ Consumer Configuratios
    static String IP_BROKER = "34.69.29.101"; /* TODO alterar IP*/
    private static String queueName = "resumo";

    private static ManagedChannel channel;

    private static String daemonSpreadIP="localhost";

    private static int daemonPort=4803;
    private final String groupToSend = "workersGroup";

    private static SpreadConnection connection;

    public static void main(String[] args) {

        if (args.length > 0) {
            srvPort = Integer.parseInt(args[0]);
            //daemonSpreadIP=args[1];
        }

        // Inicialização do Servidor
        try {
            io.grpc.Server svc = ServerBuilder.forPort(srvPort).build();
            svc.start();
            System.out.println("Server started, listening on " + srvPort);
            svc.awaitTermination();
            svc.shutdown();

        } catch (Exception ex) {
            System.out.println("Erro ao iniciar o servidor.");
        }

        // Configuração Spread
        /**try {
            connectToSpreadDeamon();
        } catch (UnknownHostException e) {
            System.out.println("Spread Deamon IP desconhecido.");
            System.exit(0);
        } catch (SpreadException e) {
            System.out.println("Erro ao conectar ao Spread Deamon.");
            System.exit(0);
        }

        // Configuração do RabbitMQ
        connectToRabbitMQAsConsumer();**/

    }

    private static void connectToSpreadDeamon() throws UnknownHostException, SpreadException {
        connection = new SpreadConnection();
        connection.connect(InetAddress.getByName(daemonSpreadIP), daemonPort, "serverApp", false, true);
        System.out.println("connected to "+ daemonSpreadIP);
    }




    private static void connectToRabbitMQAsConsumer() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(IP_BROKER);
            factory.setPort(5672);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            // Consumer handler to receive cancel receiving messages
            CancelCallback cancelCallback = (consumerTag) -> {
                System.out.println("CANCEL Received! " + consumerTag);
            };

            DeliverCallback deliverCallbackWithoutAck = (consumerTag, delivery) -> {
                String recMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
                String routingKey = delivery.getEnvelope().getRoutingKey();
                long deliverTag = delivery.getEnvelope().getDeliveryTag();
                System.out.println(consumerTag + ": Message Received:" + routingKey + ":" + recMessage);

                if (recMessage.equals("nack"))
                    channel.basicNack(deliverTag, false, true);
                else channel.basicAck(deliverTag, false);
            };
            String consumerTag = channel.basicConsume(queueName, false, deliverCallbackWithoutAck, cancelCallback);

        } catch (Exception ex) {
            System.out.println("Erro ao estabelecer ligação com RabbitMQ. ");
        }

    }

    @Override
    public void resumo(Void request, StreamObserver<Path> responseObserver) {
        System.out.println("Recebi um pedido de resume. ");
        try {
            sendMessageToSpreadGroup();
        } catch (SpreadException e) {
            System.out.println("Erro ao enviar mensagem para grupo. ");
        }
        /**
         * TODO:
         * 1. Enviar mensagem para SpreadGroup
         *      - No SpreadGroup é iniciada uma votação para saber qual dos Workers vai processar o pedido
         *      - Leader envia mensagem para RabbitMQ quando terminar trabalho
         * [Feito] 2. Preparar recepção da mensagem do líder (consumir mensagem)
         *      [Feito] - Escolher nome do exchange - resumo
         *      [Feito] - Escolher configuração da queue - Ver ficheiro "RabbitMQ esquema.png" na root do projeto
         */
    }

    @Override
    public void decarregar(Path request, StreamObserver<File> responseObserver) {
        System.out.println("Download");
        /**
         * TODO:
         * 1. Identificar, através do ID, qual o ficheiro a descarregar.
         * 2. Ir buscar o ficheiro ao file system
         * 3. Enviar o ficheiro em blocos para o cliente
         */
    }

    private void sendMessageToSpreadGroup() throws SpreadException {
        SpreadMessage msg = new SpreadMessage();
        msg.setSafe();
        msg.addGroup(groupToSend);

        String txtMessage=readline("resume");
        msg.setData(txtMessage.getBytes());

        connection.multicast(msg);
    }



    private static String readline(String msg) {
        Scanner scaninput = new Scanner(System.in);
        System.out.println(msg);
        return scaninput.nextLine();
    }


}
