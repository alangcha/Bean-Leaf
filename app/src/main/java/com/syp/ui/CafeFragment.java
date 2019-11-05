package com.syp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.syp.model.Cafe;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Item;
import com.syp.model.Singleton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class CafeFragment extends Fragment {
    private FloatingActionButton directionButton;
    Cafe cafe;
    MainActivity mainActivity;
    private TextView cafeName;
    private TextView cafeAddress;
    private TextView cafeHours;
    private ArrayList<Marker> markerArrList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Item, MenuViewHolder> adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cafe, container, false);

        // Store views
        directionButton = v.findViewById(R.id.btnDirection);
        mainActivity = (MainActivity) getActivity();
        cafeName = v.findViewById(R.id.cafe_name);
        cafeAddress = v.findViewById(R.id.cafe_address);
        cafeHours = v.findViewById(R.id.cafe_hours);

        // get cafe to show
        cafe = Singleton.get(mainActivity).getCurrentCafe();

        // load data into views
        cafeName.setText(cafe.get_name());
        cafeAddress.setText(cafe.get_address());
        cafeHours.setText(cafe.get_hours());

        // Create map
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

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:" + cafe.get_latitude()+"," + cafe.get_longitude() +
                        "?q="+cafe.get_name());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
        }});

        //Set up recycler view
        recyclerView = v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Query
        Query query = Singleton.get(mainActivity).getDatabase().child("cafes").child(Singleton.get(mainActivity).getCurrentCafeId()).child("items");

        // Firebase Options
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(query, new SnapshotParser<Item>() {
                            @NonNull
                            @Override
                            public Item parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Log.d("ITEMS","ADDED");
                                Item item = new Item();
                                item.set_name(snapshot.child("name").getValue().toString());
                                item.set_price(Double.parseDouble(snapshot.child("price").getValue().toString()));
                                item.set_caffeine(Double.parseDouble(snapshot.child("caffeine").getValue().toString()));
                                item.set_id(snapshot.child("id").getValue().toString());
                                return item;
                            }
                        })
                        .build();


        // Firebase Recycler View
        adapter = new FirebaseRecyclerAdapter<Item, MenuViewHolder>(options) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_cafe_menu_item, parent, false);

                return new MenuViewHolder(view);
            }
            @NonNull
            @Override
            protected void onBindViewHolder(MenuViewHolder holder, final int position, Item item) {
                holder.setBeverageName(item.get_name());
                holder.setBeveragePrice("$ " + item.getPrice());
                holder.setCaffeineAmt(item.get_caffeine() + " mg of caffeine");
            }
        };


        // specify an adapter
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return v;
    }
}
