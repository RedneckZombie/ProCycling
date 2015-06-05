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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
import java.util.Collections;
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

    // http://www.rjp.pan.pl/index.php?option=com_content&view=article&id=978:skroty-nazw-miesicy-&catid=44:porady-jzykowe&Itemid=58
    String []months = {"STY", "LUT", "MAR", "KWI", "MAJ", "CZE", "LIP", "SIE", "WRZ", "PAŹ", "LIS", "GRU"};

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        mode = getArguments().getString("mode");
        userID = getArguments().getString("userID");
        chartIndex = getArguments().getInt("chartIndex");
        lineChart = (LineChart) rootView.findViewById(R.id.lineChart);
        barChart = (BarChart) rootView.findViewById(R.id.barChart);

        if(mode.equals("singleTrack"))
        {
            try {
                jsonObj = new JSONObject(getArguments().getString("json"));
            } catch (JSONException e) { e.printStackTrace();}

            barChart.setVisibility(View.INVISIBLE);
            lineChart.setVisibility(View.VISIBLE);
            lineChart.setOnChartValueSelectedListener(this);
            lineChart.setOnChartGestureListener(this);
            sc = new StatisticsCalculator(jsonObj);


            setLineChartAppearance();
            setLegendAppearance();
            setSingleTrackChart(chartIndex);
            lineChart.fitScreen();
            lineChart.invalidate();  //redraw lineChart
        }
        else if(mode.equals("overall"))
        {
            lineChart.setVisibility(View.INVISIBLE);
            barChart.setVisibility(View.VISIBLE);
            barChart.setOnChartValueSelectedListener(this);
            barChart.setOnChartGestureListener(this);

            CheckingConnection cc = new CheckingConnection(getActivity());
            cc.execute();
            isConnected = cc.isConnected();
            //lineChart.setVisibility(View.INVISIBLE);
            //barChart.setVisibility(View.VISIBLE);

            setOverallChart(chartIndex);
            setBarChartAppearance();
            barChart.fitScreen();
            barChart.invalidate();
        }

        return rootView;
    }

    public void setAltitudeChart()
    {
        lineChart.setDescription("Profil trasy");
        lineChart.setDescriptionTextSize(16f);
        lineChart.setDescriptionPosition(300, 36);
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

    public void setYearlyDistanceChart()
    {
        barChart.setDescription("Roczny dystans");
        barChart.setDescriptionTextSize(16f);
        barChart.setDescriptionPosition(300, 25);
        barChart.animateY(3000);
        barChart.setHighlightIndicatorEnabled(false);

        ArrayList<String> labels = new ArrayList<>(); //date
        ArrayList<BarDataSet> barDataSets = new ArrayList<>();
        ArrayList<BarEntry> distance = new ArrayList<>();

        //ArrayList<Track> tracks = isConnected ? database() : iStorage();
        //ArrayList<Track> tracks = iStorage();
        ArrayList<Track> tracks = database();
        System.out.println("isConnected " + isConnected);
        System.out.println("yearly, size = " + tracks.size()+"\n\n");
        Collections.sort(tracks);
        for(int i = 0 ; i < tracks.size(); i++)
            System.out.println(tracks.get(i).getTrackName());

        String temp;
        int dist = 0;
        int counter = 0;
        while( !tracks.isEmpty())
        {
            dist = 0;
            temp = tracks.get(0).getTrackName().substring(0, 4);
            labels.add(temp);
            dist += tracks.get(0).getDistance();
            tracks.remove(0);
            while( !tracks.isEmpty() && tracks.get(0).getTrackName().substring(0,4).equals(temp))
            {
                dist += tracks.get(0).getDistance();
                tracks.remove(0);
            }
            distance.add(new BarEntry( ((float)dist/1000), counter));
            counter++;
        }

        BarDataSet bsd = new BarDataSet(distance, "Dystans [km]");
        bsd.setColor(getResources().getColor(R.color.green2));
        //bsd.setDrawValues(false);
        //bsd.setValueFormatter(new DecimalNumberFormatter());

        barDataSets.add(bsd);
        BarData bd = new BarData(labels, barDataSets);
        barChart.setData(bd);

    }

    public void setMonthlyDistanceChart()
    {
        barChart.setDescription("Miesięczny dystans");
        barChart.setDescriptionTextSize(16f);
        barChart.setDescriptionPosition(300, 25);
        barChart.animateY(3000);
        barChart.setHighlightIndicatorEnabled(false);

        ArrayList<String> labels = new ArrayList<>(); //date
        ArrayList<BarDataSet> barDataSets = new ArrayList<>();
        ArrayList<BarEntry> distance = new ArrayList<>();

        //ArrayList<Track> tracks = isConnected ? database() : iStorage();
        //ArrayList<Track> tracks = iStorage();
        ArrayList<Track> tracks = database();
        System.out.println("isConnected " + isConnected);
        System.out.println("monthly, size = " + tracks.size()+"\n\n");
        Collections.sort(tracks);
        for(int i = 0 ; i < tracks.size(); i++)
            System.out.println(tracks.get(i).getTrackName());


        String temp;
        int dist;
        int counter = 0;
        while( !tracks.isEmpty())
        {
            dist = 0;
            temp = tracks.get(0).getTrackName().substring(0, 6);
            labels.add(months[Integer.parseInt(temp.substring(4, 6))-1] + " " + temp.substring(0, 4));
            dist += tracks.get(0).getDistance();
            tracks.remove(0);

            while( !tracks.isEmpty() && tracks.get(0).getTrackName().substring(0,6).equals(temp))
            {
                dist += tracks.get(0).getDistance();
                tracks.remove(0);
            }
            distance.add(new BarEntry( ((float)dist/1000), counter));
            counter++;
        }

        BarDataSet bsd = new BarDataSet(distance, "Dystans [km]");
        bsd.setColor(getResources().getColor(R.color.green2));
        //bsd.setDrawValues(false);
        //bsd.setValueFormatter(new DecimalNumberFormatter());

        barDataSets.add(bsd);
        BarData bd = new BarData(labels, barDataSets);
        barChart.setData(bd);

    }

    public void setDailyDistanceChart()
    {
        barChart.setDescription("Dzienny dystans");
        barChart.setDescriptionTextSize(16f);
        barChart.setDescriptionPosition(300, 25);
        barChart.animateY(3000);
        barChart.setHighlightIndicatorEnabled(false);

        ArrayList<String> labels = new ArrayList<>(); //date
        ArrayList<BarDataSet> barDataSets = new ArrayList<>();
        ArrayList<BarEntry> distance = new ArrayList<>();

        //ArrayList<Track> tracks = isConnected ? database() : iStorage();
        //ArrayList<Track> tracks = iStorage();
        ArrayList<Track> tracks = database();
        System.out.println("isConnected " + isConnected);
        System.out.println("daily, size = " + tracks.size());

        Collections.sort(tracks);
        System.out.println("tracks.size() = " + tracks.size()+"\n\n");

        for(int i = 0 ; i < tracks.size(); i++)
            System.out.println(tracks.get(i).getTrackName());

        String temp;
        int dist = 0;
        int counter = 0;
        while( !tracks.isEmpty())
        {
            dist = 0;
            temp = tracks.get(0).getTrackName().substring(0,8);
            labels.add(temp.substring(6,8) + "." + temp.substring(4,6) + "." + temp.substring(2,4));
            dist += tracks.get(0).getDistance();
            tracks.remove(0);
            while( !tracks.isEmpty() && tracks.get(0).getTrackName().substring(0,8) == temp)
            {
                dist += tracks.get(0).getDistance();
                tracks.remove(0);
            }
            distance.add(new BarEntry(((float) dist / 1000), counter));
            counter++;
        }


        BarDataSet bsd = new BarDataSet(distance, "Dystans [km]");
        bsd.setColor(getResources().getColor(R.color.green2));
        //bsd.setDrawValues(false);
        //bsd.setValueFormatter(new DecimalNumberFormatter());

        barDataSets.add(bsd);
        BarData bd = new BarData(labels, barDataSets);
        barChart.setData(bd);
    }



    public void setBarChartAppearance()
    {
        barChart.setMaxVisibleValueCount(15);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setEnabled(true);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineWidth(2);
        xAxis.setAxisLineColor(getResources().getColor(R.color.black1));

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisLineWidth(2);
        leftAxis.setAxisLineColor(getResources().getColor(R.color.black1));
        leftAxis.setValueFormatter(new DecimalNumberFormatter());

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void setLineChartAppearance()
    {
        lineChart.setMaxVisibleValueCount(15);

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


    public void setSingleTrackChart(int index)
    {
        switch(index)
        {
            case 1: setVelocityChart(); break;
            case 2: setAltitudeChart(); break;
        }
    }

    public void setOverallChart(int index)
    {
        switch(index)
        {
            case 1: setDailyDistanceChart(); break;
            case 2: setMonthlyDistanceChart(); break;
            case 3: setYearlyDistanceChart(); break;
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
        if(mode.equals("singleTrack"))
            lineChart.highlightValue(e.getXIndex(), dataSetIndex);
        else if(mode.equals("overall"))
            barChart.highlightValue(e.getXIndex(), dataSetIndex);
    }

    @Override
    public void onNothingSelected() {  }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) { }
    @Override
    public void onChartLongPressed(MotionEvent me) {  }
    @Override
    public void onChartDoubleTapped(MotionEvent me) {  }
    @Override
    public void onChartSingleTapped(MotionEvent me) {  }
    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {  }
    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {  }
}
