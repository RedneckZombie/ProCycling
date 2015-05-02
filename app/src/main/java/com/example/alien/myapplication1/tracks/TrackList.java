package com.example.alien.myapplication1.tracks;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alien.myapplication1.R;
import com.google.android.gms.maps.SupportMapFragment;

import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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

    public  TrackList() {}

    //public TrackList(Context context){ context = context; }

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
        StatisticsCalculator stat;
        ArrayList<Track> tracks = new ArrayList<>();

        try {

            FileInputStream fis = getActivity().openFileInput("tracksNames");
            Scanner sc = new Scanner(fis);
            String content = "";
            while (sc.hasNext()) {
                content += sc.next();
            }
            fis.close();

            String tempName, name, time;
            int index, index2;
            Period p;

            int i = 0;
            int counter = 1;
            while(i < content.length())
            {
                index = content.indexOf(";", i);
                tempName = content.substring(i, index);

                if(tempName.contains(":")){
                    index2 = tempName.indexOf(":");
                    name = tempName.substring(0, index2);

                    stat = new StatisticsCalculator(readTrackFromInternalStorage(name));
                    p = stat.getTravelTime();
                    time = p.getYears()+""+p.getMonths()+p.getDays()+p.getHours()+p.getMinutes()+p.getSeconds();

                    tracks.add(new Track(counter,name, tempName.substring(index2+1), stat.getDistance(), time, stat.getAvarageSpeed()));
                }
                else{
                    stat = new StatisticsCalculator(readTrackFromInternalStorage(tempName));
                    p = stat.getTravelTime();
                    time = p.getYears()+""+p.getMonths()+p.getDays()+p.getHours()+p.getMinutes()+p.getSeconds();

                    tracks.add(new Track(counter,tempName, stat.getDistance(), time, stat.getAvarageSpeed()));
                }
                counter++;
                i = index+1;
            }

        }catch(IOException e){e.printStackTrace();}

        ///////////prezent dla Kamila <- jak miło, dziękuję :D
        return tracks;
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

    public JSONObject readTrackFromInternalStorage(String fileName)
    {
        byte [] bytes;
        JSONObject jsonTrack = null;

        try {

            FileInputStream fis = getActivity().openFileInput(fileName);
            Scanner sc = new Scanner(fis);
            String linia="";
            while(sc.hasNext())
            {
                linia+=sc.next();
            }
            jsonTrack = new JSONObject(linia);


        }catch(FileNotFoundException e){
            Toast.makeText(getActivity(), "RecordRoute: an error occurred when read from file", Toast.LENGTH_LONG).show();}
        catch(IOException e){Toast.makeText(getActivity(), "RecordRoute: error at getChannel().size()", Toast.LENGTH_LONG).show();}
        catch(JSONException e){Toast.makeText(getActivity(), "RecordRoute: error at new JSONObject(bytes.toString())", Toast.LENGTH_LONG).show();}

        return jsonTrack;
    }
}
