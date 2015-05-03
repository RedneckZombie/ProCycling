package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by Adams on 2015-05-03.
 */
public class AltitudeCorrector {

    private Context context;
    private JSONArray ptsArr;

    public AltitudeCorrector(Context cont, JSONArray json)
    {
        context = cont;
        ptsArr = json;
        correct();
        minimizeError();
    }

    private void correct()
    {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inStream = assetManager.open("geofix.dat");
            byte[] buffer = new byte[2076480];
            inStream.read(buffer);

            int prevInd = -1;
            int currInd;
            double fixValue = 0;

            int i=0;
            while(i<ptsArr.length())
            {
                Double currLat = ptsArr.getDouble(i+1);
                Double currLong = ptsArr.getDouble(i);
                if(currLat>=0)
                {
                    currInd = 90 - currLat.intValue();
                    currInd = (currInd-1) * 4;
                    currInd += 3 - (currLat - Math.floor(currLat))/0.25;
                }
                else
                {
                    currInd = 90 - currLat.intValue();
                    currInd = (currInd-1) * 4;
                    currInd += (currLat - Math.ceil(currLat))/0.25;
                }
                currInd = (currInd-1)*1440*2;
                currInd = currInd>=0 ? currInd:0;
                if(currLong < 0)
                    currLong = 360.0 + currLong;
                currInd += (currLong/0.25) * 2;
                if(currInd != prevInd)
                {
                    Log.w("alt", "binInd" + currInd);

                    byte[] b = new byte[2];
                    b[0] = buffer[currInd];
                    b[1] = buffer[currInd + 1];
                    fixValue = ByteBuffer.wrap(b).getShort() / 100.0;
                    Log.i("alt", "Lat" + currLat + " Lng" + currLong + " fix: " + fixValue);
                }
                ptsArr.put(i+2, ptsArr.getDouble(i+1) - fixValue);
                prevInd = currInd;
                i+=3;
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("geofix", "File error");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("geofix", "JSON error");
        }
    }

    private void minimizeError()
    {
        try {
            for(int i=5; i<ptsArr.length()-3; i+=3)
            {
                double avg = (ptsArr.getDouble(i) + ptsArr.getDouble(i-3) + ptsArr.getDouble(i+3))/3;
                ptsArr.put(i, avg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getCorrectedAlt()
    {
        return ptsArr;
    }
}
