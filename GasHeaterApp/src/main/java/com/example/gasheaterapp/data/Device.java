package com.example.gasheaterapp.data;

public class Device {
    String name;
    double measurement;
    Range range;

    public void setRange(Range range) {
        this.range = range;
    }

    public Range getRange() {
        return range;
    }

    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Device(String name, int minValue, int maxValue) {
        this.name = name;
        this.range = new Range(minValue, maxValue);
    }
}
