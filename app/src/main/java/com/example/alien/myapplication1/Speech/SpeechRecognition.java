package com.example.alien.myapplication1.Speech;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.example.alien.myapplication1.map.SideBar;

import java.util.ArrayList;

/**
 * Created by BotNaEasy on 2015-10-14.
 */
public class SpeechRecognition {
    protected static final int RESULT_SPEECH = 1;
    private Activity act;
    private SpeechRecognizer recogn;
    private boolean isFinished;
    private ArrayList<String> result;
    private Dictionary dictionary;
    SideBar s;
    public SpeechRecognition(Activity act, String clazz, SideBar s)
    {
        this.s=s;
        this.act = act;
        recogn = SpeechRecognizer.createSpeechRecognizer(act);
        try {
            dictionary = DictionaryFactory.createDictionary(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initListener();
    }
    public void initRecognizer()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "pl-PL");
        try{
            result = null;
            isFinished = false;
            recogn.startListening(intent);
        }catch(ActivityNotFoundException e) {
            Toast.makeText(act.getApplication(),
                    "To urządzenie nie ma wsparcia do zamiany mowy na tekst!",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void onDestroy()
    {
        recogn.stopListening();
        recogn.destroy();
    }
    public boolean isFinished() {
        return isFinished;
    }

    public ArrayList<String> getResult() {
        return result;
    }
    public int getSimmilarResult()
    {
        double MIN_ERROR = 0.3;
        int bestScore=101;
        int bestScorePosition = -1;
        String userCommand="";
        String suspectCommand="";
        for(int i=0; i<dictionary.getDictionary().length;i++)
        {
            for(int j=0;j<getResult().size();j++)
            {
                int partialResult = LevenshteinAlghoritm.getDistance(dictionary.getDictionary()[i], getResult().get(j));
                if(partialResult<bestScore) {
                    bestScore = partialResult;
                    bestScorePosition = i;
                    userCommand = getResult().get(j);
                    suspectCommand = dictionary.getDictionary()[i];
                }
            }
        }
        double averageLength = (userCommand.length()+suspectCommand.length())/2;
        if(bestScore/averageLength<MIN_ERROR)
            return bestScorePosition;
        else
            return -1;
    }
    public int recognitionResult()
    {
        //initRecognizer();
        int result = dictionary.onPositionInDictionary(getResult());
        if(result>=0)
            return result;
        result = getSimmilarResult();
        if(result>=0)
            return result;
        else{
            Toast.makeText(act, "Nie rozpoznano komendy!", Toast.LENGTH_SHORT).show();
            return -1;
        }
    }

    private void initListener()
    {
        recogn.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(act.getApplication(),
                        "Mów",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                isFinished = true;
                s.microCommandRun(recognitionResult());
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

}
