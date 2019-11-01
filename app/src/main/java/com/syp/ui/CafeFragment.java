package com.syp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.syp.Cafe;
import com.syp.MapsActivity;
import com.syp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

public class CafeFragment extends Fragment {
    private FloatingActionButton fabtn;
    Cafe cafe;
    MapsActivity mapsActivity;
    private TextView cafeNameTv;
    private ArrayList<Marker> markerArrList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cafe, container, false);

        fabtn = v.findViewById(R.id.btnCart);
        mapsActivity = (MapsActivity) getActivity();
        cafeNameTv = v.findViewById(R.id.cafe_name);

        cafe = mapsActivity.getCafeByPos(mapsActivity.getCurrentCafeIndex());
        cafeNameTv.setText(cafe.get_name());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.cafeMap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMinZoomPreference(6.0f);
                mMap.setMaxZoomPreference(14.0f);

                mMap.clear(); //clear old markers
                markerArrList.clear();

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(cafe.get_latitude(), cafe.get_longitude()))
                        .zoom(10)
                        .build();

                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(cafe.get_latitude(), cafe.get_longitude())));
                marker.setTag(0);
                markerArrList.add(marker);
            }
        });
        

        fabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = CafeFragmentDirections.actionCafeFragmentToCheckoutFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
        return v;

    }
}
