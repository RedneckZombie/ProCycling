package com.example.alien.myapplication1.Speech;

/**
 * Created by BotNaEasy on 2015-11-02.
 */
public class LevenshteinAlghoritm {
    public static int getDistance(String s, String t)
    {
        int i, j, m, n, cost;
        int d[][];

        m = s.length();
        n = t.length();

        d = new int[m+1][n+1];

        for (i=0; i<=m; i++)
            d[i][0] = i;
        for (j=1; j<=n; j++)
            d[0][j] = j;

        for (i=1; i<=m; i++)
        {
            for (j=1; j<=n; j++)
            {
                if (s.charAt(i-1) == t.charAt(j-1))
                    cost = 0;
                else
                    cost = 1;

                d[i][j] = Math.min(d[i-1][j] + 1,
                        Math.min(d[i][j-1] + 1,
                                d[i-1][j-1] + cost));
            }
        }

        return d[m][n];
    }
}
