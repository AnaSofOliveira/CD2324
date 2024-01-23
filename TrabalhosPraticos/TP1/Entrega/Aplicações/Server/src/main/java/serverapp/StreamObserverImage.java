package serverapp;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.google.protobuf.ByteString;
import imagestubs.Image;
import imagestubs.ImageId;
import imagestubs.Reply;
import io.grpc.stub.StreamObserver;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StreamObserverImage implements StreamObserver<Reply> {

    ByteArrayOutputStream imageBufferStream = new ByteArrayOutputStream();
    private StreamObserver<ImageId> responseObserver;


    private String HOST_URI = "unix:///var/run/docker.sock";
    private String containerName = "images";
    private String pathVolDir = "/usr/images";
    private String pathOutDir = "/usr/datafiles";

    private DockerClient dockerclient;
    private HostConfig hostConfig;

    private String imageName;
    List<String> keywords = new ArrayList<>();

    HashMap<String, Image> processedImages;

    public StreamObserverImage(StreamObserver<ImageId> responseObserver, HashMap<String, Image> processedImages) {
        createContainer();
        this.responseObserver = responseObserver;
        this.processedImages = processedImages;
    }

    @Override
    public void onNext(Reply reply) {
        this.imageName = reply.getName();
        this.keywords = reply.getKeywordsList();

        System.out.println("[" + reply.getId() + "] Chunck image with Name: " + this.imageName + " and Keywords: " + this.keywords);

        try {
            imageBufferStream.write(reply.getImgContent().toByteArray());

        } catch (IOException e) {
            System.out.println("Erro ao processar blocos do upload.  ");
            this.responseObserver.onError(e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        stopContainer();
        killContainer();
        System.out.println("Erro ao submeter imagem do lado do Cliente. ");
    }

    @Override
    public void onCompleted() {

        byte[] imageBytes = imageBufferStream.toByteArray();
        ByteArrayInputStream imageInputStream = new ByteArrayInputStream(imageBytes);

        System.out.println("Upload completo da imagem " + this.imageName);

        try {

            BufferedImage image = ImageIO.read(imageInputStream);
            File localImageFile = new File("/usr/images/" + imageName + ".jpg");
            ImageIO.write(image, "jpg", localImageFile);

            System.out.println("Imagem " + imageName + "armazenada na localização: /usr/images/");

            createAndRunContainer();
            // image unique identifier
            UUID uuid = UUID.randomUUID();
            ImageId id = ImageId.newBuilder().setHash(uuid.toString()).build();

            String markedImageName = imageName + "-marked.jpg";
            Image markedImage = Image.newBuilder()
                    .setId(id.getHash())
                    .setName(markedImageName)
                    .build();

            processedImages.put(id.getHash(), markedImage);

            responseObserver.onNext(id);
            responseObserver.onCompleted();

            System.out.println("server observer image");
        } catch (Exception e) {
            System.out.println("Erro ao receber término do upload: " + e.getMessage());
        }
    }

    private void createAndRunContainer() {

        initContainer();

        try {

            System.out.println("Iniciar Container");

            String inName = "../datafiles/" + this.imageName + ".jpg";
            String outName = "../datafiles/" + this.imageName + "-marked.jpg";

            List<String> command = new ArrayList<>();
            command.add(inName);
            command.add(outName);
            command.addAll(this.keywords);

            System.out.println("command: " + command);

            CreateContainerResponse containerResponse = this.dockerclient
                    .createContainerCmd(this.containerName)
                    .withName(this.containerName)
                    .withHostConfig(hostConfig)
                    .withCmd(command)
                    .exec();

            System.out.println("ID:" + containerResponse.getId());
            this.dockerclient.startContainerCmd(containerResponse.getId()).exec();

            InspectContainerResponse inspResp = this.dockerclient
                    .inspectContainerCmd(this.containerName).exec();
            System.out.println("Estado do Container: " + inspResp.getState().getStatus());

            Thread.sleep(3000);
            stopContainer();
            killContainer();

        } catch (Exception e) {
            killContainer();
        }
    }



    private void createContainer() {
        System.out.println("Criação do container");
        this.dockerclient = DockerClientBuilder
                .getInstance().withDockerHttpClient(
                        new ApacheDockerHttpClient.Builder().dockerHost(URI.create(this.HOST_URI)).build()
                )
                .build();
    }
    private void initContainer() {
        System.out.println("CLIENTE DOCKER");
        this.hostConfig = HostConfig.newHostConfig()
                .withBinds(new Bind(this.pathVolDir, new Volume(this.pathOutDir)));
        System.out.println("CONFIGURAÇÃO DO HOST");
    }

    private void stopContainer(){
        // if container is running
        System.out.println("Pausa do Container");
        this.dockerclient.killContainerCmd(this.containerName).exec();

    }

    private void killContainer(){
        //remove container
        System.out.println("Término do Container");
        this.dockerclient.removeContainerCmd(this.containerName).exec();
    }
}