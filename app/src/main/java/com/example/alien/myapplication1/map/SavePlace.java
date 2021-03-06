package com.example.alien.myapplication1.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class SavePlace extends AsyncTask<String,Void,String> {

    private Context context;

    public SavePlace(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String account_id = arg0[0];
            String title = arg0[1];
            String coord_x = arg0[2];
            String coord_y = arg0[3];

            String link = "http://rommam.cba.pl/save_place.php";
            String data  = "account_id"
                    + "=" + account_id;
            data += "&" + "title"
                    + "=" + title;
            data += "&" + "coord_x"
                    + "=" + coord_x;
            data += "&" + "coord_y"
                    + "=" + coord_y;

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
                    line = line.substring(14,15);
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
        switch (result) {
            case "1":
                Toast.makeText(context, "Dodano!", Toast.LENGTH_SHORT).show();
                break;
            case "2":
                Toast.makeText(context, "Brak połączenia z bazą danych!", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "Nie można dodać!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
