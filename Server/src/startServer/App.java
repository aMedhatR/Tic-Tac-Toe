package startServer;

import handlePlayerDB.PlayerDAO;
import socket.SocketServer;

public class App {

    private static PlayerDAO db;
    private static SocketServer serverSocket;

    public static void setDb()
    {
        db = new PlayerDAO();
    }

    public static void setServerSocket(SocketServer serverSocket1)
    {
        serverSocket = serverSocket1;
    }

    public static PlayerDAO getDB()
    {
        return db;
    }

    public static SocketServer getServerSocket() {
        return serverSocket;
    }

}
