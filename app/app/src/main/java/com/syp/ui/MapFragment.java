// Package
package com.syp.ui;

// View Imports
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

// Firebase GeoLocation Imports
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;

// Google GeoLocation Imports
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

// Firebase Database Imports
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

// Internal Class Imports
import com.syp.IOnLoadLocationListener;
import com.syp.MyLatLng;
import com.syp.model.Cafe;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Singleton;

// Data Structure Imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// -----------------------------------------------
// Fragment for showing map and markers for cafes
// -----------------------------------------------
public class MapFragment extends Fragment implements GeoQueryDataEventListener, IOnLoadLocationListener {

    private static final int REQUEST_CODE = 101;

    private LinearLayout infoBox;
    private Button viewCafeButton;
    private TextView shopName;
    private TextView shopAddress;
    private TextView shopHours;
    private MainActivity mainActivity;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Location currentLocation;
    private LocationCallback locationCallback;
    private GeoFire geoFire;
    private Marker currentUser;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private IOnLoadLocationListener listener;
    private List<LatLng> cafeLocations;
    private List<Marker> markers;
    private GeoQuery geoQuery;
    private DatabaseReference myLocationRef;
    private Button TESTinvisibleRedMarkerButton;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate View
        v = inflater.inflate(R.layout.fragment_map, container, false);

        // Get Main Activity
        mainActivity = (MainActivity) getActivity();

        // Assign views to variables
        infoBox = v.findViewById(R.id.cafe_infobox);
        shopName = v.findViewById(R.id.map_shopName);
        shopAddress = v.findViewById(R.id.map_shopAddress);
        shopHours = v.findViewById(R.id.map_shopTime);
        TESTinvisibleRedMarkerButton = v.findViewById(R.id.TESTinvisibleRedMarker);
        addTESTInvisibleRedMarkerButtonOnClick();

        // View Cafe Button & On Click Listener
        viewCafeButton = v.findViewById(R.id.view_cafe_button);
        setViewCafeOnClickListener();

        // Create Map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((GoogleMap map) -> {

            // Set map & settings
            mMap = map;
            setMapSettings();

            fetchCafes();
            setAllMarkerOnClickListeners();

            // Get user permission
            Dexter.withActivity(mainActivity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        buildLocationRequest();
                        buildLocationCallback();
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mainActivity);
                        fetchLastLocation();

