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

    public int getIncludedWordResult(List<String> commands)
    {
        int[] dictionaryPositionPoints = new int[dictionary.length];
        for(int i=0;i<dictionary.length;i++)
        {
            String[] splittedDictionary = dictionary[i].split(" ");
            for(int j=0;j<splittedDictionary.length;j++)
            {
                for(int k=0;k<commands.size();k++)
                {
                    String[] splittedCommand = commands.get(k).split(" ");
                    for(int l=0;l<splittedCommand.length;l++)
                    {
                        if(splittedCommand[l].equals(splittedDictionary[j]))
                        {
                            dictionaryPositionPoints[i]+=1;
                        }
                    }
                    /*
                    if(splittedDictionary[j].equals(commands.get(k)))
                    {
                        dictionaryPositionPoints[i]+=1;
                    }*/
                }
            }
        }
        return getBestPosition(dictionaryPositionPoints);
    }
    private int getBestPosition(int[] dict)
    {
        int result = -1;
        int var = 0;
        for(int i=0;i<dict.length;i++)
        {
            if(dict[i]>var) {
                var = dict[i];
                result = i;
            }
        }
        return result;
    }
    public String[] getDictionary() {
        return dictionary;
    }
}
