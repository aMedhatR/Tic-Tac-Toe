/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.SQLException;

import javafx.stage.StageStyle;
import socket.SocketServer;
import startServer.App;

/**
 *
 * @author Abdelrahman Mostafa
 * <Abdelrahman Mostafa at Information Technology Institute>
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/ServerPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("The Fabulous Tic Tac Toe");
//        Image icon =new Image("");
        stage.getIcons().add(new Image(main.MainApp.class.getResourceAsStream("/images/servericon.png")));

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, Exception {

       //     new SocketServer();
            
            launch();
    }
}