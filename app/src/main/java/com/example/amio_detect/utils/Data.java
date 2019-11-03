package com.example.amio_detect.utils;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Data implements Parcelable {
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

    protected Data(Parcel in) {
        if (in.readByte() == 0) {
            timestamp = null;
        } else {
            timestamp = in.readLong();
        }
        label = in.readString();
        if (in.readByte() == 0) {
            value = null;
        } else {
            value = in.readDouble();
        }
        mote = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getMoteName() {
        return this.mote;
    }

    public String getMote() {
        return "Mote: " + this.mote;
    }

    public String getLight() {
        return "Lumiere: " + this.value;
    }

    public Date getRawDate() {
        Timestamp ts = new Timestamp(this.timestamp);

        return new Date(ts.getTime());
    }

    public String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.FRANCE);

        return "Dernière acquisition: " + simpleDateFormat.format(getRawDate());
    }

    public boolean isOn() {
        return this.value > 250;
    }

    public boolean isAvailable() {
        return System.currentTimeMillis() - this.timestamp < 3600000;
    }

    @NonNull
    @Override
    public String toString() {
        return "Mote: " + this.mote + " | TimeStamp: " + this.timestamp + " | Label: " + this.label + " | Value: " + this.value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (timestamp == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(timestamp);
        }

        dest.writeString(label);

        if (value == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(value);
        }

        dest.writeString(mote);
    }
}
