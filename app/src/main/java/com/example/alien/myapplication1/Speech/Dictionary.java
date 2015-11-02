package com.example.alien.myapplication1.Speech;

import java.util.List;

/**
 * Created by BotNaEasy on 2015-11-01.
 */
public class Dictionary {
    private String[] dictionary;
    public Dictionary(String[] dict)
    {
        this.dictionary = dict;
    }
    public boolean isInDictionary(String command)
    {
        for(String com : dictionary)
        {
            if(com.equals(command))
                return true;
        }
        return false;
    }
    public int onPositionInDictionary(List<String> commands)
    {
        for(int i=0;i<dictionary.length;i++)
        {
            for(int j=0;j<commands.size();j++)
            {
                if(dictionary[i].equals(commands.get(j)))
                    return i;
            }
        }
        return -1;
    }

    public String[] getDictionary() {
        return dictionary;
    }
}
