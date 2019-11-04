package com.example.amio_detect.ui.prefs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.amio_detect.MainService;
import com.example.amio_detect.R;

import java.util.Objects;

public class PrefsFragment extends PreferenceFragmentCompat {
    public static final String SERVICES_PREF = "services";
    public static final String NOTIFICATIONS_PREF = "notifications";
    public static final String START_PREF = "start";
    public static final String STOP_PREF = "stop";
    public static final String START_WE_PREF = "start_we";
    public static final String STOP_WE_PREF = "stop_we";
    public static final String START_MAIL_PREF = "start_mail";
    public static final String STOP_MAIL_PREF = "stop_mail";
    public static final String[] WEEK = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getContext()));
        String to = sharedPref.getString("your_mail", "");
        Objects.requireNonNull(findPreference("your_mail")).setTitle(to);
    }

    @Override
    public boolean onPreferenceTreeClick (Preference preference) {
        String key = preference.getKey();

        if(key.equals("services")){
            if(preference.getPreferenceManager().getSharedPreferences().getBoolean("services", false))
                Objects.requireNonNull(getActivity()).stopService(new Intent(preference.getContext(), MainService.class));
            else Objects.requireNonNull(getActivity()).startService(new Intent(preference.getContext(), MainService.class));

            return true;
        }

        return super.onPreferenceTreeClick(preference);
    }
}