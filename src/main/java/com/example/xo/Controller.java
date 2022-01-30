package com.example.xo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class Controller implements Initializable {

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

    private Button computerTurn;

    private int player1;
    private int player2;
    private int playerTurn = 1;
    private int gameOver = 0;
    private boolean checker = false;

    ArrayList<Button> buttons;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        buttons.forEach(button ->{
            setupButton(button);
            button.setFocusTraversable(false);
        });

    }

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::resetButton);
        winnerText.setText("Tic-Tac-Toe");
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
        if(playerTurn == 1){
            button.setText("X");
            playerTurn = 0;
        }
        else if(playerTurn == 0)
        {
            public void computerTurn() {
            button.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    checker = true;
                    while (checker) {
                        int randomNum = ThreadLocalRandom.current().nextInt(0, 9);  //0 to 9
                        System.out.println("For loop");
                        //String element = buttons.get(randomNum).getText();
                        computerTurn = buttons.get(randomNum);
                        if (computerTurn.getText() == "") {
                            computerTurn.setText("0");
                            computerTurn.setDisable(true);
                            playerTurn = 1;
                            checker = false;

                            break;
                        } else if (computerTurn.getText() == "0" || computerTurn.getText() == "X") {
                            // break;
                            System.out.println("else if");
                            checker = false;
                        }
                    }

                }
            });
        }
        }
    }

    public void checkIfGameIsOver(){
        if(gameOver == 0) {
            for (int a = 0; a < 8; a++) {
                String line = switch (a) {
                    case 0 -> button1.getText() + button2.getText() + button3.getText();
                    case 1 -> button4.getText() + button5.getText() + button6.getText();
                    case 2 -> button7.getText() + button8.getText() + button9.getText();
                    case 3 -> button1.getText() + button5.getText() + button9.getText();
                    case 4 -> button3.getText() + button5.getText() + button7.getText();
                    case 5 -> button1.getText() + button4.getText() + button7.getText();
                    case 6 -> button2.getText() + button5.getText() + button8.getText();
                    case 7 -> button3.getText() + button6.getText() + button9.getText();
                    default -> null;
                };

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


}