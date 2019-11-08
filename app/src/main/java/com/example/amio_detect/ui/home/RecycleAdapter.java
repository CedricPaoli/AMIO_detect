package com.example.amio_detect.ui.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amio_detect.R;
import com.example.amio_detect.utils.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {
    private ArrayList<Data> listData;
    private Map<String,String> motePlaces = new HashMap<String, String>(){{
        this.put("151.105", "2-01");
        this.put("97.145", "2-02");
        this.put("153.111", "2-05");
        this.put("9.138", "2-06");
        this.put("53.105", "2-07");
        this.put("32.131", "2-08");
        this.put("111.130", "2-09");
        this.put("120.99", "2-10");
        this.put("200.124", "2-11");
    }};

    RecycleAdapter(ArrayList<Data> listData) {
        this.listData = listData;
    }

    /** Structure de chaque item de la liste RecyclerView **/
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView moteLabel, light, date;

        MyViewHolder(View moteLabel) {
            super(moteLabel);

            this.moteLabel = itemView.findViewById(R.id.mote);
            this.light = itemView.findViewById(R.id.light);
            this.date = itemView.findViewById(R.id.date);
        }
    }

    @NonNull
    @Override
    public RecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

        return new MyViewHolder(view);
    }

    /** Changement de couleur en fonction de l'état et ajout du numero de salle aux données affichées **/
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Data data = this.listData.get(position);
        boolean isOn = data.isOn();
        TextView moteLabel = viewHolder.moteLabel;
        TextView light = viewHolder.light;
        TextView date = viewHolder.date;
        String isOnString = data.isOn()?"allumé":"éteint";
        String lightString = data.getLight() + " (" + isOnString + ")";
        String moteString = data.getMote() + " (Salle: " + motePlaces.get(data.getMoteName()) + ")";

        moteLabel.setText(moteString);
        light.setText(lightString);
        date.setText(data.getDate());

        if(isOn) {
            moteLabel.setTextColor(Color.GREEN);
            light.setTextColor(Color.GREEN);
            date.setTextColor(Color.GREEN);
        } else {
            moteLabel.setTextColor(Color.RED);
            light.setTextColor(Color.RED);
            date.setTextColor(Color.RED);
        }
    }

    /** Mise à jour de la liste à afficher **/
    int updateList(ArrayList<Data> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        this.notifyDataSetChanged();

        System.out.println(this.listData.toString());

        return this.getItemCount();
    }

    @Override
    public int getItemCount() {
        return this.listData.size();
    }
}