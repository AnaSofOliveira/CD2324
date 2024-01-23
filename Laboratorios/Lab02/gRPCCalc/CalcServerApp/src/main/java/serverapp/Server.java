package serverapp;

import calcstubs.*;
import calcstubs.Number;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;


public class Server extends CalcServiceGrpc.CalcServiceImplBase {

    private static int svcPort = 8500;

    public static void main(String[] args) {
        try {
            if (args.length > 0) svcPort = Integer.parseInt(args[0]);
            io.grpc.Server svc = ServerBuilder
                .forPort(svcPort)
                .addService(new Server())
                .build();
            svc.start();
            System.out.println("Server started, listening on " + svcPort);
            //Scanner scan = new Scanner(System.in);
            //scan.nextLine();
            svc.awaitTermination();
            svc.shutdown();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void add(AddOperands request, StreamObserver<Result> responseObserver) {
        // Caso 1 - Unária
        int op1 = request.getOp1();
        int op2 = request.getOp2();
        System.out.println("Recebi um pedido para adicionar.");

        Result result = Result.newBuilder().setId(request.getId()).setRes(op1 + op2).build();
        System.out.println(op1 + " + " + op2 + " = " + result.getRes());

        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void generatePowers(NumberAndMaxExponent request, StreamObserver<Result> responseObserver) {
        // Caso 2 - Stream do lado do Servidor
        int baseNumber = request.getBaseNumber();
        int maxExponent = request.getMaxExponent();
        System.out.println("Recebi um pedido gerar potencias com base " + baseNumber + " e expoente máximo  " + maxExponent);

        Result result;
        for (int exp = 0; exp < maxExponent; exp++){
            int res = (int)Math.pow(baseNumber, exp);
            System.out.println(baseNumber + " ^ " + exp + " = " + res);
            result = Result.newBuilder().setId(baseNumber + "^" + exp).setRes(res).build();
            responseObserver.onNext(result);
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Number> addSeqOfNumbers(StreamObserver<Result> responseObserver) {
       // Caso 3 - Stream do lado do Cliente
        System.out.println("Recebi um pedido para fazer uma adição sequencial.");
        return new StreamObserverAddSeq(responseObserver);

    }

    @Override
    public StreamObserver<AddOperands> multipleAdd(StreamObserver<Result> responseObserver) {
       // Caso 4 - Stream do lado do Servidor e do Cliente
        System.out.println("Recebi um pedido para fazer multiplas adições.");
       return new StreamObserverMultipleAdd(responseObserver);
    }
}
