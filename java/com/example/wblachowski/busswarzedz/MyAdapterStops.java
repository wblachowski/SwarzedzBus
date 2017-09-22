package com.example.wblachowski.busswarzedz;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wblachowski on 9/3/2017.
 */

public class MyAdapterStops extends ArrayAdapter<Stop> {

        ArrayList<Stop> elements;
        public MyAdapterStops(Context context, int a, int b, ArrayList<Stop> obj)
        {
            super(context,a,b,obj);
            elements= obj;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.stop_name);
                text1.setText(elements.get(position).stop_name);
                final ImageView button_star = (ImageView) view.findViewById(R.id.imageButton);
                button_star.setTag(R.id.id,new Integer(position));
                button_star.setTag(R.id.is_favourite,elements.get(position).is_favourite);

                button_star.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                int which=(int)(view.getTag(R.id.id));
                                boolean is_favourite=(boolean)(view.getTag(R.id.is_favourite));
                                if(is_favourite){
                                        int color = Color.parseColor("#D6D6D6");
                                        button_star.setColorFilter(color);
                                }else{
                                        int color = Color.parseColor("#D32F2F");
                                        button_star.setColorFilter(color);
                                }
                                view.setTag(R.id.is_favourite,!is_favourite);
                                elements.get(which).setStopFavourite(!is_favourite);
                                MainActivity.tabFavourite.reloadElements();
                        }
                });
                if(elements.get(position).is_favourite){
                        int color = Color.parseColor("#D32F2F");
                        button_star.setColorFilter(color);
                }else{int color = Color.parseColor("#D6D6D6");
                        button_star.setColorFilter(color);
                }

                return view;
        }
}
