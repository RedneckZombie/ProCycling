package com.example.alien.myapplication1.map;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.tracks.RecordRoute;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Fragment
{
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker here;
    private Context context;

    public Map(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        setUpMapIfNeeded();

        context = getActivity();

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                RecordRoute rr = new RecordRoute(context);
            }
        });

        /*
        Runnable myRunnable = new Runnable(){

            public void run(){
                RecordRoute rr = new RecordRoute(context);
                try {
                    wait();
                }
                catch(InterruptedException ie){}
            }
        };

        Thread thread = new Thread(myRunnable);
        thread.start();
        */

        return rootView;
    }
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51, 17), 13));
        here = mMap.addMarker(new MarkerOptions().position(new LatLng(51, 17)).title("Tu jesteś"));


    }

    private void updatePosition(LatLng locat)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locat));
        here.setPosition(locat);
    }
}