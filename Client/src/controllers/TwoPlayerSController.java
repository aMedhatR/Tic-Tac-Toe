package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import mainPk.HandleOnlineSocket;
import person.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TwoPlayerSController implements Initializable {

    Button[] d = new Button[9];
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
    private Label label2;
    private int player1;
    private int player2;
    private int playerTurn = 0;
    private String shapePlayer = "";
    private int whoPlayerTurn = 0;
    private int gameOver = 0;
    private String replyMsg;
    Thread thread;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ClientPageController.thread.stop();
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
                while (true) {
                    try {
                        System.out.println("---------------------"+"repeat");
                        replyMsg = HandleOnlineSocket.getReceiveStream().readLine();
                        String[] allReplyMsg = replyMsg.split("___");
                        System.out.println("message from server: "+ replyMsg);
                        switch (allReplyMsg[0]) {

                            case "tartSet":
                                playerTurn = Integer.parseInt(allReplyMsg[2]);

                                shapePlayer = allReplyMsg[3];
                                break;
                            case "playerTurn":
                                whoPlayerTurn = Integer.parseInt(allReplyMsg[1]);
                                if(whoPlayerTurn == playerTurn) Platform.runLater(()-> turnWho.setText("Your turn"));
                                else Platform.runLater(()-> turnWho.setText(""));
                                break;
                            case "move":
                                handleMovement(allReplyMsg[1], Integer.parseInt(allReplyMsg[2]));
                                break;
                            case "won":
                                //logOut(allMsg[1]);
                                break;
                            case "draw":
                                //sendinvetationTo(allMsg[1],allMsg[2],allMsg[3]);
                                break;
                            case "loss":
                                //sendResponseTo(allMsg[1],allMsg[2],allMsg[3]);
                                break;
                            case "reset":
                                //StopGameThread();
                                break;

                            case "noReplayGame":
                                //StopGameThread();
                                break;
                        }

                    } catch (IOException e) {
                        System.out.println("---------------------"+e);
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }


    @FXML
    public void restartGame(ActionEvent event) {
        //buttons.forEach(this::resetButton);

        for (Button btn : d) {
            resetButton(btn);
        }

        winnerText.setText("Tic-Tac-Toe");

    }

    public void resetButton(Button button) {
        button.setDisable(false);
        button.setText("");
        playerTurn = 0;
        gameOver = 0;
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
        });
    }

    public void setPlayerSymbol(Button button) {

        if (playerTurn == whoPlayerTurn) {
            int index = Integer.parseInt(String.valueOf(button.getId().charAt(6)))-1;
            System.out.println(index);
            HandleOnlineSocket.getSendStream().println("updateGame___"+shapePlayer+"___"+index);
            button.setDisable(true);
            checkIfGameIsOver();
        } else if (playerTurn != whoPlayerTurn) {

        }


    }


    public void handleMovement(String shape,int index)
    {
        Platform.runLater(() -> {
            d[index].setText(shape);
            d[index].setDisable(true);
        });


    }

    public void checkIfGameIsOver() {
        if (gameOver == 0) {
            for (int a = 0; a < 8; a++) {

                String line;
                switch (a) {
                    case 0:
                        line = button1.getText() + button2.getText() + button3.getText();
                        break;
                    case 1:
                        line = button4.getText() + button5.getText() + button6.getText();
                        break;
                    case 2:
                        line = button7.getText() + button8.getText() + button9.getText();
                        break;
                    case 3:
                        line = button1.getText() + button5.getText() + button9.getText();
                        break;
                    case 4:
                        line = button3.getText() + button5.getText() + button7.getText();
                        break;
                    case 5:
                        line = button1.getText() + button4.getText() + button7.getText();
                        break;
                    case 6:
                        line = button2.getText() + button5.getText() + button8.getText();
                        break;
                    case 7:
                        line = button3.getText() + button6.getText() + button9.getText();
                        break;
                    default:
                        line = null;
                        break;
                }

                //X winner
                if (line.equals("XXX")) {
                    winnerText.setText("X won!");
                    playerTurn = 3;
                    player1++;
                    label1.setText(Integer.toString(player1));
                    gameOver++;
                }

                //O winner
                else if (line.equals("OOO")) {
                    winnerText.setText("O won!");
                    playerTurn = 3;
                    player2++;
                    label2.setText(Integer.toString(player2));
                    gameOver++;
                }
            }
        }
    }

    @FXML
    public void OnlineGameCloseButton()
    {
        CommonControllers.closeWindow(OnlineGameAnchorPane,true);
    }

}