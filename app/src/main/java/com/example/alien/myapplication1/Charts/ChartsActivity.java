package com.example.alien.myapplication1.Charts;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.map.Map;

public class ChartsActivity extends ActionBarActivity {

    Fragment fr= new Chart();
    String json;
    Button next, back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        loadJSON();
        applyChart();
        listeners();
    }

    public void listeners()
    {
        next = (Button) findViewById(R.id.button);
        back = (Button) findViewById(R.id.button2);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void loadJSON()
    {
        Intent in = getIntent();
        json = in.getStringExtra("json");
    }
    public void applyChart()
    {
        FragmentManager fm = getSupportFragmentManager();
        Bundle b = new Bundle();
        b.putString("json", json);
        fr.setArguments(b);
        fm.beginTransaction().replace(R.id.content_frame2, fr).commit();
    }

}
