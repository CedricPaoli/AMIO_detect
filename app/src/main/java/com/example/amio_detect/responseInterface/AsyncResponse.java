package com.example.amio_detect.responseInterface;

import com.example.amio_detect.utils.Data;

import java.util.ArrayList;

public interface AsyncResponse {
    void updateRecyclerView(ArrayList<Data> arrayList);
}