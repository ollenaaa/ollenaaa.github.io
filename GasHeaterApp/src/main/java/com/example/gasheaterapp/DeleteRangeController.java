package com.example.gasheaterapp;

import com.example.gasheaterapp.data.Range;
import com.example.gasheaterapp.data.file.MyFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DeleteRangeController implements Initializable {
    private GasBoilerController _mainController;
    public void set_mainController(GasBoilerController _mainController) {
        this._mainController = _mainController;
    }

    @FXML
    private ListView ListRanges;
    @FXML
    private Label informationText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundl){
        MyFile myFile = new MyFile();
        List<Range> rangesTemperature = myFile.ReadFileRange();
        List<String> temperatureRanges = new ArrayList<>();
        for(Range rangeTemperature: rangesTemperature){
            int maxValue = rangeTemperature.getMaxValue();
            int minValue = rangeTemperature.getMinValue();
            temperatureRanges.add(minValue + "°C - " + maxValue + "°C");
        }
        ObservableList<String> rangesTemperatureList = FXCollections.observableList(temperatureRanges);
        ListRanges.setItems(rangesTemperatureList);
    }

    public void Delete(ActionEvent actionEvent) {
        MyFile myFile = new MyFile();
        Integer toDelete = ListRanges.getSelectionModel().getSelectedIndex();
        if(toDelete > 0){
            List<Range> rangesTemperature = myFile.DeleteRangeFromFile(toDelete);

            final Node source = (Node) actionEvent.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();

            _mainController.updateListsView(rangesTemperature);
            stage.close();
        } else{
            informationText.setText("Chose range to delete!");
        }
    }

    public void Cancel(ActionEvent actionEvent) {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
