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
//
    JSONObject obj;
    JSONArray pts;
    JSONArray times;

    public StatisticsCalculator(JSONObject jsonobj)
    {
        obj = jsonobj;

        try {
            pts = obj.getJSONArray("points");
            times = obj.getJSONArray("times");
        }
        catch(JSONException e){e.printStackTrace();}
    }

    //return distance in meters
    public int getDistanceBetweenPoints(double long1, double lat1, double long2, double lat2)
    {
        int R = 6371000; //earh radius in meters -> result in meters
        double d2r = 0.0174532925199433; //degrees to radians
        double dLat = (lat2-lat1) * d2r;
        double dLong = (long2-long1) * d2r;
        double latt1 = lat1 * d2r;
        double latt2 = lat2 * d2r;

        double a = Math.sin(dLat/2)  * Math.sin(dLat/2)  +
                   Math.sin(dLong/2) * Math.sin(dLong/2) * Math.cos(latt1) * Math.cos(latt2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (int)(R*c);
    }

    public Period getTimeBetweenPoints(int timeIndex1, int timeIndex2)
    {
        Period period = null;
        try{
            String time1 = times.get(timeIndex1).toString();
            String time2 = times.get(timeIndex2).toString();
            DateTime point1 = new DateTime(Integer.parseInt(time1.substring(0, 4)), Integer.parseInt(time1.substring(4, 6)), Integer.parseInt(time1.substring(6, 8)), Integer.parseInt(time1.substring(8,10)), Integer.parseInt(time1.substring(10, 12)), Integer.parseInt(time1.substring(12, 14)));
            DateTime point2 = new DateTime(Integer.parseInt(time2.substring(0,4)), Integer.parseInt(time2.substring(4,6)), Integer.parseInt(time2.substring(6,8)), Integer.parseInt(time2.substring(8,10)), Integer.parseInt(time2.substring(10,12)), Integer.parseInt(time2.substring(12,14)));
            period = new Interval(point1, point2).toPeriod();

        }catch(JSONException e){e.printStackTrace();}

        return period;
    }


    //return distance in meters
    public int getDistance()
    {
        double dist = 0;

        try {

            int i = 0;
            while (i < pts.length()) {
                dist += getDistanceBetweenPoints((double) pts.get(i), (double) pts.get(i + 1), (double) pts.get(i + 3), (double) pts.get(i + 4));
                i += 3;
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


        }catch(JSONException e){e.printStackTrace();}

        return new Interval(jodaStart, jodaFinish).toPeriod();
    }

    public double getAverageSpeed()
    {
        Period t = getTravelTime();
        double time = t.getDays()*24 + t.getHours() + (double)t.getMinutes() / 60D + (double)t.getSeconds() / 3600D;

        return (double)getDistance()/1000.0 / time;
    }

    // current speed for chart
    public double getCurrentSpeed(int timeIndex)
    {
        double distance = 0, time = 0;
        Period tempPeriod;

        try {
            if(timeIndex - 2 >= 0)
            {
                distance += getDistanceBetweenPoints((double) pts.get((timeIndex-2)*3), (double) pts.get((timeIndex-2)*3+1), (double)pts.get((timeIndex-1)*3), (double)pts.get((timeIndex-1)*3+1));
                tempPeriod = getTimeBetweenPoints(timeIndex - 2, timeIndex - 1);
                time += tempPeriod.getDays()*24 + tempPeriod.getHours() + (double)tempPeriod.getMinutes() / 60D + (double)tempPeriod.getSeconds() / 3600D;
            }
            if(timeIndex - 1 >= 0)
            {
                distance += getDistanceBetweenPoints((double) pts.get((timeIndex-1)*3), (double) pts.get((timeIndex-1)*3+1), (double)pts.get(timeIndex*3), (double)pts.get(timeIndex*3+1));
                tempPeriod = getTimeBetweenPoints(timeIndex-1, timeIndex);
                time += tempPeriod.getDays()*24 + tempPeriod.getHours() + (double)tempPeriod.getMinutes() / 60D + (double)tempPeriod.getSeconds() / 3600D;
            }
            if(timeIndex + 1 < times.length())
            {
                distance += getDistanceBetweenPoints((double) pts.get(timeIndex*3), (double) pts.get(timeIndex*3+1), (double)pts.get((timeIndex+1)*3), (double)pts.get((timeIndex+1)*3+1));
                tempPeriod = getTimeBetweenPoints(timeIndex, timeIndex+1);
                time += tempPeriod.getDays()*24 + tempPeriod.getHours() + (double)tempPeriod.getMinutes() / 60D + (double)tempPeriod.getSeconds() / 3600D;
            }
            if(timeIndex+2 < times.length())
            {
                distance += getDistanceBetweenPoints((double) pts.get((timeIndex+1)*3), (double) pts.get((timeIndex+1)*3+1), (double)pts.get((timeIndex+2)*3), (double)pts.get((timeIndex+2)*3+1));
                tempPeriod = getTimeBetweenPoints(timeIndex+1, timeIndex+2);
                time += tempPeriod.getDays()*24 + tempPeriod.getHours() + (double)tempPeriod.getMinutes() / 60D + (double)tempPeriod.getSeconds() / 3600D;
            }

        }catch(JSONException e){e.printStackTrace();}

        return distance/1000.0 / time;
    }



    public int getPtsLength() {
        return pts.length();
    }
    public int getTimesLength() {
        return times.length();
    }
    public JSONArray getPts() {
        return pts;
    }
    public JSONArray getTimes() {
        return times;
    }
}
