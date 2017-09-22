package com.example.wblachowski.busswarzedz;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wblachowski on 8/22/2017.
 */

public class DataDownloader extends Thread {

    TextView container;
    Context context;
    public DataDownloader(Context context){
        this.context=context;
    }
    public DataDownloader(Context context,TextView container)
    {
        this.context=context;
        this.container=container;
    }
    public void run() {

        try {
            URL url = new URL(context.getResources().getString(R.string.web_resource) + "401lista.htm");
            new DownloadFilesTask(container).execute(url);
        } catch (MalformedURLException ex) {
            Log.d("wyj","" + ex);
        }
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, String> {
        String html;
        TextView container;
        DownloadFilesTask(TextView container){
            super();
            this.container=container;
        }
        protected String doInBackground(URL... urls) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = urls[0];
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/file_name.extension");

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
            return null;

        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(String result) {

        }
    }

}
