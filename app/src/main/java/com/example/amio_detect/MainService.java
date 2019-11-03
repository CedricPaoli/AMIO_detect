package com.example.amio_detect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.example.amio_detect.ui.notifications.NotificationsFragment;
import com.example.amio_detect.utils.Data;
import com.example.amio_detect.utils.GetSensors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainService extends Service {
    final Handler handler = new Handler();
    final Timer timer = new Timer();

    public void onCreate() {
        TimerTask task = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                         GetSensors getSensors = new GetSensors(getApplicationContext(), MainService.this);
                         getSensors.execute("http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last");
                    }
                });
            }
        };

        timer.schedule(task, 0, 5000);
    }

    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
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

    public void updateList(ArrayList<Data> list) {
        Intent intent = new Intent(MainActivity.LIST_RECEIVER);
        intent.putParcelableArrayListExtra("dataList", list);
        sendBroadcast(intent);

        this.sendNotification(list);
    }

    public void sendNotification(ArrayList<Data> list) {
        HashMap<String, Data> listMotesLit = new HashMap<>();
        Calendar c = Calendar.getInstance();
        ArrayList<Integer> weekday = new ArrayList<>();
        ArrayList<String> moteActivated = new ArrayList<>();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getApplicationContext()));
        int from = sharedPref.getInt(NotificationsFragment.START_PREF, 19);
        int to = sharedPref.getInt(NotificationsFragment.STOP_PREF, 23);

        c.setTime(new Date());

        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
        boolean isBetween = (to > from && t >= from && t <= to) || (to < from && (t >= from || t <= to));

        for (int i = 0; i < NotificationsFragment.WEEK.length; i++)
            if (!sharedPref.getBoolean(NotificationsFragment.WEEK[i], false))
                weekday.add(i);

        for (int i = 0; i < list.size(); i++) {
            Data mote = list.get(i);

            if (mote.isAvailable()) {
                String moteName = mote.getMote();

                if (!listMotesLit.containsKey(moteName) && mote.isOn()) {
                    c.setTime(mote.getRawDate());

                    if (weekday.contains(c.get(Calendar.DAY_OF_WEEK)) && isBetween)
                        moteActivated.add(moteName);

                    listMotesLit.put(moteName, mote);
                } else if (listMotesLit.containsKey(mote.getMote()) && !mote.isOn()) {
                    listMotesLit.remove(mote.getMote());
                }
            }
        }

        if(sharedPref.getBoolean(NotificationsFragment.NOTIFICATIONS_PREF, false) && moteActivated.size() > 0) {
            String moteActivatedString = moteActivated.get(0);

            if (moteActivated.size() > 1)
                for (int i = 1; i < moteActivated.size(); i++)
                    moteActivatedString = moteActivatedString.concat(", " + moteActivated.get(i));

            this.sendNotification(this, moteActivatedString);
        }
    }

    public void sendNotification(Context context, String moteLabels) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("amio_detect_1", "amio_detect", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Amio detect channnel");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{500, 500, 500, 500});
            channel.setShowBadge(false);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "amio_detect_1")
                .setContentTitle("Detection")
                .setContentText("Une lumière vient de s'allumer (mote(s): " + moteLabels + ")")
                .setSmallIcon(R.drawable.ic_stat_onesignal_default);

        NotificationManagerCompat.from(context).notify((int)(System.currentTimeMillis()/1000), builder.build());
    }
}
