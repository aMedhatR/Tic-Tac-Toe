package game;

//import org.json.simple.JSONObject;
import java.sql.Timestamp;

public class Game {

    private int id;
    private String name;
    private int playerOneId;
    private int playerTwoId;
    private int playerOneScore;
    private int playerTwoSore;
    private int winnerId;
    private Timestamp createdAt;
    private String position1;
    private String position2;
//    private GameStatus status;
//    private String board;

    public Game() {
    }

    public Game(int firstPlayer, int secondPlayer, String b) {
        playerOneId = firstPlayer;
        playerTwoId = secondPlayer;
//        board = b;
        winnerId = 0;
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
    public Game(int id, int playerOneId, int playerTwoId, int winner, Timestamp createdAt) {
        this.id = id;
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.winnerId = winner;
        this.createdAt = createdAt;
    }

    public Game(int playerOneId, int playerTwoId, String position1, String position2) {
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.position1 = position1;
        this.position2 = position2;
    }


    public Game(int playerOneId, int playerTwoId, String position1, String position2,int playerOneScore, int playerTwoSore) {
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.position1 = position1;
        this.position2 = position2;
        this.playerOneScore = playerOneScore;
        this.playerTwoSore = playerTwoSore;
    }

    public void setid(int id) {
        this.id = id;
    }

    public int getid() {
        return id;
    }

    public void setplayerOneId(int p) {
        playerOneId = p;
    }

    public void setplayerTwoId(int p) {
        playerTwoId = p;
    }

    public void setWinnerId(int p) {
        winnerId = p;
    }

    public void setPosition1(String position1) {
        this.position1 = position1;
    }

    public void setPosition2(String position2) {
        this.position2 = position2;
    }

    public String getPosition1() {
        return position1;
    }

    public String getPosition2() {
        return position2;
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

    public int getplayerOneId() {
        return playerOneId;
    }

    public int getplayerTwoId() {
        return playerTwoId;
    }

    public int getWinnerId() {
        return winnerId;
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

    public int getPlayerOneScore() {
        return playerOneScore;
    }

    public void setPlayerOneScore(int playerOneScore) {
        this.playerOneScore = playerOneScore;
    }

    public int getPlayerTwoSore() {
        return playerTwoSore;
    }

    public void setPlayerTwoSore(int playerTwoSore) {
        this.playerTwoSore = playerTwoSore;
    }

//    public GameStatus getGameStatus() {
//        return status;
//    }
}
