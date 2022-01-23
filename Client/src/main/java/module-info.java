module com.ticclient.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens MainAndControl to javafx.fxml;
    exports MainAndControl;
}