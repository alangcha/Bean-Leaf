package com.syp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.syp.model.Cafe;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Database;
import com.syp.model.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class MapFragment extends Fragment {

    private DatabaseReference ref;
    private LinearLayout infoBox;
    private Button viewCafeButton;
    private TextView shopName;
    private TextView shopAddress;
    private TextView shopHours;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate view
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Ref to mainactivity
        mainActivity = (MainActivity) getActivity();

        // link views to variables
        infoBox = v.findViewById(R.id.cafe_infobox);
        shopName = v.findViewById(R.id.map_shopName);
        shopAddress = v.findViewById(R.id.map_shopAddress);
        shopHours = v.findViewById(R.id.map_shopTime);

        viewCafeButton = v.findViewById(R.id.view_cafe_button);
        viewCafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = MapFragmentDirections.actionMapFragmentToCafeFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });

        // Create Map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                // Map Type & Zoom
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMinZoomPreference(8.0f);
                mMap.setMaxZoomPreference(20.0f);

                // Clear Previous markers
                mMap.clear();

                ArrayList<Cafe> cafes = new ArrayList<>();

                // Camera settings & start spot
                CameraPosition googlePlex = CameraPosition.builder().target(new LatLng(37.400403, -122.113402)).zoom(12)
                        .build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                // Get Cafe and add markers
                DatabaseReference cafeRef = Singleton.get(mainActivity).getDatabase().child("cafes");
                cafeRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Cafe cafe = new Cafe();
                        cafe.setId(dataSnapshot.child("id").getValue().toString());
                        cafe.setName(dataSnapshot.child("name").getValue().toString());
                        cafe.setAddress(dataSnapshot.child("address").getValue().toString());
                        cafe.setLatitude((double) dataSnapshot.child("latitude").getValue());
                        cafe.setLongitude((double) dataSnapshot.child("longitude").getValue());

                        Marker marker = mMap.addMarker(
                                new MarkerOptions().position(new LatLng(cafe.getLatitude(), cafe.getLongitude()))
                                        .title(cafe.getName()));
                        Log.d("ID", cafe.getId());
                        marker.setTag(cafe.getId());
                        Singleton.get(mainActivity).addCafeIfNotExist(cafe.getId(), cafe);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // Set onClickListener for each marker
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String id = marker.getTag().toString();
                        Singleton.get(mainActivity).setCurrentCafeId(id);
                        marker.showInfoWindow();
                        infoBox.setVisibility(View.VISIBLE);

                        DatabaseReference ref = Singleton.get(mainActivity).getDatabase()
                            .child("cafes").child(Singleton.get(mainActivity).getCurrentCafeId());

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Cafe cafe = dataSnapshot.getValue(Cafe.class);
                                shopName.setText(cafe.getName());
                                shopAddress.setText(cafe.getAddress());
                                shopHours.setText(cafe.getHours());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        return false;
                    }
                });
            }
        });

        return v;
    }

}
