package player;

import startServer.App;
import startServer.DbHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerHandler {

    private static final String SQL_CREATE = "CREATE TABLE players"
            + "("
            + " id serial,"
            + " name varchar(100) unique NOT NULL,"
            + " password varchar(15) NOT NULL,"
            + " email varchar(100) unique NOT NULL,"
            + " score integer,"
            + " status boolean NOT NULL DEFAULT FALSE,"
            + " PRIMARY KEY (id)"
            + ")";
    public static ArrayList<Player> playerslist;
    static DbHandler db = App.getDB();
    private static Map<Integer, Player> players;


//    private static final String SQL_UPDATE_TABLE = "ALTER TABLE players ADD"
//            + " status boolean NOT NULL DEFAULT FALSE";
//
//
//    public void UPDATETablePlayers() throws SQLException {
//        try {
//            PreparedStatement statement = db.connection.prepareStatement(SQL_UPDATE_TABLE);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    Vector<Player> v = new Vector<Player>();
    Player player;

    public static ResultSet getPlayers() {
        players = new LinkedHashMap<>();
        playerslist = new ArrayList<Player>();
        ResultSet resultSet ;
        try {
            Statement stmt = db.connection.createStatement();
            resultSet = stmt.executeQuery("SELECT * FROM players ORDER BY status DESC, score DESC ");
//            while (resultSet.next()) {
//                players.put(resultSet.getInt("id"), playerObiect(resultSet));
//                playerslist.add(playerObiect(resultSet));
            return resultSet;
//            }

        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
       // return playerslist;
    }

    private static Player playerObiect(ResultSet resultSet) {
        try {
            return new Player(
                    resultSet.getInt("id"), //return id
                    resultSet.getString("name"), //return name
                    // resultSet.getString("email"), // return email
                    // return status
                    // resultSet.getString("avatar"), //return avatar
                    resultSet.getInt("score"), // return score
                    resultSet.getBoolean("status")
            );

        } catch (SQLException ex) {
//            Logger.getLogger(PlayerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Player();
    }

    public static Player getPlayer(int id) {

        Player player = players.get(id);
        return player;
    }

    public void CreateDBT() throws SQLException {
        try {
//            connention.setAutoCommit(false);
            PreparedStatement statement = db.connection.prepareStatement(SQL_CREATE);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String CreatePlayer(Player player) throws SQLException {
        String res = "";
        try {
            PreparedStatement stmt = db.connection.prepareStatement("INSERT INTO Players (name , password , email , score) VALUES (? , ? , ? , ?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
                String whichError = msg.substring(msg.indexOf("(") + 1, msg.indexOf(")"));
                System.out.println(" :  " + whichError);
                res = "false___" + whichError + "___already exist";
            } else {
                res = "false___please try again";
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = "false___please try again";
        } finally {
            return res;
        }
    }

    public String getPlayer(String name, String password) {
        String res = "";
        try {
            PreparedStatement stmt = db.connection.prepareStatement("SELECT * FROM players WHERE name=? AND password=?");
            stmt.setString(1, name);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            //rs.next();
            boolean isExist = rs.next();

            if (isExist) {
                res = "true___" + rs.getString("name") + rs.getInt("score");
                PreparedStatement stmtUpdate = db.connection.prepareStatement("UPDATE players set status=TRUE WHERE name=? ");
                stmtUpdate.setString(1, name);
                int updataNumber = stmtUpdate.executeUpdate();
                System.out.println(updataNumber);
                ////////////// update status
                Player player = new Player(
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getInt("score"));
                v.add(player);
            } else {
                // if user don't exist
                res = "false___notExist";
            }
        } catch (SQLException ex) {
            res = "false___error";
            System.out.println(ex);
//            Logger.getLogger(PlayerModel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return res;
        }

    }


}
