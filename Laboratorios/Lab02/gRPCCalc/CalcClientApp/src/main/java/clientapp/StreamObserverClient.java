package clientapp;

import calcstubs.Result;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class StreamObserverClient implements StreamObserver<Result> {



    private boolean isCompleted = false;
    private boolean success = false;

    public boolean onSuccesss() {
        return success;
    }
    public boolean isCompleted(){
        return this.isCompleted;
    }
    List<Result> results = new ArrayList<>();
    public List<Result> getResults(){
        return this.results;
    }

    @Override
    public void onNext(Result result) {
        System.out.println("Resultado " + result.getId() + " = " + result.getRes());
        this.results.add(result);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error on call:"+throwable.getMessage());
        isCompleted=true; success=false;
    }

    @Override
    public void onCompleted() {
        System.out.println("Stream completo.");
        isCompleted = true;
        success = true;
    }
}
