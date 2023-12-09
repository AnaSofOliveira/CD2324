package serverapp;

import imagestubs.Image;
import imagestubs.ImageId;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import servregstubs.Reply;
import servregstubs.Result;
import servregstubs.ServerRegisterServiceGrpc;

import java.util.HashMap;

public class Server extends ServerRegisterServiceGrpc.ServerRegisterServiceImplBase {

    static io.grpc.Server svc;
    private static int srvPort = 7500;
    private static String srvIP = "localhost";

    private static int regPort = 7500;
    private static String regIP = "34.175.116.242";

    private static ManagedChannel channel;
    private static ServerRegisterServiceGrpc.ServerRegisterServiceBlockingStub blockingStub;
    private static HashMap<String, Image> processedImages = new HashMap<>();

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                regIP = args[0];
                regPort = Integer.parseInt(args[1]);
                srvIP = args[2];
                srvPort = Integer.parseInt(args[3]);
            }

            System.out.println("Register -> " + regIP + ": " + regPort);
            System.out.println("Server -> " + srvIP + ": " + srvPort);

            // Abertura de channel porque Servidor é o cliente na interação com o Register
            channel = ManagedChannelBuilder.forAddress(regIP, regPort)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext()
                    .build();
            blockingStub = ServerRegisterServiceGrpc.newBlockingStub(channel);


            svc = ServerBuilder
                    .forPort(srvPort)
                    .addService(new Server())
                    .addService(new ProcessImage(processedImages))
                    .build();
            svc.start();
            registerServer();
            System.out.println("Server started, listening on " + srvPort);
            //Scanner scan = new Scanner(System.in);
            //scan.nextLine();
            svc.awaitTermination();
            svc.shutdown();

        } catch (Exception ex) {
            System.out.println("Erro ao iniciar o servidor.");
            System.out.println("Register inoperacional ou outro serviço no IP " + regIP + ": " + regPort + " ou no IP "+ srvIP + ": " + srvPort);
        }
    }

    private static void registerServer() throws InterruptedException {
        Reply reply = Reply.newBuilder()
                .setId(Thread.currentThread().getName())
                .setIp(srvIP)
                .setPort(srvPort)
                .build();

        System.out.println("Iniciar pedido de registo -> " + srvIP + ":" + srvPort);

        Result result = blockingStub.registerServer(reply);

        if (!result.getSuccess()) {
            System.out.println("Não foi possível registar o servidor!");
            svc.awaitTermination();
            svc.shutdown();
        }

    }
}
