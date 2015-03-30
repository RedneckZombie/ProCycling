package com.example.alien.myapplication1.tracks;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by kamilos on 2015-03-28.
 */
public class RecordRoute
{
    // Acquire a reference to the system Location Manager
    // LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            //TODO
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    // Register the listener with the Location Manager to receive location updates
    // przy prędkości 30km/h w 6sekund przejedzie 50 metrów

    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6, 10, locationListener);
}
