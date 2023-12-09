package userapp;

import geststubs.Informacoes;
import io.grpc.stub.StreamObserver;

public class InfoObserver implements StreamObserver<Informacoes> {

    @Override
    public void onNext(Informacoes value) {
        System.out.println("Ficheiro disponível para download: " + value.getHash());
    }

    @Override
    public void onError(Throwable t) {
        System.out.println(t.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println(" ");
    }
}
