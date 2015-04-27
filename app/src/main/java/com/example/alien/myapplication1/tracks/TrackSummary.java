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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
    private Button buttonDetails;
    private boolean isSaved;
    private boolean correctTrack;

    public TrackSummary() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_track_summary, container, false);
        //mMap = null;
        setUpMapIfNeeded();
        arrLatLng = new ArrayList<>();
        buttonSave = (Button) rootView.findViewById(R.id.btn_save);
        isSaved = getArguments().getBoolean("isSaved");
        buttonSave.setEnabled(!isSaved);
        buttonDetails  = (Button) rootView.findViewById(R.id.btn_details);
        correctTrack = false;
        try {
            jsonObj = new JSONObject(getArguments().getString("json"));
            if(jsonObj != null)
                if(jsonObj.getJSONArray("points").length()>0)
                    correctTrack = true;
            if(correctTrack)
            {
                parse();
                drawRoute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correctTrack)
                {
                    new SaveTrack(getActivity().getApplicationContext()).execute("44", "tour de Frącz", jsonObj.toString(), "1003", "2:30:00", "23.0");
                    Toast.makeText(getActivity().getApplicationContext(), "Zapisano w bazie", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(), "Nie zarejestrowano trasy", Toast.LENGTH_LONG).show();
                isSaved = true;
                buttonSave.setEnabled(!isSaved);
            }
        });

        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correctTrack) {
                    TrackDetails detFragment = new TrackDetails();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    Bundle b = new Bundle();
                    b.putString("json", jsonObj.toString());
                    b.putBoolean("isSaved", isSaved);
                    detFragment.setArguments(b);
                    transaction.replace(R.id.summary_container, detFragment);
                    //transaction.addToBackStack(null);
                    transaction.commit();
                }

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

        if(!arrLatLng.isEmpty()) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrLatLng.get(0), 8));
            mMap.addMarker(new MarkerOptions().position(arrLatLng.get(0)).title("Start"));
            mMap.addMarker(new MarkerOptions().position(arrLatLng.get(arrLatLng.size() - 1)).title("Meta"));
        }
    }

}