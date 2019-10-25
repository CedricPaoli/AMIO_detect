package com.example.amio_detect.ui.home;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class MyParser {
    private List<Data> data_list;

    MyParser() {
        data_list = new ArrayList<>();
    }

    List readJsonStream(InputStream in) {
        Log.d("MainActivity", "readJsonStreamOk");

        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            reader.beginObject();
            if (reader.hasNext()) {
                if (reader.nextName().equals("data")) {
                    reader.beginArray();

                    while (reader.hasNext())
                        data_list.add(readData(reader));

                    reader.endArray();
                }
            }

            reader.endObject();
            return data_list;
        } catch (IOException e) {
            Log.d("MainActivity", "readJsonStreamError");
            return null;
        }
    }

    private Data readData(JsonReader reader) throws IOException {
        Long timestamp = null;
        String label = null;
        Double light_value = null;
        String mote = null;

        Log.d("MainActivity", "readJsonStreamError");

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
                case "light_value":
                    light_value = reader.nextDouble();
                    break;
                case "mote":
                    mote = reader.nextString();
                    break;
                default:
                    reader.skipValue();
            }
        }

        reader.endObject();

        return new Data(timestamp, label, light_value, mote);
    }
}
