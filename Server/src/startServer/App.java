package startServer;

import handlePlayerDB.PlayerDAO;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import socket.SocketServer;

public class App {

    private static PlayerDAO db;
    private static ServerSocket serverSocket;

    public static void setDB()
    {
        try {
            db = new PlayerDAO();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setServerSocket(ServerSocket serverSocket1)
    {
        serverSocket = serverSocket1;
    }

    public static PlayerDAO getDB()
    {
        return db;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

}
