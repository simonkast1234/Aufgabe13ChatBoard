import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class CalculateThread implements Runnable {
    Socket client;

    public CalculateThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            String calculation;
            String[] values;
            int erg;
            while((calculation = br.readLine()) != null) {
                values = calculation.split("\\+");
                erg = Integer.parseInt(values[0]) + Integer.parseInt(values[1]);
                System.out.println(calculation + "=" + erg);

                bw.write("Dein Ergebnis ist " + erg + "\n");
                bw.flush(); // wird erst beim Schließen oder Flushen losgeschickt ! Beim Closen wird aber die Socketverbindung geschlossen
            }
            br.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 while((calculation = br.readLine()) != null) {
 String[] values = calculation.split("\\+");
 System.out.println(calculation + "=" + (Integer.parseInt(values[0]) + Integer.parseInt(values[1])));
 }
 **/