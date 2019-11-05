package com.syp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private View v;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate view
        v = inflater.inflate(R.layout.fragment_map, container, false);

        // Get data base reference
        ref = Singleton.get(mainActivity).getDatabase().getReference("Cafes ");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d("FIREBASE DATA",dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Ref to mainactivity
        mainActivity = (MainActivity) getActivity();

        // link views to variables
        infoBox = v.findViewById(R.id.cafe_infobox);
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
                        cafe.set_id(dataSnapshot.child("id").getValue().toString());
                        cafe.set_name(dataSnapshot.child("name").getValue().toString());
                        cafe.set_address(dataSnapshot.child("address").getValue().toString());
                        cafe.set_latitude((double) dataSnapshot.child("latitude").getValue());
                        cafe.set_longitude((double) dataSnapshot.child("longitude").getValue());

                        Marker marker = mMap.addMarker(
                                new MarkerOptions().position(new LatLng(cafe.get_latitude(), cafe.get_longitude()))
                                        .title(cafe.get_name()));
                        Log.d("ID", cafe.get_id());
                        marker.setTag(cafe.get_id());
                        Singleton.get(mainActivity).addCafeIfNotExist(cafe.get_id(), cafe);
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
                        return false;
                    }
                });
            }
        });

        return v;
    }

}
