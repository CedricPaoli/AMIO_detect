package com.example.amio_detect.utils;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class MyParser {
    private ArrayList<Data> dataList;

    MyParser() {
        dataList = new ArrayList<>();
    }

    /** Lecture du stream et separation des data **/
    ArrayList<Data> readJsonStream(InputStream in) {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            reader.beginObject();

            if (reader.hasNext()) {
                if (reader.nextName().equals("data")) {
                    reader.beginArray();

                    while (reader.hasNext())
                        dataList.add(readData(reader));

                    reader.endArray();
                }
            }

            reader.endObject();
            Log.d("MainActivity", "readJsonStreamOk");

            return dataList;
        } catch (IOException e) {
            Log.d("MainActivity", "readJsonStreamError");

            return null;
        }
    }

    /** Lecture des data pour créer des objets de mote **/
    private Data readData(JsonReader reader) throws IOException {
        Long timestamp = null;
        String label = null;
        Double value = null;
        String mote = null;

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            switch (name) {
                case "timestamp":
                    timestamp = reader.nextLong();
                    break;
                case "label":
                    label = reader.nextString();
                    break;
                case "value":
                    value = reader.nextDouble();
                    break;
                case "mote":
                    mote = reader.nextString();
                    break;
                default:
                    reader.skipValue();
            }
        }

        reader.endObject();

        return new Data(timestamp, label, value, mote);
    }
}
