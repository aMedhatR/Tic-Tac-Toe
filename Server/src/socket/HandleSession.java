package socket;

import player.Player;
import player.PlayerHandler;

public class HandleSession {

    private final String[] cell = new String[9];
    private final boolean continueToPlay = true;
    private Handler handlerPlayer1;
    private Handler handlerPlayer2;
    public int playerId1;
    public int playerId2;
    public String playerName1;
    public String playerName2;

    public int scorePlayer1;
    public int scorePlayer2;

    private int index;
    private String statusGame;

    public HandleSession(int playerId1,String playerName1 ,Handler handlerPlayer1, int playerId2,String playerName2, Handler handlerPlayer2) {
        this.playerId2 = playerId2;
        this.playerId1 = playerId1;

        this.playerName1 = playerName1;
        this.playerName2 = playerName2;

        this.handlerPlayer1 = handlerPlayer1;
        this.handlerPlayer2 = handlerPlayer2;

        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;

        for (int i = 0; i < 9; i++)
            cell[i] = "";
    }

    public void insertMove(int index, String shape) {

        cell[index] = shape;
        System.out.println("player play");
        ///// function to send to player1 and player2
        sentMessageToPlayers("move___" + shape + "___" + index, "move___" + shape + "___" + index);

        if (shape.equals("X")) sentMessageToPlayers("playerTurn___2", "playerTurn___2");
        else sentMessageToPlayers("playerTurn___1", "playerTurn___1");

        System.out.println("checkIfGameFinish :"+ checkIfGameFinish());

        statusGame = checkIfGameIsOver();
        if (statusGame.equals("xWon")) {
            sentMessageToPlayers("status___win", "status___lose");

            handlerPlayer1.playerToDb.changeScore(playerId1,10);
            scorePlayer1 += 10;
            sentMessageToPlayers("updateScore___"+scorePlayer1+"___"+scorePlayer2, "updateScore___"+scorePlayer2+"___"+scorePlayer1);

        } else if (statusGame.equals("oWon")) {
            sentMessageToPlayers("status___lose", "status___win");
            handlerPlayer1.playerToDb.changeScore(playerId2,10);
            scorePlayer2 += 10;
            sentMessageToPlayers("updateScore___"+scorePlayer1+"___"+scorePlayer2, "updateScore___"+scorePlayer2+"___"+scorePlayer1);

        } else if (checkIfGameFinish()) {
            ///// finish game
            sentMessageToPlayers("status___draw", "status___draw");
        }


    }

    public void resetGame()
    {
        for (int i = 0; i < 9; i++)
            cell[i] = "";
        sentMessageToPlayers("playerTurn___1", "playerTurn___1");
    }

    public void sentMessageToPlayers(String msg1, String msg2) {
        handlerPlayer1.ps.println(msg1);
        handlerPlayer2.ps.println(msg2);
    }


    private boolean checkIfGameFinish() {
        for (int a = 0; a < 9; a++) {
            if (cell[a].isEmpty()) return false;
        }
        return true;
    }

    private String checkIfGameIsOver() {
        for (int a = 0; a < 8; a++) {
            String line;
            switch (a) {
                case 0:
                    line = cell[0] + cell[1] + cell[2];
                    break;
                case 1:
                    line = cell[3] + cell[4] + cell[5];
                    break;
                case 2:
                    line = cell[6] + cell[7] + cell[8];
                    break;
                case 3:
                    line = cell[0] + cell[4] + cell[8];
                    break;
                case 4:
                    line = cell[2] + cell[4] + cell[6];
                    break;
                case 5:
                    line = cell[0] + cell[3] + cell[6];
                    break;
                case 6:
                    line = cell[1] + cell[4] + cell[7];
                    break;
                case 7:
                    line = cell[2] + cell[5] + cell[8];
                    break;
                default:
                    line = null;
                    break;
            }

            System.out.println("Winning Line  is " + line);
            //X winner
            if (line.equals("XXX")) return "xWon";

            //O winner
            if (line.equals("OOO")) return "oWon";
        }
        return "continue";
    }

    public Handler getHandlerPlayer1() {
        return handlerPlayer1;
    }

    public Handler getHandlerPlayer2() {
        return handlerPlayer2;
    }

//    private void replaygame() {
//        string msgfrom1, msgfrom2;
//        try {
//            msgfrom1 = fromplayer1.readline();
//            msgfrom2 = fromplayer2.readline();
//
//            if (msgfrom1.equals("yes") && msgfrom2.equals("yes")) {
//                // initialize cells
//                for (int i = 0; i < 9; i++)
//                    cell[i] = " ";
//
//                sentmessagetoplayers("reset", "reset");
//            } else {
//                sentmessagetoplayers("noreplaygame", "noreplaygame");
//                //this.stop();
//            }
//
//        } catch (ioexception e) {
//            e.printstacktrace();
//        }
//
//    }

}
