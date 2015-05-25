package com.example.alien.myapplication1.map;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alien.myapplication1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map extends Fragment
{
    private static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static Marker here;
    private static boolean visible = false;
    private static double latitude = 51;
    private static double longitude = 17;
    private List<Point> userPoints;

    public Map(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        mMap = null;
        here = null;
        setUpMapIfNeeded();

        userPoints = new ArrayList<>();
        //LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //locationManager.getLastKnownLocation();
        return rootView;
    }

    public void onResume() {
        visible = true;
        super.onResume();
    }

    public void onPause() {
        visible = false;
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
        here = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Tu jesteś"));

    }

    public static void updatePosition(double lat, double lng)
    {
        latitude = lat;
        longitude = lng;
        LatLng location = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        here.setPosition(location);
    }

    public static boolean isMapVisible()
    {
        return visible;
    }

    public boolean addPoint(double longitude,double latitude)
    {
        return userPoints.add(new Point(longitude, latitude));
    }

    public void savePointsInInternalStorage(int id)
    {
        String temp;
        try {
            FileOutputStream fos = getActivity().openFileOutput(id+"", Context.MODE_APPEND);

            for(Point e : userPoints) {
                temp = e.longitude + "," + e.latitude + ";";
                fos.write(temp.getBytes());
            }

            fos.close();
        } catch(IOException e){ e.printStackTrace();}
    }


}