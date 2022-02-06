package controllers;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainPk.HandleOnlineSocket;
import person.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TwoPlayerSController implements Initializable {

    Button[] d = new Button[9];
    static Thread thread;
    //ArrayList<Button> buttons;
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
    private Text winnerText;
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
    private int player1;
    private int player2;
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
    private final int gameOver = 0;

    private boolean stopPlayer = false;

    private boolean stopThread = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ClientPageController.thread.stop();
        stopThread = false;
        //buttons = new ArrayList<>(Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8, button9));
        d[0] = button1;
        d[1] = button2;
        d[2] = button3;
        d[3] = button4;
        d[4] = button5;
        d[5] = button6;
        d[6] = button7;
        d[7] = button8;
        d[8] = button9;


//        buttons.forEach(button -> {
//            setupButton(button);
//            button.setFocusTraversable(false);
//        });

        for (Button btn : d) {
            setupButton(btn);
            btn.setFocusTraversable(false);
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

        //buttons.forEach(this::resetButton);

//        for (Button btn : d) {
//            resetButton(btn);
//        }
//
//        winnerText.setText("Tic-Tac-Toe");

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
//        Image onlineImage = new Image("/Images/online.png", 20, 20, false, false);
//        ImageView onlineIcon = new ImageView(onlineImage);
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

//    public void checkIfGameIsOver() {
//        if (gameOver == 0) {
//            for (int a = 0; a < 8; a++) {
//
//                String line;
//                switch (a) {
//                    case 0:
//                        line = button1.getText() + button2.getText() + button3.getText();
//                        break;
//                    case 1:
//                        line = button4.getText() + button5.getText() + button6.getText();
//                        break;
//                    case 2:
//                        line = button7.getText() + button8.getText() + button9.getText();
//                        break;
//                    case 3:
//                        line = button1.getText() + button5.getText() + button9.getText();
//                        break;
//                    case 4:
//                        line = button3.getText() + button5.getText() + button7.getText();
//                        break;
//                    case 5:
//                        line = button1.getText() + button4.getText() + button7.getText();
//                        break;
//                    case 6:
//                        line = button2.getText() + button5.getText() + button8.getText();
//                        break;
//                    case 7:
//                        line = button3.getText() + button6.getText() + button9.getText();
//                        break;
//                    default:
//                        line = null;
//                        break;
//                }
//
//                //X winner
//                if (line.equals("XXX")) {
//                    winnerText.setText("X won!");
//                    playerTurn = 3;
//                    player1++;
//                    label1.setText(Integer.toString(player1));
//                    gameOver++;
//                }
//
//                //O winner
//                else if (line.equals("OOO")) {
//                    winnerText.setText("O won!");
//                    playerTurn = 3;
//                    player2++;
//                    label2.setText(Integer.toString(player2));
//                    gameOver++;
//                }
//            }
//        }
//    }


    public void backToClientPage()
    {
        Platform.runLater(() -> {
            thread.stop();
            CommonControllers.gotoStage("ClientPage.fxml", OnlineGameAnchorPane);
        });
        stopThread = true;
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

}