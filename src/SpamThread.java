import java.util.Random;

import static java.lang.Thread.sleep;

public class SpamThread implements Runnable {
    public static int nextID = (int) (Math.random()*1000000.0);
    private String user = ChatClient.user + String.valueOf(SpamThread.nextID++);
    private String secNr = "111";

    @Override
    public void run() {
        ChatClient.write("0 " + user + " " + secNr);
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ChatClient.write(String.valueOf(ChatClient.commandIntegerMap.get(ChatClient.selectedCommand)) + " "
                + user + " "
                + secNr
                + ChatClient.createMsg
                + ChatClient.number
                + ChatClient.upDown
        );
    }
}
