package player;

import database.App;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import database.DbHandler;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerHandler {

    static DbHandler db = App.getDB();

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
//            connention.setAutoCommit(false);
            PreparedStatement statement = db.connection.prepareStatement(SQL_CREATE);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean CreatePlayer(Player player) throws SQLException {

        try {
//            connention.setAutoCommit(false);
            PreparedStatement preparedStatement = db.connection.prepareStatement("INSERT INTO Players (name , password , email , score) VALUES (? , ? , ? )", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, player.getPassword());
            preparedStatement.setString(3, player.getEmail());
            preparedStatement.executeUpdate();

            preparedStatement.close();
//            connention.commit();
//            connention.close();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    Vector<Player> v = new Vector<Player>();
    Player player;

    public Vector<Player> getPlayer(String name, String password) {
        try {
            PreparedStatement stmt = db.connection.prepareStatement("SELECT * FROM players WHERE name=? AND password=?");
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
                v.add(player);
            }
        } catch (SQLException ex) {
//            Logger.getLogger(PlayerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("tictactoedb.PlayerM.getPlayer()");

        return v;

    }
    private static Map<Integer, Player> players;
    public static ArrayList<Player> playerslist;

    public static ArrayList<Player> getPlayers() {
        players = new LinkedHashMap<>();
        playerslist = new ArrayList<Player>();
        try {
            Statement stmt = db.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM players ORDER BY score DESC");
            while (resultSet.next()) {
                players.put(resultSet.getInt("id"), playerObiect(resultSet));
                playerslist.add(playerObiect(resultSet));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return playerslist;
    }

    private static Player playerObiect(ResultSet resultSet) {
        try {
            return new Player(
                    resultSet.getInt("id"), //return id
                    resultSet.getString("name"), //return name
                    resultSet.getString("email"), // return email
                    //                    resultSet.getInt("status"), // return status
                    //                    resultSet.getString("avatar"), //return avatar
                    resultSet.getInt("score") // return score
            );

        } catch (SQLException ex) {
//            Logger.getLogger(PlayerModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Player();
    }
public static Player getPlayer(int id) {

        Player player = players.get(id);
        if (player != null) {
            return player;
        }
        return null;
    }


}
