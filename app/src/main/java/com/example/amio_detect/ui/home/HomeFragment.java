package com.example.amio_detect.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amio_detect.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private AppCompatActivity activity = (AppCompatActivity) getActivity();
    private ArrayList<Data> listData = new ArrayList<>();
    private static RecycleAdapter recycleAdapter;
    private static TextView noItem;
    private static TextView load;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        ConstraintLayout constraintLayout = root.findViewById(R.id.constraintLayout);

        noItem = root.findViewById(R.id.noItem);
        load = root.findViewById(R.id.load);
        load.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.activity));
        recyclerView.setFocusable(false);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        constraintLayout.requestFocus();

        recycleAdapter = new RecycleAdapter(this.listData);
        recyclerView.setAdapter(recycleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        GetSensors getSensors = new GetSensors(this.activity);
        getSensors.execute("http://iotlab.telecomnancy.eu:8080/iotlab/rest/data/1/light1/last");

        return root;
    }

    static void loadData(ArrayList<Data> list) {
        load.setVisibility(View.INVISIBLE);

        if (recycleAdapter.updateList(list) == 0)
            noItem.setVisibility(View.VISIBLE);
        else noItem.setVisibility(View.INVISIBLE);
    }
}