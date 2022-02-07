package socket;

import game.Game;
import game.GameHandler;
import player.Player;
import player.PlayerHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Handler extends Thread {

    static Vector<Handler> handleVector = new Vector<Handler>();
    static HashMap<Integer, Handler> handleVectorWithID = new HashMap<Integer, Handler>();
    public String playerName;
    DataInputStream dis;
    PrintStream ps;
    Socket socketTo;
    PlayerHandler playerToDb = new PlayerHandler();
    GameHandler gameToDb = new GameHandler();

    private int id;
    private HandleSession handleSession = null;
    boolean isPlaying = false;

    public Handler(Socket s) {
        try {
            socketTo = s;
            dis = new DataInputStream(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
            handleVector.add(this);
            start();

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        while (true) {
            try {
                String msg = dis.readLine();
                System.out.println("message from client" + msg);
                String[] allMsg = msg.split("___");
                switch (allMsg[0]) {
                    case "signUp":
                        signUp(allMsg);
                        break;
                    case "signIn":
                        signIn(allMsg);
                        break;
                    case "leaderBoard":
                        leaderBoard(this);
                        break;
                    case "Logout":
                        logOut(allMsg[1]);
                        break;
                    case "InvitaionTo":
                        sendinvetationTo(allMsg[1], allMsg[2], allMsg[3]);
                        break;
                    case "InvitaionResponse":
                        sendResponseTo(allMsg[1], allMsg[2], allMsg[3]);
                        break;


                    case "requestNewGame":
                        requestNewGameFrom(allMsg[1], Integer.parseInt(allMsg[2]));
                        break;

                    case "responseNewGame":
                        responseNewGame(allMsg[1]);
                        break;

                    case "openWindowGame":
                        openWindowGame();
                        break;

                    case "updateGame":
                        updateGame(allMsg[1],allMsg[2]);
                        break;

                    case "startGame":
                        startGame();
                        break;

                    case "chatall":
                        sendtoallPlayer(allMsg[1]);
                        break;
//                    case "StopGameThread":
//                        StopGameThread();
//                        break;


                    case "quitFromGame":
                        handlePlayerWantToQuit();
                        break;

                    case "requestToSaveGame":
                        handleRequestToSaveGame(Integer.parseInt(allMsg[1]));
                        break;

                    case "responseToSaveGame":
                        handleResponseToSaveGame(allMsg[1], Integer.parseInt(allMsg[2]));
                        break;
                    case "saveGameForLater":
                        handleSaveGameForLater();
                        break;

                    case "searchIfThereIsSavedGame":
                        handleSearchIfThereIsSavedGame(Integer.parseInt(allMsg[1]),Integer.parseInt(allMsg[2]));
                        break;

                    case "InvitationToSavedGame":
                        InvitationToSavedGame(Integer.parseInt(allMsg[1]));
                        break;

                    case "InvitaionResponseToSavedGame":
                        InvitaionResponseToSavedGame(allMsg[1], Integer.parseInt(allMsg[2]));
                        break;


                }
            } catch (IOException ioEs) {
                System.out.println(" : " + ioEs);
                    handleExitPlayer(id);
            }

        }
    }


    public void signUp(String[] allMsg) {
        Player player = new Player(allMsg[1], allMsg[3], allMsg[2], 0);
        try {
            String res = playerToDb.CreatePlayer(player);
            ps.println(res);
        } catch (SQLException ex2) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex2);
        }
    }

    public void signIn(String[] allMsg) {
        String res = playerToDb.getPlayer(allMsg[1], allMsg[2]);
        String[] resArr = res.split("___");

        if (Boolean.valueOf(resArr[0])) {

            int refreshId = Integer.valueOf(resArr[1]);
            this.id = refreshId;
            this.playerName = resArr[2];
            handleVectorWithID.put(refreshId, this);
            RefreshLeaderBoard(refreshId);

        }

        //System.out.println(res);
        ps.println(res);
    }

    public void logOut(String logoutId) {
        handleExitPlayer(Integer.parseInt(logoutId));
    }

    public void handleExitPlayer(int logoutId)
    {

        if(isPlaying)
        {
            handlePlayerWantToQuit();
        }

        playerToDb.changeStatus(logoutId);
        handleVectorWithID.remove(logoutId);
        RefreshLeaderBoard(logoutId);
        try {
            dis.close();
            ps.close();
            socketTo.close();
            stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleQuitPlayer(Handler toPlayer,String quittingPlayer)
    {
        toPlayer.ps.println("quitPlayer___The player "+quittingPlayer+" has withdrawn, you have got 10 points");
        toPlayer.playerToDb.changeScore(toPlayer.id,10);
        removeSessionGameFromPlayers();
    }

    public void handlePlayerWantToQuit()
    {
        Handler toPlayer = handleSession.playerId1 == id ? handleSession.getHandlerPlayer2() : handleSession.getHandlerPlayer1();
        handleQuitPlayer(toPlayer,playerName);
    }

    // leader board
    public void RefreshLeaderBoard(int exceptId) {

        // Print keys and values
        for (int i : handleVectorWithID.keySet()) {
            if (exceptId != i)
                leaderBoard(handleVectorWithID.get(i));
        }
    }

    public void leaderBoard(Handler handler) {
        String res = "";
        ResultSet leaderBoardArrL;
        leaderBoardArrL = PlayerHandler.getPlayers();
//        for (Player player : leaderBoardArrL) {
//            res+=player.getId().toString+"___"+ player.getName()+"___"+
//        }
        try {

            boolean isleaderboard = leaderBoardArrL.next();
            if (!isleaderboard) {
                handler.ps.println("false");
            } else {
                while (isleaderboard) {////true___1___abdo___100___true
                    if (handler.id != leaderBoardArrL.getInt("id")) {
                        res = "___" + leaderBoardArrL.getInt("id") + "___" + leaderBoardArrL.getString("name") +
                                "___" + leaderBoardArrL.getInt("score") + "___" + leaderBoardArrL.getBoolean("status");
                        isleaderboard = leaderBoardArrL.next();
                        res = isleaderboard + res;
                        handler.ps.println(res);
                        //System.out.println("the leader board flag is :" + isleaderboard);
                        // System.out.println(res);
                    } else {
                        isleaderboard = leaderBoardArrL.next();
                        if (!isleaderboard) {
                            res = Boolean.toString(isleaderboard);
                            handler.ps.println(res);
                        }
                    }
                }
            }
            //         System.out.println("after server loop response :"+res);
            //           ps.println("false___");

        } catch (SQLException a) {
        }
        //System.out.println(res);
    }


    // start game
    public void startGame() {
        try {
            if (handleSession.playerId1 == id) {

                ps.println("startSet___playerTurn___1___X");
                ps.println("informationAnotherPlayer___" + handleSession.playerId2 + "___" + handleSession.playerName2);
            } else {
                ps.println("startSet___playerTurn___2___O");
                ps.println("informationAnotherPlayer___" + handleSession.playerId1 + "___" + handleSession.playerName1);
            }
            String[] cells = handleSession.getCell();
            int numberOfX = 0;
            int numberOfO = 0;
            System.out.println(cells.length);
            for (int i = 0; i < cells.length; i++) {
                if(cells[i] != null){
                if (cells[i].equals("X")) ++numberOfX;
                else if (cells[i].equals("O")) ++numberOfO;
            }}


System.out.println(numberOfX+" : "+numberOfO);
            if (numberOfX == numberOfO)
                ps.println("playerTurn___1");
            else
                ps.println("playerTurn___2");
            isPlaying = true;
        }
        catch (Exception e)
        {
            System.out.println("errer at start : "+e);
            e.printStackTrace();
        }

    }

    public void updateGame(String shape, String index) {
        handleSession.insertMove(Integer.parseInt(index),shape);
    }


    // request for newGame
    public void requestNewGameFrom(String nameFrom, int idTo)
    {
        Handler reciverHandler = handleVectorWithID.get(idTo);
        reciverHandler.ps.println("requestNewGameFrom___" + nameFrom + "___" + idTo);
    }

    public void responseNewGame(String response)
    {
        if(response.equals("yes"))
        {
            handleSession.sentMessageToPlayers("responseToNewGame___"+response,"responseToNewGame___"+response);
            handleSession.resetGame();
        }
        else
        {
            handleSession.sentMessageToPlayers("responseToNewGame___"+response,"responseToNewGame___"+response);
            removeSessionGameFromPlayers();
        }
    }


    public void removeSessionGameFromPlayers()
    {
        handleSession.getHandlerPlayer1().isPlaying = false;
        handleSession.getHandlerPlayer2().isPlaying = false;
    }

    // send invitation to play
    public void sendinvetationTo(String sID, String senderName, String RID) {
        Integer Id = Integer.parseInt(sID);
        Handler reciverHandler = handleVectorWithID.get(Id);
        reciverHandler.ps.println("InvetationFrom___" + senderName + "___" + RID);
    }

    public void sendResponseTo(String sId, String Name, String resp) {
        Integer Id = Integer.parseInt(sId);
        Handler reciverHandler2 = handleVectorWithID.get(Id);
        reciverHandler2.ps.println("ResponsetoInvetation___" + resp + "___" + Name);

        // yes,no
        if (resp.equals("yes")) {
            // player 1 => reciverHandler
            // player 2 =>  this
            handleSession = new HandleSession(id,playerName ,this ,Id, reciverHandler2.playerName ,reciverHandler2 );
            reciverHandler2.handleSession = handleSession;
        }
    }

    public void openWindowGame() {

    }


    public void sendtoallPlayer(String msg){
        for (Handler i : handleVectorWithID.values()) {
            i.ps.println("msg"+"___"+playerName+" : "+msg);
        }

    }


    //// request to save game
    public void handleRequestToSaveGame(int idTo)
    {
        Handler receiverHandler = handleVectorWithID.get(idTo);
        receiverHandler.ps.println("showDialogToAskReplayGame");

    }

    public void handleResponseToSaveGame(String res,int idTo)
    {
        if(res.equals("yes")) {
            handleSession.sentMessageToPlayers("decisionToSaveGame___yes", "decisionToSaveGame___yes");
            removeSessionGameFromPlayers();
        }
        else
        {
            Handler receiverHandler = handleVectorWithID.get(idTo);
            receiverHandler.ps.println("decisionToSaveGame___no");
        }
    }

    public void handleSaveGameForLater()
    {
        String shapePlayer1 = "X";
        String shapePlayer2 = "O";

        String [] cells = handleSession.getCell();

        for(int i = 0; i < cells.length;i++)
        {
            if(cells[i].equals("X")) shapePlayer1 += i ;
            else if(cells[i].equals("O")) shapePlayer2 += i;
        }

        GameHandler.SaveGame(new Game(handleSession.playerId1,handleSession.playerId2
        ,shapePlayer1,shapePlayer2,handleSession.scorePlayer1,handleSession.scorePlayer2));

         System.out.println(shapePlayer1+"  "+shapePlayer2);
    }


    // open invatition saved game
    public void handleSearchIfThereIsSavedGame(int id1,int id2)
    {
        Game isThereGame = gameToDb.getGame(id1, id2);

        if(isThereGame!=null)
        {
            ps.println("responseSearchIfThereIsSavedGame___yes");
        }
        else
        {
            ps.println("responseSearchIfThereIsSavedGame___no");
        }
    }

    public void InvitationToSavedGame(int idTo)
    {
        Handler receiverHandler = handleVectorWithID.get(idTo);
        receiverHandler.ps.println("invitationHandlerToSavedGame___"+playerName+"___"+id);
    }

    public void InvitaionResponseToSavedGame(String res, int idInvitationFrom)
    {
        try {
            if (res.equals("yes")) {
                Game isThereGame = gameToDb.getGame(idInvitationFrom, id);

                System.out.println(isThereGame.getplayerOneId());
                System.out.println(isThereGame.getplayerTwoId());

                Handler handlePlayerOne = handleVectorWithID.get(isThereGame.getplayerOneId());
                Handler handlePlayerTwo = handleVectorWithID.get(isThereGame.getplayerTwoId());


                System.out.println(handlePlayerOne.id);
                System.out.println(handlePlayerTwo.id);
                System.out.println(id);

                HandleSession  savedHandleSession  = new HandleSession(handlePlayerOne.id, handlePlayerOne.playerName, handlePlayerOne,
                        handlePlayerTwo.id, handlePlayerTwo.playerName, handlePlayerTwo);

                handlePlayerOne.handleSession = savedHandleSession;

                handlePlayerTwo.handleSession = savedHandleSession;



            String [] cells = new String[9];
                for (int i = 0; i < 9; i++)
                    cells[i] = "";
//
            String[] positionPlayer1 = isThereGame.getPosition1().split("");
            String[] positionPlayer2 = isThereGame.getPosition2().split("");

            for(int i=1; i<positionPlayer1.length;i++)
            {
                int index = Integer.parseInt(positionPlayer1[i]);
                cells[index] = "X";
            }

            for(int i=1; i<positionPlayer2.length;i++)
            {
                int index = Integer.parseInt(positionPlayer2[i]);
                cells[index] = "O";
            }


            handleSession.setCell(cells);

            handleSession.sentMessageToPlayers("responseHandlerToSavedGame___yes___"+
                    handlePlayerTwo.id+"___"+handlePlayerTwo.playerName+"___"+
                            isThereGame.getPlayerTwoSore()+"___"+isThereGame.getPlayerOneScore()
                    +"___"+isThereGame.getPosition2()+"___"+isThereGame.getPosition1(),
                    "responseHandlerToSavedGame___yes___"+
                            handlePlayerOne.id+"___"+handlePlayerOne.playerName+"___"+
                            isThereGame.getPlayerOneScore()+"___"+isThereGame.getPlayerTwoSore()
                            +"___"+isThereGame.getPosition1()+"___"+isThereGame.getPosition2()

                    );
            } else if (res.equals("no")) {
                Handler receiverHandler = handleVectorWithID.get(idInvitationFrom);
                receiverHandler.ps.println("responseHandlerToSavedGame___" + "no___" + playerName);
            }
        }
        catch (Exception e)
        {
            System.out.println("error : "+e);
            e.printStackTrace();
        }

    }

}
