package MainAndControl;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WelcomePageControl {
    @FXML
    private Label welcomeText;
    @FXML
    private Button welcomePageExitButton;
    @FXML
    private AnchorPane WelcomeScenePane;

    Stage stage;
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
}