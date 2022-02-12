package controllers;

import ai.MiniMaxCombined;
import game.Board;
import game.Mark;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import mainPk.HandleOnlineSocket;
import person.Person;

import java.net.URL;
import java.util.ResourceBundle;

import static game.Mark.O;
import static game.Mark.X;

public class PlayerBotHardController implements Initializable {


    private static GridPane gameBoard;
    private static Board board;
    private AnimationTimer gameTimer;
    private MenuBar menuBar;
    private Menu gameMenu;
    private MenuItem newGameOption;
    private BorderPane root;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Text winnerText;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label playerlabel;
    private Button computerTurn;
    @FXML
    private AnchorPane HardBotScenePane;
    @FXML
    private AnchorPane BoardAnchorPane;
    private final boolean IsOnline = true;
    private int player1;
    private int player2;
    private AnchorPane anchorPane;
    private boolean isOnline;

    /**
     * Fills and returns a GridPane with tiles, this GridPane is representative
     * of the game board.
     *
     * @return the GridPane
     */
    private static GridPane generateGUI() {
        gameBoard = new GridPane();
        board = new Board();
        gameBoard.setAlignment(Pos.CENTER);

        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                PlayerBotHardController.Tile tile = new PlayerBotHardController.Tile(row, col, board.getMarkAt(row, col));
                GridPane.setConstraints(tile, col, row);
                gameBoard.getChildren().add(tile);
            }
        }
        return gameBoard;
    }

    /**
     * Analyses the current state of the board and plays the move best for the X
     * player. Updates the tile it places a mark on also.
     */
    private static void playAI() {
        int[] move = MiniMaxCombined.getBestMove(board);
        int row = move[0];
        int col = move[1];
        board.placeMark(row, col);
        for (Node child : gameBoard.getChildren()) {
            if (GridPane.getRowIndex(child) == row
                    && GridPane.getColumnIndex(child) == col) {
                PlayerBotHardController.Tile t = (PlayerBotHardController.Tile) child;
                t.update();
                return;
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isOnline = false;

        //   Platform.runLater(()->{
        //     BoardAnchorPane.getChildren().clear();
        BoardAnchorPane.getChildren().add(generateGUI());
        // });


        runGameLoop();
        Platform.runLater(() -> {
            if (Person.getName() != null) {
                playerlabel.setText(Person.getName());
                isOnline = true;
            }
        });

    }

    private void runGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (board.isGameOver()) {
                    endGame();
                } else {
                    if (board.isCrossTurn()) {
                        playAI();
                    }
                }
            }
        };
        gameTimer.start();
    }

    /**
     * Stops the game loop and displays the result of the game.
     */
    private void endGame() {
        gameTimer.stop();

        Mark winner = board.getWinningMark();
        if (winner==O)
        {
            player1+=10;
            System.out.println(player1);
            if (isOnline) {
                SendScoreToServer();
            }
        }
        else if (winner==X)
        {
            System.out.println(player1);
            player2+=10;
        }

        winnerText.setText("Tic-Tac-Toe");
        if (winner == Mark.BLANK) {
            winnerText.setText("Draw!");
        } else {
            label1.setText(String.valueOf(player1));
            label2.setText(String.valueOf(player2));
            winnerText.setText(winner + " wins!");
        }


    }

    private void resetGame() {
        Board.AnotherGameToggle=!Board.AnotherGameToggle;
        Platform.runLater(() -> {
            BoardAnchorPane.getChildren().clear();
            BoardAnchorPane.getChildren().add(generateGUI());
        });
        winnerText.setText("Tic-Tac-Toe");

        runGameLoop();
    }

    @FXML
    void restartGame(ActionEvent event) {
        resetGame();
    }

    public void SendScoreToServer() {
        HandleOnlineSocket.getSendStream().println("changeScore___10");
    }

    @FXML
    protected void OnlineGameCloseButton() {
        if (isOnline) {
            HandleOnlineSocket.getSendStream().println("ChangeIsPlaying___false");
            CommonControllers.gotoStage("ClientPage.fxml", HardBotScenePane);
        } else {

            CommonControllers.goToHome(HardBotScenePane);
        }

    }

    @FXML
    protected void handlePressedAction(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) HardBotScenePane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    /**
     * Objects of this class visually represent the tiles in the game and handle
     * user input.
     */
    public final static class Tile extends Button {

        private final int row;
        private final int col;
        private Mark mark;

        public Tile(int initRow, int initCol, Mark initMark) {
            row = initRow;
            col = initCol;
            mark = initMark;
            initialiseTile();
        }

        private void initialiseTile() {
            this.setOnMouseClicked(e -> {
                if (!board.isCrossTurn()) {
                    board.placeMark(this.row, this.col);
                    this.update();
                }
            });


            this.setStyle("-fx-font-size:45 ;-fx-background-color: #363350 ;" +
                    "-fx-background-radius :  20; -fx-text-fill: #21f9fb;" +
                    "-fx-border-color: #21f9fb; -fx-border-radius: 25px;");
            this.setTextAlignment(TextAlignment.CENTER);
            this.setMinSize(136, 88);
            this.setText("" + this.mark);
            this.setCursor(Cursor.HAND);


        }

        /**
         * Retrieves state of tile from board which has the corresponding row
         * and column coordinate and updates this object's text field with it.
         */
        public void update() {
            this.mark = board.getMarkAt(this.row, this.col);
            this.setText("" + mark);
        }
    }

}