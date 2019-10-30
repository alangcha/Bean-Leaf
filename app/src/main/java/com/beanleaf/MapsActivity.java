package com.beanleaf;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements LocationListener {

    private BottomNavigationView bottom_nav_view;
    private NavController navController;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected double latitude;
    protected double longitude;
    protected boolean gps_enabled;
    protected boolean network_enabled;
    TextView txtLat;
    String provider;
    String lat;

    // Cafes
    ArrayList<Cafe> cafes = new ArrayList<>();
    int index = -1;
    ArrayList<Item> order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Current lng & lat
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    0);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, this);
        }

        // Fake data
        User user1 = new User();
        user1.set_username("Jackie");
        User user2 = new User();
        user2.set_username("Ethan");

        Cafe cafe1 = new Cafe();
        cafe1.set_latitude(37.4219999);
        cafe1.set_longitude(-122.0862462);
        cafe1.set_name("Dong Cha");
        cafe1.set_owner(user1);
        cafe1.set_rating(5);

        Cafe cafe2 = new Cafe();
        cafe2.set_latitude(37.4629101);
        cafe2.set_longitude(-122.2449094);
        cafe2.set_name("Ethan's TEA");
        cafe2.set_owner(user2);
        cafe2.set_rating(5);

        for(int i = 0; i < 10; i++){
            Item temp = new Item();
            temp.set_name(Integer.toString(i));
            temp.set_price(10+i);
            temp.set_available(true);
            temp.update_rating(4.5);
            cafe1.get_menu().add_item(temp);
        }

        for(int i = 0; i < 10; i++){
            Item temp = new Item();
            temp.set_name(Integer.toString(i+5));
            temp.set_price(10+i+5);
            temp.set_available(true);
            temp.update_rating(5);
            cafe1.get_menu().add_item(temp);
        }

        Order temp = new Order();
        temp.set_consumer(user1);
        temp.set_owner(cafe1);
        user1.add_order(temp);
        for(int i = 0 ; i < 5; i++) {
            temp.add_item(cafe1.get_menu().get_items().get(i));
        }

        order = temp.get_item_purchased();

        cafes.add(cafe1);
        cafes.add(cafe2);

        // Bottom nav
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        bottom_nav_view = findViewById(R.id.bottom_nav_bar);
        NavigationUI.setupWithNavController(bottom_nav_view, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination,
                    @Nullable Bundle arguments) {
                Log.d("Navigation Changed", destination.getLabel().toString());
                String label = destination.getLabel().toString();
                if (label.equals("fragment_map") || label.equals("fragment_user")) {
                    Log.d("HERE", "here");
                    bottom_nav_view.getMenu().setGroupCheckable(0, true, true);
                } else {
                    bottom_nav_view.getMenu().setGroupCheckable(0, false, true);
                }
            }
        });

    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    public ArrayList<Cafe> getCafes() {
        return cafes;
    }

    public Cafe getCafeByPos(int pos) {
        return cafes.get(pos);
    }

    public void setCurrentCafeIndex(int index) {
        this.index = index;
    }

    public Integer getCurrentCafeIndex() {
        return index;
    }

    public ArrayList<Item> getOrder() {
        return order;
    }
}
