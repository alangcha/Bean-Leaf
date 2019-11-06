package com.syp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.syp.model.Item;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Order;
import com.syp.model.Cafe;
import com.syp.model.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CheckoutFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter adapter;
    private Singleton singleton;

    private TextView countTv;
    private Button checkoutBtn;
    private TextView subTotalTv;
    private TextView discountTv;
    private TextView taxTv;
    private TextView totalTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);
        mainActivity = (MainActivity) getActivity();
        singleton = Singleton.get(mainActivity);


        // Set up recycler view
        recyclerView = v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Query
        Query query = singleton.getDatabase().child("users").child(singleton.getUserId()).child("currentOrder");

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
        adapter = new FirebaseRecyclerAdapter<Item, CheckoutViewHolder>(options) {

            @Override
            public CheckoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_menu_row_item, parent, false);

                return new CheckoutViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(CheckoutViewHolder holder, final int position, Item item) {
                holder.setTitle(item.getName() + " | x" + item.getCount());
                holder.setPrice(Double.toString(item.getPrice()));
                holder.getRemoveBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query query = singleton.getDatabase().child("users").child(singleton.getUserId())
                                .child("currentOrder").child(item.getId());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int currCount = Integer.parseInt(dataSnapshot.child("count").getValue().toString());

                                if (currCount >=2 ) {
                                    Toast.makeText(mainActivity, "item: " + item.getName() + ", count: " + currCount , Toast.LENGTH_SHORT).show();
                                    DatabaseReference ref = singleton.getDatabase().child("users").child(singleton.getUserId())
                                            .child("currentOrder").child(item.getId()).child("count");

                                    ref.setValue(--currCount);
                                } else {
                                    Toast.makeText(mainActivity, "item removed", Toast.LENGTH_SHORT).show();
                                    DatabaseReference ref = singleton.getDatabase().child("users").child(singleton.getUserId())
                                            .child("currentOrder").child(item.getId());
                                    ref.removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }});
            }
        };

        // Get data
        mainActivity = (MainActivity) getActivity();

        // specify an adapter
        recyclerView.setAdapter(adapter);

        // Find views
        countTv = v.findViewById(R.id.SubTitle);
        checkoutBtn = v.findViewById(R.id.confirmBtn);
        subTotalTv = v.findViewById(R.id.priceTitle);
        discountTv = v.findViewById(R.id.discountTitle);
        taxTv = v.findViewById(R.id.taxTitle);
        totalTv = v.findViewById(R.id.totalTitle);

        // Calculate totals
        Query totalQuery = singleton.getDatabase().child("users").child(singleton.getUserId()).child("currentOrder");
        totalQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double subtotal = 0;
                int totalCount = 0;
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    double price = Double.parseDouble(child.child("price").getValue().toString());
                    int count = Integer.parseInt(child.child("count").getValue().toString());
                    subtotal += price * count;
                    totalCount += count;
                }
                countTv.setText(totalCount + " Items");
                subTotalTv.setText("$ " + String.format("%.2f", subtotal));
                discountTv.setText("$ " + String.format("%.2f", 0.0));
                taxTv.setText("$ " + String.format("%.2f", subtotal*0.08));
                totalTv.setText("$ " + String.format("%.2f", subtotal * 1.08));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference checkoutQuery = singleton.getDatabase().child("users").child(singleton.getUserId())
                        .child("currentOrder");
                checkoutQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    Map<String, Item> items = new HashMap<>();
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Order order = new Order();
                        for(DataSnapshot childSnapShot: dataSnapshot.getChildren()) {
                            items.put(childSnapShot.getKey(), childSnapShot.getValue(Item.class));
                        }
                        order.setCafe(singleton.getCurrentCafeId());
                        order.setUser(singleton.getUserId());
                        order.setTimestamp(System.currentTimeMillis());
                        order.setItems(items);

                        if(items.size() == 0) {
                            Toast.makeText(mainActivity, "There is no item in your cart.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Delete current order
                        checkoutQuery.removeValue();

                        // Insert order into user
                        DatabaseReference checkoutRef = singleton.getDatabase().child("users").child(singleton.getUserId())
                                .child("orders");
                        String id = checkoutRef.push().getKey();
                        order.setId(id);
                        checkoutRef.child(id).setValue(order);

                        // Insert order into cafe
                        checkoutRef = singleton.getDatabase().child("cafes").child(singleton.getCurrentCafeId())
                                .child("orders");
                        id = checkoutRef.push().getKey();
                        checkoutRef.child(id).setValue(order);

                        Toast.makeText(mainActivity, "Checkout Successful", Toast.LENGTH_SHORT).show();

                        NavDirections action = CheckoutFragmentDirections.actionCheckoutFragmentToMapFragment();
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        adapter.startListening();
        return v;
    }

}
