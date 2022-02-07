package game;

import player.PlayerHandler;
import startServer.App;
import startServer.DbHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameHandler {

    private static final String SQL_CREATE = "CREATE TABLE RestorG"
            + "("
            + " id serial,"
            + " playerOneId integer NOT NULL,"
            + " playerTwoId integer NOT NULL,"
            + " playerOneScore integer NOT NULL,"
            + " playerTwoScore integer NOT NULL,"
            + " position1 varchar(100) NOT NULL,"
            + " position2 varchar(100) NOT NULL,"
            + " PRIMARY KEY (id)"
            + ")";

    static DbHandler db = App.getDB();
    private static Map<Integer, Game> games;

    public static void SaveGame(Game game) {
        try {
            PreparedStatement statment = db.connection.prepareStatement("INSERT INTO RestorG ( playerOneId , playerTwoId ,playerOneScore,playerTwoScore, position1 , position2) VALUES (? , ? , ? , ?,?,?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            statment.setInt(1, game.getplayerOneId());
            statment.setInt(2, game.getplayerTwoId());
            statment.setInt(3, game.getPlayerOneScore());
            statment.setInt(4, game.getPlayerTwoSore());
            statment.setString(5, game.getPosition1());
            statment.setString(6, game.getPosition2());
            statment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Game getGame(int PlayerOneId, int PlayerTwoId) {
        Game game = null;
        try {
            PreparedStatement statment = db.connection.prepareStatement("SELECT * FROM RestorG WHERE (PlayerOneId=? and PlayerTwoId=?)  or (PlayerOneId=? and PlayerTwoId=?) ");
            statment.setInt(1, PlayerOneId);
            statment.setInt(2, PlayerTwoId);
            statment.setInt(3, PlayerTwoId);
            statment.setInt(4, PlayerOneId);
            ResultSet res = statment.executeQuery();
            if (res.next()) {
                game = new Game(
                        res.getInt("playerOneId"),
                         res.getInt("playerTwoId"),
                         res.getString("position1"),
                         res.getString("position2"),
                         res.getInt("playerOneScore"),
                         res.getInt("playerTwoScore")
                );
        DeleteGame(PlayerOneId, PlayerTwoId);
            } else {
                game = null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return res;
        return game;
    }

    public static void DeleteGame(int PlayerOneId, int PlayerTwoId) {
        try {
            PreparedStatement statment = db.connection.prepareStatement("DELETE FROM RestorG WHERE (PlayerOneId=? and PlayerTwoId=?)  or (PlayerOneId=? and PlayerTwoId=?)");
            statment.setInt(1, PlayerOneId);
            statment.setInt(2, PlayerTwoId);
            statment.setInt(3, PlayerTwoId);
            statment.setInt(4, PlayerOneId);
            statment.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void CreateDBT() throws SQLException {
        try {
            PreparedStatement statement = db.connection.prepareStatement(SQL_CREATE);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void getGames() {
//        games = new HashMap<>();
//        try {
//            Statement stmt = db.connection.createStatement();
//            ResultSet resultSet = stmt.executeQuery("SELECT * FROM games");
//            while (resultSet.next()) {
//
//                games.put(resultSet.getInt("id"), gameObject(resultSet));// adding all the players to map
//            }
//
//        } catch (SQLException ex) {
////            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    //    update status
//    public static Boolean updateGameStatus(int id, String status) {
//        try {
//            PreparedStatement preparedStatement = db.connection.prepareStatement("UPDATE games SET status=? WHERE id=?");
//            preparedStatement.setString(1, status);
//            preparedStatement.setInt(2, id);
//            int isUpdated = preparedStatement.executeUpdate();
//            if (isUpdated > 0) {
//                return true;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//    public static Boolean updateGameBoard(JSONObject game, int game_id) {
//        try {
//            PreparedStatement preparedStatement = db.connection.prepareStatement("UPDATE games SET board=? , status=? WHERE id=?");
//            preparedStatement.setString(1, game.toJSONString());
//            preparedStatement.setString(2, GameStatus.PAUSE.toString());
//            preparedStatement.setInt(3, game_id);
//            int isUpdated = preparedStatement.executeUpdate();
//            if (isUpdated > 0) {
//                return true;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
    public static Boolean setWinner(int game_id, int winner_id) {
        try {
            PreparedStatement preparedStatement = db.connection.prepareStatement("UPDATE games SET winner=? WHERE id=?");
            preparedStatement.setInt(1, winner_id);
            preparedStatement.setInt(2, game_id);
            int isUpdated = preparedStatement.executeUpdate();
            if (isUpdated > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean removeGame(int game_id) {
        try {
            PreparedStatement preparedStatement = db.connection.prepareStatement("DELETE FROM games WHERE id=?");
            preparedStatement.setInt(1, game_id);
            int isDeleted = preparedStatement.executeUpdate();
            if (isDeleted > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
