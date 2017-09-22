package com.example.wblachowski.busswarzedz;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wblachowski on 9/2/2017.
 */

public class MyAdapterBuses extends ArrayAdapter<Bus> {

    ArrayList<Bus> elements;
    MyAdapterBuses(Context context, int a, int b, ArrayList<Bus> obj)
    {
        super(context,a,b,obj);
        elements=obj;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text1 = (TextView) view.findViewById(R.id.bus_nr);
        TextView text2 = (TextView) view.findViewById(R.id.bus_from_to);
        text1.setText(elements.get(position).nr);
        text2.setText(elements.get(position).from +" ➝ " + elements.get(position).to);
        return view;
    }
}