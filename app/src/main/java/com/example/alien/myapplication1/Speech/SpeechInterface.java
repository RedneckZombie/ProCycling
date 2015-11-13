package com.example.alien.myapplication1.Speech;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.alien.myapplication1.NetConnection.CheckingConnection;
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
    public boolean isConncted()
    {
        boolean result = false;
        CheckingConnection cc = new CheckingConnection(act);
        cc.execute();
        while (!cc.isFinished()) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }
        if (cc.isConnected()) {
            result = true;
        }
        return result;
    }
    public void listenCommand()
    {
        if(!isConncted()) {
            Toast.makeText(act, "Brak połączenia z siecią!", Toast.LENGTH_SHORT).show();
            return;
        }
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
    public void showInfoDialog()
    {
        try {
            new AlertDialog.Builder(act)
                    .setTitle("Dostępne komendy głosowe:")
                    .setMessage(speechRecognition.getDictionary().toString())
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        }catch(Exception e){e.getMessage();}
    }
}
