/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import mainPk.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author nora
 */
public class signInController implements Initializable {
    Stage stage;
    Scene scene;
    @FXML
    private Label label;
    @FXML
    private BorderPane SignInPaneScene;
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    protected void onSignInSignUpButton() {

        stage = (Stage) SignInPaneScene.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/signup.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
    }
    @FXML
    protected void onWelcomeCloseButtonClick() {
        Alert WelcomeExitAlert= new Alert(Alert.AlertType.CONFIRMATION);
        WelcomeExitAlert.setTitle("Exit");
        WelcomeExitAlert.setHeaderText("you're about to logout!");
        WelcomeExitAlert.setContentText("Are You Sure you want to Exit ?");
        if (WelcomeExitAlert.showAndWait().get()== ButtonType.OK) {
            stage = (Stage) SignInPaneScene.getScene().getWindow();
            System.out.println("you logout");
            stage.close();
        }

    }
}
