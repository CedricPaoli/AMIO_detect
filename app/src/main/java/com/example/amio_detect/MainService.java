package com.example.amio_detect;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.amio_detect.utils.GetSensors;

import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    final Handler handler = new Handler();
    final Timer timer = new Timer();

    public void onCreate() {
        Log.d("MainActivity", "Creation du service");

        TimerTask task = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        GetSensors getSensors = new GetSensors(MainService.this);
                        getSensors.execute("http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last");

                        //Toast.makeText(MainService.this, "Une lumière vient de s'allumer (mote: " + "" + ")", Toast.LENGTH_SHORT).show();
                        Log.d("MainActivity", "Plop!");
                    }
                });
            }
        };

        timer.schedule(task, 30000, 30000);
    }

    public void onDestroy() {
        Log.d("MainActivity", "Arret du service");
        timer.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
