package com.example.wblachowski.busswarzedz;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by wblachowski on 9/2/2017.
 */

public class StopDetailsSearcher {

    public void search(DownloadedFile file, Context context) {
        String to="";
        String filename= file.name;
        if(filename.indexOf('(')>0) {
            filename = filename.substring(0, filename.indexOf('('));
            filename = filename.trim();
        }
        XmlResourceParser parser = context.getResources().getXml(R.xml.buses);

        try {
            while (parser.getEventType()!= XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType()==XmlPullParser.START_TAG) {
                    if(parser.getName().equals("line")){
                        file.to=parser.getAttributeValue(0);
                    }
                    if (parser.getName().equals("stop")) {
                        String link = parser.getAttributeValue(0);
                        if(link.startsWith(filename) && file.to!=parser.getAttributeValue(1)){
                            file.stop_name = parser.getAttributeValue(1);
                        }
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
}
