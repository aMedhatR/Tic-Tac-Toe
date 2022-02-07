package controllers;

import game.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainPk.HandleOnlineSocket;
import person.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TwoPlayersOnlineController implements Initializable {

    Button[] d = new Button[9];
    static Thread thread;
    @FXML
    private AnchorPane OnlineGameAnchorPane;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button9;

    @FXML
    private Label label1;
    @FXML
    private Label turnWho;
    @FXML
    private Label yourName;
    @FXML
    private Label playerName;
    @FXML
    private Label label2;

    private String replyMsg;
    // information players
    private int playerTurn = 0;
    private String shapePlayer = "";
    private int whoPlayerTurn = 0;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    // information  other players
    private String nameAntherPlayer = "";
    private int idAntherPlayer;

    private boolean stopPlayer = false;

    private boolean stopThread = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ClientPageController.thread.stop();
        stopThread = false;
        d[0] = button1;
        d[1] = button2;
        d[2] = button3;
        d[3] = button4;
        d[4] = button5;
        d[5] = button6;
        d[6] = button7;
        d[7] = button8;
        d[8] = button9;


        for (Button btn : d) {
            setupButton(btn);
            btn.setFocusTraversable(false);
        }
        if(ClientPageController.isSavedGame)
        {
            setDefaultButtonByLastPlaying();
            System.out.println("saved game");
        }


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HandleOnlineSocket.getSendStream().println("startGame");
                while (true && !stopThread) {
                    try {
                        System.out.println("---------------------" + "repeat");
                        replyMsg = HandleOnlineSocket.getReceiveStream().readLine();
                        String[] allReplyMsg = replyMsg.split("___");
                        System.out.println("message from server: " + replyMsg);
                        switch (allReplyMsg[0]) {

                            case "informationAnotherPlayer":
                                nameAntherPlayer = allReplyMsg[2];
                                idAntherPlayer = Integer.parseInt(allReplyMsg[1]);

                                Platform.runLater(() -> {
                                    yourName.setText(Person.getName());
                                    playerName.setText(nameAntherPlayer);
                                });

                                break;

                            case "tartSet":
                                playerTurn = Integer.parseInt(allReplyMsg[2]);

                                shapePlayer = allReplyMsg[3];


                                break;
                            case "playerTurn":
                                whoPlayerTurn = Integer.parseInt(allReplyMsg[1]);
                                if (whoPlayerTurn == playerTurn) Platform.runLater(() -> turnWho.setText("Your turn"));
                                else Platform.runLater(() -> turnWho.setText(nameAntherPlayer + " turn"));
                                break;
                            case "move":
                                handleMovement(allReplyMsg[1], Integer.parseInt(allReplyMsg[2]));
                                break;
                            case "status":
                                handleStatusGame(allReplyMsg[1]);
                                break;
                            case "reset":
                                //StopGameThread();
                                break;
                            case "requestNewGameFrom":
                                requestNewGameFrom(allReplyMsg[1]);
                                break;

                            case "responseToNewGame":
                                responseToNewGame(allReplyMsg[1]);
                                break;

                            case "updateScore":
                                scorePlayer1 = Integer.parseInt(allReplyMsg[1]);
                                scorePlayer2 = Integer.parseInt(allReplyMsg[2]);
                                updateScore();
                                break;
                            case"quitPlayer":
                                handlePlayerWasQuit(allReplyMsg[1]);
                                break;

                            case "showDialogToAskReplayGame":
                                handleShowDialogToAskReplayGame();
                                break;

                            case "decisionToSaveGame":
                                handleDecisionToSaveGame(allReplyMsg[1]);
                                break;

                        }

                    } catch (IOException e) {
                        System.out.println("---------------------" + e);
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }


    @FXML
    public void restartGame(ActionEvent event) {

        HandleOnlineSocket.getSendStream().println("requestNewGame___" + Person.getName() + "___" + idAntherPlayer);

    }

    public void responseToNewGame(String res) {
        if (res.equals("yes")) {
            for (Button btn : d) {
                resetButton(btn);
            }
            stopPlayer = false;
        } else {
            backToClientPage();
        }
    }

    public void resetButton(Button button) {
        Platform.runLater(() -> {
            button.setDisable(false);
            button.setText("");
        });

    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
        });
    }

    public void setPlayerSymbol(Button button) {

        if (playerTurn == whoPlayerTurn && !stopPlayer) {
            int index = Integer.parseInt(String.valueOf(button.getId().charAt(6))) - 1;
            System.out.println(index);
            HandleOnlineSocket.getSendStream().println("updateGame___" + shapePlayer + "___" + index);
            button.setDisable(true);
            //checkIfGameIsOver();
        } else if (playerTurn != whoPlayerTurn) {

        }


    }

    public void handleMovement(String shape, int index) {
        Platform.runLater(() -> {
            d[index].setText(shape);
            d[index].setDisable(true);
        });
    }

    public void handleStatusGame(String status) {
        stopPlayer = true;
        setEnd(status);
    }

    public void updateScore() {
        Platform.runLater(() -> {
            label1.setText(String.valueOf(scorePlayer1));
            label2.setText(String.valueOf(scorePlayer2));
        });
    }

    public void requestNewGameFrom(String name) {
        handleDialog(name + " want to play new game again, Do you want to?");
    }

    public void handlePlayerWasQuit(String msg)
    {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/AcceptInvetation.fxml"));
            DialogPane ConfirmDialogPane = null;
            try {
                ConfirmDialogPane = fxmlLoader.load();

                ConfirmDialogPane.setContentText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(ConfirmDialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);

            dialog.showAndWait().ifPresent(response -> {
                int score = Person.getScore()+10;
                if (response == ButtonType.OK) {

                    Person.setScore(score);
                    backToClientPage();
                } else if (response == ButtonType.CANCEL) {
                    /// back to home
                    backToClientPage();
                    Person.setScore(score);
                }

            });
        });
    }

    public void handleDialog(String msg) {

        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/AcceptInvetation.fxml"));
            DialogPane ConfirmDialogPane = null;
            try {
                ConfirmDialogPane = fxmlLoader.load();

                ConfirmDialogPane.setContentText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(ConfirmDialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    HandleOnlineSocket.getSendStream().println("responseNewGame___" + "yes");
                } else if (response == ButtonType.CANCEL) {
                    /// back to home
                    HandleOnlineSocket.getSendStream().println("responseNewGame___" + "no");
                }
            });
        });
    }


    public void increaseScore()
    {
        int score = Person.getScore()+10;
        Person.setScore(score);
    }

    public void setEnd (String status){
        Platform.runLater(() -> {
        Image WinImage = new Image("/Images/minionsHappy.gif", 355, 265, false, false);
        ImageView winview = new ImageView(WinImage);
        Image LoseImage = new Image("/Images/minionsSad.gif", 355, 265, false, false);
        ImageView loseview = new ImageView(LoseImage);
        Image drawImage = new Image("/Images/minionsDraw.gif", 355, 265, false, false);
        ImageView drawview = new ImageView(drawImage);
        ImageView StatusImage = null;
        switch (status) {
            case "win":
                StatusImage = winview;
                increaseScore();
                break;
            case "lose":
                StatusImage = loseview;
                break;
            case "draw":
                StatusImage = drawview;
                break;

        }
        FXMLLoader loadDialog =new FXMLLoader(getClass().getResource("/dialoguesAndControllers/WinLoseDrawDialog.fxml"));
            Stage endStage =new Stage();
        try {
            AnchorPane pane =loadDialog.load();
            pane.getChildren().add(StatusImage);
            Scene endScene =new Scene(pane);
            endStage.setScene(endScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
        endStage.show();

        });
    }

    @FXML
    public void onReplyGame()
    {
        HandleOnlineSocket.getSendStream().println("requestToSaveGame___"+idAntherPlayer);
    }

    public void handleShowDialogToAskReplayGame()
    {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/AcceptInvetation.fxml"));
            DialogPane ConfirmDialogPane = null;
            try {
                ConfirmDialogPane = fxmlLoader.load();

                ConfirmDialogPane.setContentText(nameAntherPlayer +" wants to save the game to play later, Do you want it?");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(ConfirmDialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    HandleOnlineSocket.getSendStream().println("responseToSaveGame___yes___"+idAntherPlayer);
                    HandleOnlineSocket.getSendStream().println("saveGameForLater");

                } else if (response == ButtonType.CANCEL) {
                    /// back to home
                    HandleOnlineSocket.getSendStream().println("responseToSaveGame___no___"+idAntherPlayer);
                }
            });
        });
    }

    public void handleDecisionToSaveGame(String res)
    {
        if(res.equals("yes"))
        {
            // send to server status game
            backToClientPage();
        }
        else
        {
            Platform.runLater(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/AcceptInvetation.fxml"));
                DialogPane ConfirmDialogPane = null;
                try {
                    ConfirmDialogPane = fxmlLoader.load();

                    ConfirmDialogPane.setContentText(nameAntherPlayer +" refused to save game.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(ConfirmDialogPane);
                dialog.initStyle(StageStyle.UNDECORATED);
                dialog.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                    } else if (response == ButtonType.CANCEL) {
                    }
                });
            });
        }
    }

    @FXML
    public void handleWithDraw()
    {

        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dialoguesAndControllers/AcceptInvetation.fxml"));
            DialogPane ConfirmDialogPane = null;
            try {
                ConfirmDialogPane = fxmlLoader.load();

                ConfirmDialogPane.setContentText("Are you sure about getting out? "+ nameAntherPlayer +"  will win");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(ConfirmDialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    HandleOnlineSocket.getSendStream().println("quitFromGame");
                    backToClientPage();
                } else if (response == ButtonType.CANCEL) {
                    /// back to home
                }
            });
        });


    }

    @FXML
    public void OnlineGameCloseButton() {
        CommonControllers.closeWindow(OnlineGameAnchorPane, true);
    }


    public void backToClientPage()
    {
        Platform.runLater(() -> {
            thread.stop();
            CommonControllers.gotoStage("ClientPage.fxml", OnlineGameAnchorPane);
        });
        stopThread = true;
    }

    public void setDefaultButtonByLastPlaying()
    {
        try {
            String[] positionPlayer = Game.getPositionPlayer().split("");
            String[] positionPlayerAnotherPlayer = Game.getPostionAnotherPlayer().split("");

            System.out.println(Game.getPositionPlayer());

            Platform.runLater(() -> {
                label2.setText(String.valueOf(Game.getScoreAnotherPlayer()));
                label1.setText(String.valueOf(Game.getScorePlayer()));
            });



            // text on button and disable

            for (int i = 1; i < positionPlayer.length; i++) {
                int index = Integer.parseInt(positionPlayer[i]);
                handleMovement(positionPlayer[0], index);
            }

            for (int i = 1; i < positionPlayerAnotherPlayer.length; i++) {
                int index = Integer.parseInt(positionPlayerAnotherPlayer[i]);
                handleMovement(positionPlayerAnotherPlayer[0], index);
            }
        }
        catch (Exception e)
        {
            System.out.println("error "+e);
            e.printStackTrace();
        }


    }
}