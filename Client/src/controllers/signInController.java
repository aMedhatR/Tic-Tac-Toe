/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import mainPk.HandleOnlineSocket;
import person.Person;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static mainPk.HandleOnlineSocket.getMySocket;

/**
 *
 * @author nora
 */
public class signInController implements Initializable {

    public Button welcomePageExitButton;
    Thread thread;
    private double xOffset = 0;
    private double yOffset = 0;
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
        if (getMySocket()==null) {
            new HandleOnlineSocket();
        }
    }


    @FXML
    protected void onSignInSignInButton()
    {
        boolean checkValidName;
        boolean checkValidPassword;

        String userNameTxt = username.getText().trim();
        String passwordTxt = password.getText().trim();

        if (userNameTxt.isEmpty()) {
            checkValidName = false;
            Platform.runLater(() -> errorUsername.setText("Username is required"));
        } else {
            checkValidName = true;
            errorUsername.setText("");
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
        boolean checkValid =  checkValidName && checkValidPassword;

        if (checkValid) {

            // send data to server
            HandleOnlineSocket.getSendStream().println("signIn___"+userNameTxt+"___"+passwordTxt);
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
                        try {
                            Person.setId(Integer.parseInt(allReplyMsg[1]));
                            Person.setName(allReplyMsg[2]);
                            Person.setScore(Integer.parseInt(allReplyMsg[3]));
                        }catch(Exception e)
                        {
                            System.out.println(e);
                        }
                        Platform.runLater(this::goToClientPage);
                    }
                    else
                    {
                        Platform.runLater(() -> {
                            if(allReplyMsg[1].equals("isOnlineInOtherPlace"))
                            {
                                error.setText("Your account is open in other desktop");
                            }
                            else
                            error.setText("Try Again wrong password or username");
                        });
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
    protected void onSignInSignUpButton() {
CommonControllers.gotoStage("signup.fxml",SignInPaneScene);

    }


    @FXML
    protected void goToClientPage()
    {
        CommonControllers.gotoStage("ClientPage.fxml",SignInPaneScene);

    }


    @FXML
    protected void goToHome() throws IOException {
        CommonControllers.goToHome(SignInPaneScene);
    }
    @FXML
    protected void onWelcomeCloseButtonClick() {
        CommonControllers.closeWindow(SignInPaneScene,false);
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
        Stage stage =(Stage)SignInPaneScene.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
    MediaPlayer mediaPlayer;
    public void music()
    {
        Media media= new Media(Objects.requireNonNull(getClass().getResource("/Audio/click.wav")).toExternalForm());
        mediaPlayer=new MediaPlayer(media);
        mediaPlayer.play();
    }

}
