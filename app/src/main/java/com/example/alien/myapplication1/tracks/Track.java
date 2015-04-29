package com.example.alien.myapplication1.tracks;

/**
 * Created by Alien on 2015-04-29.
 */
public class Track {
    private int _track_id;
    private String _track_name;
    private int _distance;
    private String _time;
    private double _average;

    Track(String track_name){
        _track_name = track_name;
    }

    Track(int track_id, String track_name, int distance, String time, double average){
        _track_id = track_id;
        _track_name = track_name;
        _distance = distance;
        _time = time;
        _average = average;
    }

    public int getTrackId(){
        return _track_id;
    }

    public String getTrackName(){
        return _track_name;
    }

    public int getDistance(){
        return _distance;
    }

    public String getTime(){
        return _time;
    }

    public double getAverage(){
        return _average;
    }
}
