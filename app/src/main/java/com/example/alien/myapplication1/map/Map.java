package com.example.alien.myapplication1.map;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
    private static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static Marker here;
    private Context context;
   /* private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           Log.i("testing", "onReceive");
           double lng = intent.getDoubleExtra("longitude", 0);
           double lat = intent.getDoubleExtra("latitude", 0);
           updatePosition(new LatLng(lat,lng));
        }
    };*/

    public Map(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        setUpMapIfNeeded();

        context = getActivity();

        Log.i("testing", "onCreate (Map)");
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

/*        IntentFilter filter = new IntentFilter();
        filter.addAction(MyIntentService.ACTION_MyUpdate);
        getActivity().registerReceiver(receiver, filter);*/
        setUpMapIfNeeded();
        super.onResume();

    }

    public void onPause() {

   //     getActivity().unregisterReceiver(receiver);
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
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51, 17), 13));
        here = mMap.addMarker(new MarkerOptions().position(new LatLng(51, 17)).title("Tu jesteś"));


    }

    public static void updatePosition(double lat, double lng)
    {
        LatLng location = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        here.setPosition(location);
    }

}