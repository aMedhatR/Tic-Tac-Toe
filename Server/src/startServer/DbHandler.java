package startServer;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbHandler {

    public Connection connection;
    public Statement statement;
    public PreparedStatement preparedStatement;
    public String query;
    public ResultSet resultset;

    public DbHandler()
    {
        try {
            startConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startConnect() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tictactoe",
                    "postgres", "root");
            System.out.println("Connected to serverSocket db");
        } catch (Exception e) {
            System.out.println("javadb.JavaDB" +e);
        }

    }

    private void endConnection() {
        try {
            resultset.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

