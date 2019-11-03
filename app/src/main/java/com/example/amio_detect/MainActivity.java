package com.example.amio_detect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.amio_detect.utils.AsyncResponse;
import com.example.amio_detect.utils.Data;
import com.example.amio_detect.utils.ListenFromActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    public static final String LIST_RECEIVER = "listReceiver";
    public ListenFromActivity activityListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(LIST_RECEIVER);

        this.startService(new Intent(this, MainService.class)); //A enlever
    }

    private BroadcastReceiver batchProcessReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), LIST_RECEIVER)) {
            ArrayList<? extends Data> list = intent.getParcelableArrayListExtra("dataList");
            MainActivity.this.activityListener.reloadRecycle((ArrayList<Data>) list);
        }
        }
    };

    public void setActivityListener(ListenFromActivity activityListener) {
        this.activityListener = activityListener;
    }

    public void updateRecyclerView(ArrayList<Data> list) {
        this.activityListener.reloadRecycle(list);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(batchProcessReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerReceiver(batchProcessReceiver, new IntentFilter(LIST_RECEIVER));
        super.onResume();
    }
}
