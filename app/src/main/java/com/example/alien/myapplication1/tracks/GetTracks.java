package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Alien on 2015-04-29.
 */
public class GetTracks extends AsyncTask<String,Void,String> {
    private Context context;
    private boolean isFinished=false;
    private ArrayList<Track> list;

    public GetTracks(Context context) {
        this.context = context;
    }

    public ArrayList<Track> getList()
    {
        return list;
    }
    public boolean isFinished()
    {
        return isFinished;
    }
    @Override
    protected String doInBackground(String... arg0) {
        try{
            String account_id = (String)arg0[0];
            String link = "http://rommam.cba.pl/get_tracks.php";
            String data  = "account_id"
                    + "=" + account_id;
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter
                    (conn.getOutputStream());
            wr.write(data);
            wr.flush();

            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line = null;
            ArrayList<Track> tracks = new ArrayList<Track>();
            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                if(line.contains("QUERY RESULT: ")) {
                    line = line.substring(14,15) + ";";
                    sb.append(line);
                }
                if(line.contains("TRACKID: ")){
                    String[] lineParts = line.split(";");
                    String track_id = lineParts[0].substring(9);
                    String track_name = lineParts[1].substring(11);
                    String track_dist = lineParts[2].substring(11);
                    String track_time = lineParts[3].substring(11);
                    String track_avg = lineParts[4].substring(14);

                    Track track = new Track(Integer.parseInt(track_id), track_name, Integer.parseInt(track_dist), track_time,
                                            Double.parseDouble(track_avg));

                    tracks.add(track);
                }
            }
            list = tracks;
            isFinished = true;
            return sb.toString(); //+ tracks.get(0).getTrackId()+" "+tracks.get(0).getTrackName()+" "+tracks.get(0).getDistance()+" "+tracks.get(0).getTime()+" "+tracks.get(0).getAverage();
        }catch(UnsupportedEncodingException e){
            isFinished = true;
            return new String("UEEException: " + e.getMessage());
        }
        catch(MalformedURLException e){
            isFinished = true;
            return new String("MUException: " + e.getMessage());
        }catch(IOException e){
            isFinished = true;
            return new String("IOException: " + e.getStackTrace().toString());
        }
    }

    @Override
    protected void onPostExecute(String result){
        System.out.println("Result: " + result);
    }
}
