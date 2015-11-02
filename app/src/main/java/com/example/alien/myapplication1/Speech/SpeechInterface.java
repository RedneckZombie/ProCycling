package com.example.alien.myapplication1.Speech;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.alien.myapplication1.map.SideBar;

/**
 * Created by BotNaEasy on 2015-11-01.
 */
public class SpeechInterface{
    private SpeechRecognition speechRecognition;
    private SpeechSynthesis speechSynthesis;
    private Activity act;
    public SpeechInterface(Activity act, String clazz, SideBar s)
    {
        try {
            this.act = act;
            speechRecognition = new SpeechRecognition(act, clazz, s);
            speechSynthesis = new SpeechSynthesis(act);
        }catch(Exception e){
            Toast.makeText(act, "Nie znaleziono słownika!",Toast.LENGTH_SHORT).show();
        }
    }
    public void tell(String command)
    {
        speechSynthesis.speakOut(command);
    }
    public void listenCommand()
    {
        speechRecognition.initRecognizer();
    }
    public void onDestroy()
    {
        speechRecognition.onDestroy();
        speechSynthesis.onDestroy();
    }
}
