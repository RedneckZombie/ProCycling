package com.example.alien.myapplication1.tracks;

/**
 * Created by BotNaEasy on 2015-04-29.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alien.myapplication1.R;

import java.util.ArrayList;

/**
 * Created by BotNaEasy on 2015-03-16.
 */
public class TrackAdapter extends ArrayAdapter<Track> {
    Context con;
    int layoutRID;
    ArrayList<Track> lista = null;
    public TrackAdapter(Context context, int layoutRID, ArrayList<Track> lista)
    {
        super(context, layoutRID, lista);
        this.layoutRID=layoutRID;
        this.con=context;
        this.lista=lista;
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
            LayoutInflater inflater = ((Activity)con).getLayoutInflater();
            wiersz = inflater.inflate(layoutRID, rodzic, false);

            holder = new Kontener();
            // holder.ikona = (ImageView)wiersz.findViewById(R.id.ikona);
            holder.row = (TextView)wiersz.findViewById(R.id.rowText);
            wiersz.setTag(holder);
        }
        else
        {
            holder = (Kontener)wiersz.getTag();
        }
        Track object = lista.get(pozycja);
        String temp = object.getTrackName();
        String trackName = "Trasa z "+temp.substring(6,8) + " " + temp.substring(4,6) + " " + temp.substring(0,4);
        holder.row.setText(trackName+" "+object.getTime()+" "+object.getDistance());
        return wiersz;
    }



}