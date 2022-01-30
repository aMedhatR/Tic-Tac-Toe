package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mainPk.HandleOnlineSocket;
import person.Person;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class CommonControllers {
    Stage stage;

    public static void closeWindow(AnchorPane pane ,boolean After) {
        Stage stage;
        Alert WelcomeExitAlert= new Alert(Alert.AlertType.CONFIRMATION);
        WelcomeExitAlert.setTitle("Exit");
        WelcomeExitAlert.setHeaderText("you're about to logout!");
        WelcomeExitAlert.setContentText("Are You Sure you want to Exit ?");
        if (WelcomeExitAlert.showAndWait().get()== ButtonType.OK) {
            stage = (Stage) pane.getScene().getWindow();
            System.out.println("you logout");
            if (After) {
                try {
                    CommonControllers.signOut();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            stage.close();
        }
    }
    public static void closeWindow(BorderPane pane ,boolean After) {
        Stage stage;
        Alert WelcomeExitAlert= new Alert(Alert.AlertType.CONFIRMATION);
        WelcomeExitAlert.setTitle("Exit");
        WelcomeExitAlert.setHeaderText("you're about to logout!");
        WelcomeExitAlert.setContentText("Are You Sure you want to Exit ?");
        if (WelcomeExitAlert.showAndWait().get()== ButtonType.OK) {
            stage = (Stage) pane.getScene().getWindow();
            System.out.println("you logout");
            if (After) {
                try {
                    CommonControllers.signOut();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            stage.close();
        }
    }
    public static void goToHome(AnchorPane pane) {
        Stage stage;
        Scene scene;
        stage = (Stage) pane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(mainPk.MainApp.class.getResource("/fxmlFiles/WelcomePage.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void goToHome(BorderPane pane) {
        Stage stage;
        Scene scene;
        stage = (Stage) pane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(mainPk.MainApp.class.getResource("/fxmlFiles/WelcomePage.fxml"));
        try {
            scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void signOut () throws IOException {
        HandleOnlineSocket.getSendStream().println("Logout___"+Person.getId());

        HandleOnlineSocket.getReceiveStream().close();
        HandleOnlineSocket.getSendStream().close();
        HandleOnlineSocket.getMySocket().close();
    }
}
