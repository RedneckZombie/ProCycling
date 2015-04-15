package com.example.alien.myapplication1.account;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

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

    private String mail="";
    public LogIn(Context context) {
        this.context = context;
    }

    protected void onPreExecute(){
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }

        return false;
    }

    @Override
    protected String doInBackground(String... arg0) {
        if(isConnected(context)) {
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
                        line = line.substring(14, 15) + ";";
                        sb.append(line);
                    } else if (line.contains("USERNAME: ")) {
                        line = line.substring(10) + ";";
                        sb.append(line);
                    }
                }
                return sb.toString();
            } catch (UnsupportedEncodingException e) {
                return new String("UEEException: " + e.getMessage());
            } catch (MalformedURLException e) {
                return new String("MUException: " + e.getMessage());
            } catch (IOException e) {
                return new String("IOException: " + e.getStackTrace().toString());
            }
        }
        else{
            return new String("Brak internetu");
        }
    }

    @Override
    protected void onPostExecute(String result){
        System.out.println("Result: " + result);
        String[] results = result.split(";");
        String status = results[0];
        String username = "";

        if(results.length >= 2) {
            username = results[1];
        }

        if(status.equals("1")){
            Toast.makeText(context, "Zalogowano!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, SideBar.class);
            intent.putExtra("username", username);
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
            Toast.makeText(context, "Brak połączenia z internetem!", Toast.LENGTH_SHORT).show();
        }
        System.out.println("Status: " + status);
        System.out.println("Username: " + username);
    }
}
