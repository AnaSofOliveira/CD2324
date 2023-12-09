package userapp;

import gestorstubs.File;
import io.grpc.stub.StreamObserver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadResponse implements StreamObserver<File>
{

    private String downloadPath;
    private String fileName;
    private PrintStream writeTo;

    private boolean isCompleted = false;
    private boolean isSuccess = false;
    private boolean isUnavailable = false;


    public DownloadResponse(String downloadPath){

        this.downloadPath = downloadPath;
        this.isCompleted = false;
        this.isSuccess = false;
        this.isUnavailable = false;
    }

    @Override
    public void onNext(File file) {
        if(this.writeTo == null){
            try {
                if (Files.isDirectory(Paths.get(this.downloadPath))) {
                    this.fileName = this.downloadPath + file.getFileName();
                    this.writeTo = new PrintStream(new FileOutputStream(this.fileName));
                }else{
                    this.writeTo = new PrintStream(new FileOutputStream(file.getFileName()));
                }
            } catch (FileNotFoundException e) {
                System.out.println("Não foi possível escrever na localização indicada = " + this.downloadPath);
                System.exit(0);
            }
        }

        byte[] fileBlock = file.getFileBlock().toByteArray();

        try{
            this.writeTo.write(fileBlock);
        } catch (IOException e) {
            System.out.println("Não foi possível guardar o conteúdo no ficheiro. ");
            System.exit(0);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (this.writeTo != null) {
            this.writeTo.close();
        }

        this.isCompleted = true;
        this.isSuccess = false;

        System.err.println("Erro ao descarregar a ficheiro: = " + throwable.getMessage());
        if (throwable.getMessage() != null && throwable.getMessage().startsWith("UNAVAILABLE")) {
            this.isUnavailable = true;
        }
    }


    @Override
    public void onCompleted() {
        if (writeTo != null) {
            this.writeTo.close();
        }
        this.isCompleted = true;
        this.isSuccess = true;
        System.out.println("Terminado o download.");
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isUnavailable() {
        return isUnavailable;
    }

}
