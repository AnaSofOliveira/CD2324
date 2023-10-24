package serverapp;

import calcstubs.Number;
import calcstubs.Result;
import com.google.api.SystemParameterOrBuilder;
import io.grpc.stub.StreamObserver;

public class StreamObserverAddSeq implements StreamObserver<Number> {

    StreamObserver<Result> responseObserver;
    int sum;

    public StreamObserverAddSeq(StreamObserver<Result> responseObserver) {
        this.responseObserver = responseObserver;
        this.sum = 0;
    }

    @Override
    public void onNext(Number number) {
        int num = number.getNum();
        System.out.println("Vou adicionar " + number + " a " + this.sum);
        this.sum += num;
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Erro ao processar o pedido.");
    }

    @Override
    public void onCompleted() {
        System.out.println("Não vou adicionar mais.");
        this.responseObserver.onNext(Result.newBuilder().setId("result").setRes(sum).build());
        this.responseObserver.onCompleted();
    }
}
