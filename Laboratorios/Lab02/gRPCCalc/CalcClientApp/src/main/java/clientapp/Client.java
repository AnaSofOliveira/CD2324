package clientapp;

import calcstubs.*;
import calcstubs.Number;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.*;


public class Client {

    private static String svcIP = "localhost";
    //private static String svcIP = "35.246.73.129";
    private static int svcPort = 8500;
    private static ManagedChannel channel;
    private static CalcServiceGrpc.CalcServiceBlockingStub blockingStub;
    private static CalcServiceGrpc.CalcServiceStub noBlockStub;


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
            blockingStub = CalcServiceGrpc.newBlockingStub(channel);
            noBlockStub = CalcServiceGrpc.newStub(channel);

            Scanner numbers = new Scanner(System.in);
            while (true) {
                switch (Menu()) {
                    case 1:  // adicionar dois numeros
                        System.out.println("Insert the first number: ");
                        int val1 = numbers.nextInt();
                        System.out.println("Insert the second number: ");
                        int val2 = numbers.nextInt();
                        Result res = blockingStub.add(AddOperands.newBuilder()
                                .setId(val1 + "+" + val2)
                                .setOp1(val1).setOp2(val2)
                                .build());
                        System.out.println("add " + res.getId() + "= " + res.getRes());
                        break;

                    case 2: // calcular as  potencias de x^y
                        System.out.println("Insert the base number: ");
                        int base = numbers.nextInt();
                        System.out.println("Insert the maximum exponential: ");
                        int power = numbers.nextInt();

                        NumberAndMaxExponent numberAndMaxExponent = NumberAndMaxExponent.newBuilder().setId(base+"^"+power).setBaseNumber(base).setMaxExponent(power).build();

                        StreamObserverClient streamObserverClient1 = new StreamObserverClient();

                        noBlockStub.generatePowers(numberAndMaxExponent, streamObserverClient1);

                        while (!streamObserverClient1.isCompleted()){
                            System.out.println("Active and waiting for Case2 completed ");
                            Thread.sleep(2*1000);
                        }

                        if(streamObserverClient1.onSuccesss()){
                            for(Result result: streamObserverClient1.getResults()){
                                System.out.println("Resultado " + result.getId() + " = " + result.getRes());
                            }
                        }
                        break;

                    case 3: //somar a sequencia dos numeros de x a y

                        System.out.println("Insert the minimum number: ");
                        int min = numbers.nextInt();
                        System.out.println("Insert the maximum exponential: ");
                        int max = numbers.nextInt();

                        StreamObserverClient streamObserverClient2 = new StreamObserverClient();

                        StreamObserver<Number> requestObserver = noBlockStub.addSeqOfNumbers(streamObserverClient2);

                        for(int i = min; i <= max; i++ ){
                            System.out.println("Pedido: Adiciona mais " + i);
                            requestObserver.onNext(Number.newBuilder().setNum(i).build());
                        }
                        requestObserver.onCompleted();
                        break;

                    case 4: //sequencia de operacões de soma x + y

                        StreamObserverClient streamObserverClient3 = new StreamObserverClient();

                        StreamObserver<AddOperands> requestObserverMultApp = noBlockStub.multipleAdd(streamObserverClient3);

                        val1 = 1; val2 = 2;
                        System.out.println("Pedido: " + val1 + " + " + val2);
                        requestObserverMultApp.onNext(AddOperands.newBuilder().setId(val1 + "+" + val2).setOp1(val1).setOp2(val2).build());

                        val1 = 3; val2 = 5;
                        System.out.println("Pedido: " + val1 + " + " + val2);
                        requestObserverMultApp.onNext(AddOperands.newBuilder().setId(val1 + "+" + val2).setOp1(val1).setOp2(val2).build());

                        val1 = 15; val2 = 10;
                        System.out.println("Pedido: " + val1 + " + " + val2);
                        requestObserverMultApp.onNext(AddOperands.newBuilder().setId(val1 + "+" + val2).setOp1(val1).setOp2(val2).build());

                        requestObserverMultApp.onCompleted();

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
            System.out.println(" 1 - Case1 - chamada unária: add two numbers");
            System.out.println(" 2 - Case 2 - chamada com sream de servidor: generate powers");
            System.out.println(" 3 - Case 3 - chamada com stream de cliente: add a sequence of numbers");
            System.out.println(" 4 - stream de cliente e de servidor: Multiple add operations ");
            System.out.println("99 - Exit");
            System.out.println();
            System.out.println("Choose an Option?");
            op = scan.nextInt();
        } while (!((op >= 1 && op <= 4) || op == 99));
        return op;
    }


}
