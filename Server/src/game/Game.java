package game;

//import org.json.simple.JSONObject;
import player.Player;
import java.sql.Timestamp;

public class Game {

    private int id;
    private Player playerOne;
    private Player playerTwo;
    private Player winner;
    private Timestamp createdAt;
//    private GameStatus status;
//    private String board;

    public Game() {
    }

    public Game(Player firstPlayer, Player secondPlayer, String b) {
        playerOne = firstPlayer;
        playerTwo = secondPlayer;
//        board = b;
        winner = null;
        createdAt = new Timestamp(System.currentTimeMillis());
//        status = GameStatus.REQUEST;
    }

    //    Game(int id,Player f_player, Player t_player, Player w_player, Timestamp timestamp, String s, String b) {
//        this.id=id;
//        playerOne = f_player;
//        playerTwo = t_player;
//        winner = w_player;
//        createdAt = timestamp;
////        status = GameStatus.valueOf(s);
////        board = b;
//    }
    Game(int id, Player f_player, Player t_player, Player w_player, Timestamp timestamp) {
        this.id = id;
        playerOne = f_player;
        playerTwo = t_player;
        winner = w_player;
        createdAt = timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setplayerOne(Player p) {
        playerOne = p;
    }

    public void setplayerTwo(Player p) {
        playerTwo = p;
    }

    public void setWinnerPlayer(Player p) {
        winner = p;
    }

    //    public void setBoard(JSONObject cells) {
//
//        board = cells.toJSONString();
//    }
//    public void setGameStatus(GameStatus g) {
//        status = g;
//    }
    public void setCreatedAt() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    public Player getplayerOne() {
        return playerOne;
    }

    public Player getplayerTwo() {
        return playerTwo;
    }

    public Player getWinnerPlayer() {
        return winner;
    }

    //    public String getBoard() {
//        JSONParser jp = new JSONParser();
//        JSONObject bo = new JSONObject();
//        try {
//            bo = (JSONObject) jp.parse(board);
//        } catch (ParseException ex) {
//            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return board;
//    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }

//    public GameStatus getGameStatus() {
//        return status;
//    }
}
