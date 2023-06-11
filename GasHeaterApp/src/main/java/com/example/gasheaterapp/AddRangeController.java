package com.example.gasheaterapp;

import com.example.gasheaterapp.data.Range;
import com.example.gasheaterapp.data.file.MyFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;

public class AddRangeController {
    private GasBoilerController _mainController;
    public void set_mainController(GasBoilerController _mainController) {
        this._mainController = _mainController;
    }
    @FXML
    private TextField maxValue;
    @FXML
    private TextField minValue;
    @FXML
    private Label TextMinValue;
    @FXML
    private Label TextMaxValue;


    public void Save(ActionEvent actionEvent) {
        MyFile myFile = new MyFile();
        boolean flag = Check();
        if (flag){
            List<Range> rangesTemperature = myFile.SaveRangeToFile(new Range(Integer.parseInt(minValue.getText()), Integer.parseInt(maxValue.getText())));

            final Node source = (Node) actionEvent.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();

            _mainController.updateListsView(rangesTemperature);
            stage.close();
        }
    }

    private boolean Check(){
        MyFile myFile = new MyFile();
        String regex = "\\d+$";
        List<Range> rangesTemperature = myFile.ReadFileRange();
        if(!minValue.getText().matches(regex)){
            TextMinValue.setText("Only numbers");
            TextMaxValue.setText("");
            return false;
        } else if (!maxValue.getText().matches(regex)) {
            TextMaxValue.setText("Only numbers");
            TextMinValue.setText("");
            return false;
        } else if (maxValue.getText().isEmpty()){
            TextMaxValue.setText("Fill max value");
            TextMinValue.setText("");
            return false;
        } else if (minValue.getText().isEmpty()) {
            TextMinValue.setText("Fill min value");
            TextMaxValue.setText("");
            return false;
        } else if (maxValue.getText().equals(minValue.getText()) || Integer.parseInt(maxValue.getText()) < Integer.parseInt(minValue.getText())) {
            TextMinValue.setText("Min value < max value");
            TextMaxValue.setText("Max value > min value");
            return false;
        } else if (Integer.parseInt(maxValue.getText()) > 86) {
            TextMaxValue.setText("Too big temperature");
            TextMinValue.setText("");
            return false;
        } else if (Integer.parseInt(minValue.getText()) < 42) {
            TextMaxValue.setText("");
            TextMinValue.setText("Too small temperature");
            return false;
        }
        for(Range rangeTemperature: rangesTemperature){
            int max = rangeTemperature.getMaxValue();
            int min = rangeTemperature.getMinValue();
            if(Integer.parseInt(minValue.getText()) == min && Integer.parseInt(maxValue.getText()) == max){
                TextMaxValue.setText("Already exist in ranges");
                TextMinValue.setText("Already exist in ranges");
                return false;
            }
        }
        return true;
    }

    public void Cancel(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
