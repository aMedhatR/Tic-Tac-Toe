package startServer;

import handlePlayerDB.PlayerDAO;
import java.net.ServerSocket;
import socket.SocketServer;

public class App {

    private static PlayerDAO db;
    private static ServerSocket serverSocket;

    public static void setDB()
    {
        db = new PlayerDAO();
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
