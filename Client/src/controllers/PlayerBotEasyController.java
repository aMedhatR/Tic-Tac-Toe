package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mainPk.HandleOnlineSocket;
import person.Person;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerBotEasyController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
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
    private  Label label2;

    private boolean IsOnline =true;

    private Button computerTurn;

    @FXML
    private AnchorPane EasyBotScenePane;
    @FXML
    private Label playerlabel;
    private Vector<String> vector= new Vector<String>(9);

    private int player1 =0;
    private int player2 =0;
    private int playerTurn = 1;
    private int gameOver = 0;
    private boolean isOnline;
    ArrayList<Button> buttons;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isOnline=false;
        Platform.runLater(()->{
            if (Person.getName()!=null) {
                playerlabel.setText(Person.getName());
                isOnline=true ;
            }
        });

        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        buttons.forEach(button ->{
            setupButton(button);
            button.setFocusTraversable(false);

        });
     for (int i=0;i<9;i++)
        {
            vector.add(String.valueOf(i));
        }
    }

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::resetButton);
        winnerText.setText("Tic-Tac-Toe");
        vector.clear();
        for (int i=0;i<9;i++)
        {
            vector.add(String.valueOf(i));
        }
    }

    public void resetButton(Button button){
        button.setDisable(false);
        button.setText("");
        playerTurn = 1;
        gameOver = 0;
    }

    private void setupButton(Button button) {

            button.setOnMouseClicked((MouseEvent ActionEvent) -> {
                setPlayerSymbol(button);
                button.setDisable(true);
                checkIfGameIsOver();
            });
        }


    public void setPlayerSymbol(Button button){
        int randomNum;
            button.setText("X");

        System.out.println(vector);

        System.out.println(button.getId().charAt(6));
        System.out.println(Integer.parseInt(String.valueOf(button.getId().charAt(6)))-1 );
            vector.remove(String.valueOf(Integer.parseInt(String.valueOf(button.getId().charAt(6)))-1));
            System.out.println(vector);
            if(vector.size() != 0) {
                randomNum = ThreadLocalRandom.current().nextInt(0, vector.size());  //0 to 9


                computerTurn = buttons.get(Integer.parseInt(vector.get(randomNum)));
                computerTurn.setText("0");
                computerTurn.setDisable(true);
                 System.out.println(vector);
                vector.remove(randomNum);
                   System.out.println(vector);
                System.out.println("For loop");
    }   //String element = buttons.get(randomNum).getText();
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
                }
                System.out.println("Winning Line  is "+line);
                //X winner
                if (line.equals("XXX")) {
                    winnerText.setText("X won!");
                    playerTurn = 3;
                    if (Person.getName()!=null) {
                        int score = Person.getScore() + 5;
                        Person.setScore(score);
                        SendScoreToServer();
                    }
                    player1+=5;
                    label1.setText(Integer.toString(player1));
                    gameOver++;
                }

                //O winner
                if (line.equals("000")) {
                    System.out.println("Hi Failuer");
                    winnerText.setText("O won!");
                    playerTurn = 3;
                    player2+=5;
                    label2.setText(Integer.toString(player2));
                    gameOver++;
                }
            }
        }
    }
public void SendScoreToServer()
{
    HandleOnlineSocket.getSendStream().println("changeScore___5");
}
@FXML
    protected void OnlineGameCloseButton()
{
    if (isOnline) {
        HandleOnlineSocket.getSendStream().println("ChangeIsPlaying___false");
        CommonControllers.gotoStage("ClientPage.fxml",EasyBotScenePane);
    }
    else
    {

        CommonControllers.goToHome(EasyBotScenePane);
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
        Stage stage =(Stage)EasyBotScenePane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
}