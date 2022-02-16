package mainPk;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class HandleOnlineSocket {

    private static Socket mySocket;
    private static DataInputStream dis;
    private static PrintStream ps;
    private static String remoteIP ="127.0.0.1";
    private static String portNumber ="5100";

    public HandleOnlineSocket()
    {
        try {
            System.out.println(remoteIP);
            System.out.println(portNumber);

            mySocket = new Socket(remoteIP, Integer.parseInt(portNumber));

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


    public static void setRemoteIP(String valueRemoteIP)
    {
        System.out.println(valueRemoteIP);
        remoteIP = valueRemoteIP;
    }

    public static void setvaluePortNumber(String valuePortNumber)
    {
        System.out.println(valuePortNumber);
        portNumber = valuePortNumber;
    }

}
