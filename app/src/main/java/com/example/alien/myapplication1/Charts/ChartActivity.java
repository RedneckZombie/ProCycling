package com.example.alien.myapplication1.Charts;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.alien.myapplication1.R;

public class ChartActivity extends ActionBarActivity {

    Fragment fr;
    FragmentManager fm;
    String json;
    Button next, back;
    int chartIndex; //1-velocty, 2-topographic profile
    String modeS;
    int mode; //0 - single track charts, 1 - overall charts
    final int []chartsCount = { 2, 3}; // single track charts, overall charts
    String userID;
    String []singleNames = {"Profil trasy", "Wykres predkosci"};
    //String []overalllNames = {"Miesiêczny", "Roczny", "Dzienny"};
    String []overalllNames = { "Dzienny", "Miesieczny", "Roczny"};
    String [][]names = {singleNames, overalllNames};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        next = (Button) findViewById(R.id.button2);
        back = (Button) findViewById(R.id.button);

        fm = getSupportFragmentManager();

        Intent intent = getIntent();
        modeS = intent.getStringExtra("mode");
        if(modeS.equals("singleTrack")) {
            mode = 0;
            back.setVisibility(View.INVISIBLE);
        }
        else if(modeS.equals("overall"))
            mode = 1;

        userID = intent.getStringExtra("userID");

        chartIndex = 1;
        fr = new Chart();
        listeners();

        if(mode == 0) {
            loadJSON();
            applySingleTrackChart();
        }
        else if(mode == 1){
            applyOverallChart();
        }

        back.setText(names[mode][names[mode].length-1]);
        next.setText(names[mode][1]);
    }

    public void listeners()
    {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chartIndex == chartsCount[mode])
                    chartIndex = 1;
                else
                    chartIndex++;

                System.out.println("chartIndex = " + chartIndex);

                if(mode == 0)
                {
                    switch(chartIndex)
                    {
                        case 1: next.setText("Profil trasy"); break;
                        case 2: next.setText("Wykres prêdkoœci"); break;
                    }
                }
                else if(mode == 1)
                {
                    switch(chartIndex)
                    {
                        case 1: back.setText("Roczny");
                                next.setText("Miesieczny"); break;
                        case 2: back.setText("Dzienny");
                                next.setText("Roczny"); break;
                        case 3: back.setText("Miesieczny");
                                next.setText("Dzienny"); break;
                    }
                }


                Bundle b = new Bundle();
                b.putString("json", json);
                b.putInt("chartIndex", chartIndex);
                b.putString("mode", modeS);
                b.putString("userID", userID);
                fr = new Chart();
                fr.setArguments(b);
                fm.popBackStack();
                fm.beginTransaction().replace(R.id.content_frame2, fr).commit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chartIndex == 1)
                    chartIndex = chartsCount[mode];
                else
                    chartIndex--;

                System.out.println("chartIndex = " + chartIndex);

                if(mode == 0)
                {
                    switch(chartIndex)
                    {
                        case 1: next.setText("Profil trasy"); break;
                        case 2: next.setText("Wykres prêdkoœci"); break;
                    }
                }
                else if(mode == 1)
                {
                    switch(chartIndex)
                    {
                        case 1: back.setText("Roczny");
                            next.setText("Miesieczny"); break;
                        case 2: back.setText("Dzienny");
                            next.setText("Roczny"); break;
                        case 3: back.setText("Miesieczny");
                            next.setText("Dzienny"); break;
                    }
                }



                Bundle b = new Bundle();
                b.putString("json", json);
                b.putInt("chartIndex", chartIndex);
                b.putString("mode", modeS);
                b.putString("userID", userID);
                fr = new Chart();
                fr.setArguments(b);
                fm.popBackStack();
                fm.beginTransaction().replace(R.id.content_frame2, fr).commit();
            }
        });
    }
    public void loadJSON()
    {
        Intent in = getIntent();
        json = in.getStringExtra("json");
    }
    public void applySingleTrackChart()
    {
        Bundle b = new Bundle();
        b.putString("json", json);
        b.putInt("chartIndex", chartIndex);
        b.putString("mode", "singleTrack");
        fr.setArguments(b);
        fm.beginTransaction().replace(R.id.content_frame2, fr).commit();
    }
    public void applyOverallChart()
    {
        Bundle b = new Bundle();
        b.putInt("chartIndex", chartIndex);
        b.putString("mode", "overall");
        fr.setArguments(b);
        fm.beginTransaction().replace(R.id.content_frame2, fr).commit();
    }

}
