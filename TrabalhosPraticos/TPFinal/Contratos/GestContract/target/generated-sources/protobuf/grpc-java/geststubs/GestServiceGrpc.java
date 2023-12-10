package geststubs;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * serviço com operações sobre números
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: GestService.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GestServiceGrpc {

  private GestServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "gestservice.GestService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<geststubs.Categoria,
      geststubs.Informacoes> getAgruparVendasMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "agruparVendas",
      requestType = geststubs.Categoria.class,
      responseType = geststubs.Informacoes.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<geststubs.Categoria,
      geststubs.Informacoes> getAgruparVendasMethod() {
    io.grpc.MethodDescriptor<geststubs.Categoria, geststubs.Informacoes> getAgruparVendasMethod;
    if ((getAgruparVendasMethod = GestServiceGrpc.getAgruparVendasMethod) == null) {
      synchronized (GestServiceGrpc.class) {
        if ((getAgruparVendasMethod = GestServiceGrpc.getAgruparVendasMethod) == null) {
          GestServiceGrpc.getAgruparVendasMethod = getAgruparVendasMethod =
              io.grpc.MethodDescriptor.<geststubs.Categoria, geststubs.Informacoes>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "agruparVendas"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  geststubs.Categoria.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  geststubs.Informacoes.getDefaultInstance()))
              .setSchemaDescriptor(new GestServiceMethodDescriptorSupplier("agruparVendas"))
              .build();
        }
      }
    }
    return getAgruparVendasMethod;
  }

  private static volatile io.grpc.MethodDescriptor<geststubs.Informacoes,
      geststubs.Ficheiro> getObterFicheiroMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "obterFicheiro",
      requestType = geststubs.Informacoes.class,
      responseType = geststubs.Ficheiro.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<geststubs.Informacoes,
      geststubs.Ficheiro> getObterFicheiroMethod() {
    io.grpc.MethodDescriptor<geststubs.Informacoes, geststubs.Ficheiro> getObterFicheiroMethod;
    if ((getObterFicheiroMethod = GestServiceGrpc.getObterFicheiroMethod) == null) {
      synchronized (GestServiceGrpc.class) {
        if ((getObterFicheiroMethod = GestServiceGrpc.getObterFicheiroMethod) == null) {
          GestServiceGrpc.getObterFicheiroMethod = getObterFicheiroMethod =
              io.grpc.MethodDescriptor.<geststubs.Informacoes, geststubs.Ficheiro>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "obterFicheiro"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  geststubs.Informacoes.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  geststubs.Ficheiro.getDefaultInstance()))
              .setSchemaDescriptor(new GestServiceMethodDescriptorSupplier("obterFicheiro"))
              .build();
        }
      }
    }
    return getObterFicheiroMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GestServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GestServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GestServiceStub>() {
        @java.lang.Override
        public GestServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GestServiceStub(channel, callOptions);
        }
      };
    return GestServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GestServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GestServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GestServiceBlockingStub>() {
        @java.lang.Override
        public GestServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GestServiceBlockingStub(channel, callOptions);
        }
      };
    return GestServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GestServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GestServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GestServiceFutureStub>() {
        @java.lang.Override
        public GestServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GestServiceFutureStub(channel, callOptions);
        }
      };
    return GestServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void agruparVendas(geststubs.Categoria request,
        io.grpc.stub.StreamObserver<geststubs.Informacoes> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAgruparVendasMethod(), responseObserver);
    }

    /**
     */
    default void obterFicheiro(geststubs.Informacoes request,
        io.grpc.stub.StreamObserver<geststubs.Ficheiro> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getObterFicheiroMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GestService.
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public static abstract class GestServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GestServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GestService.
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public static final class GestServiceStub
      extends io.grpc.stub.AbstractAsyncStub<GestServiceStub> {
    private GestServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GestServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GestServiceStub(channel, callOptions);
    }

    /**
     */
    public void agruparVendas(geststubs.Categoria request,
        io.grpc.stub.StreamObserver<geststubs.Informacoes> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAgruparVendasMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void obterFicheiro(geststubs.Informacoes request,
        io.grpc.stub.StreamObserver<geststubs.Ficheiro> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getObterFicheiroMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GestService.
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public static final class GestServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GestServiceBlockingStub> {
    private GestServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GestServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GestServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public geststubs.Informacoes agruparVendas(geststubs.Categoria request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAgruparVendasMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<geststubs.Ficheiro> obterFicheiro(
        geststubs.Informacoes request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getObterFicheiroMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GestService.
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public static final class GestServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<GestServiceFutureStub> {
    private GestServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GestServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GestServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<geststubs.Informacoes> agruparVendas(
        geststubs.Categoria request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAgruparVendasMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_AGRUPAR_VENDAS = 0;
  private static final int METHODID_OBTER_FICHEIRO = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_AGRUPAR_VENDAS:
          serviceImpl.agruparVendas((geststubs.Categoria) request,
              (io.grpc.stub.StreamObserver<geststubs.Informacoes>) responseObserver);
          break;
        case METHODID_OBTER_FICHEIRO:
          serviceImpl.obterFicheiro((geststubs.Informacoes) request,
              (io.grpc.stub.StreamObserver<geststubs.Ficheiro>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getAgruparVendasMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              geststubs.Categoria,
              geststubs.Informacoes>(
                service, METHODID_AGRUPAR_VENDAS)))
        .addMethod(
          getObterFicheiroMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              geststubs.Informacoes,
              geststubs.Ficheiro>(
                service, METHODID_OBTER_FICHEIRO)))
        .build();
  }

  private static abstract class GestServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GestServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return geststubs.GestServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GestService");
    }
  }

  private static final class GestServiceFileDescriptorSupplier
      extends GestServiceBaseDescriptorSupplier {
    GestServiceFileDescriptorSupplier() {}
  }

  private static final class GestServiceMethodDescriptorSupplier
      extends GestServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GestServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GestServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GestServiceFileDescriptorSupplier())
              .addMethod(getAgruparVendasMethod())
              .addMethod(getObterFicheiroMethod())
              .build();
        }
      }
    }
    return result;
  }
}
