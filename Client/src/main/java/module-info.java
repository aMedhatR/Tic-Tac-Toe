module com.mycompany.mavenprojettest {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.mavenprojettest to javafx.fxml;
    exports com.mycompany.mavenprojettest;
}
