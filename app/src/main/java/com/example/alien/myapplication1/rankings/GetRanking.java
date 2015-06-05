package com.example.alien.myapplication1.rankings;

import android.content.Context;
import android.os.AsyncTask;

import com.example.alien.myapplication1.OnASyncTaskCompleted;
import com.example.alien.myapplication1.tracks.Stats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GetRanking extends AsyncTask<String,Void,String> {
    private Context context;
    private OnASyncTaskCompleted callback;

    public GetRanking(Context context, OnASyncTaskCompleted callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String link = "http://rommam.cba.pl/get_ranking.php";

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write("");
            wr.flush();

            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                if (line.contains("QUERY RESULT: ")) {
                    sb.append(line);
                }
                else if (line.contains("USERNAME: ")) {
                    sb.append(line);
                }
            }

            return sb.toString();

        }
        catch (UnsupportedEncodingException e) {
            return "UEEException: " + e.getMessage();
        }
        catch (MalformedURLException e) {
            return "MUException: " + e.getMessage();
        }
        catch (IOException e) {
            return "IOException: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        String[] results = result.split(";");

        String status;
        String username = "";
        String distance = "";
        String time = "";
        String average = "";

        ArrayList<Rank> rank = new ArrayList<Rank>();

        if (results[0].length() > 4 ) {
            status = results[0].substring(14, 15);
        }
        else {
            status = result.substring(0,1);
        }

        if (results.length >= 3) {
            for (int i = 1; i < results.length - 1; i += 4) {
                username = results[i].substring(10);
                distance = results[i + 1].substring(10);
                time = results[i + 2].substring(6);
                average = results[i + 3].substring(9);

                rank.add(new Rank(0, username, new Stats(Integer.parseInt(distance), Double.parseDouble(average), time)));
            }
        }
        callback.onASyncTaskCompleted(rank);
    }
}
