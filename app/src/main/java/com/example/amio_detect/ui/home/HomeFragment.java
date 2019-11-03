package com.example.amio_detect.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amio_detect.MainActivity;
import com.example.amio_detect.R;
import com.example.amio_detect.ui.notifications.NotificationsFragment;
import com.example.amio_detect.utils.AsyncResponse;
import com.example.amio_detect.utils.Data;
import com.example.amio_detect.utils.GetSensors;
import com.example.amio_detect.utils.ListenFromActivity;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment implements ListenFromActivity, AsyncResponse {
    private static boolean firstOpening = true;
    private ArrayList<Data> listData = new ArrayList<>();
    private RecycleAdapter recycleAdapter;
    private TextView noItem;
    private TextView load;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        ConstraintLayout constraintLayout = root.findViewById(R.id.constraintLayout);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(activity));

        this.noItem = root.findViewById(R.id.noItem);
        this.load = root.findViewById(R.id.load);
        this.load.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setFocusable(false);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        constraintLayout.requestFocus();

        if(firstOpening) {
            firstOpening = false;

            if(!sharedPref.getBoolean(NotificationsFragment.SERVICES_PREF, false))
                this.getData(activity);
        } else this.getData(activity);

        this.recycleAdapter = new RecycleAdapter(this.listData);
        recyclerView.setAdapter(this.recycleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ((MainActivity) Objects.requireNonNull(activity)).setActivityListener(HomeFragment.this);

        return root;
    }

    private void getData(AppCompatActivity activity) {
        System.out.println(activity);
        GetSensors getSensors = new GetSensors(activity, null);
        getSensors.execute("http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last");
    }

    @Override
    public void reloadRecycle(ArrayList<Data> list) {
        this.load.setVisibility(View.INVISIBLE);

        if (this.recycleAdapter.updateList(list) == 0)
            this.noItem.setVisibility(View.VISIBLE);
        else this.noItem.setVisibility(View.INVISIBLE);
    }

    @Override
    public void updateRecyclerView(ArrayList<Data> arrayList) {
        this.reloadRecycle(arrayList);
    }
}