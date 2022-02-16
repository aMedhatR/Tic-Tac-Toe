package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class WelcomePageControl  implements Initializable {


    public Button welcomePageExitButton;
    @FXML
    private AnchorPane WelcomeScenePane;
    private double xOffset = 0;
    private double yOffset = 0;
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
    protected void onWelcomeGuestButtonClick() {

         CommonControllers.gotoStage("Home.fxml",WelcomeScenePane);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
    MediaPlayer mediaPlayer;
    public void music()
    {
        Media media= new Media(Objects.requireNonNull(getClass().getResource("/Audio/click.wav")).toExternalForm());
        mediaPlayer=new MediaPlayer(media);
        mediaPlayer.play();
    }
}