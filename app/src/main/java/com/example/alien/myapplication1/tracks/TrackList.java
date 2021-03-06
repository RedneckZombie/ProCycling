package com.example.alien.myapplication1.tracks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alien.myapplication1.R;

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
    String userID;
    ViewGroup container;

    ArrayList<Track> lista;

    public  TrackList() {}



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track_list, container, false);
        this.container = container;
        isConnected = getArguments().getBoolean("isConnected");
        username = getArguments().getString("username");
        userID = getArguments().getString("userID");
        if(isConnected) {
            lista = database();
        }
        else {
            lista = iStorage();
        }
        TrackAdapter adapter = new TrackAdapter(container.getContext(), R.layout.track_list_row, lista);
        lv =  (ListView)rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(listener);
        return rootView;
    }
///
    public void openRoute(int position)
    {
        if(lista==null)
        {
            return;
        }
        if(position>=lista.size()||position<0)
        {
            return;
        }
        String src = lista.get(position).getTrackName();
        if(src.equals("Brak tras"))
            return;
        JSONObject json;//sprawdz czy nazwa to nie brak tras bo sie wysypio
        if(!isConnected)
            json = readTrackFromInternalStorage(src);
        else {
            GetTrackDetails trackDetails = new GetTrackDetails(container.getContext());
            trackDetails.execute(String.valueOf(lista.get(position).getTrackId()));
            while (!trackDetails.isFinished()) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
            json = trackDetails.getJSON();
        }
        TrackSummary summFragment = new TrackSummary();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        Bundle b = new Bundle();
        b.putString("json", json.toString());
        b.putString("userID", userID);
        b.putBoolean("isConnected", isConnected);
        summFragment.setArguments(b);
        transaction.replace(R.id.track_list_container, summFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            openRoute(position);
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

                    tracks.add(new Track(counter,name, stat.getDistance(), time, stat.getAverageSpeed()));
                }
                else{
                    stat = new StatisticsCalculator(readTrackFromInternalStorage(tempName));
                    p = stat.getTravelTime();
                    time = p.getYears()+""+p.getMonths()+p.getDays()+p.getHours()+p.getMinutes()+p.getSeconds();

                    tracks.add(new Track(counter,tempName, stat.getDistance(), time, stat.getAverageSpeed()));
                }
                counter++;
                i = index+1;
            }

        }catch(IOException e){e.printStackTrace();}

        if(tracks.isEmpty())
            tracks.add(new Track("Brak tras"));
        return tracks;
    }
    public ArrayList<Track> database()
    {
        GetTracks gt = new GetTracks(container.getContext());
        gt.execute(userID);
        System.out.println(userID);
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
