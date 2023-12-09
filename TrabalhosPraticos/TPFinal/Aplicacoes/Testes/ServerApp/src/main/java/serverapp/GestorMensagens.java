package serverapp;

import com.google.gson.GsonBuilder;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import geststubs.Informacoes;
import io.grpc.stub.StreamObserver;
import serverapp.rabbitMessage.RMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class GestorMensagens implements DeliverCallback, CancelCallback {
    private final Channel rmqChannel;
    private final StreamObserver<Informacoes> responseObserver;
    private HashMap<Long, String> filesToDownload;

    public GestorMensagens(Channel rmqChannel, StreamObserver<Informacoes> responseObserver, HashMap<Long, String> filesToDownload) {
        this.rmqChannel = rmqChannel;
        this.responseObserver = responseObserver;
        this.filesToDownload = filesToDownload;
    }

    @Override
    public void handle(String consumerTag) throws IOException {
        /**
         * Tratamento de mensagem cancelada
         */
        System.out.println("A mensagem foi cancelada! " + consumerTag);
    }

    @Override
    public void handle(String consumerTag, Delivery message) {
        System.out.println("A mensagem foi recebida! " + consumerTag);

        String recMessage = new String(message.getBody(), StandardCharsets.UTF_8);
        String routingKey = message.getEnvelope().getRoutingKey();
        long deliverTag = message.getEnvelope().getDeliveryTag();

        System.out.println(recMessage);

        byte[] bMessage = recMessage.getBytes(StandardCharsets.UTF_8);
        String jsonMessage = new String(bMessage, StandardCharsets.UTF_8);
        RMessage rMessage = new GsonBuilder().create().fromJson(jsonMessage, RMessage.class);

        long fileId = rMessage.getId();
        String nomeFicheiro = rMessage.getNomeFicheiro();
        System.out.println(consumerTag + ": Mensagem recebida com routing id:" + fileId + " e conteúdo: " + nomeFicheiro);

        this.filesToDownload.put(fileId, nomeFicheiro);

        responseObserver.onNext(Informacoes.newBuilder().setHash(String.valueOf(fileId)).build());
        responseObserver.onCompleted();

        if (recMessage.equals("nack")) {
            try {
                this.rmqChannel.basicNack(deliverTag, false, true);
                this.rmqChannel.close();
            } catch (IOException e) {
                System.out.println("Erro RabbitMQ: " + e.getMessage());
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                this.rmqChannel.basicAck(deliverTag, false);
                this.rmqChannel.close();
            } catch (IOException | TimeoutException e) {
                System.out.println("Erro RabbitMQ: " + e.getMessage());
            }
        }


    }
}
