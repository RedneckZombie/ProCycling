package com.example.alien.myapplication1.tracks;

public class Track {
    private int track_id;
    private String track_name;
    private int distance;
    private String time;
    protected double average;

    Track(String track_name) {
        this.track_name = track_name;
    }

    Track(int track_id, String track_name, int distance, String time, double average) {
        this.track_id = track_id;
        this.track_name = track_name;
        this.distance = distance;
        this.time = time;
        this.average = average;
    }

    public int getTrackId() {
        return track_id;
    }

    public String getTrackName() {
        return track_name;
    }

    public int getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }
}
