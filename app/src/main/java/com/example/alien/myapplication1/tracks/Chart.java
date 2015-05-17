package com.example.alien.myapplication1.tracks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alien.myapplication1.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by kamilos on 2015-05-11.
 */
public class Chart extends Fragment{

    LineChart chart;
    JSONObject jsonObj;
    StatisticsCalculator sc;

    public Chart(){}


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        try {
            jsonObj = new JSONObject(getArguments().getString("json"));

        } catch (JSONException e) { e.printStackTrace();}

        chart = (LineChart) rootView.findViewById(R.id.chart);
        sc = new StatisticsCalculator(jsonObj);
        ArrayList<String> times = new ArrayList<>();
        ArrayList<LineDataSet> pointsDataSet = new ArrayList<LineDataSet>();
        ArrayList<Entry> speed = new ArrayList<Entry>();

        String temp = "";

        JSONArray time = sc.getTimes();
        for(int i = 0; i < time.length(); i++)
        {
            try {
                temp = time.get(i).toString();
            } catch (JSONException e) {e.printStackTrace(); }
            times.add(temp.substring(8,10)+":"+temp.substring(10,12)+":"+temp.substring(12,14));
            speed.add(new Entry((float) sc.getCurrentSpeed(i), 0));
        }

        LineDataSet lsd = new LineDataSet(speed, "Prędkość");



        chart.setDescription("Wykres zmiany prędkości w czasie");




        chart.invalidate(); //redraw chart


        return rootView;
    }







    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

}