                        listener = MapFragment.this;
                        settingGeoFire();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {}
                }).check();
        });
        return v;
    }

    private void setMapSettings(){

        // Clear Previous markers
        mMap.clear();

        // Map Type & Zoom
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setMapCamera();
        mMap.setMyLocationEnabled(true);
    }

    private void setMapCamera(){

        // Set max min
        mMap.setMinZoomPreference(8.0f);
        mMap.setMaxZoomPreference(20.0f);

        // Camera settings & start spot
        CameraPosition googlePlex = CameraPosition.builder()
                .target(getUserLatitudeLongitude()).zoom(12)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
    }

    //TODO get user location dynamically
    private LatLng getUserLatitudeLongitude(){
        //fetchLastLocation();
        return new LatLng(37.400403, -122.113402);
    }

    private void setViewCafeOnClickListener(){
        viewCafeButton.setOnClickListener((View v) ->
            Navigation.findNavController(v).navigate(MapFragmentDirections.actionMapFragmentToCafeFragment())
        );
    }

    private void fetchCafes(){

        // Get Database Reference to cafes
        DatabaseReference cafeRef = Singleton.get(mainActivity).getDatabase()
                .child("cafes");

        // Add Listener when info is recieved or changed
        cafeRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, MyLatLng> latLngList = new HashMap<>();

                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {


                    MyLatLng latLng = locationSnapshot.getValue(MyLatLng.class);
                    Cafe cafe = locationSnapshot.getValue(Cafe.class);

                    latLngList.put(locationSnapshot.getKey(), latLng);

                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(cafe.getLatitude(), cafe.getLongitude())).title(cafe.getName()));
                    marker.setTag(cafe.getId());
                    marker.showInfoWindow();
                    markers.add(marker);
                }
                //listener.onLoadLocationSuccess(latLngList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    private void setAllMarkerOnClickListeners(){

        // Set onClickListener for each marker
        mMap.setOnMarkerClickListener((Marker marker)->{
            Singleton.get(mainActivity).setCurrentCafeId((String) marker.getTag());
            showInfoBox();
            return false;
        });
    }

    private void addTESTInvisibleRedMarkerButtonOnClick(){
        TESTinvisibleRedMarkerButton.setOnClickListener((View v)->{
            Singleton.get(mainActivity).setCurrentCafeId((String) markers.get(0).getTag());
            showInfoBox();
        });
    }



    private void showInfoBox(){
        fetchCafeInfo();
        infoBox.setVisibility(View.VISIBLE);
    }

    private void fetchCafeInfo(){
        DatabaseReference ref = Singleton.get(mainActivity).getDatabase()
                .child("cafes").child(Singleton.get(mainActivity).getCurrentCafeId());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Get cafe as snapshot
                Cafe cafe = dataSnapshot.getValue(Cafe.class);

                // Check cafe null
                if(cafe == null)
                    return;

                // Set cafe info
                setCafeInfo(cafe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void setCafeInfo(Cafe cafe){
        shopName.setText(cafe.getName());
        shopAddress.setText(cafe.getAddress());
        shopHours.setText(cafe.getHours());
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
            lastLocation = locationResult.getLastLocation();
            addUserMarker();
            }
        };
    }

    private void addUserMarker() {
        if (lastLocation != null) {
            geoFire.setLocation("You", new GeoLocation(lastLocation.getLatitude(),
                    lastLocation.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (currentUser != null) currentUser.remove();
                    currentUser = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lastLocation.getLatitude(),
                                    lastLocation.getLongitude()))
                            .title("You"));

                    mMap.animateCamera(CameraUpdateFactory
                            .newLatLngZoom(currentUser.getPosition(), 12.0f));
                }
            });
        }
    }

    @Override
    public void onLoadLocationSuccess(HashMap<String, MyLatLng> latLngs) {
        cafeLocations = new ArrayList<>();
        for (Map.Entry<String, MyLatLng> myLatLng : latLngs.entrySet()) {
            LatLng convert = new LatLng(myLatLng.getValue().getLatitude(), myLatLng.getValue().getLongitude());
            cafeLocations.add(convert);
        }
        addUserMarker();
        addCircleArea();
    }

    private void addCircleArea() {
        if (geoQuery != null) {
            //geoQuery.removeGeoQueryEventListener(this);
            geoQuery.removeAllListeners();
        }
        for (LatLng latLng : cafeLocations) {
            mMap.addCircle(new CircleOptions().center(latLng)
                    .radius(10)
                    .strokeColor(Color.BLUE)
                    .fillColor(0x220000FF)
                    .strokeWidth(5.0f)
            );

            //Creates GeoQuery when user is in cafe location
            geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), 0.1f);
            geoQuery.addGeoQueryDataEventListener(mainActivity);
        }
    }

    private void settingGeoFire() {
        myLocationRef = FirebaseDatabase.getInstance().getReference("MyLocation");
        geoFire = new GeoFire(myLocationRef);
    }

    @Override
    public void onLoadLocationFailure(String message) {
    }

    @Override
    public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
        for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()){
            Cafe cafe = locationSnapshot.getValue(Cafe.class);
            Singleton.get(mainActivity).setUserId(cafe.getId());
        }
        NavDirections action = MapFragmentDirections.actionMapFragmentToCafeFragment();
        Navigation.findNavController(v).navigate(action);
    }

    @Override
    public void onDataExited(DataSnapshot dataSnapshot) {
        sendNotification("USER", dataSnapshot.getKey() + "%s left the cafe.");
        Intent i  = new Intent(mainActivity, FinishOrderPopUp.class);
        startActivity(i);
    }

    @Override
    public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {
        sendNotification("USER", dataSnapshot.getKey() + "%s moved within the cafe.");
    }

    @Override
    public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {
        sendNotification("USER", dataSnapshot.getKey() + "%s changed the cafe.");
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
    }

    public void sendNotification(String title, String content) {


        String NOTIFICATION_CHANNEL_ID = "cafe_multiple_location";
        NotificationManager notificationManager = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // Configuration
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mainActivity, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        Notification notification = builder.build();
        notificationManager.notify(new Random().nextInt(), notification);
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mainActivity, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                    // Place user market on the map
//                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
//                    mMap.addMarker(markerOptions);
//                    Log.d("asdf", currentLocation.getLatitude() + "" + currentLocation.getLongitude());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }
}
