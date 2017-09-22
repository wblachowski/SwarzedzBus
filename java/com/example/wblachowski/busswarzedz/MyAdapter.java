package com.example.wblachowski.busswarzedz;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by wblachowski on 8/23/2017.
 */

public class MyAdapter extends ArrayAdapter<DownloadedFile> {

    ArrayList<DownloadedFile> elements;
    SimpleDateFormat dateFormat;
    MyAdapter(Context context,int a,int b,ArrayList<DownloadedFile> obj)
    {
        super(context,a,b,obj);
        elements=obj;
        dateFormat= new SimpleDateFormat("d MMMM yyyy");
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView text1 = (TextView) view.findViewById(R.id.bus_nr);
        TextView text2 = (TextView) view.findViewById(R.id.bus_stop);
        TextView text3 = (TextView) view.findViewById(R.id.bus_to);
        text1.setText(elements.get(position).name.substring(0,3));
        text2.setText(elements.get(position).stop_name);
        text3.setText(elements.get(position).to);
        return view;
    }
}