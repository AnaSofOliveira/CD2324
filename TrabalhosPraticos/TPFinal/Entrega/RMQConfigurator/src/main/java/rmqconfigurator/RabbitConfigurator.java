package rmqconfigurator;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.simple.SimpleLoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class RabbitConfigurator {
    static String IP_BROKER;
    static Connection connection = null;
    static Channel channel = null;
    private static String exchange_name = "ExgSales";

    public static void main(String[] args) {

        if (args.length > 0) {
            IP_BROKER = args[0];
        }

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(IP_BROKER);
            factory.setPort(5672);

            connection = factory.newConnection();
            channel = connection.createChannel();

            // Add "vendas" exchange
            channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC, true);

            // Add "alimentar" queue
            channel.queueDeclare("alimentar", true, false, false, null);
            // Add "casa" queue
            channel.queueDeclare("casa", true, false, false, null);

            // Bind "vendas" exchange to "alimentar" queue with routing key "alimentar.#"
            channel.queueBind("alimentar", exchange_name, "alimentar.#");
            // Bind "vendas" exchange to "casa" queue with routing key "cada.#"
            channel.queueBind("casa", exchange_name, "casa.#");

        }catch (Exception e){
            System.out.println("Erro ao configurar RabbitMQ: " + e.getMessage());
            e.printStackTrace();
            try {
                channel.close();
                connection.close();
            } catch (Exception ex) {
                System.exit(0);
            }
        }
        System.exit(0);

    }

}
