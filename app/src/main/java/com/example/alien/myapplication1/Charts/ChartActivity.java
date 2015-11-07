package com.example.alien.myapplication1.Charts;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.Speech.MicroListener;
import com.example.alien.myapplication1.Speech.SpeechInterface;

public class ChartActivity extends ActionBarActivity implements MicroListener{

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
    //String []overalllNames = {"Miesięczny", "Roczny", "Dzienny"};
    String []overalllNames = { "Dzienny", "Miesieczny", "Roczny"};
    String [][]names = {singleNames, overalllNames};
    SpeechInterface speechInterface;

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
        setChart();
        speechInterface = new SpeechInterface(this, getClass().getSimpleName(), this);
    }

    public void setChart()
    {
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
    public void setDailyChart()
    {
        mode =1;
        chartIndex=1;
        back.setText("Roczny");
        next.setText("Miesieczny");
        setChart();
    }
    public void setMonthlyChart()
    {
        mode = 1;
        chartIndex = 2;
        back.setText("Dzienny");
        next.setText("Roczny");
        setChart();
    }
    public void setYearlyChart()
    {
        mode=1;
        chartIndex=3;
        back.setText("Miesieczny");
        next.setText("Dzienny");
        setChart();
    }

    public void listeners()
    {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chartIndex == chartsCount[mode])
                    chartIndex = 1;
                else
                    chartIndex++;

                System.out.println("chartIndex = " + chartIndex);

                if (mode == 0) {
                    switch (chartIndex) {
                        case 1:
                            next.setText("Profil trasy");
                            break;
                        case 2:
                            next.setText("Wykres prędkości");
                            break;
                    }
                } else if (mode == 1) {
                    switch (chartIndex) {
                        case 1:
                            back.setText("Roczny");
                            next.setText("Miesieczny");
                            break;
                        case 2:
                            back.setText("Dzienny");
                            next.setText("Roczny");
                            break;
                        case 3:
                            back.setText("Miesieczny");
                            next.setText("Dzienny");
                            break;
                    }
                }


                setChart();
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

                if (mode == 0) {
                    switch (chartIndex) {
                        case 1:
                            next.setText("Profil trasy");
                            break;
                        case 2:
                            next.setText("Wykres prędkości");
                            break;
                    }
                } else if (mode == 1) {
                    switch (chartIndex) {
                        case 1:
                            back.setText("Roczny");
                            next.setText("Miesieczny");
                            break;
                        case 2:
                            back.setText("Dzienny");
                            next.setText("Roczny");
                            break;
                        case 3:
                            back.setText("Miesieczny");
                            next.setText("Dzienny");
                            break;
                    }
                }
                setChart();
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

    @Override
    public void microCommandRun(int result) {
        speechInterface.tell(result+"");
        switch(result){
            case 0:
                finish();
                break;
            case 1:
                setDailyChart();
                break;
            case 2:
                setMonthlyChart();
                break;
            case 3:
                setYearlyChart();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_charts, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.listenMicro)
        {
            speechInterface.listenCommand();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        speechInterface.destroy();
        super.onDestroy();
    }
}
