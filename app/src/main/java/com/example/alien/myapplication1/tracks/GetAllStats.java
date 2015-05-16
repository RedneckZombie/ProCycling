package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.alien.myapplication1.OnASyncTaskCompleted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GetAllStats extends AsyncTask<String,Void,String> {

    private Context context;
    private OnASyncTaskCompleted callback;
    private boolean isFinished;
    private Stats stats;

    public GetAllStats(Context context, OnASyncTaskCompleted callback) {
        this.context = context;
        this.callback = callback;
        isFinished = false;
        stats = null;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Stats getStats() {
        return stats;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String account_id = (String)arg0[0];

            String link = "http://rommam.cba.pl/get_stats_all.php";
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

            // Read Server Response
            while((line = reader.readLine()) != null) {
                if(line.contains("QUERY RESULT: ")) {
                    sb.append(line);
                }
                else if(line.contains("DISTANCE: ")) {
                    sb.append(line);
                }
                else if(line.contains("AVERAGE: ")) {
                    sb.append(line);
                }
                else if(line.contains("TIME: ")) {
                    sb.append(line);
                }
            }

            isFinished = true;

            return sb.toString();

        }
        catch(UnsupportedEncodingException e) {
            isFinished = true;
            return new String("UEEException: " + e.getMessage());
        }
        catch(MalformedURLException e) {
            isFinished = true;
            return new String("MUException: " + e.getMessage());
        }
        catch(IOException e) {
            isFinished = true;
            return new String("IOException: " + e.getStackTrace().toString());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("Result: " + result);

        String[] resultParts = result.split(";");

        String status = "";
        String distance = "";
        String average = "";
        String time = "";

        if(resultParts[0].length() > 4 ) {
            status = resultParts[0].substring(14, 15);
        }
        else {
            status = result.substring(0,1);
        }

        if(resultParts.length >= 4) {
            distance = resultParts[1].substring(10);
            average = resultParts[2].substring(9);
            time = resultParts[3].substring(6);
        }

        stats = new Stats(Integer.parseInt(distance), Double.parseDouble(average), time);

        callback.onASyncTaskCompleted(stats);
        //System.out.println("RES: " + status + ", DIST: " + distance + ", AVG: " + average + ", TIME: " + time);
    }
}
