package socket;

import startServer.App;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class SocketServer {

    ServerSocket serverSocket;
    Socket s;


    public SocketServer() {
        try {
            serverSocket = new ServerSocket(5100);
            System.out.println("Connected to serverSocket");

            App.setServerSocket(serverSocket);



            
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
           for(Handler hand : Handler.handleVector)
           {
               hand.ps.close();
               try {
                   hand.dis.close();
                   hand.socketTo.close();
               } catch (IOException ex) {
                   ex.printStackTrace();
               }

           }
        }
    }



}
