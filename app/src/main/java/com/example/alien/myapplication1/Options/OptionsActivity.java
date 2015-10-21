package com.example.alien.myapplication1.Options;

import android.app.Activity;
import android.content.Context;
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
        SharedPreferences sharedPref = getPreferences(Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("enableMarkers", markersOn.isChecked());
        editor.putBoolean("enableSynth", synthOn.isChecked());
        editor.putBoolean("enableRecogn", recognOn.isChecked());
        editor.commit();
    }

}
