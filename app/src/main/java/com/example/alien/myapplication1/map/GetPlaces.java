package com.example.alien.myapplication1.map;

import android.content.Context;
import android.os.AsyncTask;

import com.example.alien.myapplication1.OnASyncTaskCompleted;
import com.example.alien.myapplication1.rankings.Rank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GetPlaces extends AsyncTask<String,Void,String> {

    private Context context;
    private boolean isFinished = false;
    private ArrayList<Place> list;

    public GetPlaces(Context context) {
        this.context = context;
    }

    public ArrayList<Place> getList() {
        return list;
    }

    public boolean isFinished() {
        return isFinished;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String link = "http://rommam.cba.pl/get_places.php";
            String data = "";

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

            ArrayList<Place> tracks = new ArrayList<Place>();

            // Read Server Response
            while((line = reader.readLine()) != null) {
                if(line.contains("QUERY RESULT: ")) {
                    line = line.substring(14,15) + ";";
                    sb.append(line);
                }

                if(line.contains("PLACEID: ")) {
                    String[] lineParts = line.split(";");

                    String place_id = lineParts[0].substring(9);
                    String account_id = lineParts[1].substring(11);
                    String title = lineParts[2].substring(7);
                    String coord_x = lineParts[3].substring(9);
                    String coord_y = lineParts[4].substring(9);

                    Place place = new Place(Integer.parseInt(place_id), Integer.parseInt(account_id), title, Double.parseDouble(coord_x),
                            Double.parseDouble(coord_y));

                    tracks.add(place);
                }
            }

            list = tracks;
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
    protected void onPostExecute(String result){System.out.println(result);}
}
