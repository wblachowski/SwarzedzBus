package com.example.wblachowski.busswarzedz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import layout.stop_list;

public class StopsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_list);
        String nr=getIntent().getStringExtra("nr").toString();
        String to=getIntent().getStringExtra("to").toString();
        stop_list stop_list_fragment = new stop_list();
        stop_list_fragment.setAttributes(nr,to);
        getSupportFragmentManager().beginTransaction()
              .add(R.id.fragment_container, stop_list_fragment, stop_list.class.getSimpleName()).commit();
        setTitle("Linia "+nr);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

