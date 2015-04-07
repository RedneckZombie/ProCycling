package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by kamilos on 2015-03-28.
 */


//po wybraniu z menu opcji nagrywaj, odpalić tą klasę w nowym wątku
// dodać do onLocationChanged wywołanie metody statycznej z MapActivity


public class RecordRoute
{
    // Acquire a reference to the system Location Manager
    LocationManager locationManager ;
    double longitude = -1, latitude = -1, altitude = -1;
    Context context;

    public RecordRoute(Context context)
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6, 10, locationListener);

        this.context = context;
    }

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {

        // Called when a new location is found by the network location provider.
        public void onLocationChanged(Location location) {

            longitude = location.getLongitude();
            latitude = location.getLatitude();
            altitude = location.getAltitude();

            //Start MyIntentService
            Intent intentMyIntentService = new Intent(context, MyIntentService.class);
            context.startService(intentMyIntentService);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };
}
