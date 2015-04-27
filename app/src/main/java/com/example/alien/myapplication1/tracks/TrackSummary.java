package com.example.alien.myapplication1.tracks;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.alien.myapplication1.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TrackSummary extends Fragment {
    private GoogleMap mMap;
//    private Context context;
    private JSONObject jsonObj;
    private ArrayList<LatLng> arrLatLng;
    private Button buttonSave;

    public TrackSummary() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track_summary, container, false);
        //mMap = null;
        setUpMapIfNeeded();
        arrLatLng = new ArrayList<>();
        buttonSave = (Button) rootView.findViewById(R.id.btn_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jsonObj != null)
                    new SaveTrack(getActivity().getApplicationContext()).execute("44","tour de Frącz", jsonObj.toString(), "1003", "02:14:50", "38.88");
                else
                    Toast.makeText(getActivity().getApplicationContext(), "brak danych trasy", Toast.LENGTH_LONG).show();
            }
        });
        try {
            jsonObj = new JSONObject(getArguments().getString("json"));
            parse();
            drawRoute();
        } catch (JSONException e) {
            e.printStackTrace();
    }

//        context = getActivity();

        return rootView;
    }

    public void onResume() {
       super.onResume();
    }

    public void onPause() {
        super.onPause();
    }


    private void setUpMapIfNeeded() {

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1,1),10));
    }

    private void parse() throws JSONException {
        JSONArray jPoints = jsonObj.getJSONArray("points");
        Toast.makeText(getActivity().getApplicationContext(), jPoints.getString(0), Toast.LENGTH_LONG).show();
        for(int i=0; i<jPoints.length(); i+=3)
        {
            arrLatLng.add(new LatLng((Double) jPoints.get(i+1), (Double) jPoints.get(i)));
        }
    }

    private void drawRoute()
    {
        PolylineOptions polyLineOptions = new PolylineOptions();
        polyLineOptions.addAll(arrLatLng);
        polyLineOptions.width(2);
        polyLineOptions.color(Color.BLUE);
        mMap.addPolyline(polyLineOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrLatLng.get(0),10));
    }

}