package com.example.alien.myapplication1.Charts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alien.myapplication1.NetConnection.CheckingConnection;
import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.tracks.GetTracks;
import com.example.alien.myapplication1.tracks.StatisticsCalculator;
import com.example.alien.myapplication1.tracks.Track;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.DefaultValueFormatter;
import com.github.mikephil.charting.utils.Highlight;

import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by kamilos on 2015-05-11.
 */
public class Chart extends Fragment implements OnChartValueSelectedListener, OnChartGestureListener {

    LineChart lineChart;
    BarChart barChart;
    JSONObject jsonObj;
    StatisticsCalculator sc;
    int chartIndex;
    String mode; //singleTrack or overall - charts based on all tracks
    String userID;
    boolean isConnected;

    public Chart(){}


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        mode = getArguments().getString("mode");
        userID = getArguments().getString("userID");
        lineChart = (LineChart) rootView.findViewById(R.id.lineChart);

        if(mode.equals("singleTrack"))
        {
            try {
                jsonObj = new JSONObject(getArguments().getString("json"));
                chartIndex = getArguments().getInt("chartIndex");

            } catch (JSONException e) { e.printStackTrace();}

            lineChart.setOnChartValueSelectedListener(this);
            lineChart.setOnChartGestureListener(this);
            sc = new StatisticsCalculator(jsonObj);


            setChartAppearance();
            setLegendAppearance();
            setLineChart(chartIndex);
        }
        else if(mode.equals("overall"))
        {
            barChart = (BarChart) rootView.findViewById(R.id.barChart);
            CheckingConnection cc = new CheckingConnection(getActivity());
            cc.execute();
            isConnected = cc.isConnected();
            //lineChart.setVisibility(View.INVISIBLE);
            //barChart.setVisibility(View.VISIBLE);

            setDailyDistanceChart();
        }


        lineChart.fitScreen();
        lineChart.invalidate();  //redraw lineChart

        return rootView;
    }

    public void setAltitudeChart()
    {
        lineChart.setDescription("Profil trasy");
        lineChart.setDescriptionTextSize(16f);
        lineChart.setDescriptionPosition(400, 36);
        lineChart.animateXY(3000, 2000);
        lineChart.setHighlightIndicatorEnabled(false);

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<LineDataSet> altitudeDataSet = new ArrayList<>();
        ArrayList<Entry> attitude = new ArrayList<>();

        JSONArray jPoints = null;
        JSONArray time = sc.getTimes();
        String temp="";
        try{
            jPoints= jsonObj.getJSONArray("points");
        }catch(Exception e){}

        for(int i = 0; i < time.length(); i++)
        {
            try {
                temp = time.get(i).toString();
                attitude.add(new Entry((float)jPoints.getDouble(i*3+2), i));
            } catch (JSONException e) {e.printStackTrace(); }

            labels.add(temp.substring(8, 10) + ":" + temp.substring(10, 12) + ":" + temp.substring(12, 14));
            //speed.add(new Entry((float) sc.getCurrentSpeed(i), i));
        }


        LineDataSet lsd = new LineDataSet(attitude, "Wysokość ");
        altitudeDataSet.add(lsd);
        lsd.setColor(getResources().getColor(R.color.blue1));
        lsd.setCircleColor(getResources().getColor(R.color.blue1));
        lsd.setValueFormatter(new DefaultValueFormatter(0));
        lsd.setCircleSize(3f);

        LineData ld = new LineData(labels, altitudeDataSet);
        lineChart.setData(ld);
    }

    public void setVelocityChart()
    {
        lineChart.setDescription("Wykres zmiany prędkości w czasie");
        lineChart.setDescriptionTextSize(16f);
        lineChart.setDescriptionPosition(400, 36);
        lineChart.animateXY(3000, 2000);
        lineChart.setHighlightIndicatorEnabled(false);

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<LineDataSet> pointsDataSet = new ArrayList<>();
        ArrayList<Entry> speed = new ArrayList<>();

        String temp = "";

        JSONArray time = sc.getTimes();
        for(int i = 0; i < time.length(); i++)
        {
            try {
                temp = time.get(i).toString();
            } catch (JSONException e) {e.printStackTrace(); }
            labels.add(temp.substring(8, 10) + ":" + temp.substring(10, 12) + ":" + temp.substring(12, 14));
            speed.add(new Entry((float) sc.getCurrentSpeed(i), i));
        }

        LineDataSet lsd = new LineDataSet(speed, "Prędkość [km/h]");
        lsd.setColor(getResources().getColor(R.color.blue1));
        lsd.setCircleColor(getResources().getColor(R.color.blue1));
        lsd.setValueFormatter(new DecimalNumberFormatter());
        lsd.setCircleSize(3f);


        pointsDataSet.add(lsd);

        LineData ld = new LineData(labels, pointsDataSet);
        lineChart.setData(ld);

    }

    public void setDailyDistanceChart()
    {
        barChart.setDescription("Dzienny dystans");
        barChart.setDescriptionTextSize(16f);
        barChart.setDescriptionPosition(440, 36);
        barChart.animateXY(3000, 2000);
        barChart.setHighlightIndicatorEnabled(false);

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<LineDataSet> pointsDataSet = new ArrayList<>();
        ArrayList<Entry> distance = new ArrayList<>();

        ArrayList<Track> tracks = isConnected ? database() : iStorage();

        for(Track t:tracks)
            System.out.println("Dystans "+ t.getDistance());

    }



    public void setChartAppearance()
    {
        lineChart.setMaxVisibleValueCount(15);

        System.out.println("half x: " + lineChart.getCenter().x);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineWidth(2);
        xAxis.setAxisLineColor(getResources().getColor(R.color.black1));

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisLineWidth(2);
        leftAxis.setAxisLineColor(getResources().getColor(R.color.black1));
        leftAxis.setValueFormatter(new DecimalNumberFormatter());

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void setLegendAppearance()
    {
        Legend legend = lineChart.getLegend();
        legend.setTextSize(14f);
        //legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(14f);
    }


    public void setLineChart(int index)
    {
        switch(index)
        {
            case 1: setVelocityChart(); break;
            case 2: setAltitudeChart(); break;
        }
    }



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
        GetTracks gt = new GetTracks(getActivity());
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






    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        lineChart.highlightValue(e.getXIndex(), dataSetIndex);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) { }
    @Override
    public void onChartLongPressed(MotionEvent me) {   }
    @Override
    public void onChartDoubleTapped(MotionEvent me) {    }
    @Override
    public void onChartSingleTapped(MotionEvent me) {    }
    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {    }
    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {    }
}
