package com.example.alien.myapplication1.Options;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.Speech.MicroListener;
import com.example.alien.myapplication1.Speech.SpeechInterface;

public class OptionsActivity extends ActionBarActivity implements MicroListener{

    Button exit;
    Button save;
    RadioButton markersOn;
    RadioButton markersOff;
    RadioButton recognOn;
    RadioButton recognOff;
    RadioButton synthOn;
    RadioButton syntOff;
    SpeechInterface speechInterface;
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
        speechInterface = new SpeechInterface(this, getClass().getSimpleName(), this);
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
        finish();
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

    private void enableMarkers(boolean isEnable)
    {
        markersOn.setChecked(isEnable);
        markersOff.setChecked(!isEnable);
    }
    private void enableSpeechRecognition(boolean isEnable)
    {
        recognOn.setChecked(isEnable);
        recognOff.setChecked(!isEnable);
    }
    private void enableSpeechSynthesis(boolean isEnable)
    {
        synthOn.setChecked(isEnable);
        syntOff.setChecked(!isEnable);
    }
    @Override
    public void microCommandRun(int result) {
        switch (result){
            case 0:
                saveOptions();
                break;
            case 1:
                finish();
                break;
            case 2:
                enableMarkers(true);
                break;
            case 3:
                enableMarkers(false);
                break;
            case 4:
                enableSpeechRecognition(true);
                break;
            case 5:
                enableSpeechRecognition(false);
                break;
            case 6:
                enableSpeechSynthesis(true);
                break;
            case 7:
                enableSpeechSynthesis(false);
                break;
            case 8:
                showInfoDialog();
                break;
        }
    }

    @Override
    public void showInfoDialog() {
        speechInterface.showInfoDialog();
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
        else if(id==R.id.avaible_comands)
        {
            showInfoDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        speechInterface.destroy();
        super.onDestroy();
    }
}
