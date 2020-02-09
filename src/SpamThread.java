import java.util.Random;

public class SpamThread implements Runnable {
    public static double nextID = Math.random()*10000000000000.0;
    private String user = "SK:"+String.valueOf(SpamThread.nextID++) + "-spammingThread";
    private String secNr = "111";

    @Override
    public void run() {
        ChatClient.write(String.valueOf(ChatClient.commandIntegerMap.get(ChatClient.selectedCommand)) + " "
                + user + " "
                + secNr
                + ChatClient.createMsg
                + ChatClient.number
                + ChatClient.upDown
        );
    }
}
