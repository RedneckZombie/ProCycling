package com.example.alien.myapplication1.rankings;

import com.example.alien.myapplication1.tracks.Stats;

/**
 * Created by Adams on 2015-05-24.
 */
public class Rank{
    private int position;
    private String username;
    private Stats stats;

    public Rank(int pos, String user, Stats st)
    {
        position = pos;
        username = user;
        stats = st;
    }

    public String getUsername() {
        return username;
    }

    public int getPosition() {
        return position;
    }

    public Stats getStats() {
        return stats;
    }

    public void setPosition(int x)
    {
        position = x;
    }
}
