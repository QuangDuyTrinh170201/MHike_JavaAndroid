package com.example.mhike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HikeAdapter extends BaseAdapter {
    private ArrayList<HikingModel> hikeList;
    private HikingDatabase database;
    private Context context;
    private TextView hikename, location, date, parking, difficulty, description, leadername, length;


    public HikeAdapter(ArrayList<HikingModel> hikeList, HikingDatabase database, Context context) {
        this.hikeList = hikeList;
        this.database = database;
        this.context = context;
    }

    @Override
    public int getCount() {
        return hikeList.size();
    }

    @Override
    public Object getItem(int position) {
        return hikeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        view = inflater.inflate(R.layout.custom_listview, null);

        HikingModel hikesModel = hikeList.get(i);
        hikename = view.findViewById(R.id.txtHikeName);
        location = view.findViewById(R.id.txtLocation);
        date = view.findViewById(R.id.txtDate);
        length = view.findViewById(R.id.txtLength);
        parking = view.findViewById(R.id.txtParking);
        difficulty = view.findViewById(R.id.txtDifficulty);
        description = view.findViewById(R.id.txtDesc);
        leadername = view.findViewById(R.id.txtLeaderName);


        hikename.setText(hikesModel.getHikeName());
        location.setText(hikesModel.getLocation());
        date.setText(hikesModel.getDate());
        parking.setText(hikesModel.getParking());
        length.setText(String.valueOf(hikesModel.getLength()) + "km");
        difficulty.setText(hikesModel.getDifficulty());
        description.setText(hikesModel.getDescription());
        leadername.setText(hikesModel.getLeaderName());

        return view;
    }
}
