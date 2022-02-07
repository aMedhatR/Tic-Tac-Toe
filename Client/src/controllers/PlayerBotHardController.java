package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class PlayerBotHardController implements Initializable {

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

    private Vector<String> vector= new Vector<String>(9);

    private int player1;
    private int player2;
    private int playerTurn = 1;
    private int gameOver = 0;
    boolean playerWin = false;
    boolean computerWin = false;

    ArrayList<Button> buttons;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        playerWin = false;
        computerWin = false;
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
        button.setDisable(true);
        System.out.println(vector);

        System.out.println(button.getId().charAt(6));
        System.out.println(Integer.parseInt(String.valueOf(button.getId().charAt(6)))-1 );

       //     vector.remove(String.valueOf(Integer.parseInt(String.valueOf(button.getId().charAt(6)))-1));
            System.out.println(vector);
            if(vector.size() != 0) {
             //   randomNum = ThreadLocalRandom.current().nextInt(0, vector.size());  //0 to 9


            //    computerTurn = buttons.get(Integer.parseInt(vector.get(randomNum)));
                System.out.println(findBestMove());
                computerTurn = buttons.get(findBestMove());
                computerTurn.setText("0");
                computerTurn.setDisable(true);
                 System.out.println(vector);
               // vector.remove(randomNum);
                   System.out.println(vector);
                System.out.println("For loop");
    }   //String element = buttons.get(randomNum).getText();
    }

    public void checkIfGameIsOver(){
        if(gameOver == 0) {
            for (int a = 0; a < 8; a++) {
                String line;
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
                    player1++;
                    label1.setText(Integer.toString(player1));
                    gameOver++;
                    playerWin = true;
                    computerWin = false;
                }

                //O winner
                if (line.equals("000")) {
                    System.out.println("Hi Failuer");
                    winnerText.setText("O won!");
                    playerTurn = 3;
                    player2++;
                    label2.setText(Integer.toString(player2));
                    gameOver++;
                    computerWin = true;
                    playerWin = false;
                }
            }
        }
    }

    Boolean isMovesLeft()
    {
        //used to check if there is enable button and the value inside it is empty
        for (int i = 0; i < 9; i++)
                if (buttons.get(i).getText() == "")
                    return true;
        return false;
    }

    int evaluate()
    {
        // used to check who win if player or computer in the game to
        //use it in minimax method
        checkIfGameIsOver();
                if (computerWin)
                    return +10;
                else if (playerWin)
                    return -10;
        return 0;
    }

    int minimax(int depth, Boolean isMax)
    {
        int score = evaluate();

        // If Maximizer has won the game
        // return his/her evaluated score
        if (score == 10)
            return score;

        // If Minimizer has won the game
        // return his/her evaluated score
        if (score == -10)
            return score;

        // If there are no more moves and
        // no winner then it is a tie
        if (isMovesLeft() == false)
            return 0;

        // If this maximizer's move
        if (isMax)
        {
            int best = -1000;

            // Traverse all cells
            for (int i = 0; i < 9; i++)
            {
                    // Check if cell is empty
                if (buttons.get(i).getText() == "")
                    {
                        // Make the move
                        buttons.get(i).setText("0");

                        // Call minimax recursively and choose
                        // the maximum value
                        best = Math.max(best, minimax(depth + 1, !isMax));

                        // Undo the move
                        buttons.get(i).setText("");
                    }
            }
            return best;
        }

        // If this minimizer's move
        else
        {
            int best = 1000;

            // Traverse all cells
            for (int i = 0; i < 9; i++)
            {
                    // Check if cell is empty
                if (buttons.get(i).getText() == "")
                    {
                        // Make the move for player turn
                        // thus it will continue the tree
                        //and had all the posibilities
                        buttons.get(i).setText("X");

                        // Call minimax recursively and choose
                        // the minimum value
                        best = Math.min(best, minimax(depth + 1, !isMax));

                        // Undo the move
                        buttons.get(i).setText("");
                    }
            }
            return best;
        }
    }

    int findBestMove()
    {
        int bestVal = -1000;
        int bestMove= -1;
        boolean checkPlayerMove = false;


        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (int i = 0; i < 9; i++)
        {
            // Check if cell is empty
            System.out.println("buttons.get(i).getText() :"+buttons.get(i).getText());
           if(Objects.equals(buttons.get(i).getText(), new String(""))) // --> true
           // if (buttons.get(i).getText().equals(""))
            {
                System.out.println("in best move if condition");
                //before make any move just check if pla
                // Make the move
                buttons.get(i).setText("0");

                // compute evaluation function for this
                // move.
                int moveVal = minimax(0, false);

                // Undo the move
                buttons.get(i).setText("");

                // If the value of the current move is
                // more than the best value, then update
                // best/
                if (moveVal > bestVal)
                {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }

//        System.out.printf("The value of the best Move " +
//                "is : %d\n\n", bestVal);

        return bestMove;
    }
@FXML
    protected void OnlineGameCloseButton()
{

}

}