package mainPk;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class HandleOnlineSocket {

    private static Socket mySocket;
    private static DataInputStream dis;
    private static PrintStream ps;
    public static String remoteIP;
    public static Integer portNumber;

    public HandleOnlineSocket()
    {
        try {
            if(remoteIP.equals(null)) {
                mySocket = new Socket("127.0.0.1", 5100);
            }
            else  mySocket = new Socket(remoteIP, portNumber);

            //  mySocket = new Socket("154.183.150.115",8080);


            dis = new DataInputStream(mySocket.getInputStream());
            ps = new PrintStream(mySocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("server is down");
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
