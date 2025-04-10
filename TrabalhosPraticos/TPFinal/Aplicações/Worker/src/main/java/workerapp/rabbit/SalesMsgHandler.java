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

public class SalesMsgHandler implements CancelCallback, DeliverCallback {
    private final Channel rmqChannel;
    private final long workerID;
    private static final String SHARED_FOLDER = "/var/sharedfiles/";

    public SalesMsgHandler(Channel rmqChannel, long workerID) {
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
        SalesMessage salesMessage = new GsonBuilder().create().fromJson(jsonMessage, SalesMessage.class);

        boolean fileSaved = FilesManager.addProductToFile(salesMessage, SHARED_FOLDER + workerID + ".txt");

        if(fileSaved){
            this.rmqChannel.basicAck(deliverTag, false);
        }else {

            this.rmqChannel.basicNack(deliverTag, false, true);
        }

    }
}
