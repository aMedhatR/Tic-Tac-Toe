package controllers;

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

public class WelcomePageControl {
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
        Alert WelcomeExitAlert= new Alert(Alert.AlertType.CONFIRMATION);
        WelcomeExitAlert.setTitle("Exit");
        WelcomeExitAlert.setHeaderText("you're about to logout!");
        WelcomeExitAlert.setContentText("Are You Sure you want to Exit ?");
        if (WelcomeExitAlert.showAndWait().get()== ButtonType.OK) {
            stage = (Stage) WelcomeScenePane.getScene().getWindow();
            System.out.println("you logout");
            stage.close();
        }

    }

    @FXML
    protected void onWelcomeSignInButtonClick() {

        stage = (Stage) WelcomeScenePane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/signIn.fxml"));
        try {
           scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
    }
    @FXML
    protected void onWelcomeSignUpButtonClick() {

        stage = (Stage) WelcomeScenePane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/signup.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
    }
    @FXML
    protected void onWelcomeGuestButtonClick() {

        stage = (Stage) WelcomeScenePane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/Home.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
    }
}