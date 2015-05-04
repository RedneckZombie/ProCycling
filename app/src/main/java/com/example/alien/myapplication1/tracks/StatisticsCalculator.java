package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by kamilos on 2015-04-27.
 */
public class StatisticsCalculator {

    JSONObject obj;
    JSONArray pts;
    JSONArray times;

    StatisticsCalculator(JSONObject jsonobj)
    {
        obj = jsonobj;

        try {
            pts = obj.getJSONArray("points");
            times = obj.getJSONArray("times");
        }
        catch(JSONException e){}
    }

    //return distance in meters
    public double getDistanceBetweenPoints(double long1, double lat1, double long2, double lat2)
    {
        double d2r = 0.0174532925199433;  // pi / 180
        double dlong = (long2 - long1) * d2r;
        double dlat = (lat2 - lat1) * d2r;
        double a = Math.pow(Math.sin(dlat / 2.0), 2) + Math.cos(lat1 * d2r) * Math.cos(lat2 * d2r) * Math.pow(Math.sin(dlong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367 * c;

        return (int)(d*1000);
    }

    //return distance in meters
    public int getDistance()
    {
        double dist = 0;

        try {

            int i = 0;
            while (i < pts.length()) {
                dist += getDistanceBetweenPoints((double) pts.get(i), (double) pts.get(i + 1), (double) pts.get(i + 3), (double) pts.get(i + 4));
                i += 6;
            }
        }catch(JSONException e){}

        return (int)dist;
    }

    // HHMMSS - 2 signs for hours, minutes and seconds
    public Period getTravelTime()
    {
        DateTime jodaStart = null, jodaFinish = null;

        try {
            String start = obj.getString("start");
            String finish = obj.getString("finish");


            jodaStart  = new DateTime(Integer.parseInt(start.substring(0,4)), Integer.parseInt(start.substring(4,6)), Integer.parseInt(start.substring(6,8)), Integer.parseInt(start.substring(8,10)), Integer.parseInt(start.substring(10,12)), Integer.parseInt(start.substring(12,14)));
            jodaFinish = new DateTime(Integer.parseInt(finish.substring(0,4)), Integer.parseInt(finish.substring(4,6)), Integer.parseInt(finish.substring(6,8)), Integer.parseInt(finish.substring(8,10)), Integer.parseInt(finish.substring(10,12)), Integer.parseInt(finish.substring(12,14)));


        }catch(JSONException e){}

        return new Interval(jodaStart, jodaFinish).toPeriod();
    }

    public double getAvarageSpeed()
    {
        Period t = getTravelTime();
        double time = t.getDays()*24 + t.getHours() + (double)t.getMinutes() / 60D + (double)t.getSeconds() / 3600D;

        return getDistance()/1000 / time;
    }
}
