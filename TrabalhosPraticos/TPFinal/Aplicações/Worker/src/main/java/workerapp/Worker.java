package workerapp;

import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import workerapp.rabbit.PoSMsgHandler;
import workerapp.rabbit.RMessage;
import workerapp.spread.AdvancedMessageHandler;
import workerapp.spread.SMessage;
import workerapp.spread.MessageValues;
import spread.SpreadConnection;
import spread.SpreadGroup;
import spread.SpreadMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static workerapp.WState.*;


public class Worker extends Thread{

    private static final String PATH2SAVE_RESUME = "/var/sharedfiles/resume/";
    private static final String SHARED_FOLDER = "/var/sharedfiles";
    private static final String SPREAD_GROUP_NAME = "workers";
    public WState state;
    private long id;
    private SpreadConnection connection;
    private AdvancedMessageHandler handler;
    private boolean end = false;
    private String resumeExchangeName;
    private String resumeJobType;
    private String mainJobType;

    Channel rmqChannel;
    Connection rmqCon;

    public Worker(String jobType, String rmqBrokerIP, int rmqBrokerPort, String spreadDaemonIP, int spreadDaemonPort){
        try {
            this.id = System.currentTimeMillis();
            this.mainJobType = jobType;
            this.state = WORKING;
            this.connection = new SpreadConnection();
            this.connection.connect(InetAddress.getByName(spreadDaemonIP), spreadDaemonPort, String.valueOf(id), false, true);
            new SpreadGroup().join(this.connection, SPREAD_GROUP_NAME);
            this.handler = new AdvancedMessageHandler(this);
            this.connection.add(this.handler);

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rmqBrokerIP);
            factory.setPort(rmqBrokerPort); //5672
            rmqCon = factory.newConnection();
            rmqChannel = rmqCon.createChannel();


        } catch (Exception e) {
            System.out.println("Error Message: " + e.getMessage());
            try {
                rmqChannel.close();
                rmqCon.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    @Override
    public void run() {
        System.out.println("Worker ID: " + this.id);
        System.out.println("Função: Registar itens da categoria " + this.mainJobType );

        while(!end){
            switch (this.state){
                case WORKING:
                    registerSales();
                    break;

                case IN_ELECTION:
                    break;

                case LEADER:
                    resumeSales();
                    break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void resumeSales(){
        System.out.println("A criar ficheiro de resumo de vendas do tipo " + resumeJobType);
        long fileId = System.currentTimeMillis();

        String fileName = "resume_" + fileId + ".txt";

        String outputPathFile = PATH2SAVE_RESUME + fileName;

        List<String> files = FilesManager.getFilesInDirectory(SHARED_FOLDER);

        FilesManager.filterFiles(files, outputPathFile, resumeJobType);

        System.out.println("Ficheiro de resumo " + fileName + " guardado.");

        RMessage message = new RMessage(fileId, outputPathFile);
        String jsonMessage = new GsonBuilder().create().toJson(message);

        try {
            rmqChannel.basicPublish(this.resumeExchangeName, "", true, null, jsonMessage.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Não foi possível agregar os ficheiros: " + e.getMessage());
        }

        this.state = WORKING;
    }

    private void registerSales() {
        PoSMsgHandler gestorMensagens = new PoSMsgHandler(rmqChannel, this.id);

        try {
            rmqChannel.basicConsume(mainJobType, false, gestorMensagens, gestorMensagens);

        } catch (IOException e) {
            System.out.println("Erro ao consumir mensagem");
        }
    }

    public WState getWorkerState(){
        return this.state;
    }
    
    public long getWorkerId(){
        return this.id;
    }

    public void setWorkerState(WState estado) {
        this.state = estado;
    }

    public void setResumeExchangeName(String resumeExchangeName) {
        this.resumeExchangeName = resumeExchangeName;
    }

    public void setResumeJobType(String resumeJobType) {
        this.resumeJobType = resumeJobType;
    }

    public void sendMessageToAll(long senderID, MessageValues messageValue) throws Exception {

        SMessage message = new SMessage(senderID, messageValue, null, null);

        String json = new GsonBuilder().create().toJson(message);

        SpreadMessage spreadMessage = new SpreadMessage();
        spreadMessage.setData(json.getBytes(StandardCharsets.UTF_8));
        spreadMessage.addGroup(SPREAD_GROUP_NAME);
        this.connection.multicast(spreadMessage);
    }


    public static void main(String[] args) {

        String jobType = "casa";

        String rmqBrokerIP = "34.16.113.117";
        int rmqBrokerPort = 5672;

        String spreadDaemonIP = "localhost";
        int spreadDaemonPort = 4803;

        if(args.length > 0){
            jobType = args[0];

            rmqBrokerIP = args[1];
            rmqBrokerPort = Integer.parseInt(args[2]);

            spreadDaemonIP = args[3];
            spreadDaemonPort = Integer.parseInt(args[4]);
        }

        try {
            Worker newWorker = new Worker(jobType, rmqBrokerIP, rmqBrokerPort, spreadDaemonIP, spreadDaemonPort);
            newWorker.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }






}
