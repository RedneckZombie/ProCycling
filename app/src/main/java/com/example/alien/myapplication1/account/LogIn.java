package com.example.alien.myapplication1.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.alien.myapplication1.NetConnection.CheckingConnection;
import com.example.alien.myapplication1.map.SideBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class LogIn extends AsyncTask<String,Void,String> {
    private Context context;
    private String res;
    private boolean isFinished=false;
    CheckingConnection isConnected;
    private String mail="";

    public LogIn(Context context) {

        this.context = context;
        isConnected = new CheckingConnection(context);
    }

    public boolean isFinished()
    {
        return isFinished;
    }
    protected void onPreExecute(){
        isConnected.execute();
    }



    @Override
    protected String doInBackground(String... arg0) {
        if(isConnected.isConnected()) {
            mail = (String) arg0[0];
            try {
                String email = (String) arg0[0];
                String password = (String) arg0[1];
                String link = "http://rommam.cba.pl/login.php";
                String data = "email"
                        + "=" + email;
                data += "&" + "password"
                        + "=" + password;
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
                while ((line = reader.readLine()) != null) {
                    if (line.contains("QUERY RESULT: ")) {
                        sb.append(line);
                    } else if (line.contains("USERNAME: ")) {
                        sb.append(line);
                    } else if (line.contains("ACCOUNTID: ")) {
                        sb.append(line);
                    }
                }
                res=sb.toString().substring(0,1);
                isFinished=true;
                return sb.toString();
            } catch (UnsupportedEncodingException e) {
                isFinished=true;
                return new String("UEEException: " + e.getMessage());
            } catch (MalformedURLException e) {
                isFinished=true;
                return new String("MUException: " + e.getMessage());
            } catch (IOException e) {
                isFinished=true;
                return new String("IOException: " + e.getStackTrace().toString());
            }
        }
        else{
            isFinished=true;
            return new String("Brak internetu");
        }

    }

    public String getRes()
    {
        return res;
    }

    @Override
    protected void onPostExecute(String result){
        if(result.equals("Brak internetu"))
        {
            result = "3;";
        }
        System.out.println("Result: " + result);
        String[] results = result.split(";");
        String status = "";
        if(results[0].length() > 4 ) {
            status = results[0].substring(14, 15);
        }
        else{
            status = result.substring(0,1);
        }
        String username = "";
        String userID = "";
        if(results.length >= 3) {
            username = results[1].substring(10);
            userID = results[2].substring(11);
        }
        //System.out.println("Results: " + results[0] + ", " + results[1] + ", " + results[2]);
        System.out.println("Results2: " + status + ", " + username + ", " + userID);

        if(status.equals("1")){
            Toast.makeText(context, "Zalogowano!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, SideBar.class);
            intent.putExtra("username", username);
            intent.putExtra("userID", userID);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
        else if(status.equals("2")){
            Toast.makeText(context, "Brak połączenia z bazą danych!", Toast.LENGTH_SHORT).show();
        }
        else if (status.equals("0")){
            Toast.makeText(context, "Nie poprawny email lub hasło!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Brak internetu!", Toast.LENGTH_SHORT).show();
        }
        System.out.println("Status: " + status);
        System.out.println("Username: " + username);
    }
}
