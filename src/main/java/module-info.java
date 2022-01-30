module com.example.xo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.xo to javafx.fxml;
    exports com.example.xo;
}