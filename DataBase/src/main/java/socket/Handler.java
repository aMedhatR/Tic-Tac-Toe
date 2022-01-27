package socket;

import handlePlayerDB.Player;
import startServer.App;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

public class Handler extends Thread{

    DataInputStream dis;
    PrintStream ps;
    static Vector<Handler> handleVector = new Vector<Handler>();

    public Handler(Socket s) {
        try {
            dis = new DataInputStream(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
            handleVector.add(this);
            start();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        while (true) {
            try {
                String msg = dis.readLine();
               String[] allMsg =  msg.split("___");
               switch (allMsg[0]) {
                   case "signUp":
                       App.getDB().CreatePlayer();
                       break;
               }
                System.out.println(msg);



            } catch (IOException ioEs) {
                System.out.println(ioEs);
            }
        }
    }
}
