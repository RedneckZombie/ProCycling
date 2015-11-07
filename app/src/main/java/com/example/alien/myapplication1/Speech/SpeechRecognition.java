package com.example.alien.myapplication1.Speech;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

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
    MicroListener micro;
    private int intParam;
    private String stringParam;
    public SpeechRecognition(Activity act, String clazz, MicroListener micro)
    {
        this.micro=micro;
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
    public int recognitionResult()
    {
        int result = dictionary.onPositionInDictionary(getResult());
        if(result>=0)
            return result;
        result = getSimmilarResult();
        if(result>=0)
            return result;
        result = dictionary.getIncludedWordResult(getResult());
        setParams();
        if(result>=0)
            return result;
        else{
            Toast.makeText(act, "Nie rozpoznano komendy!", Toast.LENGTH_SHORT).show();
            return -1;
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
    public void showResult()
    {
        for(int i=0;i<result.size();i++) {
            System.out.println(i + ": " + result.get(i));
        }
    }
    private void setParams()
    {
        stringParam = null;
        intParam = 0;
        List<String> dictWithParams = new ArrayList<String>();
        String[] dict = dictionary.getDictionary().clone();
        for(int i=0;i<dict.length;i++)
        {
            if(dict[i].contains("param")) {
                dict[i] = dict[i].replace("param","");
                dict[i] = dict[i].trim();
                dictWithParams.add(dict[i]);
            }
        }
        if(dictWithParams.isEmpty())
            return;

        /*NAJLEPIEJ PASUJĄCA KOMENDA Z PARAMETREM
        int[] pointsOnPosition = new int[dictWithParams.size()];
        for(int i=0;i<getResult().size();i++)
        {
            String[] splittedCommand = getResult().get(i).split(" ");
            for(int j=0;j<splittedCommand.length;j++)
            {
                for(int k=0;k<dictWithParams.size();k++)
                {
                    if(dictWithParams.get(k).equals(splittedCommand[j]))
                    {
                        pointsOnPosition[k]+=1;
                    }
                }
            }
        }*/
        System.out.println("dojszedlem");
        for(int i=0;i<getResult().size();i++)
        {
            String[] splittedCommand = getResult().get(i).split(" ");
            if(splittedCommand.length==2)//komenda + parametr
            {
                System.out.println("dojszedlem2");
                try{
                    if(stringParam==null)
                        stringParam = splittedCommand[1];//parametr
                    intParam = Integer.parseInt(splittedCommand[1]);
                    break;
                }catch(Exception e){
                    continue;
                }
            }
        }
    }
    public String getStringParam()
    {
        return stringParam;
    }
    public int getIntParam()
    {
        return intParam;
    }
    public void showParams()
    {
        System.out.println("String: "+getStringParam());
        System.out.println("Int: " + getIntParam());
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
                showResult();
                showParams();
                micro.microCommandRun(recognitionResult());
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