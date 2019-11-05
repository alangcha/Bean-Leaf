package com.syp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import com.google.android.material.navigation.NavigationView;

import com.syp.model.*;
import com.syp.model.Database;
import com.syp.ui.AddItemExistingFragment;
import com.syp.ui.AddItemNewFragment;
import com.syp.ui.AddShopFragment;
import com.syp.ui.MerchantShopFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private AppBarConfiguration mAppBarConfiguration;
    protected LocationManager locationManager;
    protected double latitude;
    protected double longitude;

    // Cafes
    ArrayList<Cafe> cafes;
    int currentCafeIndex;
    int currentItemIndex;
    User user;
    Order currentOrder;
    Cafe newCafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize singleton
        Singleton.get(this);

        // Insert fake data
//        Singleton.get(this).insertCafes();

        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivityForResult(i, 10);

        // TODO: CHECK IF USER VISITED A CAFE WHILE APP WAS CLOSED


        // Populate navigation bar with maps page
        setContentView(R.layout.activity_maps);

        // Get current latitude and longitude
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                            },
                            0);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        if (checkLocationPermission())
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, this);

        // Create navigation tab with drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Creating top level destinations in nav graph hierarchy
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapFragment, R.id.userFragment, R.id.statisticsFragment, R.id.checkoutFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OnActivityResult", "Success");

        // Image Code Range
        if (requestCode > 0 && requestCode < 6 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            ImageView imageView;
            if(resultCode == AddItemNewFragment.RESULT_LOAD_IMAGE_NEW)
                imageView = (ImageView) findViewById(R.id.additemnew_image);
            else if(resultCode == AddItemExistingFragment.RESULT_LOAD_IMAGE_EXISTING)
                imageView = (ImageView) findViewById(R.id.additemexisting_image);
            else if(resultCode == AddShopFragment.RESULT_LOAD_IMAGE_REG)
                imageView = (ImageView) findViewById(R.id.addshop_reg_image);
            else if(resultCode == MerchantShopFragment.RESULT_LOAD_IMAGE_CHANGESHOP)
                imageView = (ImageView) findViewById(R.id.shopImage);
            else
                imageView = (ImageView) findViewById(R.id.addshop_cafe_image);

//            if(imageView != null){
//                dataInstance.uploadFile(selectedImage, getFileExtension(selectedImage));
//            }

            Picasso.get().load(selectedImage).into(imageView);
        }

        if(requestCode == 10 && resultCode == RESULT_OK && null != data){
            String email = data.getStringExtra("email");
            String displayName = data.getStringExtra("displayName");
            // Query database for email
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    // -----------------------------------------------------------
    // Functions for retrieving and pushing data to main
    // -----------------------------------------------------------

    public void addUser(User u){
        user = u;
        Database.createUser(u);
    }

    public void setUser(User u){
        user = u;
    }

    public void addOrder(){
        Database.addOrderToUser(currentOrder, user);
        currentOrder = new Order();
        currentOrder.setUser(user);
    }

    public void editUserEmail(String email){
        //user.set_email(email);
        Database.editUserEmail(email);
    }

    public void editUserPassword(String password){
        //user.set_password(password);
        Database.editUserPassword(password);
    }

    public ArrayList<Cafe> getCafes() {
        return cafes;
    }

    public Cafe getCafeByPos(int pos) {
        return cafes.get(pos);
    }

    public void setCurrentCafeIndex(int index) {
        this.currentCafeIndex = index;
    }

    public Integer getCurrentCafeIndex() {
        return currentCafeIndex;
    }

    public int getCurrentItemIndex() {
        return currentItemIndex;
    }

    public void setCurrentItemIndex(int index){ this.currentItemIndex = index; }

    public Order getCurrentOrder(){ return currentOrder; }

    public void addItemToOrder(ArrayList<Item> items) {
        for(Item i : items){
            currentOrder.add_item(i);
        }
    }

    public Cafe getNewCafe(){
        return newCafe;
    }

    public void createNewCafe(){
        newCafe.set_owner(user);
    }

}
