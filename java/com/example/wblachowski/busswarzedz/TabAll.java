package com.example.wblachowski.busswarzedz;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wblachowski on 8/22/2017.
 */
public class TabAll extends Fragment{
    ArrayList<Bus> buses;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_all, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.allListView);
        buses = new ArrayList<Bus>();
        XmlResourceParser parser = getContext().getResources().getXml(R.xml.buses);
        String from="";
        String to="";
        String nr="";
        try {
            while (parser.getEventType()!= XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType()==XmlPullParser.START_TAG) {
                    if(parser.getName().equals("bus")){
                        nr=parser.getAttributeValue(0);
                    }
                    if(parser.getName().equals("line")){
                        to=parser.getAttributeValue(0);
                        from="";
                    }
                    if (parser.getName().equals("stop") && from=="") {
                        from=parser.getAttributeValue(1);
                        buses.add(new Bus(nr,from,to));
                    }
                }

                try {
                    parser.next();
                } catch (IOException e) {
                }
            }
        } catch (XmlPullParserException e) {

        }

        MyAdapterBuses adapter=new MyAdapterBuses(getActivity(),R.layout.buses_row,R.id.bus_nr,buses);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent myIntent = new Intent(getActivity(), StopsList.class);
                myIntent.putExtra("nr",buses.get(position).nr);
                myIntent.putExtra("to",buses.get(position).to);
                getActivity().startActivity(myIntent);
            }
        });

        // new DataDownloader((TextView)rootView.findViewById(R.id.section_label)).run();
        //new DataDownloader(getContext()).run();
        return rootView;
    }



}
