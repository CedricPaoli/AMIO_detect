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
import com.example.amio_detect.responseInterface.AsyncResponse;
import com.example.amio_detect.responseInterface.ListenFromActivity;
import com.example.amio_detect.ui.prefs.PrefsFragment;
import com.example.amio_detect.utils.Data;
import com.example.amio_detect.utils.GetSensors;

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

            if(!sharedPref.getBoolean(PrefsFragment.SERVICES_PREF, true))
                this.getData(activity);
        } else this.getData(activity);

        this.recycleAdapter = new RecycleAdapter(this.listData);
        recyclerView.setAdapter(this.recycleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ((MainActivity) Objects.requireNonNull(activity)).setActivityListener(HomeFragment.this);

        return root;
    }

    /** Executer une asyncTask pour la liste des motes **/
    private void getData(AppCompatActivity activity) {
        GetSensors getSensors = new GetSensors(activity, null);
        getSensors.execute("http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last");
    }

    /** Mise à jour du fragment Home avec la liste en argument **/
    @Override
    public void reloadRecycle(ArrayList<Data> list) {
        this.load.setVisibility(View.INVISIBLE);

        if (this.recycleAdapter.updateList(list) == 0)
            this.noItem.setVisibility(View.VISIBLE);
        else this.noItem.setVisibility(View.INVISIBLE);
    }

    /** Reception du resultat de l'asyncTask **/
    @Override
    public void updateRecyclerView(ArrayList<Data> arrayList) {
        this.reloadRecycle(arrayList);
    }
}