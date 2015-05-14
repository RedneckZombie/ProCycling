package com.example.alien.myapplication1.tracks;

public class Track {
    private int track_id;
    private String track_name;
    private String custom_name;
    private int distance;
    private String time;
    private double average;

    Track(String track_name) {
        this.track_name = track_name;
    }

    Track(int track_id, String track_name, int distance, String time, double average) {
        this.track_id = track_id;
        this.track_name = track_name;
        this.distance = distance;
        this.time = time;
        this.average = average;
        this.custom_name = "";
    }

    Track(int track_id, String track_name, String custom, int distance, String time, double average) {
        this.track_id = track_id;
        this.track_name = track_name;
        this.distance = distance;
        this.time = time;
        this.average = average;
        this.custom_name = custom;
    }

    public int getTrackId() {
        return track_id;
    }

    public String getTrackName() {
        return track_name;
    }

    public String getCustomName() {
        return custom_name;
    }

    public int getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

    public double getAverage() {
        return average;
    }

    public void setCustomName(String custom) {
        custom_name = custom;
    }
}
