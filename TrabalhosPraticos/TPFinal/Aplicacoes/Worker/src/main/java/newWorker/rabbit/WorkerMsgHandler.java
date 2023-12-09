package newWorker.rabbit;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;

public class WorkerMsgHandler implements DeliverCallback, CancelCallback {

    private final Channel rmqChannel;
    public WorkerMsgHandler(Channel rmqChannel) {
        this.rmqChannel = rmqChannel;
    }

    @Override
    public void handle(String consumerTag) throws IOException {
        /**
         * Tratamento de mensagem cancelada
         */
        System.out.println("A mensagem foi cancelada! " + consumerTag);
    }

    @Override
    public void handle(String consumerTag, Delivery message) throws IOException {

    }
}
