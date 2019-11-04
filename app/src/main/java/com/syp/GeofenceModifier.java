package com.syp;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.app.PendingIntent;
import android.content.Intent;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

//import com.syp.GeofenceBroadcastReceiveer;

public class GeofenceModifier extends AppCompatActivity {
    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;;
    public GeofenceModifier(AppCompatActivity activity) {
        geofenceList = new ArrayList<>();
        geofencingClient = LocationServices.getGeofencingClient(activity);
        this.createGeofence("test", 34.024885, -118.285940, (float)1000.0, (long)10000000000.0, true, true);
        this.geofencingClient.addGeofences(this.getGeofencingRequest(), this.getGeofencePendingIntent());
    }
    public void createGeofence(String id, double latitude, double longitude, float radius, long expDurationInMs, boolean activateOnEnter, boolean activateOnExit) {
        int transitionTypes = 0;
        if (activateOnEnter && !activateOnExit) {
            transitionTypes = Geofence.GEOFENCE_TRANSITION_ENTER;
        } else if (!activateOnEnter && activateOnExit) {
            transitionTypes = Geofence.GEOFENCE_TRANSITION_EXIT;
        } else if (activateOnEnter && activateOnExit) {
            transitionTypes = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT;
        }
        geofenceList.add(new Geofence.Builder()
            // Set the request ID of the geofence. This is a string to identify this
            // geofence.
            .setRequestId(id)
            .setCircularRegion(latitude, longitude, radius)
            .setExpirationDuration(expDurationInMs)
            .setTransitionTypes(transitionTypes)
            .build());
    }
    public GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(geofenceList);
        return builder.build();
    }
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, com.syp.GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }
}
