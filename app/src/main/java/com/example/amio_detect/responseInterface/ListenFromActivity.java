package com.example.amio_detect.responseInterface;

import com.example.amio_detect.utils.Data;

import java.util.ArrayList;

public interface ListenFromActivity {
    /** Mise à jour du fragment Home avec la liste en argument **/
    void reloadRecycle(ArrayList<Data> list);
}