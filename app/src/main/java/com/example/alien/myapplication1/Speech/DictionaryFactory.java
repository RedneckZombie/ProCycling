package com.example.alien.myapplication1.Speech;

import com.example.alien.myapplication1.map.SideBar;

import java.util.ArrayList;

/**
 * Created by BotNaEasy on 2015-11-01.
 */
public class DictionaryFactory {
    public static Dictionary createDictionary(String clazz) throws Exception {
        Dictionary dict=null;
        switch(clazz){
            case "SideBar" :
               String[] komendy = {
                        "Rejestruj trase",
                        //"Zakończ rejestrację trasy",
                        "Moje trasy",
                        "Mapa",
                        "Statystyki",
                        "Rankingi",
                        "Wyloguj",
                        "Zamknij"
                };
                dict = new Dictionary(komendy);
                break;
            default:
                throw new Exception("Unknown dictionary");
        }
        return dict;
    }
}
