package registerapp;

import cliregstubs.ClientRegisterServiceGrpc;
import cliregstubs.Result;
import cliregstubs.Void;
import io.grpc.stub.StreamObserver;
import servregstubs.Reply;

import java.util.LinkedList;

public class ClientRegister extends ClientRegisterServiceGrpc.ClientRegisterServiceImplBase {

    private LinkedList<Reply> registeredServers;

    public ClientRegister(LinkedList<Reply> servers) {
        this.registeredServers = servers;
    }

    @Override
    public void getServer(Void request, StreamObserver<Result> responseObserver) {
        Reply reply = this.registeredServers.removeFirst();

        Result result = Result.newBuilder()
                .setId(Thread.currentThread().getName())
                .setIp(reply.getIp())
                .setPort(reply.getPort())
                .build();

        responseObserver.onNext(result);
        responseObserver.onCompleted();

        this.registeredServers.addLast(reply);
    }
}