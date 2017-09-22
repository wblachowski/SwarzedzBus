package com.example.wblachowski.busswarzedz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by wblachowski on 9/6/2017.
 */

public class TabFavourite extends Fragment {

    ArrayList<Stop> favouriteStops;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_favourite, container, false);
        String current_to = "";
        String current_nr = "";
        favouriteStops = new ArrayList<Stop>();
        File file = new File(getContext().getFilesDir() + "/buses.xml");
        if (file.exists()) {
            InputStream in_s = null;
            try {
                in_s = new FileInputStream(getContext().getFilesDir() + "/buses.xml");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in_s, null);
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        if (parser.getName().equals("bus")) {
                            current_nr = parser.getAttributeValue(0);
                        }
                        if (parser.getName().equals("line")) {
                            current_to = parser.getAttributeValue(0);
                        }
                        if (parser.getName().equals("stop") && parser.getAttributeValue(2).equals("1")) {
                            favouriteStops.add(new Stop(getContext(), parser.getAttributeValue(1), parser.getAttributeValue(0), parser.getAttributeValue(2), current_to, current_nr));
                        }
                    }

                    try {
                        parser.next();
                    } catch (IOException e) {
                    }
                }
            } catch (XmlPullParserException e) {

            }

        }
        ListView listView = (ListView) rootView.findViewById(R.id.favouriteListView);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                try {
                    final DownloadTask downloadTask = new DownloadTask(getContext());
                    downloadTask.execute(getString(R.string.web_resource) + favouriteStops.get(position).link);
                }catch(Exception ex){
                    String msg=ex.getMessage();
                }
            }
        });
        if(favouriteStops.size()>0) {
            MyAdapterFavourites adapter = new MyAdapterFavourites(getActivity(), R.layout.downloads_row, R.id.bus_nr, favouriteStops);
            listView.setAdapter(adapter);
        }else{
            ArrayAdapter<String> adapterString = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,new String[]{"Brak elementów"});
            listView.setAdapter(adapterString);
        }
        return rootView;
    }

    public void reloadElements()
    {
        String current_nr="";
        String current_to="";
        favouriteStops = new ArrayList<Stop>();
        File file = new File(getContext().getFilesDir() + "/buses.xml");
        if (file.exists()) {
            InputStream in_s = null;
            try {
                in_s = new FileInputStream(getContext().getFilesDir() + "/buses.xml");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in_s, null);
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        if (parser.getName().equals("bus")) {
                            current_nr = parser.getAttributeValue(0);
                        }
                        if (parser.getName().equals("line")) {
                            current_to = parser.getAttributeValue(0);
                        }
                        if (parser.getName().equals("stop") && parser.getAttributeValue(2).equals("1")) {
                            favouriteStops.add(new Stop(getContext(), parser.getAttributeValue(1), parser.getAttributeValue(0), parser.getAttributeValue(2), current_to, current_nr));
                        }
                    }
                    try {
                        parser.next();
                    } catch (IOException e) {
                    }
                }
            } catch (XmlPullParserException e) {
            }
        }
        ListView listView = (ListView) getActivity().findViewById(R.id.favouriteListView);
        listView.setOnCreateContextMenuListener(this);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                try {
                    final DownloadTask downloadTask = new DownloadTask(getContext());
                    downloadTask.execute(getString(R.string.web_resource) + favouriteStops.get(position).link);
                }catch(Exception ex){
                    String msg=ex.getMessage();
                }
            }
        });
        if(favouriteStops.size()>0) {
            MyAdapterFavourites adapter = new MyAdapterFavourites(getActivity(), R.layout.downloads_row, R.id.bus_nr, favouriteStops);
            listView.setAdapter(adapter);
        }else{
            ArrayAdapter<String> adapterString = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,new String[]{"Brak elementów"});
            listView.setAdapter(adapterString);
        }
    }

    private void reloadList(){
        ListView listView = (ListView) getActivity().findViewById(R.id.favouriteListView);
        if(favouriteStops.size()>0) {
            MyAdapterFavourites adapter = new MyAdapterFavourites(getActivity(), R.layout.downloads_row, R.id.bus_nr, favouriteStops);
            listView.setAdapter(adapter);
        }else{
            ArrayAdapter<String> adapterString = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,new String[]{"Brak elementów"});
            listView.setAdapter(adapterString);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.favouriteListView){
            AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(favouriteStops.get(info.position).stop_name);
            String[] menuItems = getResources().getStringArray(R.array.context_menu_favourite);
            for(int i=0; i<menuItems.length; i++){
                menu.add(Menu.NONE,i,i,menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if (getUserVisibleHint()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int menuItemIndex = item.getItemId();
            String[] menuItems = getResources().getStringArray(R.array.context_menu_favourite);
            String menuItemName = menuItems[menuItemIndex];
            String listItemName = favouriteStops.get(info.position).stop_name;
            switch (menuItemIndex) {
                case 0: {
                    favouriteStops.get(info.position).setStopFavourite(false);
                    favouriteStops.remove(info.position);
                    reloadList();
                }
            }
            return true;
        }
        return false;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        private String path;
        ProgressDialog progress;
        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                String filename=sUrl[0].substring(sUrl[0].lastIndexOf("/")+1,sUrl[0].length());
                // path=getContext().getFilesDir()+"/downloads/"+filename;
                path=getContext().getExternalFilesDir(null).getPath()+"/" + filename;
                output = new FileOutputStream(path);

                byte data[] = new byte[1024*1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            try {
                if(new File(path).exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                    intent.setDataAndType(Uri.fromFile(new File(path)), "application/pdf");
                    context.startActivity(intent);
                }
            }
            catch(Exception ex){
                String msg=ex.getMessage();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            progress = new ProgressDialog(getContext());
            // progress.setTitle("Loading");
            progress.setIndeterminate(false);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);

            // if we get here, length is known, now set indeterminate to false

        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            progress.dismiss();
            if (result != null)
                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
        }
    }
}
