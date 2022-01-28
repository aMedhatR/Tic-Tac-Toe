package handlePlayerDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import socket.SocketServer;

public class PlayerDAO {

    public Connection c;
    public PreparedStatement stmt;
    Vector<Player> v = new Vector<Player>();
    Player player;

    public PlayerDAO() throws Exception {
        Connect();
    }

    private void Connect() throws Exception {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/tictactoe",
                    "postgres", "1571997n");
            System.out.println("Connected to database");
        } catch (Exception e) {
            System.out.println("javadb.JavaDB" + e);
        }
    }

    private static final String SQL_CREATE = "CREATE TABLE players"
            + "("
            + " id serial,"
            + " name varchar(100) unique NOT NULL,"
            + " password varchar(15) NOT NULL,"
            + " email varchar(100) unique NOT NULL,"
            + " score integer,"
            + " PRIMARY KEY (id)"
            + ")";

    public void CreateDBT() throws SQLException {
        try {
            stmt = c.prepareStatement(SQL_CREATE);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String CreatePlayer(Player player) throws SQLException {
        String res = "";

        try {
            stmt = c.prepareStatement("INSERT INTO Players (name , password , email , score) VALUES (? , ? , ? , ?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setString(1, player.getName());
            stmt.setString(2, player.getPassword());
            stmt.setString(3, player.getEmail());
            stmt.setInt(4, player.getScore());
            stmt.executeUpdate();
            res = "true";

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());

            if (e.getSQLState().equals("23505")) {
                String msg = e.getMessage();
                String whichError = msg.substring(msg.indexOf("(")+1, msg.indexOf(")"));
                System.out.println(" :  " + whichError);
                res =  "false___" + whichError +"___already exist";
            } else {
                res =  "false___please try again";
            }
        } catch (Exception e) {
            e.printStackTrace();
            res =  "false___please try again";
        }
        finally{
            return res;
        }
    }

    public Vector<Player> getPlayer(String name, String password) {
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM players WHERE name=? AND password=?");
            stmt.setString(1, name);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            if (rs.getString("name").equals(name) && rs.getString("password").equals(password)) {

                Player player = new Player(
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getInt("score"));
                //v.add(player);
                //rs.close();
                //stmt.close();
                //c.close();
                System.out.println(rs.getString("name"));

            }
        } catch (SQLException ex) {
//            Logger.getLogger(PlayerModel.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } catch (Exception exc) {
            System.out.println(exc);
        }
        System.out.println("signed in successfully ! yay!");
        return v;

    }
}
