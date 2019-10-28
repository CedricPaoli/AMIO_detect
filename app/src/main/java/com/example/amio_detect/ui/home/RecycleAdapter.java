package com.example.amio_detect.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amio_detect.R;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {
    private ArrayList<Data> listData;

    RecycleAdapter(ArrayList<Data> listData) {
        this.listData = listData;
    }

    /** Structure de chaque item de la liste RecyclerView **/
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView moteLabel, light;

        MyViewHolder(View moteLabel) {
            super(moteLabel);

            this.moteLabel = itemView.findViewById(R.id.mote);
            this.light = itemView.findViewById(R.id.light);
        }
    }

    @NonNull
    @Override
    public RecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        viewHolder.moteLabel.setText(this.listData.get(position).getMote());
        viewHolder.light.setText(this.listData.get(position).getLight());
    }

    int updateList(ArrayList<Data> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        this.notifyDataSetChanged();

        System.out.println(this.listData.toString());
        System.out.println(this.getItemCount());

        return this.getItemCount();
    }

    @Override
    public int getItemCount() {
        return this.listData.size();
    }
}