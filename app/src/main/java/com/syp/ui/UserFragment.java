package com.syp.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Cafe;
import com.syp.model.Item;
import com.syp.model.Order;
import com.syp.model.Singleton;
import com.syp.model.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

// hardcoded data:

public class UserFragment extends Fragment {

    private MainActivity mainActivity;
    private RecyclerView recyclerView;
    private RecyclerView orderRecycleView;

    private RecyclerView.LayoutManager layoutManagerShops;
    private RecyclerView.LayoutManager layoutManagerOrders;

    private FirebaseRecyclerAdapter adapter;
    private FirebaseRecyclerAdapter adapterO;

    private Button addShop;
    private TextView name;
    private TextView email;
    private TextView gender;
    private List<String> userCafeIds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();
        View v = inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = v.findViewById(R.id.myShopsRecycle);
        layoutManagerShops = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManagerShops);

        addShop = v.findViewById(R.id.shopAddButton);
        name = v.findViewById(R.id.profileName);
        email = v.findViewById(R.id.profileEmail);
        gender = v.findViewById(R.id.profileGender);

        addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to OrderItemFragment
                NavDirections action = UserFragmentDirections.actionUserFragmentToAddShopFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });

        // Get cafe from firebase
        DatabaseReference myRef = Singleton.get(mainActivity).getDatabase().child("users")
                .child(Singleton.get(mainActivity).getUserId());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user =  dataSnapshot.getValue(User.class);
                name.setText(user.getDisplayName());
                Toast.makeText(mainActivity, user.getEmail(), Toast.LENGTH_SHORT).show();
                email.setText(user.getEmail());
                gender.setText("Male");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = Singleton.get(mainActivity).getDatabase().child("users").child(Singleton.get(mainActivity).getUserId()).child("cafes");
        // Firebase Options
        FirebaseRecyclerOptions<Cafe> options =
                new FirebaseRecyclerOptions.Builder<Cafe>()
                        .setQuery(query, new SnapshotParser<Cafe>() {
                            @NonNull
                            @Override
                            public Cafe parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Cafe cafe = snapshot.getValue(Cafe.class);
                                return cafe;
                            }
                        })
                        .build();

        // Firebase Recycler View
        adapter = new FirebaseRecyclerAdapter<Cafe, CafeViewHolder>(options) {
            @NonNull
            @Override
            public CafeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_merchant_shop, parent, false);

                return new CafeViewHolder(view);
            }
            @NonNull
            @Override
            protected void onBindViewHolder(CafeViewHolder holder, final int position, Cafe cafe) {
                holder.setCafeName(cafe.getName());
                ImageButton b = holder.getEditButton();
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Singleton.get(mainActivity).setCurrentCafeId(cafe.getId());
                        Toast.makeText(mainActivity, "name: " + cafe.getName() + ", id: " + Singleton.get(mainActivity).getCurrentItemId(), Toast.LENGTH_SHORT).show();

                        // Navigate to OrderItemFragment
                        NavDirections action = UserFragmentDirections.actionUserFragmentToMerchantShopFragment();
                        Navigation.findNavController(v).navigate(action);
                    }
                });
            }
        };

        // specify an adapter
        recyclerView.setAdapter(adapter);
        adapter.startListening();







        orderRecycleView = v.findViewById(R.id.myOrdersRecycle);
        layoutManagerOrders = new LinearLayoutManager(getActivity());
        orderRecycleView.setLayoutManager(layoutManagerOrders);

        Query queryO = Singleton.get(mainActivity).getDatabase().child("users").child(Singleton.get(mainActivity).getUserId()).child("orders");
        // Firebase Options
        FirebaseRecyclerOptions<Order> optionsO =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(queryO, new SnapshotParser<Order>() {
                            @NonNull
                            @Override
                            public Order parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Order order = snapshot.getValue(Order.class);
                                return order;
                            }
                        })
                        .build();

        // Firebase Recycler View
        adapterO = new FirebaseRecyclerAdapter<Order, OrderCell>(optionsO) {

            @NonNull
            @Override
            public OrderCell onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_cell, parent, false);
                return new OrderCell(view);
            }
            @NonNull
            @Override
            protected void onBindViewHolder(OrderCell holder, final int position, Order order) {

                holder.setOrderCafeName(order.getCafe());

                SimpleDateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy");

                holder.setOrderCafeDate(dateFormat.format(order.getTimestampAsDate()));
                holder.setOrderPrice(Double.toString(order.getTotalSpent()));
                holder.setOrderCaffeine(Double.toString(order.getTotalCaffeine()));
            }
        };

        // specify an adapter
        orderRecycleView.setAdapter(adapterO);
        adapterO.startListening();

        return v;
    }

    private boolean isEmailFormat(String s){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (s == null)
            return false;
        return pat.matcher(s).matches();
    }
}