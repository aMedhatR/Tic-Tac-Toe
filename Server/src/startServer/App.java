package startServer;


import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static DbHandler db;
    private static ServerSocket serverSocket;

    public static void setDB()
    {
        try {
            db = new DbHandler();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setServerSocket(ServerSocket serverSocket1)
    {
        serverSocket = serverSocket1;
    }

    public static DbHandler getDB()
    {
        return db;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

}
