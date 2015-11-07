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
                        "rejestruj trase",
                        "moje trasy",
                        "mapa",
                        "statystyki",
                        "rankingi",
                        "wyloguj",
                        "zamknij",
                        "zakończ rejestracje trasy",
                        "wykresy",
                        "trasa param"
                };
                dict = new Dictionary(komendy);
                break;
            case "ChartActivity" :
                String[] komendy2 = {
                        "cofnij",
                        "wykres dzienny",
                        "wykres miesięczny",
                        "wykres roczny"
                };
                dict = new Dictionary(komendy2);
                break;
            case "LogInActivity" :
                String[] komendy3 = {
                        "zapamiętaj",
                        "zaloguj",
                        "zaloguj jako gość",
                        "rejestracja",
                        "zamknij"
                };
                dict = new Dictionary(komendy3);
                break;
            case "RegistrationActivity" :
                String[] komendy4 = {
                        "zamknij",
                        "rejestruj",
                        "wyczyść"
                };
                dict = new Dictionary(komendy4);
                break;
            default:
                throw new Exception("Unknown dictionary");
        }
        return dict;
    }
}