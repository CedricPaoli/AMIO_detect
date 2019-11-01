package com.example.amio_detect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //startService(new Intent(this, MainService.class));

        SharedPreferences sharedPref =
                PreferenceManager
                        .getDefaultSharedPreferences(this);
        /*Boolean switchPref = sharedPref.getBoolean
                (NotificationsFragment.KEY_PREF_EXAMPLE_SWITCH, false);
        Toast.makeText(this, switchPref.toString(),
                Toast.LENGTH_SHORT).show();*/

        sendEmail();

    }

    protected void sendEmail() {
        Log.i("Send email", "sending mail");
        SharedPreferences sharedPref =
                PreferenceManager
                        .getDefaultSharedPreferences(this);

        try {
            String to = "";
            to = sharedPref.getString("your_mail",to);
            GMailSender sender = new GMailSender("no.reply.amio@gmail.com", "TNCY@2019");
            sender.sendMail("Test Mail",
                    "This is Body",
                    "no.reply.amio@gmail.com",
                    to);
            Log.i("SendMail", "mail sent to : " + to + " || ");
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }
}
