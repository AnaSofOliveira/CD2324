package posapp;


import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.*;

public class PointOfSales {

    private static String rmqBrokerIP;
    private static int rmqBrokerPort;
    private final static String exchangeName = "ExgSales";
    static boolean exit = false;

    static Channel channel;

    public static void main(String[] args) {
        if (args.length > 0) {
            rmqBrokerIP = args[0];
            rmqBrokerPort = Integer.parseInt(args[1]);
        }

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rmqBrokerIP); factory.setPort(rmqBrokerPort); //5672

            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            //channel.addReturnListener(new ReturnedMessage());

            do{
                switch (Menu()){
                    case 1:
                        enviarProdutosAlimentares();
                        break;
                    case 2:
                        enviarProdutosCasa();
                        break;
                    case 99:
                        System.exit(0);
                    default:
                        break;
                }
            }while (!exit);

            channel.close();
            connection.close();

        } catch (Exception ex) {
            System.out.println("Erro ao estabelecer ligação com RabbitMQ." + ex.getMessage());
        }
    }

    private static void enviarProdutosAlimentares() throws IOException {

        String[] produtos = {"Arroz", "Feijao", "Acucar", "Cafe", "Chocolate",
                            "Farinha de Trigo", "Leite", "Oleo", "Pao", "Sumo de laranja"};

        int[] quantidades = {1, 2, 1, 1, 2,
                            1, 6, 1, 8, 1};

        double[] precosUnidade = {1.49, 1.19, 1.49, 17.49, 0.69,
                                0.75, 0.85,  1.39, 0.19, 2.89};

        for (int i=0; i<produtos.length; i++){
            String prod = produtos[i];
            int quant = quantidades[i];
            double preco = quant * precosUnidade[i];

            enviarProduto("alimentar", prod, quant, preco);
        }

    }

    private static void enviarProdutosCasa() throws IOException {

        String[] produtos = {"Colcha", "Faqueiro", "Estante 2x2", "Arvore de Natal", "Estante 3x3",
                            "Sofa", "Poltrona", "Movel", "Cadeira", "Tabuleiro de Forno"};

        int[] quantidades = {1, 2, 1, 1, 2,
                            1, 6, 1, 8, 1};

        double[] precosUnidade = {17.55, 50.00, 35.00, 27.50, 75.00,
                                    379.00, 239.00, 149.00, 35.00, 15.00};

        for (int i=0; i<produtos.length; i++){
            String prod = produtos[i];
            int quant = quantidades[i];
            double preco = quant * precosUnidade[i];

            enviarProduto("casa", prod, quant, preco);
        }

    }

    private static String readline(String msg) {
        Scanner scaninput = new Scanner(System.in);
        System.out.println(msg);
        return scaninput.nextLine();
    }

    private static int Menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Enviar 10 produtos alimentares.");
            System.out.println(" 2 - Enviar 10 produtos de casa.");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op >= 1 && op <= 2) || op == 99));
        return op;
    }


    private static void enviarProduto(String categoria, String produto, int quantidade, double preco) throws IOException {

        SalesMessage posMessage = new SalesMessage(produto, categoria, quantidade, preco);
        String jsonMessage = new GsonBuilder().create().toJson(posMessage);

        channel.basicPublish(exchangeName, categoria, true, null, jsonMessage.getBytes());
        System.out.println("Enviei mensagem: " + jsonMessage + " para ser registado.");
    }

}