package com.example.alien.myapplication1.tracks;

public class Track implements Comparable {
    private int track_id;
    private String track_name; //date+hour -> yyyymmddhhmmss
    private int distance; // in meters probably
    private String time; // travel time
    protected double average;

    public Track(String track_name) {
        this.track_name = track_name;
    }

    public Track(int track_id, String track_name, int distance, String time, double average) {
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


    //sort ascending
    @Override
    public int compareTo(Object o) {
        int compare = track_name.substring(0, 4).compareTo(o.toString().substring(0, 4)); //compare year
        if (compare == 0) {
            compare = track_name.substring(4, 6).compareTo(o.toString().substring(4, 6)); //compare month
            if (compare == 0) {
                return -track_name.substring(6, 8).compareTo(o.toString().substring(6, 8)); //compare day
            } else
                return -compare;
        } else
            return -compare;
    }
}
