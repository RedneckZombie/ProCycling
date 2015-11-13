package com.example.alien.myapplication1.rankings;

import com.example.alien.myapplication1.tracks.Stats;

import java.util.List;

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
    public static int getUserPosition(List<Rank> ranks, String user)
    {
        for(int i=0;i<ranks.size();i++)
        {
            System.out.println(ranks.get(i).getUsername()+" = "+ user);
            if(ranks.get(i).getUsername().equals(user))
                return i;
        }
        return -1;
    }
}
