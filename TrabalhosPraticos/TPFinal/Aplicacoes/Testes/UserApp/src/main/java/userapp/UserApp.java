package userapp;

import geststubs.Categoria;
import geststubs.GestServiceGrpc;
import geststubs.Informacoes;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.*;


public class UserApp {

    private static String svcIP = "localhost";
    //private static String svcIP = "35.246.73.129";
    private static int svcPort = 8500;
    private static ManagedChannel channel;

    static GestServiceGrpc.GestServiceBlockingStub blockingStub;
    static GestServiceGrpc.GestServiceStub noBlockStub;

    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                svcIP = args[0];
                svcPort = Integer.parseInt(args[1]);
            }
            System.out.println("connect to "+svcIP+":"+svcPort);
            channel = ManagedChannelBuilder.forAddress(svcIP, svcPort)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();

            blockingStub = GestServiceGrpc.newBlockingStub(channel);
            noBlockStub = GestServiceGrpc.newStub(channel);

            Scanner numbers = new Scanner(System.in);
            while (true) {
                switch (Menu()) {
                    case 1:
                        InfoObserver infoObs = new InfoObserver();
                        System.out.println("Qual a categoria do resumo? \n * Alimentar \n * Casa \n * Todas");
                        String tipo = readline("Categoria = ");
                        noBlockStub.agruparVendas(Categoria.newBuilder().setTipo(tipo).build(), infoObs);
                        break;

                    case 2:
                        String documentId = readline("Qual o ID do documento?");
                        FileObserver fileObserver = new FileObserver();
                        noBlockStub.obterFicheiro(Informacoes.newBuilder().setHash(documentId).build(), fileObserver);

                        System.out.println("Enviei pedido para obter ficheiro agregado. ");

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
