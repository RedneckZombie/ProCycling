package com.example.alien.myapplication1.tracks;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.alien.myapplication1.R;

import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class TrackDetails extends Fragment {
    private Button buttonMap;
    //private Button buttonSave;
    private JSONObject jsonObj;
    //private Boolean isSaved;
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
        //buttonSave = (Button) rootView.findViewById(R.id.btn_save);
        TextView distTxt = (TextView) rootView.findViewById(R.id.txtDist);
        double dist = calc.getDistance();
        dist /= 1000.0;
        distTxt.setText(new DecimalFormat("#0.00").format(dist) + " km");
        TextView spdTxt = (TextView) rootView.findViewById(R.id.txtSpd);
        spdTxt.setText(new DecimalFormat("#0.00").format(calc.getAverageSpeed()) + " km/h");
        TextView timeTxt = (TextView) rootView.findViewById(R.id.txtTime);
        Period trTime = calc.getTravelTime();
        timeTxt.setText(String.format("%02d:%02d:%02d", trTime.getHours(), trTime.getMinutes(), trTime.getSeconds()) + "");

        //isSaved = getArguments().getBoolean("isSaved");


        //buttonSave.setEnabled(!isSaved);

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackSummary summFragment = new TrackSummary();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                Bundle b = new Bundle();
                b.putString("json", jsonObj.toString());
                //b.putBoolean("isSaved", isSaved);
                summFragment.setArguments(b);
                transaction.replace(R.id.details_container, summFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

//        buttonSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               isSaved = true;
//               buttonSave.setEnabled(!isSaved);
//            }
//        });


        return rootView;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

}