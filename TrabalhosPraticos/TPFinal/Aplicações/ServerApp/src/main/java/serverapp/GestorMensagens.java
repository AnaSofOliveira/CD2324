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
    private final String nome_exchange;
    private final String nome_queue;
    private HashMap<Long, String> filesToDownload;

    public GestorMensagens(Channel rmqChannel, String nome_exchange, String nome_queue, StreamObserver<Informacoes> responseObserver, HashMap<Long, String> filesToDownload) {
        this.rmqChannel = rmqChannel;
        this.nome_exchange = nome_exchange;
        this.nome_queue = nome_queue;
        this.responseObserver = responseObserver;
        this.filesToDownload = filesToDownload;
    }

    @Override
    public void handle(String consumerTag){
        System.out.println(" ");
    }

    @Override
    public void handle(String consumerTag, Delivery message) {

        String recMessage = new String(message.getBody(), StandardCharsets.UTF_8);
        String routingKey = message.getEnvelope().getRoutingKey();
        long deliverTag = message.getEnvelope().getDeliveryTag();

        System.out.println(recMessage);

        byte[] bMessage = recMessage.getBytes(StandardCharsets.UTF_8);
        String jsonMessage = new String(bMessage, StandardCharsets.UTF_8);
        RMessage rMessage = new GsonBuilder().create().fromJson(jsonMessage, RMessage.class);

        long fileId = rMessage.getId();
        String nomeFicheiro = rMessage.getNomeFicheiro();
        System.out.println("Recebi mensagem com id " + fileId + ". Ficheiro armazenado: " + nomeFicheiro);

        this.filesToDownload.put(fileId, nomeFicheiro);

        System.out.println("Enviar resposta ao cliente com ID: " + fileId + " para download.");
        responseObserver.onNext(Informacoes.newBuilder().setHash(String.valueOf(fileId)).build());
        responseObserver.onCompleted();

        if (recMessage.equals("nack")) {
            try {
                this.rmqChannel.basicNack(deliverTag, false, true);
                deleteRabbitMQStructure();
            } catch (IOException e) {
                System.out.println("Erro RabbitMQ: " + e.getMessage());
            }
        }
        else {
            try {
                this.rmqChannel.basicAck(deliverTag, false);
                deleteRabbitMQStructure();
            } catch (IOException e) {
                System.out.println("Erro RabbitMQ: " + e.getMessage());
            }
        }


    }

    public void deleteRabbitMQStructure() throws IOException {
        this.rmqChannel.queueUnbind(this.nome_exchange, this.nome_queue, "");
        this.rmqChannel.queueDelete(this.nome_queue);
        this.rmqChannel.exchangeDelete(this.nome_exchange);
    }
}
