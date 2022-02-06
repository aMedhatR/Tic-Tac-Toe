/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import mainPk.HandleOnlineSocket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import person.Person;

/**
 *
 * @author nora
 */
public class signInController implements Initializable {
    Stage stage;
    Scene scene;
    Thread thread;

    @FXML
    private Label errorUsername;
    @FXML
    private Label errorPassword;
    @FXML
    private Label error;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @FXML
    private BorderPane SignInPaneScene;
    @FXML

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


    @FXML
    protected void onSignInSignInButton()
    {
        boolean checkValidName = false;
        boolean checkValidPassword = false;

        String userNameTxt = username.getText().trim();
        String passwordTxt = password.getText().trim();

        if (userNameTxt.isEmpty()) {
            checkValidName = false;
            Platform.runLater(() -> {
                errorUsername.setText("Username is required");
            });
        } else {
            checkValidName = true;
            errorUsername.setText("");
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
        boolean checkValid =  checkValidName && checkValidPassword;

        if (checkValid) {

            // send data to server
            HandleOnlineSocket.getSendStream().println("signIn___"+userNameTxt+"___"+passwordTxt);
            // get data from server
            // to not hold the screen in gui client => open it in anther thread
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String replyMsg;
                    try {
                        replyMsg = HandleOnlineSocket.getReceiveStream().readLine();
                        System.out.println(replyMsg);
                        String[] allReplyMsg = replyMsg.split("___");
                        if(allReplyMsg[0].equals("true"))
                        {
                            try {
                                Person.setId(Integer.parseInt(allReplyMsg[1]));
                                Person.setName(allReplyMsg[2]);
                                Person.setScore(Integer.parseInt(allReplyMsg[3]));
                            }catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            Platform.runLater(() -> {

                                goToClientPage();
                            });
                        }
                        else
                        {
                            Platform.runLater(() -> {
                                error.setText("Try Again wrong password or username");
                            });
                        }

                    } catch (IOException ex) {
                    }
                    finally {
                        thread.stop();
                    }
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
    protected void goToClientPage()
    {
        stage = (Stage) SignInPaneScene.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/ClientPage.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
    }


    @FXML
    protected void goToHome() {
        CommonControllers.goToHome(SignInPaneScene);
    }
    @FXML
    protected void onWelcomeCloseButtonClick() {
        CommonControllers.closeWindow(SignInPaneScene,false);
    }
}
