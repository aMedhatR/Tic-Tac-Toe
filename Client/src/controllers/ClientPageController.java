package controllers;

import game.Game;
import helper.Notification;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainPk.HandleOnlineSocket;
import person.Person;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;


public class ClientPageController implements Initializable {
    public static Thread thread;

    boolean Flag;
    boolean UpdateFlag;
    private String playerChosen = "0";
    @FXML
    private VBox VboxScrollPaneLeaderBoard;

    @FXML
    private AnchorPane ClientScenePane;

    private int SelectedId;
    private HashMap<String, Integer> NameIdMap;
    @FXML
    private Label CurrentPlayerNameLabel;
    @FXML
    private Label CurrentPlayerScoreLabel;
    @FXML
    private TextField txtfiled;
    @FXML
    private TextArea txtarea;
    boolean StopThread = false;
    public static boolean isSavedGame = false;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StopThread = false;
        HandleOnlineSocket.getSendStream().println("leaderBoard___");
        Platform.runLater(() -> {
            CurrentPlayerNameLabel.setText(Person.getName());
            CurrentPlayerScoreLabel.setText(String.valueOf(Person.getScore()));
        });

        NameIdMap = new HashMap<>();
        thread = new Thread(() -> {
            String replyMsg;
            while (!StopThread) {
                try {
                    Flag = true;
                    UpdateFlag = true;

                    while (Flag) {
                        replyMsg = HandleOnlineSocket.getReceiveStream().readLine();

                        System.out.println(replyMsg);

                        if (replyMsg.equals("false")) {
                            break;
                        }
                        String[] allReplyMsg = replyMsg.split("___");
                        switch (allReplyMsg[0]) {
                            case "InvetationFrom":
                                invitationHandler(allReplyMsg);
                                break;
                            case "ResponsetoInvetation":
                                responseHandler(allReplyMsg);
                                break;
                            case "msg":
                                appendmsg(allReplyMsg[1]);

                                break;

                            case "responseSearchIfThereIsSavedGame":
                                handleResponseSearchIfThereIsSavedGame(allReplyMsg[1]);
                                break;

                            case "invitationHandlerToSavedGame":
                                invitationHandlerToSavedGame(allReplyMsg);
                                break;
                            case "responseHandlerToSavedGame":
                                responseHandlerToSavedGame(allReplyMsg);
                                break;
                            case "notification":
                                notification(allReplyMsg[1]);
                                break;
                            case"judgeDaySeqInit":
                                selfDestruction();
                                break;
                            default:
                                updateLeaderboardHandler(allReplyMsg);
                                break;

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //System.out.println(e);
                } finally {
                    // thread.stop();
                }
            }
        });
        thread.start();
    }

    @FXML
    protected void clientPageCloseButton() {

        CommonControllers.closeWindow(ClientScenePane, true);

    }

    public void addNewLeaderBoardElement(String Name, String Score, boolean Status, boolean isPlaying) {

        Image onlineImage = new Image("/Images/online.png", 20, 20, false, false);
        ImageView onlineIcon = new ImageView(onlineImage);
        Image offlineImage = new Image("/Images/offline.png", 20, 20, false, false);
        ImageView offlineIcon = new ImageView(offlineImage);
        Image busyImage = new Image("/Images/busy.png", 20, 20, false, false);
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
        if (Status && isPlaying) {
            StatusImage = busyIcon;

        } else if (Status) {
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
        if (Status && !isPlaying) {
            hbox.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                for (int i = 0; i < VboxScrollPaneLeaderBoard.getChildren().size(); i++) {
                    if (i % 2 == 0) {
                        ((HBox) VboxScrollPaneLeaderBoard.getChildren().get(i)).setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
                hbox.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                //System.out.println(((Label) hbox.getChildren().get(0)).getText());
                playerChosen = ((Label) hbox.getChildren().get(0)).getText();
                UpdateSelectedPlayerId(playerChosen, NameIdMap);
                //System.out.println("mouse click detected! " + mouseEvent.getSource());


            });
        }
        VboxScrollPaneLeaderBoard.getChildren().add(hbox);
        Separator separator = new Separator(Orientation.HORIZONTAL);
        VboxScrollPaneLeaderBoard.getChildren().add(separator);


    }


    protected boolean checkForCheckedPlayers() {
        //System.out.println("no player has been chosen");
        //System.out.println("the chosen player is " + playerChosen);
        return !Objects.equals(playerChosen, "0");
    }

    protected void UpdateSelectedPlayerId(String Name, HashMap<String, Integer> map) {
        if (map.containsKey(Name)) {
            // Mapping
            SelectedId = map.get(Name);
        }
        //System.out.println("the Player is :" + Name + "And his id is : " + SelectedId);
    }

    @FXML

    protected void ClientPageSingleGame() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/AcceptInvetation.fxml"));
        DialogPane ConfirmDialogPane = null;
        try {
            ConfirmDialogPane = fxmlLoader.load();

            ConfirmDialogPane.setContentText("Are you up for a challenge, Please click ok if you want hard mode, cancel for easy mode ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(ConfirmDialogPane);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                HandleOnlineSocket.getSendStream().println("ChangeIsPlaying___true");
                StopThread();
                CommonControllers.gotoStage("PlayerBotHard.fxml", ClientScenePane);

            } else if (response == ButtonType.CANCEL) {
                /// back to home

                HandleOnlineSocket.getSendStream().println("ChangeIsPlaying___true");
                StopThread();
                CommonControllers.gotoStage("PlayerBotEasy.fxml", ClientScenePane);
            }

        });


    }

