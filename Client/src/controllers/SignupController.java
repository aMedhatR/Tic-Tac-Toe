package controllers;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import mainPk.MainApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    Stage stage;
    Scene scene;
    @FXML
    private BorderPane SignUpScenePane;
    @FXML
    private Label errorEmail;
    @FXML
    private Label errorUsername;
    @FXML
    private Label errorPassword;
    @FXML
    private Label errorConfirmPassword;

    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


    @FXML
    protected void onSignUpPageSignUpButton() throws IOException {

        boolean checkValidName = false;
        boolean checkValidEmail = false;
        boolean checkValidPassword = false;
        boolean checkValidConfirmPassword = false;

        String userNameTxt = username.getText().trim();
        String emailTxt = email.getText().trim();
        String passwordTxt = password.getText().trim();
        String confirmPasswordTxt = confirmPassword.getText().trim();

        String regexMail = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        if (userNameTxt.isEmpty()) {
            checkValidName = false;
            Platform.runLater(() -> {
                errorUsername.setText("Username is required");
            });
        } else {
            checkValidName = true;
            errorUsername.setText("");
        }

        if (emailTxt.isEmpty()) {
            checkValidEmail = false;
            Platform.runLater(() -> {
                errorEmail.setText("Email is required");
            });
        } else if (!Pattern.matches(regexMail, email.getText())) {
            checkValidEmail = false;
            Platform.runLater(() -> {
                errorEmail.setText("Please enter a valid mail");
            });
        } else {
            checkValidEmail = true;
            errorEmail.setText("");
        }


        if (passwordTxt.isEmpty()) {
            checkValidPassword = false;
            Platform.runLater(() -> {
                errorPassword.setText("Password is required");
            });
        } else if (passwordTxt.length() < 8) {
            checkValidPassword = false;
            Platform.runLater(() -> {
                errorPassword.setText("Password must be at least 8");
            });
        } else {
            checkValidPassword = true;
            errorPassword.setText("");
        }

        if (confirmPasswordTxt.isEmpty()) {
            checkValidConfirmPassword = false;
            Platform.runLater(() -> {
                errorConfirmPassword.setText("Confirm Password is required");
            });
        } else if (!confirmPasswordTxt.equals(passwordTxt)) {
            checkValidConfirmPassword = false;
            Platform.runLater(() -> {
                errorConfirmPassword.setText("Please check your password");
            });
        } else {
            checkValidConfirmPassword = true;
            errorConfirmPassword.setText("");
        }

        boolean checkValid = checkValidConfirmPassword && checkValidEmail && checkValidName && checkValidPassword;

        if (checkValid) {
            Socket mySocket;
            DataInputStream dis;
            PrintStream ps;

            mySocket = new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(mySocket.getInputStream());
            ps = new PrintStream(mySocket.getOutputStream());

            ps.println("signUp___"+userNameTxt+"___"+emailTxt+"___"+passwordTxt);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String replyMsg;
                        try {
                            replyMsg = dis.readLine();
                            System.out.println(replyMsg);
                        } catch (IOException ex) {
                        }
                    }
                }

            }).start();


            // if return true form server
            // go to second stage
            // stage = (Stage) SignUpScenePane.getScene().getWindow();
            // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/signup.fxml"));
            // try {
            // scene = new Scene(fxmlLoader.load());
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            // stage.setScene(scene);
            //else
            // show error from server
        }
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
        Alert WelcomeExitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        WelcomeExitAlert.setTitle("Exit");
        WelcomeExitAlert.setHeaderText("you're about to logout!");
        WelcomeExitAlert.setContentText("Are You Sure you want to Exit ?");
        if (WelcomeExitAlert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) SignUpScenePane.getScene().getWindow();
            System.out.println("you logout");
            stage.close();
        }

    }


}
