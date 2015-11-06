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
                        "Moje trasy",
                        "Mapa",
                        "Statystyki",
                        "Rankingi",
                        "Wyloguj",
                        "Zamknij",
                        "Zakończ rejestracje trasy",
                        "Wykresy"
                };
                dict = new Dictionary(komendy);
                break;
            case "ChartActivity" :
                String[] komendy2 = {
                        "Cofnij",
                        "Wykres dzienny",
                        "Wykres miesięczny",
                        "Wykres roczny"
                };
                dict = new Dictionary(komendy2);
                break;
            default:
                throw new Exception("Unknown dictionary");
        }
        return dict;
    }
}