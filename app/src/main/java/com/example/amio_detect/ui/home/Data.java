package com.example.amio_detect.ui.home;

public class Data {
    private Long timestamp;
    private String label;
    private Double light_value;
    private String mote;

    public Data(Long timestamp, String label, Double light_value, String mote) {
        this.timestamp = timestamp;
        this.label = label;
        this.light_value = light_value;
        this.mote = mote;
    }

    public String getString() {
        return "Data = TimeStamp: " + this.timestamp + " | Label: " + this.label + " | Light value: " + this.light_value + " | Mote: " + this.mote;
    }
}
