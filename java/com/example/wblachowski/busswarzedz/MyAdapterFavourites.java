package com.example.wblachowski.busswarzedz;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wblachowski on 9/6/2017.
 */

public class MyAdapterFavourites extends ArrayAdapter<Stop> {

    ArrayList<Stop> elements;
    MyAdapterFavourites(Context context, int a, int b, ArrayList<Stop> obj)
    {
        super(context,a,b,obj);
        elements=obj;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text1 = (TextView) view.findViewById(R.id.bus_nr);
        TextView text2 = (TextView) view.findViewById(R.id.bus_stop);
        TextView text3 = (TextView) view.findViewById(R.id.bus_to);
        text1.setText(elements.get(position).nr);
        text2.setText(elements.get(position).stop_name);
        text3.setText(elements.get(position).to);
        return view;
    }
}