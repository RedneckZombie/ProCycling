package com.example.alien.myapplication1.Speech;

/**
 * Created by BotNaEasy on 2015-11-03.
 */
public interface MicroListener{
    SpeechInterface speech = null;
    void microCommandRun(int result);
    void showInfoDialog();
}
