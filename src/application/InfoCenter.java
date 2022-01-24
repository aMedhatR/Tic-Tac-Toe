/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 *
 * @author Ahmed Medhat Mohamed
 */
public class InfoCenter {

//need seters and geters
    private StackPane pane;
    private Label message;
    private Button startGameButton;

    public InfoCenter() {
        pane = new StackPane();
        pane.setMinSize(UIConstants.appWidth, UIConstants.infoCenterHeight);
        pane.setTranslateX(UIConstants.appWidth / 2);
        pane.setTranslateY(UIConstants.infoCenterHeight / 2);

//message
        message = new Label("Tic-Tac-Toe");
        message.setMinSize(UIConstants.appWidth, UIConstants.infoCenterHeight);
        message.setFont(Font.font(30));
        message.setAlignment(Pos.CENTER);
        message.setTranslateY(-20);   //like margin in css
        pane.getChildren().add(message);

        startGameButton = new Button("Start New Game");
        startGameButton.setMinSize(135, 30);
        startGameButton.setTranslateY(20);
        pane.getChildren().add(startGameButton);
    }

    public StackPane getStackPane() {

        return pane;
    }

    public void updateMessage(String message) {
        this.message.setText(message);

    }

    public void showStartButton() {
        startGameButton.setVisible(true);
    }

    public void hideStartButton() {
        startGameButton.setVisible(false);
    }

    public void setStartButtonOnAction(EventHandler<ActionEvent> onAction) {
        startGameButton.setOnAction(onAction);
    }
}
