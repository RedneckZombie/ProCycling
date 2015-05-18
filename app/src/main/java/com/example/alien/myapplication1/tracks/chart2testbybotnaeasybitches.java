package com.example.alien.myapplication1.tracks;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.myapplication1.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BotNaEasy on 2015-05-17.
 */
public class chart2testbybotnaeasybitches extends Fragment {
    LineChart chart;
    JSONObject jsonObj;
    StatisticsCalculator sc;

    public chart2testbybotnaeasybitches(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        try {
            jsonObj = new JSONObject(getArguments().getString("json"));

        } catch (JSONException e) { e.printStackTrace();}
        chart = (LineChart) rootView.findViewById(R.id.chart);

        ArrayList<String> times = new ArrayList<>();
        ArrayList<LineDataSet> pointsDataSet = new ArrayList<LineDataSet>();
        ArrayList<Entry> attitude = new ArrayList<Entry>();

        sc = new StatisticsCalculator(jsonObj);

        JSONArray jPoints = null;
        JSONArray time = sc.getTimes();
        String temp="";
        try{
            jPoints= jsonObj.getJSONArray("points");
        }catch(Exception e){}

        for(int i = 0; i < jPoints.length(); i++)
        {
            try {
                temp = time.get(i).toString();
            } catch (JSONException e) {e.printStackTrace(); }
            times.add(temp.substring(8,10)+":"+temp.substring(10,12)+":"+temp.substring(12,14));
            //speed.add(new Entry((float) sc.getCurrentSpeed(i), i));
        }
        for(int i=2;i<jPoints.length();i+=3)
        {
            attitude.add(new Entry(i, i));
        }

        LineDataSet lsd = new LineDataSet(attitude, "Wysokoœæ");
        pointsDataSet.add(lsd);

        LineData ld = new LineData(times, pointsDataSet);
        chart.setData(ld);

        chart.setDescription("Wykres zmiany wysokoœci w czasie");




        chart.invalidate();  //redraw chart

        return rootView;
    }


}
