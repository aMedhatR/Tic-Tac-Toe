package mainPk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/ClientPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("The Fabulous Tic Tac Toe");
//        Image icon =new Image("");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

   public static void main(String[] args) {
        new HandleOnlineSocket();
        launch();
    }
}