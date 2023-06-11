package com.example.gasheaterapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GasBoilerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GasBoilerApplication.class.getResource("gas-boiler.fxml"));
        fxmlLoader.setCharset(StandardCharsets.UTF_8);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Gas Heater");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}