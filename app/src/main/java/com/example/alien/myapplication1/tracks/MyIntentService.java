package com.example.alien.myapplication1.tracks;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by kamilos on 2015-04-07.
 */
public class MyIntentService extends IntentService {

    public String ACTION_MyUpdate = "com.example.androidintentservice.UPDATE";
    public double lon = -1, lat = -1, alt = -1;

    public MyIntentService() {
        super("com.example.alien.myapplication1.tracks.MyIntentService");
    }

    public MyIntentService(double latitude, double longitude, double altitude)
    {
        super("com.example.alien.myapplication1.tracks.MyIntentService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //send update
        Intent intentUpdate = new Intent();
        intentUpdate.setAction(ACTION_MyUpdate);
        intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        intentUpdate.putExtra("longitude", lon);
        intentUpdate.putExtra("latitude", lat);
        intentUpdate.putExtra("altitude", alt);
        sendBroadcast(intentUpdate);
    }
}
