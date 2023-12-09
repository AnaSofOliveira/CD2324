package clientapp;

import cliregstubs.*;
import cliregstubs.Void;
import com.google.protobuf.ByteString;
import imagestubs.ImageId;
import imagestubs.ProcessImageServiceGrpc;
import imagestubs.Reply;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Client {

    private static String regIP = "localhost";
    private static int regPort = 8500;

    private static String srvIP;
    private static int srvPort;
    private static ManagedChannel registerChannel;
    private static ManagedChannel serverChannel;
    private static ClientRegisterServiceGrpc.ClientRegisterServiceBlockingStub blockingStub;
    private static ProcessImageServiceGrpc.ProcessImageServiceStub noBlockStub;

    private static Scanner scanner = new Scanner(System.in);
    private static boolean connected;


    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                regIP = args[0];
                regPort = Integer.parseInt(args[1]);
            }

            // Estabelecer ligação com Register e pedir IP e porto do Servidor
            System.out.println("Estabelecer ligação com Register no " + regIP + ":" + regPort);
            registerChannel = ManagedChannelBuilder.forAddress(regIP, regPort)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext()
                    .build();
            blockingStub = ClientRegisterServiceGrpc.newBlockingStub(registerChannel);
            Result result = blockingStub.getServer(Void.newBuilder().build());

            srvIP = result.getIp();
            srvPort = result.getPort();

            // Estabelecer ligação com Servidor a partir da informação fornecida pelo Register
            System.out.println("Ligar ao servidor no " + srvIP + ":" + srvPort);
            serverChannel = ManagedChannelBuilder.forAddress(srvIP, srvPort)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext()
                    .build();

            noBlockStub = ProcessImageServiceGrpc.newStub(serverChannel);


            while (true) {
                switch (Menu()) {
                    case 1:
                        //System.out.println("Submeter imagem. ");
                        uploadImage();
                        break;
                    case 2:
                        //System.out.println("Download imagem. ");
                        downloadImage();
                        break;

                    case 99:
                        System.exit(0);
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Erro ao iniciar o Cliente: " + ex.getMessage());
        }
    }

    private static int Menu() {
        String menuInput;
        int op;

        do {
            System.out.println();
            System.out.println("    MENU");
            System.out.println(" 1 - Submeter imagem.");
            System.out.println(" 2 - Descarregar imagem.");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Escolha uma opção?");

            menuInput = scanner.next();
            op = Integer.parseInt(menuInput);
        } while (!((op >= 1 && op <= 4) || op == 99));
        return op;
    }

    private static void downloadImage() {
        System.out.println("Insira o ID da imagem: ");
        scanner.nextLine();
        String imageId = scanner.nextLine();

        System.out.println("Insira o local (caminho) onde quer guardar a imagem?");
        String downPath = scanner.nextLine();

        DownloadFileObserver downloadFileObserver = new DownloadFileObserver(downPath);
        ImageId identif = ImageId.newBuilder().setHash(imageId).build();

        noBlockStub.downloadImage(identif, downloadFileObserver);

        while (!downloadFileObserver.isCompleted()) {
            try {
                System.out.println("A descarregar imagem...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (downloadFileObserver.isSuccess()) {
            System.out.println("Download terminado!");
        } else {
            if (downloadFileObserver.isUnavailable()) {
                connected = false;
            }
        }


    }

    private static void uploadImage() {
        System.out.println("Insira o local (caminho) completo da imagem?");
        scanner.nextLine();
        String imagePath = scanner.nextLine();

        System.out.println("Indique um nome para a sua imagem.");
        String imageName = scanner.nextLine().replace(" ", "_");
        System.out.println("Name: " + imageName);

        System.out.println("Insira as keywords a serem marcadas na imagem.");
        // TODO: make sure the file is jpg because the MarkApp reads jpg
        String keywords = scanner.nextLine();
        System.out.println(keywords);
        List<String> listOfKeywords = List.of(keywords.split(" "));
        System.out.println(listOfKeywords);

        try (FileInputStream fileInputStream = new FileInputStream(imagePath)) {
            byte[] buffer = new byte[32 * 1024]; // 32 kB buffer size

            int bytesRead;
            StreamObserverUploadImageResponse streamObserver = new StreamObserverUploadImageResponse();
            StreamObserver<Reply> requestStreamObserver = noBlockStub.uploadImage(streamObserver);

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                UUID uuid = UUID.randomUUID();
                Reply reply = Reply.newBuilder()
                        .setId(uuid.toString())
                        .setName(imageName)
                        .setImgContent(ByteString.copyFrom(buffer, 0, bytesRead))
                        .addAllKeywords(listOfKeywords)
                        .build();
                requestStreamObserver.onNext(reply);
            }
            requestStreamObserver.onCompleted();

        } catch (IOException e) {
            System.out.println("Erro ao enviar a imagem para o Servidor: " + e.getMessage());
        }
    }
}