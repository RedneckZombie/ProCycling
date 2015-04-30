package com.example.alien.myapplication1.tracks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.alien.myapplication1.R;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

/**
 * Created by BotNaEasy on 2015-04-29.
 */
public class TrackList extends Fragment {
    private static boolean visible;
    ListView lv;
    boolean isConnected;
    String username;
    ViewGroup container;

    ArrayList<Track> lista;
    public TrackList(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track_list, container, false);
        this.container = container;
        isConnected = getArguments().getBoolean("isConnected");
        username = getArguments().getString("username");
        if(isConnected) {//
            System.out.println("Jest net");
            lista = database();
        }
        else {
            System.out.println("Ni ma net");
            lista = iStorage();
        }
        TrackAdapter adapter = new TrackAdapter(container.getContext(), R.layout.track_list_row, lista);
        lv =  (ListView)rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(listener);
        return rootView;
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };
    public ArrayList<Track> iStorage()
    {
        ///////////prezent dla Kamila
        return null;
    }
    public ArrayList<Track> database()
    {
        GetTracks gt = new GetTracks(container.getContext());
        gt.execute("44");
        while(!gt.isFinished())
        {
            try{
                Thread.sleep(100);
            }catch(Exception e){}
        }
        ArrayList<Track> lista = gt.getList();
        if(lista.isEmpty())
            lista.add(new Track("Brak tras"));
        return lista;
    }

    public void onResume() {
        visible = true;
        super.onResume();
    }

    public void onPause() {
        visible = false;
        super.onPause();
    }
    public boolean isListVisible()
    {
        return visible;
    }
}
