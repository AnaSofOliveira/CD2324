package clientapp;

import imagestubs.Image;
import io.grpc.stub.StreamObserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadFileObserver implements StreamObserver<Image> {

    private String downPath;
    private PrintStream writeTo;
    private String fileName;
    private boolean isCompleted;
    private boolean isSuccess;

    private boolean isUnavailable;


    public DownloadFileObserver(String downPath) {
        this.downPath = downPath;
    }

    @Override
    public void onNext(Image image) {

        if (writeTo == null) {
            try {
                if (Files.isDirectory(Paths.get(downPath))) {
                    fileName = downPath + "\\" + image.getName();
                    writeTo = new PrintStream(new FileOutputStream(fileName));
                } else {
                    writeTo = new PrintStream(new FileOutputStream(image.getName()));
                }
            } catch (Exception e) {
                System.out.println("Erro na localização da imagem: " + e.getMessage());
            }
        }

        byte[] markedImage = image.getMarkedImage().toByteArray();

        try {
            writeTo.write(markedImage);
        } catch (IOException e) {
            System.out.println("Erro ao descarregar imagem: " + e.getMessage());
        }

    }

    @Override
    public void onError(Throwable throwable) {
        if (writeTo != null) {
            writeTo.close();
        }

        isCompleted = true;
        isSuccess = false;

        System.err.println("Erro ao descarregar a imagem: = " + throwable.getMessage());
        if (throwable.getMessage() != null && throwable.getMessage().startsWith("UNAVAILABLE")) {
            isUnavailable = true;
        }
    }

    @Override
    public void onCompleted() {
        if (writeTo != null) {
            writeTo.close();
        }
        isCompleted = true;
        isSuccess = true;
        System.out.println("Download terminado!");
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isUnavailable() {
        return isUnavailable;
    }
}
