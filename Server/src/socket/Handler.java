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
    DataInputStream dis;
    PrintStream ps;
    Socket socketTo;

    PlayerHandler playerToDb = new PlayerHandler();

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

                }
            } catch (IOException ioEs) {
                try {
                    dis.close();
                    socketTo.close();
                    ps.close();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(" : " + ioEs);
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
        if(Boolean.valueOf(resArr[0]))
        {
            handleVectorWithID.put(Integer.valueOf(resArr[1]),this);
            RefreshLeaderBoard();
        }

        System.out.println(res);
        ps.println(res);
    }
    public void logOut(String logoutId)  {
        playerToDb.changeStatus(Integer.parseInt(logoutId));
        handleVectorWithID.remove(logoutId);
        RefreshLeaderBoard();
    }
    public void RefreshLeaderBoard()
    {
        for (Handler i : handleVectorWithID.values()) {
            System.out.println("Sending update");
            i.ps.println("update");

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

boolean isleaderboard=leaderBoardArrL.next();
            if (!isleaderboard){handler.ps.println("false");}
            else {
                while (isleaderboard) {////true___1___abdo___100___true

                    res = "___" + leaderBoardArrL.getInt("id") + "___" + leaderBoardArrL.getString("name") +
                            "___" + leaderBoardArrL.getInt("score") + "___" + leaderBoardArrL.getBoolean("status");
                    isleaderboard = leaderBoardArrL.next();
                    res = Boolean.toString(isleaderboard) + res;
                    handler.ps.println(res);
                    System.out.println("the leader board flag is :" + isleaderboard);
                    System.out.println(res);

                }
            }
 //         System.out.println("after server loop response :"+res);
 //           ps.println("false___");

        } catch (SQLException a) {

        }


        //System.out.println(res);

    }
}
