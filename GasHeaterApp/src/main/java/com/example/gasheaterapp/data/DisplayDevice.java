package com.example.gasheaterapp.data;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.skins.LcdSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class DisplayDevice extends Device{
    private double hight;
    private double width;
    private double layoutX;
    private double layoutY;
    private String unit;
    private double mouseOffsetX;
    private double mouseOffsetY;
    private Range range;

    public DisplayDevice(String name, int min, int max) {
        super(name, min, max);
    }

    public void setHight(double hight) {
        this.hight = hight;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }

    public void visualize(Gauge gauge, Pane pane) {
        gauge.setSkin(new LcdSkin(gauge));

        gauge.setPrefSize(width, hight);

        gauge.setLayoutX(layoutX);
        gauge.setLayoutY(layoutY);

        gauge.setTitle(getName());
        gauge.setUnit(unit);
        gauge.setMaxMeasuredValue(getRange().maxValue);
        gauge.setMinMeasuredValue(getRange().minValue);
        gauge.setMinMeasuredValueVisible(true);
        gauge.setMaxMeasuredValueVisible(true);
        if(getName().matches("Manometer")){
            gauge.setValue(1.5);
        }
        else{
            gauge.setValue((getRange().maxValue + getRange().minValue) / 2);
        }

        pane.getChildren().add(gauge);

        gauge.setOnMousePressed(this::handleGaugeMousePressed);
        gauge.setOnMouseDragged(this::handleGaugeMouseDragged);
    }

    private void handleGaugeMousePressed(MouseEvent event) {
        mouseOffsetX = event.getX();
        mouseOffsetY = event.getY();
    }

    private void handleGaugeMouseDragged(MouseEvent event) {
        double offsetX = event.getX() - mouseOffsetX;
        double offsetY = event.getY() - mouseOffsetY;

        Gauge gauge = (Gauge) event.getSource();

        double newX = gauge.getLayoutX() + offsetX;
        double newY = gauge.getLayoutY() + offsetY;

        double maxWidth = 580.0 - gauge.getWidth();
        double maxHeight = 200.0 - gauge.getHeight();
        newX = Math.max(0, Math.min(newX, maxWidth));
        newY = Math.max(0, Math.min(newY, maxHeight));

        gauge.setLayoutX(newX);
        gauge.setLayoutY(newY);
    }

}
