package com.example.gasheaterapp.data;

public class WarningDisplay extends Device{
    private String color;
    private String message;

    public String getMessage() {
        return message;
    }

    public String getColor() {
        return color;
    }

    private void WarningView(MessageType messageType) {
        if (messageType == MessageType.INFORMATION) {
            this.color = "#abffb9";
            this.message = "INFORMATION! Temperature is normal.";
        } else if (messageType == MessageType.WARNING) {
            this.color = "#ffef95";
            this.message = "WARNING! Temperature is hot. Can be danger.";
        } else {
            this.color = "#f2a3a3";
            this.message = "ALERT! Temperature is too hot!";
        }
    }


    public enum MessageType {
        INFORMATION,
        WARNING,
        ALERT
    }

    public WarningDisplay(MessageType messageType) {
        super("Warning Display", 0, 0);
        WarningView(messageType);
    }

    public WarningDisplay(String name, int min, int max) {
        super(name, min, max);
    }

}
