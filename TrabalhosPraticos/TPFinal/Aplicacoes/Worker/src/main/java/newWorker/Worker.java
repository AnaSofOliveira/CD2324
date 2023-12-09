package newWorker;

import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import newWorker.rabbit.PoSMsgHandler;
import newWorker.rabbit.RMessage;
import newWorker.spread.SMessage;
import newWorker.spread.MessageValues;
import spread.SpreadConnection;
import spread.SpreadGroup;
import spread.SpreadMessage;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static newWorker.WState.*;


public class Worker extends Thread{

    private static String rmqBrokerIP ="34.28.139.32";
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

    public Worker(long id, String spreadDaemon, String jobType){
        try {
            this.id = id;
            this.mainJobType = jobType;
            this.state = WORKING;
            this.connection = new SpreadConnection();
            this.connection.connect(InetAddress.getByName(spreadDaemon), 0, String.valueOf(id), false, true);
            new SpreadGroup().join(this.connection, "workers");
            this.handler = new AdvancedMessageHandler(this);
            this.connection.add(this.handler);


            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rmqBrokerIP);
            factory.setPort(5672);
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
        System.out.println("ID: " + this.id);
        while(!end){
            switch (this.state){
                case WORKING:
                    registerSales();
                    break;

                case IN_ELECTION:
                    break;

                case LEADER:
                    try {
                        resumeSales();
                    } catch (IOException e) {
                        System.out.println("Não foi possível agregar os ficheiros: " + e.getMessage());
                    }
                    break;

            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void resumeSales() throws IOException {
        System.out.println("Vou agregar as vendas.");

        long fileId = System.currentTimeMillis();

        String outputPathFile = "/usr/sharedfiles/resume/resume_" + fileId + ".txt";
        System.out.println("Output Path: " + outputPathFile);

        List<String> files = FilesManager.getFilesInDirectory("/usr/sharedfiles");
        System.out.println("Files: " + files);

        FilesManager.filterFiles(files, outputPathFile, resumeJobType);

        System.out.println(outputPathFile);

        RMessage message = new RMessage(fileId, outputPathFile);
        String jsonMessage = new GsonBuilder().create().toJson(message);

        rmqChannel.basicPublish(this.resumeExchangeName, "", true, null, jsonMessage.getBytes(StandardCharsets.UTF_8));

        this.state = WORKING;
    }

    private void registerSales() {
        System.out.println("Vou registar as vendas do PointOfSales.");

        PoSMsgHandler gestorMensagens = new PoSMsgHandler(rmqChannel, this.id);

        try {
            String consumerTag = rmqChannel.basicConsume(mainJobType, false, gestorMensagens, gestorMensagens);

        } catch (IOException e) {
            System.out.println("Erro ao consumir mensagem");
        }

        // Registar como consumer da queue JobType

        // Ler mensagens e guardar em ficheiro

        // Ficar à espera na queue do jobType indicado
        // Lêr mensagens e registar no ficheiro

        /**
         * 1. Criar relatório com ID à medica que entram novas vendas a partir da queue onde estou registado.
         */

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
        long id = System.currentTimeMillis();
        String jobType = "casa"; // "casa"
        String DaemonIP = "localhost";

        System.out.println("ID: " + id );
        System.out.println("Job Type: " + jobType );
        System.out.println("Daemon IP: " + DaemonIP );

        try {
            Worker newWorker = new Worker(id, DaemonIP, jobType);
            newWorker.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }






}
