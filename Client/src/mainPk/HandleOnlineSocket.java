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
<<<<<<< HEAD
//            mySocket = new Socket("127.0.0.1", 5100);
=======
            //mySocket = new Socket("127.0.0.1", 5100);
>>>>>>> 97b2610dffbee4803cc00a7f4db756bb7040e75f
            mySocket = new Socket("197.53.101.24", 8080);

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
