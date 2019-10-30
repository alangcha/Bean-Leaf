package com.beanleaf.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.syp.Cafe;
import com.syp.MapsActivity;
import com.syp.R;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    private ArrayList<Marker> markerArrList = new ArrayList<>();
    private static final LatLng MOUNTAIN_VIEW = new LatLng(37.4, -122.1);
    private ArrayList<Cafe> cafes;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get cafes
        final MapsActivity activity = (MapsActivity) getActivity();
        cafes = activity.getCafes();

        view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMinZoomPreference(6.0f);
                mMap.setMaxZoomPreference(14.0f);

                mMap.clear(); //clear old markers
                markerArrList.clear();

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(37.400403, -122.113402))
                        .zoom(10)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                // Index of marker
                for (int i = 0; i < cafes.size(); i++) {
                    Log.d("LATITUDE", Double.toString(cafes.get(i).get_latitude()));
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(cafes.get(i).get_latitude(), cafes.get(i).get_longitude())));
                    marker.setTag(i);
                    markerArrList.add(marker);
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        int index = (int) (marker.getTag());
                        activity.setCurrentCafeIndex(index);
                        NavDirections action = MapFragmentDirections.actionMapFragmentToCafeFragment();
                        Navigation.findNavController(view).navigate(action);
                        return false;
                    }
                });
            }
        });
        return view;
    }

}
