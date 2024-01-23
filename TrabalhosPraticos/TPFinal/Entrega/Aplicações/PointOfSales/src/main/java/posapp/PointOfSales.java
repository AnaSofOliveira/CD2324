package posapp;


import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;

import java.util.*;

public class PointOfSales {

    private static String rmqBrokerIP;
    private static int rmqBrokerPort;
    private final static String exchangeName = "ExgSales";
    static boolean exit = false;

    public static void main(String[] args) {
        if (args.length > 0) {
            rmqBrokerIP = args[0];
            rmqBrokerPort = Integer.parseInt(args[1]);
        }

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rmqBrokerIP); factory.setPort(rmqBrokerPort); //5672

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //channel.addReturnListener(new ReturnedMessage());

            do{
                // Send message to exchange
                String categoria = readline("Qual a categoria do produto? Alimentar (a) ou Casa (c).");
                if (categoria.compareTo("exit") == 0) exit=false;
                categoria = categoria.equals("a") ? "alimentar" : categoria.equals("c") ? "casa" : "";

                if(categoria.compareTo("") != 0) {
                    String produto = readline("Qual o produto?");
                    if (produto.compareTo("exit") == 0) exit = false;

                    PoSMessage posMessage = new PoSMessage(produto, categoria);
                    String jsonMessage = new GsonBuilder().create().toJson(posMessage);

                    channel.basicPublish(exchangeName, categoria, true, null, jsonMessage.getBytes());
                    System.out.println("Enviei mensagem:" + jsonMessage + " para ser registado.");
                }
            }while (!exit);

            channel.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("Erro ao estabelecer ligação com RabbitMQ.");
        }
    }

    private static String readline(String msg) {
        Scanner scaninput = new Scanner(System.in);
        System.out.println(msg);
        return scaninput.nextLine();
    }

}