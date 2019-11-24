// Pacakge
package com.syp.ui;

// View & Nav Imports
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Google Map Imports
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

// Firebase Imports
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

// Package Class Imports
import com.syp.model.Cafe;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Item;
import com.syp.model.Singleton;



// ----------------------------------------------------------
// Fragment for viewing specified merchant shop & statistics
// ----------------------------------------------------------
public class ViewMerchantCafeFragment extends Fragment {

    // Main Activity & Layout Inflater
    MainActivity mainActivity;
    LayoutInflater layoutInflater;


    // Views
    private TextView cafeName;
    private TextView cafeAddress;
    private TextView cafeHours;
    private TextView cafeTotalSales;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private GoogleMap mMap;
    private ImageButton editButton;
    private View v;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate view
        v = inflater.inflate(R.layout.fragment_merchant_cafe, container, false);

        // Main Activity & Inflater
        mainActivity = (MainActivity) getActivity();
        layoutInflater = inflater;


        // Connect Views to variables
        cafeName = v.findViewById(R.id.cafeEditCafeName);
        cafeAddress = v.findViewById(R.id.cafeEditCafeAddress);
        cafeHours = v.findViewById(R.id.cafeEditCafeHours);
        cafeTotalSales = v.findViewById(R.id.cafeEditTotalSales);

        // Edit Button & On Click
        editButton = v.findViewById(R.id.cafeEditEditButton);
        setEditCafeOnClickListener();

        //Set up recycler view
        recyclerView = v.findViewById(R.id.cafeEditUserCafes);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        fetchMerchantCafeItems();

        // Create map and load cafe from firebase
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.cafeEditMap);
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

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("CANCEL", "Failed to read value.", error.toException());
                    }
                });
            }
        });



        return v;
    }

    private void setEditCafeOnClickListener(){
        editButton.setOnClickListener((View v) -> {
            NavDirections action = ViewMerchantCafeFragmentDirections.actionViewMerchantCafeFragmentToMerchantShopFragment();
            Navigation.findNavController(v).navigate(action);
        });
    }

    private void fetchMerchantCafeItems(){
        // Merchant Cafe Items Query
        Query merchantCafeItemsQuery = Singleton.get(mainActivity).getDatabase()
            .child(Singleton.firebaseCafeTag)
            .child(Singleton.get(mainActivity).getCurrentCafeId())
            .child(Singleton.firebaseItemsTag);

        // Firebase Options
        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>()
            .setQuery(merchantCafeItemsQuery, Item.class)
            .build();


        // Firebase Recycler View
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Item, RowMerchantItemStatisticsFragment>(options) {
            @NonNull
            @Override
            public RowMerchantItemStatisticsFragment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RowMerchantItemStatisticsFragment(layoutInflater.inflate(R.layout.fragment_row_merchant_item_statistics, parent, false));
            }
            @NonNull
            @Override
            protected void onBindViewHolder(RowMerchantItemStatisticsFragment holder, final int position, @NonNull Item item) {
                holder.setCafeItemStatisticsInfo(item, mainActivity, null);
            }
        };

        // specify an adapter
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
