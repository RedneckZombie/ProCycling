package com.example.alien.myapplication1.Charts;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by kamilos on 2015-05-17.
 */
public class DecimalNumberFormatter implements ValueFormatter {

    private DecimalFormat df;

    public DecimalNumberFormatter()
    {
        df = new DecimalFormat("##0.0");
    }

    @Override
    public String getFormattedValue(float value) {
        return df.format(value);
    }
}
