package serverapp;

import com.google.protobuf.ByteString;
import imagestubs.*;
import io.grpc.stub.StreamObserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

public class ProcessImage extends ProcessImageServiceGrpc.ProcessImageServiceImplBase {

    private HashMap<String, Image> processedImages;

    public ProcessImage(HashMap<String, Image> processedImages) {
        this.processedImages = processedImages;
    }

    @Override
    public StreamObserver<Reply> uploadImage(StreamObserver<ImageId> responseObserver) {
        return new StreamObserverImage(responseObserver, processedImages);
    }

    @Override
    public void downloadImage(ImageId request, StreamObserver<Image> responseObserver) {

        String hash = request.getHash();
        ImageId imageId = ImageId.newBuilder().setHash(hash).build();

        String markedImageName = processedImages.get(imageId.getHash()).getName();
        System.out.println("Leitura da imagem  " + markedImageName + " com id: " + imageId.getHash() );

        try (FileInputStream fileInputStream = new FileInputStream("/usr/images/" + markedImageName)) {
            byte[] buffer = new byte[32 * 1024]; // 32 kB buffer size

            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                Image image = Image.newBuilder().setId(hash).setName(markedImageName).setMarkedImage(ByteString.copyFrom(buffer, 0, bytesRead)).build();
                responseObserver.onNext(image);
            }
            responseObserver.onCompleted();

        } catch (IOException e) {
            System.out.println("Erro no envio da imagem para o cliente: " + e.getMessage());
        }


    }
}
