package com.example.amio_detect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("DataReceiver", "DataReceiver BOOT_COMPLETED");
        }

        Log.e("DataReceiver", "Received");
        context.startService(new Intent(context, MainService.class));
    }
}
