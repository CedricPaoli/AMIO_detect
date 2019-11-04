package com.example.amio_detect.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DataListParcelable implements Parcelable {
    private ArrayList dataList;

    private DataListParcelable(Parcel in) {
        this.dataList = in.readArrayList(Data.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.dataList);
    }

    public static final Creator<DataListParcelable> CREATOR = new Creator<DataListParcelable>() {
        @Override
        public DataListParcelable createFromParcel(Parcel in) {
            return new DataListParcelable(in);
        }

        @Override
        public DataListParcelable[] newArray(int size) {
            return new DataListParcelable[size];
        }
    };
}
