package socket;

import player.PlayerHandler;
import startServer.App;

import java.net.Socket;
import java.net.ServerSocket;

public class SocketServer {

    ServerSocket serverSocket;
    Socket s;


    public SocketServer() {
        try {
            serverSocket = new ServerSocket(5200);
            System.out.println("Connected to serverSocket");

            App.setServerSocket(serverSocket);
            App.setDB();


            
            while (true) {
                Socket s = serverSocket.accept();
                new Handler(s);
            }

//            ps.close();
//            dis.close();
//            s.close();
//            serverSocket.close();
        } catch (Exception e) {
            System.out.println("error from serverSocket: " + e);
        }
    }



}
