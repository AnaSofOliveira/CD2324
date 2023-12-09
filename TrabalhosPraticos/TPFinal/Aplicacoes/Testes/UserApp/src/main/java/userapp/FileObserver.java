package userapp;

import geststubs.Ficheiro;
import io.grpc.stub.StreamObserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FileObserver implements StreamObserver<Ficheiro> {

    private PrintStream writeTo;
    private String downPath = "C:\\usr\\downloads";

    @Override
    public void onNext(Ficheiro ficheiro) {

        String nomeFicheiro = ficheiro.getNomeFicheiro();
        System.out.println("Recebi parte de um ficheiro " + nomeFicheiro + " no onNext.");

        if (writeTo == null) {
            try {
                if (Files.isDirectory(Paths.get(downPath))) {
                    String pathToFile = downPath + File.separator + nomeFicheiro;
                    writeTo = new PrintStream(new FileOutputStream(pathToFile));
                } else {
                    writeTo = new PrintStream(new FileOutputStream(nomeFicheiro));
                }
            } catch (Exception e) {
                System.out.println("Erro na localização da imagem: " + e.getMessage());
            }
        }

        byte[] blocoFicheiro = ficheiro.getBlocos().toByteArray();

        try {
            writeTo.write(blocoFicheiro);
        } catch (IOException e) {
            System.out.println("Erro ao descarregar imagem: " + e.getMessage());
        }
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("Erro no pedido agruparVendas:" + t.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Recebi o término das informações no onCompleted.");
    }
}
