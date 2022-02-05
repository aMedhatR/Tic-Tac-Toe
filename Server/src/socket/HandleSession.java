package socket;

public class HandleSession {


    private final String[] cell = new String[9];
    private final boolean continueToPlay = true;
    private final Handler handlerPlayer1;
    private final Handler handlerPlayer2;
    public int playerId1;
    public int playerId2;
    private int index;
    private String statusGame;

    public HandleSession(int playerId1, Handler handlerPlayer1, int playerId2, Handler handlerPlayer2) {
        this.playerId2 = playerId2;
        this.playerId1 = playerId1;
        this.handlerPlayer1 = handlerPlayer1;
        this.handlerPlayer2 = handlerPlayer2;

        for (int i = 0; i < 9; i++)
            cell[i] = " ";
    }

    public void insertMove(int index, String shape) {

        cell[index] = shape;
        System.out.println("player play");
        ///// function to send to player1 and player2
        sentMessageToPlayers("move___" + shape + "___" + index, "move___" + shape + "___" + index);

        if (shape.equals("X")) sentMessageToPlayers("playerTurn___2", "playerTurn___2");
        else sentMessageToPlayers("playerTurn___1", "playerTurn___1");

        statusGame = checkIfGameIsOver();
        if (statusGame.equals("xWon")) {
            sentMessageToPlayers("won___x1___", "loss___o2___");

        } else if (statusGame.equals("oWon")) {
            sentMessageToPlayers("won___o2___", "loss___x1___");

        } else if (checkIfGameFinish()) {
            ///// finish game
            sentMessageToPlayers("draw", "draw");
        }


    }


    private void sentMessageToPlayers(String msg1, String msg2) {
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
