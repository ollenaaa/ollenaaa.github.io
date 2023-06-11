module com.example.gasheaterapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires eu.hansolo.medusa;
    requires com.google.gson;


    opens com.example.gasheaterapp to javafx.fxml;
    exports com.example.gasheaterapp;
}