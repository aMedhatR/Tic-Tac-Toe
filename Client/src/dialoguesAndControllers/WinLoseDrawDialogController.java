package dialoguesAndControllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WinLoseDrawDialogController {
    @FXML
    private AnchorPane WinLosePageAnchorPane;
    @FXML
    private static ImageView WinLoseDrawImage;

    @FXML
    public void clientPageCloseButton() {
        Stage stage;
        stage = (Stage) WinLosePageAnchorPane.getScene().getWindow();
        stage.close();
    }

}