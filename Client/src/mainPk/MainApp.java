package mainPk;

import controllers.ClientPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

    public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
       music();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/WelcomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("The Fabulous Tic Tac Toe");
        stage.getIcons().add(new Image(mainPk.MainApp.class.getResourceAsStream("/Images/icon.png")));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();


    }
    MediaPlayer mediaPlayer;
    public void music()
    {

        Media media= new Media(getClass().getResource("/Audio/backgroundmusic.mp3").toExternalForm());
        mediaPlayer=new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();
    }

   public static void main(String[] args) {

        launch();
    }
}