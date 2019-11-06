package com.syp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.syp.model.Cafe;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Item;
import com.syp.model.Singleton;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class CafeFragment extends Fragment {
    private FloatingActionButton directionButton;
    MainActivity mainActivity;
    private TextView cafeName;
    private TextView cafeAddress;
    private TextView cafeHours;
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

        // Create map and load cafe from firebase
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.cafeMap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setMinZoomPreference(6.0f);
                mMap.setMaxZoomPreference(14.0f);

                mMap.clear(); //clear old markers

                // Get cafe from firebase
                DatabaseReference myRef = Singleton.get(mainActivity).getDatabase().child("cafes").child(Singleton.get(mainActivity).getCurrentCafeId());

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Cafe cafe =  dataSnapshot.getValue(Cafe.class);
                        Log.d("VALUE LISTENER", cafe.getName());


                        // load data into views
                        cafeName.setText(cafe.getName());
                        cafeAddress.setText(cafe.getAddress());
                        cafeHours.setText(cafe.getHours());

                        CameraPosition googlePlex = CameraPosition.builder()
                                .target(new LatLng(cafe.getLatitude(), cafe.getLongitude()))
                                .zoom(10)
                                .build();

                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(cafe.getLatitude(), cafe.getLongitude())));
                        marker.setTag(0);

                        directionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri gmmIntentUri = Uri.parse("geo:" + cafe.getLatitude()+"," + cafe.getLongitude() +
                                        "?q="+cafe.getName());
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                startActivity(mapIntent);
                            }});
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("CANCEL", "Failed to read value.", error.toException());
                    }
                });
            }
        });

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
                                Item item = snapshot.getValue(Item.class);
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
                holder.setBeverageName(item.getName());
                holder.setBeveragePrice("" + item.getPrice());
                holder.setCaffeineAmt(item.getCaffeine() + "");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Singleton.get(mainActivity).setCurrentItemId(item.getId());

                        // Navigate to OrderItemFragment
                        NavDirections action = CafeFragmentDirections.actionCafeFragmentToOrderItemFragment();
                        Navigation.findNavController(v).navigate(action);
                    }
                });
            }
        };

        // specify an adapter
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        return v;
    }
}
