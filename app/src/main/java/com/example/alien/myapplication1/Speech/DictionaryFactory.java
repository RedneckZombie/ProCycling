package com.example.alien.myapplication1.Speech;

/**
 * Created by BotNaEasy on 2015-11-01.
 */
public class DictionaryFactory {
    public static Dictionary createDictionary(String clazz) throws Exception {
        Dictionary dict=null;
        switch(clazz){
            case "SideBarActivity" :
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
                        "trasa param",
                        "opcje",
                        "włącz markery",
                        "wyłącz markery",
                        "marker param",
                        "komendy",
                        "ja w rankingu"
                };
                dict = new Dictionary(komendy);
                break;
            case "ChartActivity" :
                String[] komendy2 = {
                        "cofnij",
                        "wykres dzienny",
                        "wykres miesięczny",
                        "wykres roczny",
                        "wykres profilu",
                        "wykres prędkości",
                        "komendy"
                };
                dict = new Dictionary(komendy2);
                break;
            case "LogInActivity" :
                String[] komendy3 = {
                        "zapamiętaj",
                        "zaloguj",
                        "zaloguj jako gość",
                        "rejestracja",
                        "zamknij",
                        "komendy"
                };
                dict = new Dictionary(komendy3);
                break;
            case "RegistrationActivity" :
                String[] komendy4 = {
                        "zamknij",
                        "rejestruj",
                        "wyczyść",
                        "komendy"
                };
                dict = new Dictionary(komendy4);
                break;
            case "OptionsActivity" :
                String[] komendy5 = {
                        "zapisz",
                        "wyjdź",
                        "włącz markery",
                        "wyłącz markery",
                        "włącz rozpoznawanie komend",
                        "wyłącz rozpoznawanie komend",
                        "włącz wypowiedzi głosowe",
                        "wyłącz wypowiedzi",
                        "komendy"
                };
                dict = new Dictionary(komendy5);
                break;
            default:
                throw new Exception("Unknown dictionary");
        }
        return dict;
    }
}