    @FXML
    protected void ClientPageTwoPlayers() {

        HandleOnlineSocket.getSendStream().println("ChangeIsPlaying___true");
        StopThread();
        CommonControllers.gotoStage("TwoPlayersOffline.fxml", ClientScenePane);
    }

    @FXML
    protected void btnOnlineGameClientPage() {
        if (checkForCheckedPlayers())
            HandleOnlineSocket.getSendStream().println("searchIfThereIsSavedGame___" + Person.getId() + "___" + SelectedId);
        else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/PleaseChoosePlayer.fxml"));
                DialogPane failDialogPane = fxmlLoader.load();
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(failDialogPane);
                dialog.initStyle(StageStyle.UNDECORATED);
                dialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        dialog.close();
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleResponseSearchIfThereIsSavedGame(String res) {
        if (res.equals("yes")) dialogToAskNewGameOrSavedGame();
        else if (res.equals("no")) newGame();
    }

    public void dialogToAskNewGameOrSavedGame() {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/AcceptInvetation.fxml"));
            DialogPane ConfirmDialogPane = null;
            try {
                ConfirmDialogPane = fxmlLoader.load();

                ConfirmDialogPane.setContentText("Do you want to open saved game or new game, Please click ok if saved game, cancel for new game ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(ConfirmDialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    HandleOnlineSocket.getSendStream().println("InvitationToSavedGame___" + SelectedId);
                } else if (response == ButtonType.CANCEL) {
                    /// back to home
                    newGame();
                }
            });
        });

    }


    public void invitationHandlerToSavedGame(String[] allReplyMsg) {
        //System.out.println("if condition statisfied");
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/AcceptInvetation.fxml"));
            DialogPane ConfirmDialogPane = null;
            try {
                ConfirmDialogPane = fxmlLoader.load();

                ConfirmDialogPane.setContentText(allReplyMsg[1] + " invites you to play saved game together");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(ConfirmDialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    //System.out.println("trying to send response");
                    Platform.runLater(() -> HandleOnlineSocket.getSendStream().println("InvitaionResponseToSavedGame___" + "yes" + "___" + allReplyMsg[2]));
                    // CommonControllers.gotoStage("TwoPlayersGame.fxml",ClientScenePane);
                } else if (response == ButtonType.CANCEL) {
                    //System.out.println("trying to send response");
                    HandleOnlineSocket.getSendStream().println("InvitaionResponseToSavedGame___" + "no" + "___" + allReplyMsg[2]);
                }
            });
        });
    }

    public void responseHandlerToSavedGame(String[] allReplyMsg) {
        //System.out.println("else if statsfied");
        if (allReplyMsg[1].equals("no")) {
            Platform.runLater(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/PleaseChoosePlayer.fxml"));
                DialogPane failDialogPane = null;
                try {
                    failDialogPane = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (failDialogPane != null) {
                    failDialogPane.setContentText("the invitation to " + allReplyMsg[2] + " been declined ");
                }
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(failDialogPane);
                dialog.initStyle(StageStyle.UNDECORATED);
                dialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        dialog.close();
                    }

                });
            });
        } else if (allReplyMsg[1].equals("yes")) {

            Platform.runLater(() -> {

                System.out.println(allReplyMsg[4] + " : " + allReplyMsg[5]);
                Game.setIdAnotherPlayer(Integer.parseInt(allReplyMsg[2]));
                Game.setNameAnotherPlayer(allReplyMsg[3]);
                Game.setScoreAnotherPlayer(Integer.parseInt(allReplyMsg[4]));
                Game.setScorePlayer(Integer.parseInt(allReplyMsg[5]));

                Game.setPostionAnotherPlayer(allReplyMsg[6]);
                Game.setPositionPlayer(allReplyMsg[7]);
                isSavedGame = true;
                CommonControllers.gotoStage("TwoPlayersOnline.fxml", ClientScenePane);
            });
        }
    }


    public void newGame() {
        Platform.runLater(() -> {
            if (checkForCheckedPlayers()) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/SendInvetation.fxml"));
                DialogPane ConfirmDialogPane = null;
                try {
                    ConfirmDialogPane = fxmlLoader.load();

                    ConfirmDialogPane.setContentText("Are You Sure To Send The Invetation to " + playerChosen + " ?");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Dialog<ButtonType> dialog = new Dialog<>();

                dialog.setDialogPane(ConfirmDialogPane);
                dialog.initStyle(StageStyle.UNDECORATED);
                dialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        HandleOnlineSocket.getSendStream().println("InvitaionTo___" + SelectedId + "___" + CurrentPlayerNameLabel.getText() + "___" + Person.getId());
                    }
                    if (response == ButtonType.CANCEL) {
                        dialog.close();
                    }
                });
            } else {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/PleaseChoosePlayer.fxml"));
                    DialogPane failDialogPane = fxmlLoader.load();
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setDialogPane(failDialogPane);
                    dialog.initStyle(StageStyle.UNDECORATED);
                    dialog.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            dialog.close();
                        }

                    });
