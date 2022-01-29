package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientPageController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    private AnchorPane ClientScenePane;


    Stage stage;
    Scene scene;
    @FXML
    protected void clientPageCloseButton() {
        CommonControllers.closeWindow(ClientScenePane);

    }

}
