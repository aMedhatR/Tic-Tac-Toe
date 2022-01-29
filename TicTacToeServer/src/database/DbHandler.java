package database;

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

    private void startConnect() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tictactoe",
                    "postgres", "E$$r@@");
            System.out.println("javadb.JavaDB.Connect()");
        } catch (Exception e) {
            System.out.println("javadb.JavaDB");
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
