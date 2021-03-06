package controllers;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mainPk.HandleOnlineSocket;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static mainPk.HandleOnlineSocket.getMySocket;

/**
 * FXML Controller class
 *
 * @author nora
 */
public class SignupController implements Initializable {

    public Button welcomePageExitButton;
    public Button signUp;
    /**
     * Initializes the controller class.
     */
    Stage stage;
    Scene scene;
    private double xOffset = 0;
    private double yOffset = 0;
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

    Thread thread;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (getMySocket()==null) {
            System.out.println("new scocket is open");
            new HandleOnlineSocket();
        }

    }
    @FXML
    protected void goToHome() {

        CommonControllers.goToHome(SignUpScenePane);
    }

    @FXML
    protected void onSignUpPageSignUpButton() {

        boolean checkValidName;
        boolean checkValidEmail;
        boolean checkValidPassword;
        boolean checkValidConfirmPassword;

        String userNameTxt = username.getText().trim();
        String emailTxt = email.getText().trim();
        String passwordTxt = password.getText().trim();
        String confirmPasswordTxt = confirmPassword.getText().trim();

        String regexMail = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        if (userNameTxt.isEmpty()) {
            checkValidName = false;
            Platform.runLater(() -> errorUsername.setText("Username is required"));
        } else {
            checkValidName = true;
            errorUsername.setText("");
        }

        if (emailTxt.isEmpty()) {
            checkValidEmail = false;
            Platform.runLater(() -> errorEmail.setText("Email is required"));
        } else if (!Pattern.matches(regexMail, email.getText())) {
            checkValidEmail = false;
            Platform.runLater(() -> errorEmail.setText("Please enter a valid mail"));
        } else {
            checkValidEmail = true;
            errorEmail.setText("");
        }


        if (passwordTxt.isEmpty()) {
            checkValidPassword = false;
            Platform.runLater(() -> errorPassword.setText("Password is required"));
        } else if (passwordTxt.length() < 8) {
            checkValidPassword = false;
            Platform.runLater(() -> errorPassword.setText("Password must be at least 8"));
        } else {
            checkValidPassword = true;
            errorPassword.setText("");
        }

        if (confirmPasswordTxt.isEmpty()) {
            checkValidConfirmPassword = false;
            Platform.runLater(() -> errorConfirmPassword.setText("Confirm Password is required"));
        } else if (!confirmPasswordTxt.equals(passwordTxt)) {
            checkValidConfirmPassword = false;
            Platform.runLater(() -> errorConfirmPassword.setText("Please check your password"));
        } else {
            checkValidConfirmPassword = true;
            errorConfirmPassword.setText("");
        }

        boolean checkValid = checkValidConfirmPassword && checkValidEmail && checkValidName && checkValidPassword;

        if (checkValid) {

            // send data to server
            HandleOnlineSocket.getSendStream().println("signUp___"+userNameTxt+"___"+emailTxt+"___"+passwordTxt);

            // get data from server
            // to not hold the screen in gui client => open it in anther thread
             thread = new Thread(() -> {
                     String replyMsg;
                     try {
                         replyMsg = HandleOnlineSocket.getReceiveStream().readLine();
                         System.out.println(replyMsg);
                         String[] allReplyMsg = replyMsg.split("___");
                         if(allReplyMsg[0].equals("true"))
                         {
                             Platform.runLater(this::onSignUpPageSignInButton);
                         }
                         else
                         {
                             if(allReplyMsg[1].equals("name"))
                             {
                                 Platform.runLater(() -> errorUsername.setText("Name "+allReplyMsg[2]));
                             }
                             else if(allReplyMsg[1].equals("email"))
                             {
                                 Platform.runLater(() -> errorEmail.setText("Email "+allReplyMsg[2]));
                             }
                         }

                     } catch (IOException ex) {
                     }
                     finally {
                             thread.stop();
                     }
             });

            thread.start();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                        String replyMsg;
//                        try {
//                            replyMsg = dis.readLine();
//                            System.out.println(replyMsg);
//                            String[] allReplyMsg = replyMsg.split("___");
//                            if(allReplyMsg[0].equals("true"))
//                            {
//                                Platform.runLater(() -> {
//                                    onSignUpPageSignInButton();
//                                });
//                            }
//                            else
//                            {
//                                if(allReplyMsg[1].equals("name"))
//                                {
//                                    Platform.runLater(() -> {
//                                        errorUsername.setText("Name "+allReplyMsg[2]);
//                                    });
//                                }
//                                else if(allReplyMsg[1].equals("email"))
//                                {
//                                    Platform.runLater(() -> {
//                                        errorEmail.setText("Email "+allReplyMsg[2]);
//                                    });
//                                }
//                            }
//
//                        } catch (IOException ex) {
//                        }
//                    }
//                }
//
//            }).start();
//
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
        CommonControllers.closeWindow(SignUpScenePane,false);
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
        Stage stage =(Stage)SignUpScenePane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
}