//               Scene scene = new Scene(parent);
//               Stage stage = new Stage();
//               stage.initModality(Modality.APPLICATION_MODAL);
//               stage.initStyle(StageStyle.UNDECORATED);
//               stage.setScene(scene);
//               stage.showAndWait().ifPresent(response -> {
//                   if (response == ButtonType.OK) {
//                       formatSystem();
//                   }
//               });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void invitationHandler(String[] allReplyMsg) {
        //System.out.println("if condition statisfied");
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/AcceptInvetation.fxml"));
            DialogPane ConfirmDialogPane = null;
            try {
                ConfirmDialogPane = fxmlLoader.load();

                ConfirmDialogPane.setContentText(allReplyMsg[1] + " invites you to play together");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(ConfirmDialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    //System.out.println("trying to send response");
                    Platform.runLater(() -> {

                        isSavedGame = false;
                        HandleOnlineSocket.getSendStream().println("InvitaionResponse___" + allReplyMsg[2] + "___" + Person.getName() + "___" + "yes");

                        CommonControllers.gotoStage("TwoPlayersOnline.fxml", ClientScenePane);


                    });


                    // CommonControllers.gotoStage("TwoPlayersOnline.fxml",ClientScenePane);
                } else if (response == ButtonType.CANCEL) {
                    //System.out.println("trying to send response");
                    HandleOnlineSocket.getSendStream().println("InvitaionResponse___" + allReplyMsg[2] + "___" + Person.getName() + "___" + "no");
                }
            });
        });
    }

    public void responseHandler(String[] allReplyMsg) {
        //System.out.println("else if statsfied");
        if (allReplyMsg[1].equals("no")) {
            Platform.runLater(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/PleaseChoosePlayer.fxml"));
                DialogPane failDialogPane = null;
                try {
                    failDialogPane = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (failDialogPane != null) {
                    failDialogPane.setContentText("the invitation to" + allReplyMsg[2] + " been declined ");
                }
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(failDialogPane);
                dialog.initStyle(StageStyle.UNDECORATED);
                dialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        dialog.close();
                    }

                });
            });
        } else if (allReplyMsg[1].equals("yes")) {

            Platform.runLater(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/PleaseChoosePlayer.fxml"));
                DialogPane failDialogPane = null;
                try {
                    failDialogPane = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (failDialogPane != null) {
                    failDialogPane.setContentText("the invitation to" + allReplyMsg[2] + " has been  accepted ");
                }
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(failDialogPane);
                dialog.initStyle(StageStyle.UNDECORATED);
                dialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {


                        isSavedGame = false;
                        CommonControllers.gotoStage("TwoPlayersOnline.fxml", ClientScenePane);

                    }
                });
            });
        }
    }

    public void updateLeaderboardHandler(String[] allReplyMsg) {
        if (UpdateFlag) {
            //System.out.println("second loop after update flag");
            Platform.runLater(() -> VboxScrollPaneLeaderBoard.getChildren().clear());
            UpdateFlag = false;
        }
        if (allReplyMsg[0].equals("rue"))
            Flag = true;
        else
            Flag = Boolean.parseBoolean(allReplyMsg[0]);
        //System.out.println("client while loop out Flag :" + Flag);

        Platform.runLater(() -> {
            addNewLeaderBoardElement(allReplyMsg[2], allReplyMsg[3], Boolean.parseBoolean(allReplyMsg[4]), Boolean.parseBoolean(allReplyMsg[5]));
            NameIdMap.put(allReplyMsg[2], Integer.parseInt(allReplyMsg[1]));
        });
    }

    @FXML
    protected void send() {
        System.out.println("send action");
        HandleOnlineSocket.getSendStream().println("chatall" + "___" + txtfiled.getText());
        txtfiled.setText("");
    }

    @FXML
    public void LogoutPlayerName() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/AcceptInvetation.fxml"));
        DialogPane ConfirmDialogPane = null;
        try {
            ConfirmDialogPane = fxmlLoader.load();

            ConfirmDialogPane.setContentText("Do you want to log out?");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(ConfirmDialogPane);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                HandleOnlineSocket.getSendStream().println("ChangeIsPlaying___false");
                try {
                    CommonControllers.signOut();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                CommonControllers.goToHome(ClientScenePane);

            } else if (response == ButtonType.CANCEL) {
                dialog.close();
            }

        });
    }
    public void selfDestruction() {
        Platform.runLater(()->{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/PleaseChoosePlayer.fxml"));
        DialogPane ConfirmDialogPane = null;
        try {
            ConfirmDialogPane = fxmlLoader.load();
            ConfirmDialogPane.setContentText("Server is Down please try to login again");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(ConfirmDialogPane);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {

                HandleOnlineSocket.getSendStream().println("ChangeIsPlaying___false");
                try {
                    CommonControllers.signOut();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                CommonControllers.goToHome(ClientScenePane);

            }

        });
});
    }
    protected void appendmsg(String msg) {
        Platform.runLater(() -> txtarea.appendText("\n" + msg));
    }

    protected void notification(String notificationBody) {
        Platform.runLater(() -> new Notification(notificationBody));
    }

    public void StopThread() {
        StopThread = true;
        thread.stop();
    }

    @FXML
    protected void handlePressedAction(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) ClientScenePane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);

    }
}
