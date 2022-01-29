package database;

import database.DbHandler;
import player.PlayerHandler;
import player.Player;
import java.net.ServerSocket;


public class App {
    private static DbHandler db;
    private static ServerSocket serverSocket;

    public static void setDB() {
        db = new DbHandler();
        PlayerHandler.getPlayers();
    }

    public static DbHandler getDB() {
        return db;
    }

    public static void setServerSocket(ServerSocket serverSocket1) {
        serverSocket = serverSocket1;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

}
