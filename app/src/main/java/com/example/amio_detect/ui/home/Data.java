package com.example.amio_detect.ui.home;

import androidx.annotation.NonNull;

public class Data {
    private Long timestamp;
    private String label;
    private Double value;
    private String mote;

    Data(Long timestamp, String label, Double light_value, String mote) {
        this.timestamp = timestamp;
        this.label = label;
        this.value = light_value;
        this.mote = mote;
    }

    String getMote() {
        return "Mote: " + this.mote;
    }

    String getLight() {
        return "Lumiere: " + this.value;
    }

    @NonNull
    @Override
    public String toString() {
        return "Mote: " + this.mote + " | TimeStamp: " + this.timestamp + " | Label: " + this.label + " | Value: " + this.value;
    }
}
