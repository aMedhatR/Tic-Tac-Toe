package socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class HandleSession {


    public int playerId1;
    public int playerId2;

    private final String[] cell = new String[9];
    private final boolean continueToPlay = true;

    private int index;
    private String statusGame;

    public HandleSession(int playerId1,int playerId2) {
        this.playerId2 = playerId2;
        this.playerId1 = playerId1;
        for (int i = 0; i < 9; i++)
            cell[i] = " ";
    }

//    public void insertMove()
//    {
//        sentMessageToPlayers("playerTurn___1", "playerTurn___1");
//
//        String msg1 = fromPlayer1.readLine();
//        String[] allMsg1 = msg1.split("___");
//        index = Integer.parseInt(allMsg1[0]);
//        cell[index] = "X";
//
//        System.out.println("xxx");
//        ///// function to send to player1 and player2
//        sentMessageToPlayers("move___X___" + index, "move___X___" + index);
//
//
//        statusGame = checkIfGameIsOver();
//        if (statusGame.equals("xWon")) {
//            ////////////// send 1 ,2 players
//            sentMessageToPlayers("won___x1___", "loss___o2___");
//            ///// finish game
//            replayGame();
//
//        }
//
//        if (!checkIfGameFinish()) {
//            sentMessageToPlayers("playerTurn___2", "playerTurn___2");
//            String msg2 = fromPlayer2.readLine();
//            String[] allMsg2 = msg2.split("___");
//            index = Integer.parseInt(allMsg2[0]);
//            cell[index] = "O";
//            ///// function to send to player1 and player2
//            sentMessageToPlayers("move___O___" + index, "move___O___" + index);
//
//
//            statusGame = checkIfGameIsOver();
//            if (statusGame.equals("oWon")) {
//                ////////////// send 2 players
//                sentMessageToPlayers("won___o2___", "loss___x1___");
//                ///// finish game
//                replayGame();
//            }
//            if (checkIfGameFinish()) {
//                ///// finish game
//                sentMessageToPlayers("draw", "draw");
//                replayGame();
//            }
//
//        } else {
//            ///// finish game
//            sentMessageToPlayers("draw", "draw");
//            replayGame();
//        }
//
//    }
//    }
//
//
//
//    private boolean checkIfGameFinish() {
//        for (int a = 0; a < 8; a++) {
//            if (cell[a].isEmpty()) return false;
//        }
//        return true;
//    }
//
//    private String checkIfGameIsOver() {
//        for (int a = 0; a < 8; a++) {
//            String line;
//            switch (a) {
//                case 0:
//                    line = cell[0] + cell[1] + cell[2];
//                    break;
//                case 1:
//                    line = cell[3] + cell[4] + cell[5];
//                    break;
//                case 2:
//                    line = cell[6] + cell[7] + cell[8];
//                    break;
//                case 3:
//                    line = cell[0] + cell[4] + cell[8];
//                    break;
//                case 4:
//                    line = cell[2] + cell[4] + cell[6];
//                    break;
//                case 5:
//                    line = cell[0] + cell[3] + cell[6];
//                    break;
//                case 6:
//                    line = cell[1] + cell[4] + cell[7];
//                    break;
//                case 7:
//                    line = cell[2] + cell[5] + cell[8];
//                    break;
//                default:
//                    line = null;
//                    break;
//            }
//
//            System.out.println("Winning Line  is " + line);
//            //X winner
//            if (line.equals("XXX")) return "xWon";
//
//            //O winner
//            if (line.equals("OOO")) return "oWon";
//        }
//        return "continue";
//    }
//
//    private void sentMessageToPlayers(String msg1, String msg2) {
//        toPlayer1.println(msg1);
//        toPlayer2.println(msg2);
//    }
//
//    private void replayGame() {
//        String msgFrom1, msgFrom2;
//        try {
//            msgFrom1 = fromPlayer1.readLine();
//            msgFrom2 = fromPlayer2.readLine();
//
//            if (msgFrom1.equals("yes") && msgFrom2.equals("yes")) {
//                // Initialize cells
//                for (int i = 0; i < 9; i++)
//                    cell[i] = " ";
//
//                sentMessageToPlayers("reset", "reset");
//            } else {
//                sentMessageToPlayers("noReplayGame", "noReplayGame");
//                //this.stop();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

}
