package clientapp;

import imagestubs.ImageId;
import io.grpc.stub.StreamObserver;


public class StreamObserverUploadImageResponse implements StreamObserver<ImageId> {
    @Override
    public void onNext(ImageId identifier) {
        System.out.println("ID da Imagem: " + identifier.getHash());
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println("Erro ao descarregar a imagem: = " + throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Upload completo!");
    }
}
