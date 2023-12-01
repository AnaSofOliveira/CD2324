package serverapp;

import calcstubs.AddOperands;
import calcstubs.Result;
import io.grpc.stub.StreamObserver;

public class StreamObserverMultipleAdd implements StreamObserver<AddOperands> {

    StreamObserver<Result> responseObserver;

    public StreamObserverMultipleAdd(StreamObserver<Result> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(AddOperands addOperands) {
        int op1 = addOperands.getOp1();
        int op2 = addOperands.getOp2();

        Result result = Result.newBuilder().setId(addOperands.getId()).setRes(op1 + op2).build();
        System.out.println(op1 + " + " + op2 + " = " + result.getRes());

        responseObserver.onNext(result);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Erro ao processar o pedido.");
    }

    @Override
    public void onCompleted() {

        System.out.println("Não vou fazer mais adições");
        this.responseObserver.onCompleted();
    }
}
