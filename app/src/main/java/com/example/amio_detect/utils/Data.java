package com.example.amio_detect.utils;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public String getMote() {
        return "Mote: " + this.mote;
    }

    public String getLight() {
        return "Lumiere: " + this.value;
    }

    public String getDate() {
        Timestamp ts = new Timestamp(this.timestamp);
        Date date = new Date(ts.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.FRANCE);

        return "Dernière acquisition: " + simpleDateFormat.format(date);
    }

    public boolean isOn() {
        return this.value > 250;
    }

    @NonNull
    @Override
    public String toString() {
        return "Mote: " + this.mote + " | TimeStamp: " + this.timestamp + " | Label: " + this.label + " | Value: " + this.value;
    }
}
