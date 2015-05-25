package com.example.alien.myapplication1.rankings;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alien.myapplication1.R;


import java.util.ArrayList;

/**
 * Created by Adams on 2015-05-24.
 */
public class RankAdapter extends ArrayAdapter<Rank> {

    private Context cont;
    private int layoutRID;
    private ArrayList<Rank> list;

    public RankAdapter(Context context, int layoutRID, ArrayList<Rank> list)
    {
        super(context, layoutRID, list);
        this.layoutRID=layoutRID;
        this.cont=context;
        this.list=list;
    }

    static class Kontener
    {
        TextView row;
    }

    public View getView(int pozycja, View konwertowanyWidok, ViewGroup rodzic)
    {
        View wiersz = konwertowanyWidok;
        Kontener holder = null;
        if(wiersz == null)
        {
            LayoutInflater inflater = ((Activity)cont).getLayoutInflater();
            wiersz = inflater.inflate(layoutRID, rodzic, false);

            holder = new Kontener();
            holder.row = (TextView)wiersz.findViewById(R.id.rowText);
            wiersz.setTag(holder);
        }
        else
        {
            holder = (Kontener)wiersz.getTag();
        }
        Rank object = list.get(pozycja);

        String content = String.format("%4d %-20s %7.1f %10s %4.1f", object.getPosition(), object.getUsername(),
                object.getStats().getDistance()/1000.0, object.getStats().getTime(), object.getStats().getAverage());
        holder.row.setText(content);
        return wiersz;
    }
}
