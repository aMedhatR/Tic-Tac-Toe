package mainPk;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class HandleOnlineSocket {

    private static Socket mySocket;
    private static DataInputStream dis;
    private static PrintStream ps;

    public HandleOnlineSocket()
    {
        try {
            mySocket = new Socket("127.0.0.1", 5100);

<<<<<<< HEAD
            mySocket = new Socket("154.183.150.115",8080);
=======

           // mySocket = new Socket("154.183.150.115",8080);

>>>>>>> c8543ec9b6f2b05e19c3611aad0aa9ff3f10eca8

            dis = new DataInputStream(mySocket.getInputStream());
            ps = new PrintStream(mySocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataInputStream getReceiveStream()
    {
        return dis;
    }

    public static PrintStream getSendStream()
    {
        return ps;
    }

    public static Socket getMySocket() {
        return mySocket;
    }
}
