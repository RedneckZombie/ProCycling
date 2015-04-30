package com.example.alien.myapplication1.NetConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by BotNaEasy on 2015-04-30.
 */
public class CheckingConnection extends AsyncTask<Void,Void,Void>
{


    private boolean isConnected;
    private boolean isFinished=false;
    private Context con;
    public boolean isConnected()
    {
        return isConnected;
    }
    public boolean isFinished()
    {
        return isFinished;
    }
    public CheckingConnection(Context con)
    {
        this.con = con;
    }
    public boolean testingConnection(Context context) {
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
                    urlc.disconnect();
                    isConnected=true;
                    return isConnected;
                } else {
                    urlc.disconnect();
                    isConnected=false;
                    return isConnected;
                }
            } catch (IOException e) {
                isConnected=false;
                return isConnected;
            }
        }
        isConnected=false;
        return isConnected;
    }
    @Override
    protected Void doInBackground(Void... params) {
        testingConnection(con);
        isFinished=true;
        return null;
    }
}
