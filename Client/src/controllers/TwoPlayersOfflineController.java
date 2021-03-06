package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mainPk.HandleOnlineSocket;
import person.Person;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class TwoPlayersOfflineController implements Initializable {
    public Button welcomePageExitButton;
    public Label playerName;
    public Label homeLabel;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private Button button1;
    @FXML
    private AnchorPane OnlineGameAnchorPane;
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
    private Label winnerText;

    @FXML
    private Label label1;

    @FXML
    private  Label label2;
    @FXML
    private Label yourName;


    private int player1=0;
    private int player2=0;
    private int playerTurn = 0;
    private int gameOver = 0;
    private boolean isOnline =false;
    ArrayList<Button> buttons;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isOnline=false;
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));

        buttons.forEach(button ->{
            setupButton(button);
            button.setFocusTraversable(false);
            Platform.runLater(()->{
                if (Person.getName()!=null) {
                    yourName.setText(Person.getName());
                    isOnline=true;
                }
            });
        });
    }

    @FXML
    public void restartGame() {
        buttons.forEach(this::resetButton);
        Platform.runLater(()-> winnerText.setText("Tic-Tac-Toe"));
    }

    public void resetButton(Button button){
        button.setDisable(false);
        button.setText("");
        playerTurn = 0;
        gameOver = 0;
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setDisable(true);
            checkIfGameIsOver();
        });
    }

    public void setPlayerSymbol(Button button){
        if(playerTurn == 1){
            button.setText("X");
            playerTurn = 0;
        } else if(playerTurn == 0){
            button.setText("O");
            playerTurn = 1;
        }

    }

    public void checkIfGameIsOver(){
        if(gameOver == 0) {
            for (int a = 0; a < 8; a++) {
                String line ;
                switch (a) {
                    case 0 : line=button1.getText() + button2.getText() + button3.getText();break;
                    case 1 : line= button4.getText() + button5.getText() + button6.getText();break;
                    case 2 : line=button7.getText() + button8.getText() + button9.getText();break;
                    case 3 : line=button1.getText() + button5.getText() + button9.getText();break;
                    case 4 : line=button3.getText() + button5.getText() + button7.getText();break;
                    case 5 : line=button1.getText() + button4.getText() + button7.getText();break;
                    case 6 : line=button2.getText() + button5.getText() + button8.getText();break;
                    case 7 : line=button3.getText() + button6.getText() + button9.getText();break;
                    default : line=null;break;
                };

                //X winner
                if (line.equals("XXX")) {
                    Platform.runLater(()->{


                    winnerText.setText("X won!");
                    playerTurn = 3;
                    player1+=10;
                    label1.setText(Integer.toString(player1));
                    gameOver++;
                    });
                }

                //O winner
                else if (line.equals("OOO")) {
                    Platform.runLater(()->{
                    winnerText.setText("O won!");
                    playerTurn = 3;
                        if (Person.getName()!=null) {
                            int score = Person.getScore() + 5;
                            Person.setScore(score);
                            SendScoreToServer();
                        }
                    player2+=10;
                    label2.setText(Integer.toString(player2));
                    gameOver++;
                    });
                }
            }
        }
    }

    @FXML
    protected void OnlineGameCloseButton()
    {
        if (!isOnline) {
            CommonControllers.closeWindow(OnlineGameAnchorPane, false);
        }
        else {
            HandleOnlineSocket.getSendStream().println("ChangeIsPlaying___false");
            CommonControllers.gotoStage("ClientPage.fxml", OnlineGameAnchorPane);
        }

    }
    public void SendScoreToServer()
    {
        HandleOnlineSocket.getSendStream().println("changeScore___5");
    }
    @FXML
    protected void goToHome()
    {
        if (!isOnline) {
            CommonControllers.goToHome(OnlineGameAnchorPane);
        }
        else
        {
            HandleOnlineSocket.getSendStream().println("ChangeIsPlaying___false");
            CommonControllers.gotoStage("ClientPage.fxml", OnlineGameAnchorPane);

        }
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
        Stage stage =(Stage)OnlineGameAnchorPane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
}