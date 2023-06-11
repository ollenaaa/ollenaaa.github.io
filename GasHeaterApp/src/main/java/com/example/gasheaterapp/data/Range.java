package com.example.gasheaterapp.data;

public class Range{
    int minValue;
    int maxValue;
    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
    public Range(int minValue, int maxValue){
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
}
