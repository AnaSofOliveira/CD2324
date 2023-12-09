package registerapp;

import cliregstubs.*;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import servregstubs.Reply;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

public class Register {

    private static int svcPort = 8500;
    static LinkedList<Reply> servers = new LinkedList<>();

    public static void main(String[] args) {
        try {
            if (args.length > 0) svcPort = Integer.parseInt(args[0]);
            io.grpc.Server svc = ServerBuilder
                    .forPort(svcPort)
                    .addService(new ClientRegister(servers))
                    .addService(new ServerRegister(servers))
                    .build();
            svc.start();
            System.out.println("Register iniciado a aguardar pedidos no porto " + svcPort);
            //Scanner scan = new Scanner(System.in);
            //scan.nextLine();
            svc.awaitTermination();
            svc.shutdown();

        } catch (Exception ex) {
            System.out.println("Erro ao iniciar Register.");
        }
    }
}
