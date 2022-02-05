package socket;

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
    private int id;
    private HandleSession handleSession;

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

                    case "openWindowGame":
                        openWindowGame();
                        break;

                    case "updateGame":
                        updateGame();
                        break;

                    case "startGame":
                        startGame();
                        break;

//                    case "StopGameThread":
//                        StopGameThread();
//                        break;

                }
            } catch (IOException ioEs) {
                System.out.println(" : " + ioEs);
                try {
                    dis.close();
                    ps.close();
                    socketTo.close();
                    stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
        playerToDb.changeStatus(Integer.parseInt(logoutId));
        handleVectorWithID.remove(logoutId);
        RefreshLeaderBoard(Integer.parseInt(logoutId));
    }

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

    public void startGame() {
        if (handleSession.playerId1 == id)
            ps.println("startSet___playerTurn___1");
        else
            ps.println("startSet___playerTurn___2");
    }


    public void updateGame() {

    }

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
            handleSession = new HandleSession(id, Id);
            reciverHandler2.handleSession = handleSession;
        }
    }

    public void openWindowGame() {

    }

    //public void StopGameThread()
//    {
//        thread.stop();
//    }
}
