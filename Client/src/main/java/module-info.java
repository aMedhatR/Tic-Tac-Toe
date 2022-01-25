module com.mycompany.mavenprojettest {
    requires javafx.controls;
    requires javafx.fxml;

<<<<<<< HEAD
    requires org.controlsfx.controls;

    opens MainAndControl to javafx.fxml;
    exports MainAndControl;
    exports controllers;
    opens controllers to javafx.fxml;
}
=======
    opens com.mycompany.mavenprojettest to javafx.fxml;
    exports com.mycompany.mavenprojettest;
}
>>>>>>> 2b295b34507b9190adb15d2f5897d20d7f105039
