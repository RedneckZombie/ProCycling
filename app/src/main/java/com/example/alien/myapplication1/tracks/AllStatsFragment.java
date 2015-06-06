package com.example.alien.myapplication1.tracks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alien.myapplication1.Charts.ChartActivity;
import com.example.alien.myapplication1.R;


import java.text.DecimalFormat;

/**
 * Created by Adams on 2011-05-16.
 */


public class AllStatsFragment extends Fragment implements View.OnClickListener {

    String userID;
    Button charts;

    public AllStatsFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_all_stats, container, false);
        TextView username = (TextView) rootView.findViewById(R.id.usernameView);
        username.setText(getArguments().getString("username"));

        userID = getArguments().getString("userID");
        charts = (Button) rootView.findViewById(R.id.allStatsChartButton);
        charts.setOnClickListener(this);

        TextView distTxt = (TextView) rootView.findViewById(R.id.txtDist);
        double dist = getArguments().getInt("dist");
        dist /= 1000.0;
        distTxt.setText(new DecimalFormat("#0.00").format(dist) + " km");
        TextView spdTxt = (TextView) rootView.findViewById(R.id.txtSpd);
        double avg = getArguments().getDouble("avg");
        spdTxt.setText(new DecimalFormat("#0.00").format(avg) + " km/h");
        TextView timeTxt = (TextView) rootView.findViewById(R.id.txtTime);
        timeTxt.setText(getArguments().getString("time"));
        return rootView;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), ChartActivity.class);
        intent.putExtra("mode", "overall");
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
}

