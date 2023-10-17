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
        int op1 = request.getOp1();
        int op2 = request.getOp2();

        Result result = Result.newBuilder().setId(request.getId()).setRes(op1 + op2).build();

        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void generatePowers(NumberAndMaxExponent request, StreamObserver<Result> responseObserver) {
        int baseNumber = request.getBaseNumber();
        int maxExponent = request.getMaxExponent();

        Result result;
        for (int exp = 0; exp < maxExponent; exp++){

            result = Result.newBuilder().setId(request.getId()).setRes(baseNumber**exp);
            responseObserver.onNext(result);
        }

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Number> addSeqOfNumbers(StreamObserver<Result> responseObserver) {
       // not implemented
        return null;

    }

    @Override
    public StreamObserver<AddOperands> multipleAdd(StreamObserver<Result> responseObserver) {
       // not implemented
        return null;

    }
}
