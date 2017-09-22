package com.example.wblachowski.busswarzedz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import static android.R.attr.path;

/**
 * Created by wblachowski on 8/22/2017.
 */
public class TabDownloaded extends Fragment {

    ArrayList<DownloadedFile> downloadedFiles;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_downloaded, container, false);
        ArrayList<DownloadedFile> listOfFiles=new ArrayList<DownloadedFile>();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        //production version
        //File f = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        File f = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))+"/camera");
        File[] matchingFiles = f.listFiles();
        downloadedFiles = new ArrayList<DownloadedFile>();
        for(File file : matchingFiles) {
            if(file.getName().matches("4[0-9][0-9].*\\.pdf")) {
                StopDetailsSearcher sds = new StopDetailsSearcher();
                DownloadedFile dfile = new DownloadedFile(file);
                sds.search(dfile,getContext());
                downloadedFiles.add(dfile);
            }
        }
        downloadedFiles.sort(
                new Comparator<DownloadedFile>(){
                    @Override
                    public int compare(DownloadedFile file1, DownloadedFile file2){
                        if(file1.date.getTime()==file2.date.getTime()){return 0;}
                        if(file1.date.getTime()>file2.date.getTime()){return -1;}
                        if(file1.date.getTime()<file2.date.getTime()){return 1;}
                        return 0;
                    }
                }
        );
        ListView listView = (ListView) rootView.findViewById(R.id.downloadedListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                downloadedFiles.get(position).open(getContext());
            }
        });
        if(downloadedFiles.size()>0){
            MyAdapter adapter=new MyAdapter(getActivity(),R.layout.downloads_row,R.id.bus_nr,downloadedFiles);
            listView.setAdapter(adapter);
            registerForContextMenu(listView);
        }else{
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,
                    android.R.id.text1,new String[]{getResources().getString(R.string.no_files)});
            listView.setAdapter(arrayAdapter);
        }
        return rootView;
    }

    private void reloadList()
    {
        ListView listView = (ListView) getView().findViewById(R.id.downloadedListView);
        if(downloadedFiles.size()>0){
            MyAdapter adapter=new MyAdapter(getActivity(),R.layout.downloads_row,android.R.id.text1,downloadedFiles);
            listView.setAdapter(adapter);
            registerForContextMenu(listView);
        }else{
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,
                    android.R.id.text1,new String[]{getResources().getString(R.string.no_files)});
            listView.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.downloadedListView){
            AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(downloadedFiles.get(info.position).name);
            String[] menuItems = getResources().getStringArray(R.array.context_menu);
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
            String[] menuItems = getResources().getStringArray(R.array.context_menu);
            String menuItemName = menuItems[menuItemIndex];
            String listItemName = downloadedFiles.get(info.position).name;
            switch (menuItemIndex) {
                case 0: {
                    downloadedFiles.get(info.position).delete();
                    downloadedFiles.remove(info.position);
                    reloadList();
                }
            }
            return true;
        }
        return false;
    }


}
