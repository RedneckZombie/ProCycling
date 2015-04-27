package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.alien.myapplication1.map.SideBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SaveTrack extends AsyncTask<String,Void,String> {
    private Context context;

    public SaveTrack(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            String account = (String)arg0[0];
            String track_name = (String)arg0[1];
            String gps_data = (String)arg0[2];
            int distance = Integer.parseInt((String)arg0[3]);
            String time = (String)arg0[4];
            double average = Double.parseDouble((String)arg0[5]);

            String link = "http://rommam.cba.pl/save_track.php";
            String data  = "account"
                    + "=" + account;
            data += "&" + "track_name"
                    + "=" + track_name;
            data += "&" + "gps_data"
                    + "=" + gps_data;
            data += "&" + "distance"
                    + "=" + distance;
            data += "&" + "time"
                    + "=" + time;
            data += "&" + "average"
                    + "=" + average;
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter
                    (conn.getOutputStream());
            wr.write( data );
            wr.flush();

            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                if(line.contains("QUERY RESULT: ")) {
                    line = line.substring(14,15) + ";";
                    sb.append(line);
                }
            }
            return sb.toString();
        }catch(UnsupportedEncodingException e){
            return new String("UEEException: " + e.getMessage());
        }
        catch(MalformedURLException e){
            return new String("MUException: " + e.getMessage());
        }catch(IOException e){
            return new String("IOException: " + e.getStackTrace().toString());
        }
    }

    @Override
    protected void onPostExecute(String result){
        System.out.println("Result: " + result);
    }
}
