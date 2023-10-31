package registerapp;

import io.grpc.stub.StreamObserver;
import servregstubs.Reply;
import servregstubs.Result;
import servregstubs.ServerRegisterServiceGrpc;

public class ServerRegister extends ServerRegisterServiceGrpc.ServerRegisterServiceImplBase {

    @Override
    public void registerServer(Reply request, StreamObserver<Result> responseObserver) {
        throw new java.lang.UnsupportedOperationException("Not implemented.");
    }
}
