package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.example.alien.myapplication1.map.Map;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by kamilos on 2015-03-28.
 */



// plik z nazmami zarejestrowanych tras nazywa się tracksNames


public class RecordRoute
{
    // Acquire a reference to the system Location Manager
    private LocationManager locationManager ;
    private double longitude = -1, latitude = -1, altitude = -1;
    private Context context;

    private int second, minute, hour, day, month, year;
    private Time today;
    private String start, finish, pts = "";
    private DateTime jodaStart, jodaFinish;
    private JSONObject obj;
    private JSONArray points, times;

    private boolean recording = false;


    public RecordRoute(Context context)
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
        Log.e("testing", "RecordRoute konstruktor");
        obj  = new JSONObject();
        today = new Time(Time.getCurrentTimezone());
        points = new JSONArray();
        times = new JSONArray();

        if(!fileExistance("tracksNames"))
        {
            createFileWithTracksNames();
            Toast.makeText(context, "utworzono plik z nazwami tras", Toast.LENGTH_LONG).show();
        }

    }

    public void createListener(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {

        // Called when a new location is found by the network location provider.
        public void onLocationChanged(Location location) {

            System.out.println("Zmieniam sie :D");
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

    public JSONObject getJSON()
    {
        return obj;
    }

    public void updateTimeAndDate()
    {
        today.setToNow();

        year = today.year;
        month = today.month+1;
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
        System.out.println("start recoding");
        Log.i("testing", "log  w start recording");

        updateTimeAndDate();

        //create new objects to not append old ones
        obj    = new JSONObject();
        points = new JSONArray();
        times  = new JSONArray();

        recording = true;
        start = formatDate(year, month, day)+formatTime(hour, minute, second);
        jodaStart = new DateTime(year, month,day, hour, minute, second);

        try{
            obj.put("start", formatDate(year, month, day)+formatTime(hour, minute, second));
        }catch(JSONException e){}
    }

    public void stopRecording()
    {
        recording = false;
        updateTimeAndDate();
        finish = formatDate(year, month, day)+formatTime(hour, minute, second);
        jodaFinish = new DateTime(year, month, day, hour, minute, second);

        Interval interval2 = new Interval(jodaStart, jodaFinish);
        Period p2 = interval2.toPeriod();
        Toast.makeText(context, p2.getYears()+" "+ p2.getMonths() + " " + p2.getDays() + " " + p2.getHours() + " " + p2.getMinutes() + " " + p2.getSeconds(), Toast.LENGTH_LONG).show();


        try{
            //AltitudeCorrector altCor = new AltitudeCorrector(context, points);

            obj.put("finish", formatDate(year, month, day)+formatTime(hour, minute, second));
            //obj.put("points", altCor.getCorrectedAlt());
            obj.put("points", points);
            obj.put("times", times);

            if(points.length() > 3)
                saveTrackInInternalStorage(finish);
        }catch(JSONException e){}
    }

    public boolean isRecording()
    {
        return recording;
    }

    public void saveTrackInInternalStorage(String fileName)
    {

        try {
            // load tracks names
            FileInputStream fis = context.openFileInput("tracksNames");
            Scanner sc = new Scanner(fis);
            String content="";
            while(sc.hasNext())
            {
                content+=sc.next();
            }
            fis.close();

            String fn = fileName;
            int counter = 2;

            //look for file name which not exists
            while(content.contains(fn))
            {
                fn = fileName + counter;
                counter++;
            }

            String js = obj.toString();
            byte [] jarray = js.getBytes();

            FileOutputStream fos = context.openFileOutput(fn, Context.MODE_PRIVATE);
            fos.write(jarray);
            fos.close();

            FileOutputStream fos2 = context.openFileOutput("tracksNames", Context.MODE_PRIVATE);
            System.out.println("cont: "+content);
            System.out.println("fn: "+fn);
            content += fn + ";";
            //fn +=";";
            fos2.write(content.getBytes());
            fos2.close();

        }catch(IOException e){Toast.makeText(context,  "RecordRoute: an error occurred when write to file", Toast.LENGTH_LONG).show();}


        //Log.i("testing", obj);
        System.out.println(obj);
    }

    public boolean fileExistance(String fname){
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }

    public void createFileWithTracksNames()
    {
        try {

            FileOutputStream fos = context.openFileOutput("tracksNames", Context.MODE_PRIVATE);
            fos.close();
        }catch(IOException e){Toast.makeText(context,  "RecordRoute: error in CreateFileWithTracksNames", Toast.LENGTH_LONG).show();}
    }

    public boolean deleteTrackNamesFile()
    {
        File dir = context.getFilesDir();
        File file = new File(dir, "tracksNames");
        return file.delete();
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




/*

    //return distance in kilometers
    public double getDistanceBetweenPoints(double long1, double lat1, double long2, double lat2)
    {
        double d2r = 0.0174532925199433;  // pi / 180
        double dlong = (long2 - long1) * d2r;
        double dlat = (lat2 - lat1) * d2r;
        double a = Math.pow(Math.sin(dlat / 2.0), 2) + Math.cos(lat1 * d2r) * Math.cos(lat2 * d2r) * Math.pow(Math.sin(dlong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367 * c;

        return d;
    }

    public double getDistance(String trackName)
    {
        double dist = 0;

        byte [] bytes;
        obj.toString();

        try {

            FileInputStream fis = context.openFileInput(trackName);
            Scanner sc = new Scanner(fis);
            String linia="";
            while(sc.hasNext())
            {
                linia+=sc.next();
            }
            JSONObject jsonobj = new JSONObject(linia);

            JSONArray pts = jsonobj.getJSONArray("points");

            int i = 0;
            while(i < pts.length()) {
                dist += getDistanceBetweenPoints((double) pts.get(i), (double) pts.get(i + 1), (double) pts.get(i + 3), (double) pts.get(i + 4));
                i += 6;
            }

        }catch(FileNotFoundException e){Toast.makeText(context, "RecordRoute: an error occurred when read from file", Toast.LENGTH_LONG).show();}
        catch(IOException e){Toast.makeText(context, "RecordRoute: error at getChannel().size()", Toast.LENGTH_LONG).show();}
        catch(JSONException e){Toast.makeText(context, "RecordRoute: error at new JSONObject(bytes.toString())", Toast.LENGTH_LONG).show();}

        return dist;
    }

    // HHMMSS - 2 signs for hours, minutes and seconds
    public Period getTravelTime(String trackName)
    {
        return new Interval(jodaStart, jodaFinish).toPeriod();
    }

    public double avarageSpeed(String trackName)
    {
        Period t = getTravelTime(trackName);
        double time = t.getDays()*24 + t.getHours() + (double)t.getMinutes() / 60D + (double)t.getSeconds() / 3600D;

        return getDistance(trackName) / time;
    }


 */
