package userapp;


import gestorstubs.GestorServiceGrpc;
import gestorstubs.Path;
import gestorstubs.Void;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserApp {


    private static HashMap<String, Path> filesToDownload = new HashMap<>();
    private static String srvIP;
    private static int srvPort;
    private static ManagedChannel serverChannel;
    private static GestorServiceGrpc.GestorServiceStub noBlockStub;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                srvIP = args[0];
                srvPort = Integer.parseInt(args[1]);
            }

            // Estabelecer ligação com Register e pedir IP e porto do Servidor
            System.out.println("Estabelecer ligação com o Servidor no " + srvIP + ":" + srvPort);
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8000)
                    .usePlaintext()
                    .build();

            noBlockStub = GestorServiceGrpc.newStub(channel);

            while (true) {
                switch (Menu()) {
                    case 1:
                        //System.out.println("Gerar registo de vendas. ");
                        resumeSalesRegistry();
                        break;
                    case 2:
                        //System.out.println("Download registo de vendas.");
                        listAvailableResumes();
                        break;
                    case 3:
                        //System.out.println("Download registo de vendas.");
                        downloadResumeFile();
                        break;

                    case 99:
                        System.exit(0);
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar o Cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private static int Menu() {
        String menuInput;
        int op;

        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Gerar registo de vendas.");
            System.out.println(" 2 - Listar registos de vendas preparados para download.");
            System.out.println(" 3 - Download registo de vendas.");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Escolha uma opção?");

            menuInput = scanner.next();
            op = Integer.parseInt(menuInput);
        } while (!((op >= 1 && op <= 3) || op == 99));
        return op;
    }


    private static void resumeSalesRegistry(){
        // Response observer para receber a resposta com o Path para o documento assincronamente
        ResumeResponse resumeResponse = new ResumeResponse(filesToDownload);
        Void message = Void.newBuilder().build();

        noBlockStub.resumo(message, resumeResponse);
    }

    private static void listAvailableResumes() {
        for (Map.Entry<String, Path> entry: filesToDownload.entrySet()) {
            System.out.println("-> " + entry.getValue().getFileName() + " | ID: " + entry.getKey());
        }
    }

    private static void downloadResumeFile(){

        System.out.println("Insira o ID do documento:");
        String id = scanner.nextLine();

        Path path = filesToDownload.get(id);

        System.out.println("Insira o caminho para onde quer que o ficheiro seja descarregado: ");
        String downloadPath = scanner.nextLine();

        DownloadResponse downloadResponse = new DownloadResponse(downloadPath);
        noBlockStub.decarregar(path, downloadResponse);

    }

}