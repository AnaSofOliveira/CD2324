package userapp;


import gestorstubs.Path;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;

public class ResumeResponse implements StreamObserver<Path> {

    private final HashMap<String, Path> filesToDownload;

    public ResumeResponse(HashMap<String, Path> filesToDownload){
        this.filesToDownload = filesToDownload;
    }

    @Override
    public void onNext(Path path) {
        System.out.println("Your file is available to be downloaded. ");
        System.out.println("FilePath = " + path.getFileName());

        this.filesToDownload.put(path.getHash(), path);

    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Erro ao processar resposta no servidor:" + throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Your file is available to be downloaded. ");
    }
}
