package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetTrackDetails extends AsyncTask<String,Void,String> {

    protected Context context;
    private JSONObject json;
    private boolean isFinished = false;

    public GetTrackDetails(Context context) {
        this.context = context;
    }

    public JSONObject getJSON() {
        return json;
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String track_id = arg0[0];

            String link = "http://rommam.cba.pl/get_track_details.php";
            String data  = "track_id"
                    + "=" + track_id;

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                if (line.contains("QUERY RESULT: ")) {
                    line = line.substring(14,15) + ";";
                    sb.append(line);
                }

                if (line.contains("GPSDATA: ")) {
                    line = line.substring(9);
                    json = new JSONObject(line);
                }
            }

            isFinished = true;

            return sb.toString();

        }
        catch (UnsupportedEncodingException e) {
            isFinished = true;
            return "UEEException: " + e.getMessage();
        }
        catch (MalformedURLException e) {
            isFinished = true;
            return "MUException: " + e.getMessage();
        }
        catch (IOException e) {
            isFinished = true;
            return "IOException: " + e.getMessage();
        }
        catch (JSONException e) {
            isFinished = true;
            return "JSONException: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("Result: " + result);
    }
}
