package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alien.myapplication1.Charts.Chart;
import com.example.alien.myapplication1.Charts.ChartsActivity;
import com.example.alien.myapplication1.R;

import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class TrackDetails extends Fragment {
    private Button buttonMap, buttonChart;
    private JSONObject jsonObj;
    private StatisticsCalculator calc;
    private Context context;

    public TrackDetails() {
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_track_details, container, false);

        context = container.getContext();
        try {
            jsonObj = new JSONObject(getArguments().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        calc = new StatisticsCalculator(jsonObj);

        buttonMap = (Button) rootView.findViewById(R.id.btn_map);
        buttonChart = (Button) rootView.findViewById(R.id.btn_chart);
        TextView distTxt = (TextView) rootView.findViewById(R.id.txtDist);
        double dist = calc.getDistance();
        dist /= 1000.0;
        distTxt.setText(new DecimalFormat("#0.00").format(dist) + " km");
        TextView spdTxt = (TextView) rootView.findViewById(R.id.txtSpd);
        spdTxt.setText(new DecimalFormat("#0.00").format(calc.getAverageSpeed()) + " km/h");
        TextView timeTxt = (TextView) rootView.findViewById(R.id.txtTime);
        Period trTime = calc.getTravelTime();
        timeTxt.setText(String.format("%02d:%02d:%02d", trTime.getHours(), trTime.getMinutes(), trTime.getSeconds()) + "");

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackSummary summFragment = new TrackSummary();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putString("json", jsonObj.toString());
                summFragment.setArguments(b);
                transaction.replace(R.id.details_container, summFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        buttonChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Chart chart = new Chart();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putString("json", jsonObj.toString());
                chart.setArguments(b);
                ft.replace(R.id.details_container, chart).addToBackStack(null).commit();
                */

                /*
                Chart2 chart2 = new Chart2();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putString("json", jsonObj.toString());
                chart2.setArguments(b);
                ft.replace(R.id.details_container, chart2).addToBackStack(null).commit();
                */
                Intent in = new Intent(context, ChartsActivity.class);
                in.putExtra("json", jsonObj.toString());
                startActivity(in);

            }
        });


        return rootView;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

}