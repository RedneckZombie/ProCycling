package com.example.alien.myapplication1.Speech;

/**
 * Created by BotNaEasy on 2015-10-20.
 */
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class SpeechSynthesis implements TextToSpeech.OnInitListener{
    TextToSpeech tts;

    public SpeechSynthesis(Context ctx)
    {
       tts= new TextToSpeech(ctx, this);
        onInit(TextToSpeech.SUCCESS);
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(new Locale("pl", "PL"));

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
    public void speakOut(String text) {

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
