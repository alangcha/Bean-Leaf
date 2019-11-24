// Package
package com.syp.ui;

// Fragment imports
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// View Imports
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

// Firebase imports (Firebase required)
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

// Package imports
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Cafe;
import com.syp.model.Order;
import com.syp.model.Singleton;
import com.syp.model.User;

// ----------------------------------------------------------
// Fragment for page showing user orders, profile, and shops
// ----------------------------------------------------------
public class UserProfileFragment extends Fragment {

    // Activity and Inflater Variables
    private MainActivity mainActivity;
    private LayoutInflater layoutInflater;

    // View variables
    private View v;
    private Button addShop;
    private TextView name;
    private TextView email;
    private TextView gender;
    private RecyclerView userCafes;
    private RecyclerView userOrders;

    // ---------------------------------------
    // On Create (Fragment Override Required)
    // ---------------------------------------
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate view tih user fragment xml
        v = inflater.inflate(R.layout.fragment_user, container, false);

        // Assign each view to variables
        mainActivity = (MainActivity) getActivity();
        layoutInflater = inflater;

        // User Info Section
        name = v.findViewById(R.id.profileName);
        email = v.findViewById(R.id.profileEmail);
        gender = v.findViewById(R.id.profileGender);
        fetchUserInfo();

        // Recycle View, LayoutManager, & Init for User Shops
        userCafes = v.findViewById(R.id.myShopsRecycle);
        userCafes.setLayoutManager(new LinearLayoutManager(mainActivity));
        fetchUserCafes();

        // Add Shop Button
        addShop = v.findViewById(R.id.shopAddButton);
        setAddShopOnClickListener();

        // Recycle View, Layout Manager, & Init for User Orders
        userOrders = v.findViewById(R.id.myOrdersRecycle);
        userOrders.setLayoutManager(new LinearLayoutManager(mainActivity));
        fetchUserOrders();

        return v;
    }

    // ---------------------------------------
    // On Create (Fragment Override Required)
    // ---------------------------------------
    private void setAddShopOnClickListener(){

        // set OnClickListener for add shop button
        // ( Add Shop takes user from Profile Page -> Add Shop Page
        addShop.setOnClickListener((View v) -> {
            NavDirections action = UserProfileFragmentDirections.actionUserFragmentToAddShopFragment();
            Navigation.findNavController(v).navigate(action);
        });
    }

    // -------------------------------------------------------------
    // Set appropriate UI for user info text boxes with User object
    // -------------------------------------------------------------
    private void setUserInfo(User user){
        // Set appropriate views
        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        gender.setText(user.getGender());
    }

    // ------------------------------------------
    // Add FireBase Event Listener for User Info
    // ------------------------------------------
    private void fetchUserInfo(){

        // Get User Database Reference from firebase
        DatabaseReference userRef = Singleton.get(mainActivity).getDatabase()
                .child(Singleton.firebaseUserTag)
                .child(Singleton.get(mainActivity).getUserId());

        // Add Event Listener for spot in database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Convert to user
                User user =  dataSnapshot.getValue(User.class);

                if(user != null)
                    setUserInfo(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    // ---------------------------------------------------
    // Add FireBase Event Listener for List of User Cafes
    // ---------------------------------------------------
    private void fetchUserCafes(){

        // Get query from databse
        Query userCafeQueries = Singleton.get(mainActivity).getDatabase()
                .child(Singleton.firebaseUserTag)
                .child(Singleton.get(mainActivity).getUserId())
                .child(Singleton.firebaseCafeTag);

        // Firebase Option Builder to convert Data Snapshot to Cafe class
        FirebaseRecyclerOptions<Cafe> options = new FirebaseRecyclerOptions.Builder<Cafe>()
            .setQuery(userCafeQueries, Cafe.class)
            .build();

        // Firebase Create Cafe View Holder
        FirebaseRecyclerAdapter userCafeAdapter = new FirebaseRecyclerAdapter<Cafe, CafeViewHolder>(options) {
            @NonNull
            @Override
            public CafeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CafeViewHolder(layoutInflater.inflate(R.layout.fragment_merchant_shop, parent, false));
            }
            @Override
            protected void onBindViewHolder(@NonNull CafeViewHolder holder, final int position, @NonNull Cafe cafe) {
                holder.setCafeInfo(cafe, mainActivity, UserProfileFragmentDirections.actionUserFragmentToViewMerchantCafeFragment());
            }
        };

        // Set Adapter and start listening
        userCafes.setAdapter(userCafeAdapter);
        userCafeAdapter.startListening();

    }

    // ----------------------------------------------------
    // Add FireBase Event Listener for List of User Orders
    // ----------------------------------------------------
    private void fetchUserOrders(){

        // Get query from databse
        Query userOrdersQuery = Singleton.get(mainActivity).getDatabase()
                .child(Singleton.firebaseUserTag)
                .child(Singleton.get(mainActivity).getUserId())
                .child(Singleton.firebaseOrderTag);

        // Firebase Option Builder to convert Data Snapshot to Order class
        FirebaseRecyclerOptions<Order> optionsO = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(userOrdersQuery, Order.class)
                .build();

        // Firebase Create Cafe View Holder
        FirebaseRecyclerAdapter userOrdersAdapter = new FirebaseRecyclerAdapter<Order, RowUserOrder>(optionsO) {
            @NonNull
            @Override
            public RowUserOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RowUserOrder(layoutInflater.inflate(R.layout.fragment_user_order_row, parent, false));
            }
            @Override
            protected void onBindViewHolder(RowUserOrder holder, final int position, @NonNull Order order) {
                holder.setOrder(order);
            }
        };

        // specify an adapter
        userOrders.setAdapter(userOrdersAdapter);
        userOrdersAdapter.startListening();
    }
}