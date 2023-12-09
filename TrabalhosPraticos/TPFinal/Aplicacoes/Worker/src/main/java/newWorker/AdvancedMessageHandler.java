package newWorker;

import com.google.gson.GsonBuilder;
import newWorker.Worker;
import newWorker.spread.SMessage;
import spread.AdvancedMessageListener;
import spread.MembershipInfo;
import spread.SpreadGroup;
import spread.SpreadMessage;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import static newWorker.spread.MessageValues.*;
import static newWorker.WState.*;

public class AdvancedMessageHandler implements AdvancedMessageListener {

    private final Worker worker;
    private boolean electionRunning = false;

    public AdvancedMessageHandler(Worker worker){
        this.worker = worker;
    }
    @Override
    public void regularMessageReceived(SpreadMessage spreadMessage) {

        byte[] bMessage = spreadMessage.getData();
        String jsonStr = new String(bMessage, StandardCharsets.UTF_8);
        SMessage message = new GsonBuilder().create().fromJson(jsonStr, SMessage.class);

        long senderID = message.getId();

            try {

                switch (message.getValue()) {
                    case RESUME:
                        this.worker.setResumeExchangeName(message.getExchange());
                        this.worker.setResumeJobType(message.getTipo());
                        startElection(this.worker.getWorkerId());
                        break;

                    case START_ELECTION:
                        printLog(senderID, "START_ELECTION");
                        respondToElection(senderID);
                        break;

                    case OK:
                        printLog(senderID, "OK");
                        respondToElection(senderID);
                        break;

                    case COORDINATOR:
                        printLog(senderID, "COORDINATOR");
                        acknowledgeLeader(senderID);
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    @Override
    public void membershipMessageReceived(SpreadMessage spreadMessage) {
        System.out.println("MemberShip ThreadID:" + Thread.currentThread().getId());
        MembershipInfo info = spreadMessage.getMembershipInfo();
        if (info.isSelfLeave()) {
            System.out.println("Left group:"+info.getGroup().toString());
        } else {
            //if (info.getMembers() != null) {
            SpreadGroup[] members = info.getMembers();
            System.out.println("members of belonging group:"+info.getGroup().toString());
            for (int i = 0; i < members.length; ++i) {
                System.out.print(members[i] + ":");
            }
            System.out.println();
        }
    }

    public void respondToElection(long senderID) throws Exception {
        if(senderID < this.worker.getWorkerId()) {
            worker.sendMessageToAll(this.worker.getWorkerId(), OK);
            startElection(this.worker.getWorkerId());
        }else if(senderID > this.worker.getWorkerId()){
            worker.state = WORKING;
        }
    }

    private void startElection(long workerId) throws Exception {
        worker.sendMessageToAll(workerId, START_ELECTION);
        worker.state = IN_ELECTION;

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (worker.state == IN_ELECTION) {
                            try {
                                declareVictory();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                1000
        );
    }

    public void declareVictory() throws Exception {
            this.worker.setWorkerState(LEADER);
            printLog(this.worker.getWorkerId(), "I am the new leader.");
            worker.sendMessageToAll(this.worker.getWorkerId(), COORDINATOR);
    }


    private void acknowledgeLeader(long senderID) throws Exception {
        System.out.println("SenderID: " + senderID);
        System.out.println("WorkerID: " + this.worker.getWorkerId());
        if (senderID != this.worker.getWorkerId()) {
            System.out.println("Estava em ELECTION. Vou passar o meu estado para WORKING");
            this.worker.setWorkerState(WORKING);
            printLog(this.worker.getWorkerId(), "acknowledges the new leader" + senderID);
        }
    }


    public void printLog(long senderID, String mensagem){

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date resultdate = new Date(System.currentTimeMillis());

        String id = (senderID == this.worker.getWorkerId()) ? "Eu" : String.valueOf(senderID);
        System.out.println(sdf.format(resultdate) + " | " + id + " --> " + mensagem);
    }

}
