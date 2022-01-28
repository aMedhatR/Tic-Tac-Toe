package socket;

import handlePlayerDB.Player;
import startServer.App;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Handler extends Thread {

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
                String[] allMsg = msg.split("___");
                switch (allMsg[0]) {
                    case "signUp":
                        signUp(allMsg);
                        break;
                }
            } catch (IOException ioEs) {
                System.out.println(ioEs);
            } 
        }
    }
    
    
    public void signUp(String[] allMsg)
    {
        Player player = new Player(allMsg[1],allMsg[3],allMsg[2],10);
        try {
            String res = App.getDB().CreatePlayer(player);
            ps.println(res);
        } catch (SQLException ex2) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex2);
        }
    }
    
}
