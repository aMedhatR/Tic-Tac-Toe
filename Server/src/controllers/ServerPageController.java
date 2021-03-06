/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import player.PlayerHandler;
import socket.SocketServer;
import startServer.App;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;
import static socket.Handler.judgeDaySeqInitiate;

/**
 *
 * @author Abdelrahman Mostafa
 * <Abdelrahman Mostafa at Information Technology Institute>
 */
public class ServerPageController implements Initializable {
private boolean ServerFlag = false ;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Label label;
    @FXML
    private AnchorPane ServerScenePane;
    @FXML
    private VBox VboxScrollPaneLeaderBoard;
    @FXML
    private Button serverPageServerButton;
    @FXML
    private Label ServerButtonLabel;
    @FXML
    private ImageView buttonServerImage;
    private  SocketServer SerSocketobj ;
    private static boolean updateflag=false;
    private  Thread CurrentThread;
    Thread thread;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.setDB();
        GetPlayersForLeaderBoard();

    thread =new Thread(new Runnable() {
        @Override
        public void run() {
            while (true)
            {
                if (updateflag)
                {
                    System.out.println("in thread after the update flag is true");
                    GetPlayersForLeaderBoard();

                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
        thread.start();
    }
public static void update ()
{
    System.out.println("in update method");
     updateflag =true;

}

    @FXML
    protected void ServerButtonAction()
    {

        if (!ServerFlag) {
            Platform.runLater(()->{
                ServerButtonLabel.setText("Server Is Running");
                Image ServerOnImage = new Image("/images/serverOn.png", 70, 61, false, false);

                buttonServerImage.setImage(ServerOnImage);

            });
            CurrentThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    SerSocketobj = new SocketServer();
                    System.out.println("Server is initiated");

                }
            });
            CurrentThread.start();
        }
        else
        {
            Platform.runLater(()->{
                judgeDaySeqInitiate();
                ServerButtonLabel.setText("Server Is Off");
                Image ServerOffImage = new Image("/images/serverOff.png", 70, 61, false, false);
                buttonServerImage.setImage(ServerOffImage);
            });
            try {
                CurrentThread.stop();

                App.getServerSocket().close();

                SerSocketobj=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ServerFlag = !ServerFlag;


    }

    public void GetPlayersForLeaderBoard() {

        ResultSet leaderBoardArrL;
        leaderBoardArrL = PlayerHandler.getPlayers();
        boolean isleaderboard = false;
        try {
            isleaderboard = leaderBoardArrL.next();
            Platform.runLater(()->{
                VboxScrollPaneLeaderBoard.getChildren().clear();
            });

        if (isleaderboard) {
               // handler.ps.println("false");
                    while (isleaderboard) {

                            addNewLeaderBoardElement(leaderBoardArrL.getString("name"),
                                    String.valueOf(leaderBoardArrL.getInt("score")),
                                    leaderBoardArrL.getBoolean("status"),  leaderBoardArrL.getBoolean("isplaying"));

              //          addNewLeaderBoardElement(leaderBoardArrL.getString("name"), String.valueOf(leaderBoardArrL.getInt("score")),leaderBoardArrL.getBoolean("status"));
                        isleaderboard = leaderBoardArrL.next();
                }
            updateflag=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addNewLeaderBoardElement(String Name, String Score, boolean Status,boolean isPlaying) {
        Platform.runLater(()->{
          
        ClassLoader loader = this.getClass().getClassLoader();
		InputStream onlineImage1 = loader.getResourceAsStream( "images/online.png");
        InputStream offlineImage1 = loader.getResourceAsStream( "images/offline.png");
        InputStream busyImage1 = loader.getResourceAsStream( "images/busy.png");
        Image onlineImage = new Image(onlineImage1, 20, 20, false, false);
        ImageView onlineIcon = new ImageView(onlineImage);
        Image offlineImage = new Image(offlineImage1, 20, 20, false, false);
        ImageView offlineIcon = new ImageView(offlineImage);
        Image busyImage = new Image(busyImage1, 20, 20, false, false);
        ImageView busyIcon = new ImageView(busyImage);
        HBox hbox = new HBox();
        Label NameLabel = new Label(Name);
        NameLabel.setMinWidth(190);
        NameLabel.setMinHeight(34);
        NameLabel.setFont(Font.font(17));

        Label ScoreLabel = new Label(Score);
        ScoreLabel.setMinWidth(60);
        ScoreLabel.setMinHeight(34);
        ScoreLabel.setFont(Font.font(17));
        ImageView StatusImage;
            if (Status&&isPlaying) {
                StatusImage=busyIcon;
            }
            else if (Status&&!isPlaying) {
                StatusImage = onlineIcon;
            }
            else
            {
                StatusImage = offlineIcon;
            }


        hbox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        Separator FirstSep = new Separator(Orientation.VERTICAL);
        Separator SecondSep = new Separator(Orientation.VERTICAL);
        hbox.getChildren().add(NameLabel);
        hbox.getChildren().add(FirstSep);
        hbox.getChildren().add(ScoreLabel);
        hbox.getChildren().add(SecondSep);
        hbox.getChildren().add(StatusImage);

        hbox.setCursor(Cursor.HAND);

        VboxScrollPaneLeaderBoard.getChildren().add(hbox);
        Separator separator = new Separator(Orientation.HORIZONTAL);
        VboxScrollPaneLeaderBoard.getChildren().add(separator);
        });

    }

    @FXML
    protected void handlePressedAction(MouseEvent event)
    {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    @FXML
    protected void handleMovementAction(MouseEvent event)
    {
        Stage stage =(Stage)ServerScenePane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
    @FXML
    protected void clientPageCloseButton()
    {
        thread.stop();
        Stage stage;
        stage =(Stage)ServerScenePane.getScene().getWindow();
        stage.close();
    }
}