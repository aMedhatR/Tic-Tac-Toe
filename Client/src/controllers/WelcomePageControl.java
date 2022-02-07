package controllers;

import javafx.fxml.Initializable;
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
        HandleOnlineSocket.getReceiveStream().close();
        HandleOnlineSocket.getSendStream().close();
        HandleOnlineSocket.getMySocket().close();
         CommonControllers.gotoStage("Home.fxml",WelcomeScenePane);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new HandleOnlineSocket();
    }
}