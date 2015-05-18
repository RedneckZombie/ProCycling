package com.example.alien.myapplication1.Charts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.tracks.StatisticsCalculator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kamilos on 2015-05-11.
 */
public class Chart extends Fragment implements OnChartValueSelectedListener, OnChartGestureListener {

    LineChart chart;
    JSONObject jsonObj;
    StatisticsCalculator sc;

    double scalex = 1, scaley = 1;

    public Chart(){}


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);


        try {
            jsonObj = new JSONObject(getArguments().getString("json"));

        } catch (JSONException e) { e.printStackTrace();}

        chart = (LineChart) rootView.findViewById(R.id.chart);
        chart.setOnChartValueSelectedListener(this);
        chart.setOnChartGestureListener(this);
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
            speed.add(new Entry((float) sc.getCurrentSpeed(i), i));
        }

        LineDataSet lsd = new LineDataSet(speed, "Prędkość [km/h]");
        lsd.setColor(getResources().getColor(R.color.blue1));
        lsd.setCircleColor(getResources().getColor(R.color.blue1));
        lsd.setValueFormatter(new DecimalNumberFormatter());

        pointsDataSet.add(lsd);

        LineData ld = new LineData(times, pointsDataSet);
        //ld.setDrawValues(false);
        chart.setData(ld);

        setChartAppearance();
        setLegendAppearance();

        chart.invalidate();  //redraw chart

        return rootView;
    }

    public void setChartAppearance()
    {
        chart.fitScreen();
        chart.invalidate();

        chart.setDescription("Wykres zmiany prędkości w czasie");
        chart.setDescriptionTextSize(16f);
        chart.setDescriptionPosition(440, 36);
        chart.animateXY(3000, 2000);
        chart.setHighlightIndicatorEnabled(false);

        //chart.setDrawGridBackground(false);
        chart.setMaxVisibleValueCount(15);

        System.out.println("half x: "+chart.getCenter().x);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineWidth(2);
        xAxis.setAxisLineColor(getResources().getColor(R.color.black1));

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisLineWidth(2);
        leftAxis.setAxisLineColor(getResources().getColor(R.color.black1));
        leftAxis.setValueFormatter(new DecimalNumberFormatter());

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public void setLegendAppearance()
    {
        Legend legend = chart.getLegend();
        legend.setTextSize(14f);
        //legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(14f);

    }






    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        chart.highlightValue(e.getXIndex(), dataSetIndex);
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
