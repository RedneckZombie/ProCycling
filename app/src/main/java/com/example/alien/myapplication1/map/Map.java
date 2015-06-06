package com.example.alien.myapplication1.map;

import android.app.ActionBar;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.alien.myapplication1.NetConnection.CheckingConnection;
import com.example.alien.myapplication1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map extends Fragment implements GoogleMap.OnMapLongClickListener, View.OnClickListener
{
    private static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static Marker here;
    private static boolean visible = false;
    private static double latitude = 52.15182309;
    private static double longitude = 19.42829922;
    private List<Place> allPlaces;
    private String userID;
    private boolean ifShowsMarkers=true;

    private boolean isVisible=false;

    EditText interestingPlace;
    Button ok;
    Button cancel;
    LinearLayout linLayouyt, mapLayout;

    LatLng ll;

    public Map(){}
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        mMap = null;
        here = null;
        allPlaces = new ArrayList<Place>();
        setUpMapIfNeeded();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc != null)
        {
            latitude = loc.getLatitude();
            longitude = loc.getLongitude();
        }

        initialize(rootView);
        mapListeners();
        readUserId();
        showsMarkers();
        return rootView;
    }

    public void offMarkers()
    {
        ifShowsMarkers=false;
        mMap.clear();
    }
    public void onMarkers()
    {
        ifShowsMarkers=true;
        showsMarkers();
    }
    public void showsMarkers()
    {
        if(ifShowsMarkers)
        {
            CheckingConnection check_conn = new CheckingConnection(getActivity());
            check_conn.execute();
            while(!check_conn.isFinished()){
                try{
                    Thread.sleep(10);
                }catch(Exception e){}
            }
            if(check_conn.isConnected()) {
                GetPlaces places = new GetPlaces(getActivity());
                places.execute();
                while (!places.isFinished()) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }
                allPlaces = places.getList();

                if (allPlaces != null) {
                    for (int i = 0; i < allPlaces.size(); i++) {
                        setMarker(allPlaces.get(i).latitude, allPlaces.get(i).longitude, allPlaces.get(i).getTitle());
                    }
                }
                System.out.println("showMarker");
            }
        }
    }
    public void readUserId()
    {
        userID=getArguments().getString("userID");
    }
    public void initialize(View view)
    {
        interestingPlace = (EditText)view.findViewById(R.id.etPlace);
        ok = (Button) view.findViewById(R.id.btOK);
        cancel = (Button) view.findViewById(R.id.btCancel);
        linLayouyt = (LinearLayout) view.findViewById(R.id.linLayout);
        mapLayout = (LinearLayout) view.findViewById(R.id.mapLinLayout);
    }
    public void hideObjects()
    {
        interestingPlace.setVisibility(View.GONE);
        interestingPlace.setText("");
        linLayouyt.setVisibility(View.GONE);
        isVisible=false;
    }
    public void showObjects()
    {
        interestingPlace.setVisibility(View.VISIBLE);
        linLayouyt.setVisibility(View.VISIBLE);
        isVisible=true;
    }
    public void mapListeners()
    {
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        mMap.setOnMapLongClickListener(this);
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
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
        if(latitude == 52.15182309 && longitude == 19.42829922)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 4));
        else
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
            here = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Tu jesteś"));
        }
    }

    public static void updatePosition(double lat, double lng)
    {
        latitude = lat;
        longitude = lng;
        LatLng location = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
        if(here == null)
            here = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Tu jesteś"));
        else
        {
            here.setPosition(location);
        }
    }

    public static boolean isMapVisible()
    {
        return visible;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        System.out.println("IsVisible: "+ isVisible);
        if(!isVisible)
        {
            showObjects();
            mapLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    380));
            ll=latLng;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btCancel:
                break;
            case R.id.btOK:
                addMarker();
                saveMarker();
                break;
        }
        if(isVisible)
        {
            hideObjects();
            mapLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
        }
    }
    public void setMarker(double x, double y, String name)
    {
        mMap.addMarker(new MarkerOptions()
        .position(new LatLng(y,x))
        .title(name));
    }
    public void addMarker()
    {
        mMap.addMarker(new MarkerOptions()
                .position(ll)
                .title(interestingPlace.getText().toString()));
    }

    public void saveMarker()
    {
        CheckingConnection check_conn = new CheckingConnection(getActivity());
        check_conn.execute();
        while(!check_conn.isFinished()){
            try{
                Thread.sleep(10);
            }catch(Exception e){}
        }
        if(check_conn.isConnected())//jest net ->kod Artura
        {
            SavePlace sp = new SavePlace(getActivity());

            sp.execute(userID, interestingPlace.getText().toString(), String.valueOf(ll.latitude), String.valueOf(ll.longitude));
        }
        else{//ni ma neta-> kod Kamila
            String temp;
            try {
                FileOutputStream fos = getActivity().openFileOutput(userID+"", Context.MODE_APPEND);

                temp = ll.longitude + "," + ll.latitude + ";";
                fos.write(temp.getBytes());

                fos.close();
            } catch(IOException e){ e.printStackTrace();}
        }
    }

}