package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Pair;
import mainPk.HandleOnlineSocket;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class WelcomePageControl implements Initializable {


    public Button welcomePageExitButton;
    MediaPlayer mediaPlayer;
    public TextField remoteIPAddressField;
    public TextField PortField;
    @FXML
    private AnchorPane WelcomeScenePane;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    protected void onWelcomeCloseButtonClick() {
        CommonControllers.closeWindow(WelcomeScenePane, false);

    }

    @FXML
    protected void onWelcomeSignInButtonClick() {
        CommonControllers.gotoStage("signIn.fxml", WelcomeScenePane);
    }

    @FXML
    protected void onWelcomeSignUpButtonClick() {
        CommonControllers.gotoStage("signup.fxml", WelcomeScenePane);

    }

    @FXML
    protected void onWelcomeGuestButtonClick() {

        CommonControllers.gotoStage("Home.fxml", WelcomeScenePane);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    protected void handlePressedAction(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) WelcomeScenePane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    public void music() {
        Media media = new Media(Objects.requireNonNull(getClass().getResource("/Audio/click.wav")).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    @FXML
    protected void addIPandPort() {

        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Connect to server");
        dialog.setHeaderText("Please insert remoteIP and portNumber");

        // Set the icon (must be included in the project).
//        dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType connectButtonType = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField ipAddress = new TextField();
        ipAddress.setPromptText("Please enter IP:");
        TextField portNumber = new TextField();
        portNumber.setPromptText("Please enter portNumber:");

        grid.add(new Label("Ip Address:"), 0, 0);
        grid.add(ipAddress, 1, 0);
        grid.add(new Label("Port Number:"), 0, 1);
        grid.add(portNumber, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node connectButton = dialog.getDialogPane().lookupButton(connectButtonType);
        connectButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        ipAddress.textProperty().addListener((observable, oldValue, newValue) -> {
            connectButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> ipAddress.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == connectButtonType) {
                return new Pair<>(ipAddress.getText(), portNumber.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(connectToServer -> {
            String remoteIP = connectToServer.getKey();
            String portNum = connectToServer.getValue();
            HandleOnlineSocket.setvaluePortNumber(portNum);
            HandleOnlineSocket.setRemoteIP(remoteIP);
        });






    }
}