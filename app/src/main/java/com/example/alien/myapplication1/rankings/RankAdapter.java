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
    private int rankType;

    public RankAdapter(Context context, int layoutRID, ArrayList<Rank> list, int rankType)
    {
        super(context, layoutRID, list);
        this.layoutRID=layoutRID;
        this.cont=context;
        this.list=list;
        this.rankType = rankType;
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

        String rankValue = "";
        switch(rankType)
        {
            case 1:
                rankValue = String.format("%.2f",object.getStats().getDistance()/1000.0) + "km";
                break;
            case 2:
                rankValue = object.getStats().getTime();
                break;
            case 3:
                rankValue = String.format("%.2f",object.getStats().getAverage()) + "km/h";
                break;
        }
        String content = String.format("%4d %-30s %10s", object.getPosition(), object.getUsername(),
                rankValue);
        System.out.println(content);
        holder.row.setText(content);
        return wiersz;
    }
}
