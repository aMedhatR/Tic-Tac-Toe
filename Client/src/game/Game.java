package game;

public class Game {
    private static int idAnotherPlayer;
    private static String nameAnotherPlayer;
    private static int scoreAnotherPlayer ;
    private static int scorePlayer ;

    private static String postionAnotherPlayer ;

    public static String getPositionPlayer() {
        return positionPlayer;
    }

    public static void setPositionPlayer(String positionPlayer) {
        Game.positionPlayer = positionPlayer;
    }

    private static String positionPlayer ;


    public static int getIdAnotherPlayer() {
        return idAnotherPlayer;
    }

    public static void setIdAnotherPlayer(int idAnotherPlayer) {
        Game.idAnotherPlayer = idAnotherPlayer;
    }

    public static String getNameAnotherPlayer() {
        return nameAnotherPlayer;
    }

    public static void setNameAnotherPlayer(String nameAnotherPlayer) {
        Game.nameAnotherPlayer = nameAnotherPlayer;
    }

    public static int getScoreAnotherPlayer() {
        return scoreAnotherPlayer;
    }

    public static void setScoreAnotherPlayer(int scoreAnotherPlayer) {
        Game.scoreAnotherPlayer = scoreAnotherPlayer;
    }

    public static int getScorePlayer() {
        return scorePlayer;
    }

    public static void setScorePlayer(int scorePlayer) {
        Game.scorePlayer = scorePlayer;
    }

    public static String getPostionAnotherPlayer() {
        return postionAnotherPlayer;
    }

    public static void setPostionAnotherPlayer(String postionAnotherPlayer) {
        Game.postionAnotherPlayer = postionAnotherPlayer;
    }
}
