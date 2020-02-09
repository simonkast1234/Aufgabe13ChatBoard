import Prog1Tools.IOTools;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatClient {
    private static String matNr;
    private static String user;
    private static String secNr;
    private static String createMsg;
    private static String number;
    private static String upDown;

    public static void main(String[] args) {

        // Assign each Command an Integer
        Map<Command, Integer> commandIntegerMap = new HashMap<>();
        for (int i = 0; i < Command.values().length; i++) {
            commandIntegerMap.put(Command.values()[i], i);
        }

        // Request initial data from user
        setMatNr();

        // Program loop
        Command selectedCommand;
        while((selectedCommand = setCommand()) != Command.QUIT) {
            write(String.valueOf(commandIntegerMap.get(selectedCommand)) + " "
                        + user + " "
                        + secNr
                        + createMsg
                        + number
                        + upDown
                );
        }
    }

    private static Command setCommand() {
        while(true) {
            refreshVariables();
            String tmp = readConsole(
                    "user " + user + " password " + secNr + " ([s]etUser)\n" +
                            "[r]egister [u]nblock [c]reate [d]elete [v]ote [g]etBoard [q]uit ").toLowerCase();
            switch (tmp) {
                case "r": // register
                    setUser();
                    setSecNr();
                    return Command.REGISTER;
                case "u": // unblock
                    return Command.UNBLOCK;
                case "c": // create
                    createMsg = " " + readConsole("Message to create: ");
                    return Command.CREATE;
                case "d": // delete
                    number = " " + readConsole("ID to delete: ");
                    return Command.DELETE;
                case "v": // vote
                    number = " " + readConsole("ID to vote: ");
                    setUpDown();
                    return Command.VOTE;
                case "g": // getBoard
                    number = " " + readConsole("number of random posts to list: ");
                    return Command.GETBOARD;
                case "s": // setUser
                    setUser();
                    setSecNr();
                    return setCommand();
                case "q": // quit
                    System.out.println("bye");
                    return Command.QUIT;
            }
        }
    }

    private static void refreshVariables() {
        createMsg = "";
        number = "";
        upDown = "";
    }

    private static void setUpDown() {
        String tmp = readConsole("up or down: ");
        do {
            if(tmp.equalsIgnoreCase("up")) upDown = " up";
            if(tmp.equalsIgnoreCase("down")) upDown = " down";
        } while(!(tmp.equalsIgnoreCase("up") || tmp.equalsIgnoreCase("down")));
    }

    private static void setMatNr() {
        do {
            matNr = readConsole("MatNr: ");
        } while(matNr == null || matNr.length() != 6);
    }

    private static void setUser() {
        do {
            user = readConsole("username: ");
        } while(user.length() == 0);
    }

    private static void setSecNr() {
        do {
            secNr = readConsole("secNr: ");
        } while(secNr.length() == 0);
    }

    private static void write(String msg) {
        try(Socket s = new Socket("vm1.mcc.tu-berlin.de",8080);
            BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());
            InputStream is = s.getInputStream()) {

            // Authenticate
            bos.write(matNr.getBytes()); bos.flush(); bos.write(-1); bos.flush();
            String serverText = read(is);
            if(!serverText.equals("Authentication ok")) throw new IOException("Authentication failed, server: " + serverText);

            // send msg
            bos.write(msg.getBytes()); bos.flush(); bos.write(-1); bos.flush();

            // print reply
            System.out.println(read(is));
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String read(InputStream is) throws IOException {
        List<Byte> data = new ArrayList<>();
        do {
            data.add((byte)is.read());
        } while(!data.get(data.size()-1).equals(Byte.valueOf("-1")));
        data.remove(data.size()-1);

        String dataString = "";
        for (int i = 0; i < data.size(); i++) {
            dataString += (char)data.get(i).byteValue();
        }
        return dataString;
    }

    private static String readConsole(String msg) {
        //return IOTools.readString(msg);
        System.out.print(msg);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (Exception e) {
            return "";
        }

    }

}