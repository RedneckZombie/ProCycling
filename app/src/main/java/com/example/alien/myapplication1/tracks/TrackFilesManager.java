package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.widget.Toast;

import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Adams on 2015-05-10.
 * Synchronizuje trasy do bazy i usuwa nadmiar, odpalane po zakonczeniu rejestracji trasy,
 * przy zapisywaniu jej do bazy (jeszcze gdzies?)
 */
public class TrackFilesManager {

    private Context context;
    private ArrayList<String> listInter;



    public TrackFilesManager(Context context)
    {
        this.context = context;
        synchronize();
        if(listInter.size()>30)
            removeTracksFromInterStor();
    }

    private void removeTracksFromInterStor() {
        int numOfRemoved = listInter.size() - 30;
        List<String> tracksToRemove = listInter.subList(0, numOfRemoved);
//        for(String trRem : tracksToRemove)
//        {
//            File dir = context.getFilesDir();
//            File file = new File(dir, trRem);
//            file.delete();
//        }

        //TODO: usun z pliku z lista tras

    }

    private void synchronize()
    {
        listInter = trackNamesFromInterStorage();
        ArrayList<String> listDB = trackNamesFromDB();

        for(String trackName : listInter)
        {
            if(!listDB.contains(trackName))
            {
                    JSONObject jsonObj = readTrackFromInternalStorage(trackName);
                    StatisticsCalculator calc = new StatisticsCalculator(jsonObj);
                    Period trTime = calc.getTravelTime();
                    new SaveTrack(context).execute("44", trackName, jsonObj.toString(),
                            String.valueOf(calc.getDistance()), String.format("%02d:%02d:%02d", trTime.getHours(), trTime.getMinutes(), trTime.getSeconds()), String.valueOf(calc.getAverageSpeed()));
            }
        }
    }

    private ArrayList<String> trackNamesFromInterStorage()
    {
        ArrayList<String> tracks = new ArrayList<>();

        try {

            FileInputStream fis = context.openFileInput("tracksNames");
            Scanner sc = new Scanner(fis);
            String content = "";
            while (sc.hasNext()) {
                content += sc.next();
            }
            fis.close();

            String tempName, name;
            int index, index2;

            int i = 0;
            while(i < content.length())
            {
                index = content.indexOf(";", i);
                tempName = content.substring(i, index);

                if(tempName.contains(":")){
                    index2 = tempName.indexOf(":");
                    name = tempName.substring(0, index2);
                    tracks.add(name);
                }
                else
                {
                    tracks.add(tempName);
                }
                i = index+1;
            }

        }catch(IOException e){e.printStackTrace();}
        return tracks;
    }

    private ArrayList<String> trackNamesFromDB()
    {
        GetTracks gt = new GetTracks(context);
        gt.execute("44");
        while(!gt.isFinished())
        {
            try{
                Thread.sleep(100);
            }catch(Exception e){}
        }
        ArrayList<String> lista = new ArrayList<>();
        for(Track t : gt.getList())
        {
            lista.add(t.getTrackName());
        }
        return lista;
    }

    public JSONObject readTrackFromInternalStorage(String fileName)
    {
        JSONObject jsonTrack = null;

        try {

            FileInputStream fis = context.openFileInput(fileName);
            Scanner sc = new Scanner(fis);
            String linia="";
            while(sc.hasNext())
            {
                linia+=sc.next();
            }
            jsonTrack = new JSONObject(linia);


        }catch(FileNotFoundException e){
            Toast.makeText(context, "RecordRoute: an error occurred when read from file", Toast.LENGTH_LONG).show();}
        catch(JSONException e){Toast.makeText(context, "RecordRoute: error at new JSONObject(bytes.toString())", Toast.LENGTH_LONG).show();}

        return jsonTrack;
    }

}
