package com.example.alien.myapplication1.account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class Registration extends AsyncTask<String,Void,String> {

    private Context context;

    public Registration(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String email = arg0[0];
            String password = arg0[1];
            String user_name = arg0[2];
            String dateOfBirth = arg0[3];
            String sex = arg0[4];
            String city = arg0[5];

            String link = "http://rommam.cba.pl/registration.php";
            String data  = "email"
                    + "=" + email;
            data += "&" + "password"
                    + "=" + password;
            data += "&" + "user_name"
                    + "=" + user_name;
            data += "&" + "dateOfBirth"
                    + "=" + dateOfBirth;
            data += "&" + "sex"
                    + "=" + sex;
            data += "&" + "city"
                    + "=" + city;

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
                Toast.makeText(context, "Zarejestrowano!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case "2":
                Toast.makeText(context, "Brak połączenia z bazą danych!", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "Ten adres email jest już zajęty!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
