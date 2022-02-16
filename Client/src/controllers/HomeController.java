/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author asmaaMohamed
 */
public class HomeController implements Initializable {
    public Button btnSinglePlayer;
    public Label labSinglePlayer;
    public Button welcomePageExitButton;
    public Button btnOnlineGame;
    public Circle circleOnlineGame;
    public Button btnTwoPlayer;
    public Label txtAlert;
    @FXML
    private AnchorPane HomePagepaneScene;
    Stage stage;
    Scene scene;
    private double xOffset = 0;
    private double yOffset = 0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    protected void onHomePageOnlineButton() {

        stage = (Stage) HomePagepaneScene.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/signIn.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
    }
    @FXML
    protected void goToHome() {
        CommonControllers.goToHome(HomePagepaneScene);
    }
    @FXML
    protected void onWelcomeCloseButtonClick() {
        CommonControllers.closeWindow(HomePagepaneScene,false);
    }
    @FXML
    protected void homepageSinglbtn(){
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
                CommonControllers.gotoStage("PlayerBotHard.fxml",HomePagepaneScene);

            } else if (response == ButtonType.CANCEL) {
                /// back to home
                CommonControllers.gotoStage("PlayerBotEasy.fxml",HomePagepaneScene);
            }

    });
    }
    @FXML
    protected void homepageTwoPlayersbtn()
    {
        CommonControllers.gotoStage("TwoPlayersOffline.fxml",HomePagepaneScene);
    }

    @FXML
    protected void handlePressedAction(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) HomePagepaneScene.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);

    } 

}
