package com.example.alien.myapplication1.tracks;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.alien.myapplication1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TrackDetails extends Fragment {
    //    private Context context;
    private Button buttonMap;
    private Button buttonSave;
    private JSONObject jsonObj;
    private Boolean isSaved;

    public TrackDetails() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_track_details, container, false);
        //arrLatLng = new ArrayList<>();

        buttonMap = (Button) rootView.findViewById(R.id.btn_map);
        buttonSave = (Button) rootView.findViewById(R.id.btn_save);

        isSaved = getArguments().getBoolean("isSaved");
        try {
            jsonObj = new JSONObject(getArguments().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                if(jsonObj != null) {

                    try {
                        if(jsonObj.getJSONArray("points").length()>0) {
                            new SaveTrack(getActivity().getApplicationContext()).execute("44", "tour de Frącz", jsonObj.toString());
                            Toast.makeText(getActivity().getApplicationContext(), "zapisano w bazie", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getActivity().getApplicationContext(), "Nie zarejestrowano trasy", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else
                    Toast.makeText(getActivity().getApplicationContext(), "Brak danych trasy", Toast.LENGTH_LONG).show();
                isSaved = true;
                buttonSave.setEnabled(!isSaved);
            }
        });

//        context = getActivity();

        return rootView;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }





}