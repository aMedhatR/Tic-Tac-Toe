package socket;

import startServer.App;

import java.net.Socket;
import java.net.ServerSocket;

public class SocketServer {

    ServerSocket serverSocket;
    Socket s;

    public SocketServer() {
        try {
            serverSocket = new java.net.ServerSocket(5005);
            App.setServerSocket(serverSocket);
            App.getDB();

            while (true) {
                Socket s = serverSocket.accept();
                new Handler(s);
            }

//            ps.close();
//            dis.close();
//            s.close();
//            serverSocket.close();
        } catch (Exception e) {
            System.out.println("error from server " + e);
        }
    }



}
