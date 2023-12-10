package workerapp.rabbit;

import com.google.gson.GsonBuilder;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import workerapp.FilesManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PoSMsgHandler implements CancelCallback, DeliverCallback {
    private final Channel rmqChannel;
    private final long workerID;
    private static final String SHARED_FOLDER = "/var/sharedfiles/";

    public PoSMsgHandler(Channel rmqChannel, long workerID) {
        this.rmqChannel = rmqChannel;
        this.workerID = workerID;
    }

    @Override
    public void handle(String consumerTag){
        System.out.println("A mensagem foi cancelada! " + consumerTag);
    }

    @Override
    public void handle(String consumerTag, Delivery message) throws IOException {
        // Delivery message -> Mensagem com origem no PointOfSales

        String recMessage = new String(message.getBody(), StandardCharsets.UTF_8);
        long deliverTag = message.getEnvelope().getDeliveryTag();

        System.out.println(recMessage);

        byte[] bMessage = recMessage.getBytes(StandardCharsets.UTF_8);
        String jsonMessage = new String(bMessage, StandardCharsets.UTF_8);
        PoSMessage posMessage = new GsonBuilder().create().fromJson(jsonMessage, PoSMessage.class);

        String produto = posMessage.getProduto();
        String categoria = posMessage.getCategoria();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultdate = new Date(System.currentTimeMillis());

        String date = sdf.format(resultdate);

        boolean fileSaved = FilesManager.addProductToFile(date, produto, categoria, SHARED_FOLDER + workerID + ".txt");

        if(fileSaved){
            this.rmqChannel.basicAck(deliverTag, false);
        }else {

            this.rmqChannel.basicNack(deliverTag, false, true);
        }

    }
}
