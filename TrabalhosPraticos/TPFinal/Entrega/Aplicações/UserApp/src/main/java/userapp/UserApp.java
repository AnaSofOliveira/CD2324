package userapp;

import geststubs.Categoria;
import geststubs.GestServiceGrpc;
import geststubs.Informacoes;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.*;


public class UserApp {

    private static String svcIP;
    private static int svcPort;
    private static ManagedChannel channel;

    static GestServiceGrpc.GestServiceBlockingStub blockingStub;
    static GestServiceGrpc.GestServiceStub noBlockStub;

    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                svcIP = args[0];
                svcPort = Integer.parseInt(args[1]);
            }else {
                System.out.println("Indique o IP e o Porto do Servidor. ");
                System.exit(0);
            }
            channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                .usePlaintext()
                .build();

            System.out.println("Conectado ao Servidor " + svcIP + ":" + svcPort);

            blockingStub = GestServiceGrpc.newBlockingStub(channel);
            noBlockStub = GestServiceGrpc.newStub(channel);

            while (true) {
                switch (Menu()) {
                    case 1:
                        String categoria = readline("Qual a categoria do resumo? Alimentar (a) ou Casa (c).");
                        if (categoria.compareTo("exit") == 0) break;
                        categoria = categoria.equals("a") ? "alimentar" : categoria.equals("c") ? "casa" : "";

                        Informacoes informacoes = blockingStub.agruparVendas(Categoria.newBuilder().setTipo(categoria).build());
                        System.out.println("O ficheiro com ID: " + informacoes.getHash() + " está disponível para download.");
                        break;

                    case 2:
                        String documentId = readline("Qual o ID do ficheiro?");
                        FileObserver fileObserver = new FileObserver();
                        noBlockStub.obterFicheiro(Informacoes.newBuilder().setHash(documentId).build(), fileObserver);

                        break;
                    case 99:
                        System.exit(0);
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static int Menu() {
        int op;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Faz a sintese dos ficheiros.");
            System.out.println(" 2 - Descarrega o ficheiro de sintese.");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op >= 1 && op <= 2) || op == 99));
        return op;
    }


    private static String readline(String msg) {
        Scanner scaninput = new Scanner(System.in);
        System.out.println(msg);
        return scaninput.nextLine();
    }


}
