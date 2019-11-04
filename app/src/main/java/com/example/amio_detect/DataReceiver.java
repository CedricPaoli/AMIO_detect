package com.example.amio_detect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.amio_detect.ui.prefs.PrefsFragment;

public class DataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) && sharedPref.getBoolean(NotificationsFragment.SERVICES_PREF, true))
            context.startService(new Intent(context, MainService.class));
    }
}