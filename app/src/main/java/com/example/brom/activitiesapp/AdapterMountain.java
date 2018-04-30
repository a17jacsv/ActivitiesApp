package com.example.brom.activitiesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacobsvensson on 2018-04-17.
 s*/

public class AdapterMountain extends ArrayAdapter{
    private Context context;
    private List<Mountains> mtnList = new ArrayList<>();

    public AdapterMountain(Context c, ArrayList<Mountains> list){
        super(c, 0, list);
        context = c;
        mtnList = list;
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;

        if(listItem == null){
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item_textview, parent, false);
        }

        Mountains currentMountain = mtnList.get(position);

        TextView name = listItem.findViewById(R.id.textView);
        name.setText(currentMountain.getName());

        TextView location = listItem.findViewById(R.id.textView2);
        location.setText(currentMountain.getLocation());

        TextView height = listItem.findViewById(R.id.textView3);
        height.setText(Integer.toString(currentMountain.getHeight()));

        return listItem;
    }

}