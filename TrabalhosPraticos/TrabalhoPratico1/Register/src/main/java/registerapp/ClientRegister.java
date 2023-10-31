package registerapp;

import cliregstubs.ClientRegisterServiceGrpc;
import cliregstubs.Result;
import cliregstubs.Void;
import io.grpc.stub.StreamObserver;

public class ClientRegister extends ClientRegisterServiceGrpc.ClientRegisterServiceImplBase {

    @Override
    public void getServer(Void request, StreamObserver<Result> responseObserver) {
        throw new java.lang.UnsupportedOperationException("Not implemented.");
    }
}
