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
    final int []chartsCount = { 2, 5}; // single track charts, overall charts
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        fm = getSupportFragmentManager();

        Intent intent = getIntent();
        modeS = intent.getStringExtra("mode");
        if(modeS.equals("singleTrack"))
            mode = 0;
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
    }

    public void listeners()
    {
        next = (Button) findViewById(R.id.button2);
        back = (Button) findViewById(R.id.button);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chartIndex == chartsCount[mode])
                    chartIndex = 1;
                else
                    chartIndex++;



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
