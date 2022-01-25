package controllers;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import MainAndControl.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author nora
 */
public class SignupController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Stage stage ;
    Scene scene;
    @FXML
    private BorderPane SignUpScenePane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    protected void onSignUpPageSignUpButton() {

        stage = (Stage) SignUpScenePane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/signup.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
    }
    @FXML
    protected void onSignUpPageSignInButton() {

        stage = (Stage) SignUpScenePane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/signIn.fxml"));
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
            stage = (Stage) SignUpScenePane.getScene().getWindow();
            System.out.println("you logout");
            stage.close();
        }

    }
}