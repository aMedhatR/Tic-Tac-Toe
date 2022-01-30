package controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mainPk.HandleOnlineSocket;
import person.Person;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ClientPageController implements Initializable {
    Thread thread;

    private String playerChosen = "0";
    @FXML
    private VBox VboxScrollPaneLeaderBoard;
    @FXML
    private Label testcolorforanother;
    @FXML
    private AnchorPane ClientScenePane;
    @FXML
    private ScrollPane ScrollPaneLeaderBoard;
    private boolean LeaderBoardChoiceFlag;
    private int SelectedId;
    private HashMap<String, Integer> NameIdMap;
    @FXML
    private Label CurrentPlayerNameLabel;
    @FXML
    private Label CurrentPlayerScoreLabel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HandleOnlineSocket.getSendStream().println("leaderBoard___");
        Platform.runLater(()->{
            CurrentPlayerNameLabel.setText(Person.getName());
            CurrentPlayerScoreLabel.setText(String.valueOf(Person.getScore()));
        });

        NameIdMap = new HashMap<>();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String replyMsg;
                while (true)
                    {
                        try {

                            boolean Flag = true;
                            VboxScrollPaneLeaderBoard.getChildren().clear();

                            while (Flag) {
                                replyMsg = HandleOnlineSocket.getReceiveStream().readLine();
                                System.out.println(replyMsg);
                                System.out.println("client while loop recieved");
                                if (replyMsg.equals("false")) {
                                    break;
                                }
                                String[] allReplyMsg = replyMsg.split("___");
                                Flag = Boolean.parseBoolean(allReplyMsg[0]);
                                System.out.println("client while loop out Flag :" + Flag);

                                Platform.runLater(() -> {
                                    addNewLeaderBoardElement(allReplyMsg[2], allReplyMsg[3], Boolean.parseBoolean(allReplyMsg[4]));
                                    NameIdMap.put(allReplyMsg[2], Integer.parseInt(allReplyMsg[1]));
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(e);
                        } finally {
                            // thread.stop();
                        }
                    }
                }
        });
        thread.start();
    }

    @FXML
    protected void clientPageCloseButton() {

        CommonControllers.closeWindow(ClientScenePane,true);

    }

    public void addNewLeaderBoardElement(String Name, String Score, boolean Status) {

        Image onlineImage = new Image("/Images/online.png", 20, 20, false, false);
        ImageView onlineIcon = new ImageView(onlineImage);
        Image offlineImage = new Image("/Images/offline.png", 20, 20, false, false);
        ImageView offlineIcon = new ImageView(offlineImage);
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
        if (Status) {
            StatusImage = onlineIcon;
        } else {
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
        if (Status) {
            hbox.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    for (int i = 0; i < VboxScrollPaneLeaderBoard.getChildren().size(); i++) {
                        if (i % 2 == 0) {
                            ((HBox) VboxScrollPaneLeaderBoard.getChildren().get(i)).setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                    hbox.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    System.out.println(((Label) hbox.getChildren().get(0)).getText());
                    playerChosen = ((Label) hbox.getChildren().get(0)).getText();
                    UpdateSelectedPlayerId(playerChosen,NameIdMap);
                    System.out.println("mouse click detected! " + mouseEvent.getSource());


                }
            });
        }
        VboxScrollPaneLeaderBoard.getChildren().add(hbox);
        Separator separator = new Separator(Orientation.HORIZONTAL);
        VboxScrollPaneLeaderBoard.getChildren().add(separator);


    }

    @FXML
    protected void btnOnlineGameClientPage() {
        checkForCheckedPlayers();
    }

    protected boolean checkForCheckedPlayers() {
        if (playerChosen == "0") {
            System.out.println("no player has been chosen");
            return false;
        } else {
            System.out.println("the chosen player is " + playerChosen);
            return true;
        }
    }
    protected void UpdateSelectedPlayerId(String Name ,HashMap<String, Integer> map)
    {
        if (map.containsKey(Name)) {
            // Mapping
            SelectedId = (Integer) map.get(Name);
        }
        System.out.println("the Player is :"+Name+ "And his id is : "+SelectedId);
    }
    @FXML
    protected void testclick() {
        addNewLeaderBoardElement("Abdo", "100", true);


    }

    @FXML
    protected void testbtn2() {
        addNewLeaderBoardElement("nora", "100", false);
    }
}
