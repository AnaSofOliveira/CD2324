package registerapp;

import io.grpc.stub.StreamObserver;
import servregstubs.Reply;
import servregstubs.Result;
import servregstubs.ServerRegisterServiceGrpc;

import java.util.LinkedList;

public class ServerRegister extends ServerRegisterServiceGrpc.ServerRegisterServiceImplBase {

    private LinkedList<Reply> registeredServers;

    public ServerRegister(LinkedList<Reply> servers) {
        this.registeredServers = servers;
    }

    @Override
    public void registerServer(Reply request, StreamObserver<Result> responseObserver) {
        System.out.println("Recebi um pedido para iniciar registo.");
        registeredServers.add(request);
        Result reply = Result.newBuilder().setSuccess(true).build();
        responseObserver.onNext(reply);
        System.out.println("Servidor registado no IP " + request.getIp() + ":" + request.getPort());
        System.out.println("Lista de servidores: " + this.registeredServers);
        responseObserver.onCompleted();
    }
}
