package com.example.gasheaterapp;

import com.example.gasheaterapp.data.DisplayDevice;
import com.example.gasheaterapp.data.Range;
import com.example.gasheaterapp.data.WarningDisplay;
import com.example.gasheaterapp.data.file.MyFile;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import eu.hansolo.medusa.Gauge;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GasBoilerController implements Initializable {
    DisplayDevice deviceThermometer;
    DisplayDevice deviceManometer;
    WarningDisplay warningDisplay;
    Gauge gaugeThermometer = new Gauge();
    Gauge gaugeManometer = new Gauge();

    @FXML
    private ComboBox rangeTemperatures;
    @FXML
    private Pane mainPane;
    @FXML
    private Label warningLable;
    @FXML
    private Pane warningPane;
    @FXML
    private Button auto;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MyFile myFile = new MyFile();
        List<Range> rangesTemperature = myFile.ReadFileRange();
        updateListsView(rangesTemperature);

        deviceThermometer = new DisplayDevice("Thermometer", 42, 86);
        deviceThermometer.setMeasurement(64);
        deviceManometer = new DisplayDevice("Manometer", 1, 3);

        createDisplayThermometer();
        createDisplayManometer();

        WarningInformation((int)deviceThermometer.getMeasurement());
    }

    public List<Range> updateListsView(List<Range> rangesTemperature){
        List<String> temperatureRanges = new ArrayList<>();
        for(Range rangeTemperature: rangesTemperature){
            int maxValue = rangeTemperature.getMaxValue();
            int minValue = rangeTemperature.getMinValue();
            temperatureRanges.add(minValue + "°C - " + maxValue + "°C");
        }
        rangeTemperatures.setItems(FXCollections.observableList(temperatureRanges));
        return rangesTemperature;
    }

    public void createDisplayThermometer(){
        deviceThermometer.setUnit("°C");
        deviceThermometer.setHight(100.0);
        deviceThermometer.setWidth(175.0);
        deviceThermometer.setLayoutX(300.0);
        deviceThermometer.setLayoutY(50.0);
        deviceThermometer.visualize(gaugeThermometer, mainPane);
    }

    public void createDisplayManometer(){
        deviceManometer.setUnit("bar");
        deviceManometer.setHight(100.0);
        deviceManometer.setWidth(135.0);
        deviceManometer.setLayoutX(125.0);
        deviceManometer.setLayoutY(50.0);
        deviceManometer.visualize(gaugeManometer, mainPane);
    }

    public void rangeOfTemperatures(ActionEvent actionEvent) {
        Integer index = rangeTemperatures.getSelectionModel().getSelectedIndex();

        String str = (rangeTemperatures.getItems().get(index)).toString();

        String[] values = str.split("-");
        String minValueString = values[0].trim().replace("°C", "");
        String maxValueString = values[1].trim().replace("°C", "");

        int maxValue = Integer.parseInt(maxValueString);
        int minValue = Integer.parseInt(minValueString);

        warningLable.setText(warningDisplay.getMessage());
        warningPane.setBackground(new Background(new BackgroundFill(Color.web(warningDisplay.getColor()), CornerRadii.EMPTY, Insets.EMPTY)));

        deviceThermometer.setRange(new Range(minValue, maxValue));
        deviceThermometer.setMeasurement((deviceThermometer.getRange().getMaxValue() + deviceThermometer.getRange().getMinValue()) / 2);

        gaugeThermometer.setMaxMeasuredValue(deviceThermometer.getRange().getMaxValue());
        gaugeThermometer.setMinMeasuredValue(deviceThermometer.getRange().getMinValue());
        gaugeThermometer.setValue(deviceThermometer.getMeasurement());

        WarningInformation((int) deviceThermometer.getMeasurement());

        findPressure();

        mainPane.requestLayout();

    }

    public void WarningInformation(int temperature) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if(temperature <= 56.0){
                    warningDisplay = new WarningDisplay(WarningDisplay.MessageType.INFORMATION);
                } else if (temperature > 56 && temperature <= 71.0) {
                    warningDisplay = new WarningDisplay(WarningDisplay.MessageType.WARNING);
                } else if (temperature > 71 && temperature < 87) {
                    warningDisplay = new WarningDisplay(WarningDisplay.MessageType.ALERT);
                }
                Platform.runLater(() -> warningPane.setBackground(new Background(new BackgroundFill(Color.web(warningDisplay.getColor()), CornerRadii.EMPTY, Insets.EMPTY))));
                Platform.runLater(() -> warningLable.setText(warningDisplay.getMessage()));
                return null;
            }
        };

        new Thread(task).start();
    }

    public void plusTemperature(ActionEvent actionEvent) {
        Double prevTemperature = deviceThermometer.getMeasurement();
        if(prevTemperature < deviceThermometer.getRange().getMaxValue()){
            deviceThermometer.setMeasurement(prevTemperature + 1);
            gaugeThermometer.setValue(deviceThermometer.getMeasurement());
            findPressure();
            WarningInformation((int) deviceThermometer.getMeasurement());
        }
    }

    public void minusTemperature(ActionEvent actionEvent) {
        Double prevTemperature = deviceThermometer.getMeasurement();
        if(prevTemperature > deviceThermometer.getRange().getMinValue()) {
            deviceThermometer.setMeasurement(prevTemperature - 1);
            gaugeThermometer.setValue(deviceThermometer.getMeasurement());
            findPressure();
            WarningInformation((int) deviceThermometer.getMeasurement());
        }
    }

    public void AddRange(ActionEvent actionEvent) {
        Stage newWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(AddRangeController.class.getResource("add-range.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newWindow.setTitle("Add Range Of Temperature");
        newWindow.setScene(new Scene(root, 300, 200));
        AddRangeController secondController = loader.getController();
        secondController.set_mainController(this);
        newWindow.show();
    }

    Timer timer = null;
    boolean isRunning = false;

    public void Auto(ActionEvent actionEvent) {
        if (!isRunning) {
            MyFile myFile = new MyFile();

            auto.setText("STOP");

            gaugeThermometer.setMinMeasuredValue(42);
            gaugeThermometer.setMaxMeasuredValue(86);
            deviceThermometer.setRange(new Range(42, 86));

            List<Integer> measurements = myFile.readRandomTemperatureValues();

            timer = new Timer();
            TimerTask task = new TimerTask() {
                private int index = 0;

                @Override
                public void run() {
                    if (index < measurements.size()) {
                        int measurement = measurements.get(index);
                        gaugeThermometer.setValue(measurement);
                        deviceThermometer.setMeasurement(measurement);
                        findPressure();
                        WarningInformation((int)deviceThermometer.getMeasurement());
                        index++;
                    } else {
                        timer.cancel();
                        auto.setText("AUTO");
                        isRunning = false;
                    }
                }
            };

            long intervalInMillis = (long) (5 * 1000);
            timer.schedule(task, 0, intervalInMillis);
            isRunning = true;
        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            auto.setText("AUTO");
            isRunning = false;
        }
    }


    public void DeleteRange(ActionEvent actionEvent) {
        Stage newWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(DeleteRangeController.class.getResource("delete-range.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newWindow.setTitle("Delete Range Of Temperature");
        newWindow.setScene(new Scene(root, 300, 200));
        DeleteRangeController thirdController = loader.getController();
        thirdController.set_mainController(this);
        newWindow.show();
    }

    public void findPressure(){
        Double pressure = ((273.0 + deviceThermometer.getMeasurement() * 2) / 273.0);
        deviceManometer.setMeasurement(pressure);
        gaugeManometer.setValue(deviceManometer.getMeasurement());
    }
}