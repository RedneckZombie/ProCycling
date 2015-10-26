package com.example.alien.myapplication1.Options;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.alien.myapplication1.R;

public class OptionsActivity extends Activity {

    Button exit;
    Button save;
    RadioButton markersOn;
    RadioButton markersOff;
    RadioButton recognOn;
    RadioButton recognOff;
    RadioButton synthOn;
    RadioButton syntOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        initialize();
        listeners();
        setData();
    }
    public void initialize()
    {
        exit = (Button) findViewById(R.id.exit_button);
        save = (Button) findViewById(R.id.save_button);
        markersOff = (RadioButton) findViewById(R.id.markers_off);
        markersOn = (RadioButton) findViewById(R.id.marekers_on);
        recognOff = (RadioButton) findViewById(R.id.recognition_off);
        recognOn = (RadioButton) findViewById(R.id.recognition_on);
        synthOn = (RadioButton) findViewById(R.id.synthesis_on);
        syntOff = (RadioButton) findViewById(R.id.synthesis_off);
    }
    public void listeners()
    {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOptions();
                finish();
            }
        });
    }
    public void saveOptions()
    {
        SharedPreferences sharedPref = getSharedPreferences("PREFS", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        boolean enableMarkers = markersOn.isChecked();
        boolean enableSynth = synthOn.isChecked();
        boolean enableRecogn = recognOn.isChecked();
        editor.putBoolean("enableMarkers", enableMarkers);
        editor.putBoolean("enableSynth", enableSynth);
        editor.putBoolean("enableRecogn", enableRecogn);
        editor.apply();
    }
    public void setData(){
        SharedPreferences preferences = getSharedPreferences("PREFS", Activity.MODE_PRIVATE);
        boolean enableMarkers = preferences.getBoolean("enableMarkers", false);
        boolean enableSynth = preferences.getBoolean("enableSynth", false);
        boolean enableRecogn = preferences.getBoolean("enableRecogn", false);
        markersOn.setChecked(enableMarkers);
        markersOff.setChecked(!enableMarkers);
        recognOn.setChecked(enableRecogn);
        recognOff.setChecked(!enableRecogn);
        synthOn.setChecked(enableSynth);
        syntOff.setChecked(!enableSynth);
    }

}
