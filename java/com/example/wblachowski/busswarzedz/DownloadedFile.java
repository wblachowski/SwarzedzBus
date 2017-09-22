package com.example.wblachowski.busswarzedz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.Date;

/**
 * Created by wblachowski on 8/23/2017.
 */

public class DownloadedFile {
    String path;
    String name;
    String stop_name;
    String to;
    Date date;

    DownloadedFile(File file){
        if(file.getName().endsWith(".pdf")){
            name=file.getName().substring(0,file.getName().length()-4);
        }else {
            name = file.getName();
        }
        stop_name=name;
        to="";
        path=file.getPath();
        date = new Date(file.lastModified());
    }

    public void open(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            intent.setDataAndType(Uri.parse("file:///" + path), "application/pdf");
            context.startActivity(intent);

        }
        catch(Exception ex){
            String msg=ex.getMessage();
        }
    }
    public void delete(){
        File fdelete=new File(path);
        fdelete.delete();
    }
}
