package com.example.amio_detect.ui.notifications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.amio_detect.R;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        loadPref(root);

        setOnCheckedChangeListener(root);

        return root;
    }

    private void loadPref(View root) {
        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getActivity());

        // get min and max text view
        TextView tvMin = (TextView) root.findViewById(R.id.textMin1);
        TextView tvMax = (TextView) root.findViewById(R.id.textMax1);

        Switch s1 = (Switch) root.findViewById(R.id.switch1);
        Switch s2 = (Switch) root.findViewById(R.id.switch2);

        EditText mail = root.findViewById(R.id.yourMail);

        String s = "";

        //int i = prefs.getInt("starting_survey_hour", -1);
        //tvMin.setText(i + 's');

        /*i = prefs.getInt("stopping_survey_hour", -1);
        if (i < 0) {
            tvMax.setText(i);
            Log.e("msg", i+"");
        }
        */

        s1.setChecked(prefs.getBoolean("service_on",false));
        s2.setChecked(prefs.getBoolean("notification_on",false));

        mail.setText(prefs.getString("your_mail",s));

    }


    private void setOnCheckedChangeListener(View root) {

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) root.findViewById(R.id.rangeSeekbar1);

        // get min and max text view
        final TextView tvMin = (TextView) root.findViewById(R.id.textMin1);
        final TextView tvMax = (TextView) root.findViewById(R.id.textMax1);

        final Switch s1 = (Switch) root.findViewById(R.id.switch1);
        final Switch s2 = (Switch) root.findViewById(R.id.switch2);

        final EditText mail = root.findViewById(R.id.yourMail);

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                int mini = minValue.intValue()%24;
                int maxi = maxValue.intValue()%24;
                tvMin.setText(String.valueOf(mini));
                tvMax.setText(String.valueOf(maxi));


                // Saving the new values
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt("starting_survey_hour", mini);
                edit.commit();
                edit.putInt("stopping_survey_hour", maxi);
                edit.commit();

            }
        });

        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });

        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean("service_on", s1.isChecked());
                edit.commit();
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean("notification_on", s2.isChecked());
                edit.commit();
            }
        });

        mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("your_mail", mail.getText().toString());
                edit.commit();
            }
        });
    }

}