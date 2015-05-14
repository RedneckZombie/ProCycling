package com.example.alien.myapplication1.tracks;

public class Stats {
    private int distance;
    private double average;
    private String time;

    public Stats(int distance, double average, String time) {
        this.distance = distance;
        this.average = average;
        this.time = time;
    }

    public int getDistance() {
        return distance;
    }

    public double getAverage() {
        return average;
    }

    public String getTime() {
        return time;
    }
}
