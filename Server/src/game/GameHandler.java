
package game;

import player.PlayerHandler;

import startServer.App;
import startServer.DbHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameHandler {

    static DbHandler db = App.getDB();
    private static Map<Integer, Game> games;

    public static void getGames() {
        games = new HashMap<>();
        try {
            Statement stmt = db.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM games");
            while (resultSet.next()) {

                games.put(resultSet.getInt("id"), gameObject(resultSet));// adding all the players to map
            }

        } catch (SQLException ex) {
//            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Game gameObject(ResultSet resultSet) {
        try {
            return new Game(
                    resultSet.getInt("id"),
                    PlayerHandler.getPlayer(resultSet.getInt("playerOne")),
                    PlayerHandler.getPlayer(resultSet.getInt("playerTwo")),
                    PlayerHandler.getPlayer(resultSet.getInt("winner")),
                    resultSet.getTimestamp("created_at")
//                    resultSet.getString("status"),
//                    resultSet.getString("board")
            );
        } catch (SQLException ex) {
            Logger.getLogger(GameHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    public static int createGame(JSONObject game) {
//        try {
//            PreparedStatement statment = db.connection.prepareStatement("INSERT games SET from_player=?, to_player=?", Statement.RETURN_GENERATED_KEYS);
//            statment.setString(1, game.get("from_id").toString());
//            statment.setString(2, game.get("to_id").toString());
//            int isInsearted = statment.executeUpdate();
//            if (isInsearted > 0) {
//                ResultSet keys = statment.getGeneratedKeys();
//                if (keys.next()) {
//                    int id = keys.getInt(1);
//                    return id;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    public static Game getGame(int id) {
        try {
            PreparedStatement statment = db.connection.prepareStatement("SELECT * FROM games WHERE id=?");
            statment.setInt(1, id);
            ResultSet res = statment.executeQuery();
            if (res.next()) {
                return gameObject(res);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


//    public static int getGame(JSONObject jsonObject) {
//        try {
//            PreparedStatement statment = db.connection.prepareStatement("SELECT * FROM games WHERE status='PAUSE' AND from_player=? AND to_player=?");
//            statment.setString(1, jsonObject.get("from_id").toString());
//            statment.setString(2, jsonObject.get("to_id").toString());
//            ResultSet res = statment.executeQuery();
//            if (res.next()) {
//                return res.getInt("id");
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(PlayerModel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return 0;
//    }
//
//
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
