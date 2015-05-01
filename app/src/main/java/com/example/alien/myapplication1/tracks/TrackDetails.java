package com.example.alien.myapplication1.tracks;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alien.myapplication1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class TrackDetails extends Fragment {
    private Button buttonMap;
    private Button buttonSave;
    private JSONObject jsonObj;
    private Boolean isSaved;
    private StatisticsCalculator calc;

    public TrackDetails() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_track_details, container, false);

        try {
            jsonObj = new JSONObject(getArguments().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //arrLatLng = new ArrayList<>();
        calc = new StatisticsCalculator(jsonObj);

        buttonMap = (Button) rootView.findViewById(R.id.btn_map);
        buttonSave = (Button) rootView.findViewById(R.id.btn_save);
        TextView distTxt = (TextView) rootView.findViewById(R.id.txtDist);
        distTxt.setText(new DecimalFormat("#0.00").format(calc.getDistance()) + " km");
        TextView spdTxt = (TextView) rootView.findViewById(R.id.txtSpd);
        spdTxt.setText(new DecimalFormat("#0.00").format(calc.getAvarageSpeed()) + " km/h");
        TextView timeTxt = (TextView) rootView.findViewById(R.id.txtTime);
        timeTxt.setText(calc.getTravelTime() + "");

        isSaved = getArguments().getBoolean("isSaved");


        buttonSave.setEnabled(!isSaved);

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackSummary summFragment = new TrackSummary();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putString("json", jsonObj.toString());
                b.putBoolean("isSaved", isSaved);
                summFragment.setArguments(b);
                transaction.replace(R.id.details_container, summFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SaveTrack(getActivity().getApplicationContext()).execute("44", "tour de Frącz", jsonObj.toString(), String.valueOf(calc.getDistance()), String.valueOf(calc.getTravelTime()), String.valueOf(calc.getAvarageSpeed()));
                Toast.makeText(getActivity().getApplicationContext(), "zapisano w bazie", Toast.LENGTH_LONG).show();
                isSaved = true;
                buttonSave.setEnabled(!isSaved);
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