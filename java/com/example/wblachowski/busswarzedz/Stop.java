package com.example.wblachowski.busswarzedz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static android.R.attr.path;

/**
 * Created by wblachowski on 9/3/2017.
 */

public class Stop {
    public String stop_name;
    public String link;
    public String to;
    public String nr;
    public boolean is_favourite;
    public Context context;
    public Stop(Context context, String name,String link,String is_favourite,String to,String nr){
        this.stop_name=name;
        this.link=link;
        this.is_favourite= (is_favourite.equals("1"));
        this.context = context;
        this.to=to;
        this.nr=nr;
    }
    public void setStopFavourite(boolean is_favourite) {
        File buses = new File(context.getFilesDir()+"/buses.xml");
        if(!buses.exists()){
            return;
        }
        String newXml="";
        String current_to="";
        int lineindex=0;
        try (BufferedReader br = new BufferedReader(new FileReader(buses))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineindex=line.indexOf("line");
                if(lineindex>0){
                    if(line.indexOf("\"")>0) {
                        current_to = line.substring(line.indexOf("\"") + 1);
                    }
                    if(current_to.indexOf("\"")>0) {
                        current_to = current_to.substring(0, current_to.indexOf("\""));
                    }


                }
                if(line.indexOf(link)>0 && current_to.equals(to)){
                    int position=line.indexOf("is-favourite");
                    if(is_favourite){
                        line=line.substring(0,position+14) + "1" + line.substring(position+15,line.length());
                    }else{
                        line=line.substring(0,position+14) + "0" + line.substring(position+15,line.length());
                    }
                }
                newXml+=line+"\n";
            }
            FileWriter fw = new FileWriter(buses.getPath());
            fw.write(newXml);
            fw.close();
        }
        catch(FileNotFoundException ex){

        }
        catch(IOException ex) {

        }
    }

}
