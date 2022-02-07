package controllers;

import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import mainPk.HandleOnlineSocket;
import mainPk.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomePageControl  implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private Button welcomePageExitButton;
    @FXML
    private AnchorPane WelcomeScenePane;
    private double xOffset = 0;
    private double yOffset = 0;

    Stage stage;
    Scene scene;
    @FXML
    protected void onWelcomeCloseButtonClick() {
        CommonControllers.closeWindow(WelcomeScenePane,false);

    }

    @FXML
    protected void onWelcomeSignInButtonClick() {
        CommonControllers.gotoStage("signIn.fxml",WelcomeScenePane);
    }

    @FXML
    protected void onWelcomeSignUpButtonClick() {
        CommonControllers.gotoStage("signup.fxml",WelcomeScenePane);

    }

    @FXML
    protected void onWelcomeGuestButtonClick() throws IOException {

         CommonControllers.gotoStage("Home.fxml",WelcomeScenePane);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new HandleOnlineSocket();
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
        Stage stage =(Stage)WelcomeScenePane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
}