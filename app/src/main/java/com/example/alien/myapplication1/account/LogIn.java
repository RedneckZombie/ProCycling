package com.example.alien.myapplication1.account;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.alien.myapplication1.NetConnection.CheckingConnection;
import com.example.alien.myapplication1.NetConnection.OnASyncTaskCompleted;
import com.example.alien.myapplication1.map.SideBarActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LogIn extends AsyncTask<String,Void,String> {

    private Context context;
    private OnASyncTaskCompleted callback;
    CheckingConnection isConnected;

    public LogIn(Context context, OnASyncTaskCompleted callback) {
        this.context = context;
        this.callback = callback;
        isConnected = new CheckingConnection(context);
    }

    protected void onPreExecute() {
        isConnected.execute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        if (isConnected.isConnected()) {
            try {
                String email = arg0[0];
                String password = arg0[1];

                String link = "http://botnaeasy.cba.pl/login.php";
                String data = "email"
                        + "=" + email;
                data += "&" + "password"
                        + "=" + password;

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
                        sb.append(line);
                    }
                    else if (line.contains("USERNAME: ")) {
                        sb.append(line);
                    }
                    else if (line.contains("ACCOUNTID: ")) {
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
        else {
            return "Brak internetu";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Brak internetu")) {
            result = "3;";
        }

        String[] results = result.split(";");

        String status;
        String username = "";
        String userID = "";

        if (results[0].length() > 4 ) {
            status = results[0].substring(14, 15);
        }
        else {
            status = result.substring(0,1);
        }

        if (results.length >= 3) {
            username = results[1].substring(10);
            userID = results[2].substring(11);
        }

        callback.onASyncTaskCompleted(status);

        switch (status) {
            case "1":
                Toast.makeText(context, "Zalogowano!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, SideBarActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("userID", userID);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case "2":
                Toast.makeText(context, "Brak połączenia z bazą danych!", Toast.LENGTH_SHORT).show();
                break;
            case "0":
                Toast.makeText(context, "Nie poprawny email lub hasło!", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "Brak internetu!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}