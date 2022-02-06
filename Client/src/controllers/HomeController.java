/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import mainPk.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author asmaaMohamed
 */
public class HomeController implements Initializable {
    @FXML
    private AnchorPane HomePagepaneScene;
    Stage stage;
    Scene scene;
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
        CommonControllers.gotoStage("PlayerVsComputer.fxml",HomePagepaneScene);
    }
}
