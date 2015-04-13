package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.example.alien.myapplication1.map.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by kamilos on 2015-03-28.
 */


//po wybraniu z menu opcji nagrywaj, odpalić tą klasę w nowym wątku
// dodać do onLocationChanged wywołanie metody statycznej z MapActivity


public class RecordRoute
{
    // Acquire a reference to the system Location Manager
    private LocationManager locationManager ;
    private double longitude = -1, latitude = -1, altitude = -1;
    private Context context;

    private int second, minute, hour, day, month, year;
    private Time today;
    private String start, finish, pts = "";
    private JSONObject obj;
    private JSONArray points, times;

    private boolean recording = false;

    public RecordRoute(Context context)
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
        this.context = context;
        Log.e("testing", "RecordRoute konstruktor");

        today = new Time(Time.getCurrentTimezone());
        obj = new JSONObject();
        points = new JSONArray();
        times = new JSONArray();


    }

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {

        // Called when a new location is found by the network location provider.
        public void onLocationChanged(Location location) {

            longitude = location.getLongitude();
            latitude = location.getLatitude();
            altitude = location.getAltitude();

            updateTimeAndDate();

            if(recording)
            {
                points.put(new Double(longitude));
                points.put(new Double(latitude));
                points.put(new Double(altitude));
                times.put(formatDate(year, month, day)+formatTime(hour, minute, second));
            }

            Log.i("testing", "ReordRoute onLocationChanged " + longitude + "  " + latitude + "  " + altitude);
            if(Map.isMapVisible()) {
                Toast.makeText(context, "GPS" + longitude + " " + latitude+"\n"+
                        hour+":"+minute+":"+second+"\n"+day+"."+month+"."+year, Toast.LENGTH_LONG).show();
                Map.updatePosition(latitude, longitude);
            }

            pts += longitude + ";" + latitude + ";" + altitude + ";";

            Log.i("testing", pts);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    public void updateTimeAndDate()
    {
        today.setToNow();

        year = today.year;
        month = today.month;
        day = today.monthDay;

        hour = today.hour;
        minute = today.minute;
        second = today.second;
    }

    public String formatTime(int h, int m, int s)
    {
        String result = "";
        result += h < 10 ? "0" + h : h;
        result += m < 10 ? "0" + m : m;
        result += s < 10 ? "0" + s : s;
        return result;
    }

    public String formatDate(int r, int m, int d)
    {
        String result = r+"";
        result += m < 10 ? "0" + m : m;
        result += d < 10 ? "0" + d : d;
        return result;
    }

    public void startRecording()
    {
        recording = true;
        start += formatDate(year, month, day)+formatTime(hour, minute, second);

        try{
            obj.put("start", formatDate(year, month, day)+formatTime(hour, minute, second));
        }catch(JSONException e){}
    }

    public void stopRecording()
    {
        recording = false;
        finish += formatDate(year, month, day)+formatTime(hour, minute, second);

        try{
            obj.put("finish", formatDate(year, month, day)+formatTime(hour, minute, second));
            obj.put("points", points);
            obj.put("times", times);

            saveTrackInInternalStorage("test");
            readTrackFromInternalStorage("test");
        }catch(JSONException e){}
    }

    public boolean isRecording()
    {
        return recording;
    }

    public void saveTrackInInternalStorage(String fileName)
    {
        String js = obj.toString();
        byte [] jarray = js.getBytes();
        try {

            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(jarray);
            fos.close();
        }catch(IOException e){Toast.makeText(context,  "RecordRoute: an error occurred when write to file", Toast.LENGTH_LONG).show();}

        //Log.i("testing", obj);
        System.out.println(obj);
    }

    public void readTrackFromInternalStorage(String fileName)
    {
        byte [] bytes;
        obj.toString();

        try {

            FileInputStream fis = context.openFileInput(fileName);
            Scanner sc = new Scanner(fis);
            String linia="";
            while(sc.hasNext())
            {
                linia+=sc.next();
            }
            //bytes = new byte[(int)fis.getChannel().size()];
            //fis.read(bytes);

            JSONObject jsonobj = new JSONObject(linia);
            System.out.println("odczytany "+jsonobj);
            //jsonobj.put(bytes);

            System.out.println("read try");
        }catch(FileNotFoundException e){Toast.makeText(context, "RecordRoute: an error occurred when read from file", Toast.LENGTH_LONG).show();}
        catch(IOException e){Toast.makeText(context, "RecordRoute: error at getChannel().size()", Toast.LENGTH_LONG).show();}
        catch(JSONException e){Toast.makeText(context, "RecordRoute: error at new JSONObject(bytes.toString())", Toast.LENGTH_LONG).show();}
    }
}

/*

try{
            obj.put("start", formatDate(year, month, day)+formatTime(hour, minute, second));
            obj.put("finish", formatDate(year, month, day)+formatTime(hour, minute, second));
            points.put(new Double(longitude));
            points.put(new Double(latitude));
            points.put(new Double(altitude));
            times.put(formatDate(year, month, day)+formatTime(hour, minute, second));
            obj.put("points", points);
            obj.put("times", times);

        }catch(JSONException e){}

 */
