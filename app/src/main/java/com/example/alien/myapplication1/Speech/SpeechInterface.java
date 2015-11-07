package com.example.alien.myapplication1.Speech;

import android.app.Activity;
import android.widget.Toast;

import com.example.alien.myapplication1.Options.Preferences;


/**
 * Created by BotNaEasy on 2015-11-01.
 */
public class SpeechInterface{
    private SpeechRecognition speechRecognition;
    private SpeechSynthesis speechSynthesis;
    private Activity act;
    private Preferences prefs;
    public SpeechInterface(Activity act, String clazz, MicroListener micro)
    {
        try {
            this.act = act;
            prefs = new Preferences(act);
            speechRecognition = new SpeechRecognition(act, clazz, micro);
            speechSynthesis = new SpeechSynthesis(act);
        }catch(Exception e){
            Toast.makeText(act, "Nie znaleziono słownika!",Toast.LENGTH_SHORT).show();
        }
    }
    public void tell(String command)
    {
        if(prefs.getPreferencesBoolean("enableSynth"))
            speechSynthesis.speakOut(command);

    }
    public void listenCommand()
    {
        if(prefs.getPreferencesBoolean("enableRecogn"))
            speechRecognition.initRecognizer();
        else{
            Toast.makeText(act, "Rozpoznawanie komend głosowych jest wyłączone!", Toast.LENGTH_SHORT).show();
        }
    }
    public String getStringParam()
    {
        return speechRecognition.getStringParam();
    }
    public int getIntParam()
    {
        return speechRecognition.getIntParam();
    }
    public void destroy()
    {
        speechRecognition.onDestroy();
        speechSynthesis.onDestroy();
    }
}
