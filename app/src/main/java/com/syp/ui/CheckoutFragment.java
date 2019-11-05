package com.syp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    private TextView countTv;
    private TextView title;
    private Button checkoutBtn;
    int count = 0;

    private ArrayList<Item> items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);
        mainActivity = (MainActivity) getActivity();
        Order curOrder = Singleton.get(mainActivity).getCurrentOrder();


        // Set up recycler view
        recyclerView = v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        title = v.findViewById(R.id.Title);
        title.setText(curOrder.get_owner().get_name());

        // Query
        Query query = FirebaseDatabase.getInstance().getReference().child("orders");

        // Firebase Options
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(query, new SnapshotParser<Item>() {
                            @NonNull
                            @Override
                            public Item parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Item item = new Item();
                                item.set_name(snapshot.child("title").getValue().toString());
                                item.set_price(Double.parseDouble(snapshot.child("price").getValue().toString()));
                                item.set_id(snapshot.child("id").getValue().toString());
                                return item;
                            }
                        })
                        .build();

        // Firebase Recycler View
        adapter = new FirebaseRecyclerAdapter<Item, CheckoutViewHolder>(options) {
            @Override
            public CheckoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_menu_row_item, parent, false);

                return new CheckoutViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(CheckoutViewHolder holder, final int position, Item item) {
                Log.d("ITEM", item.get_name());
                holder.setTitle(item.get_name());
                holder.setPrice(Double.toString(item.getPrice()));
            }
        };

        // Get data
        mainActivity = (MainActivity) getActivity();
        items = mainActivity.getCurrentOrder().get_item_purchased();

        // specify an adapter
        recyclerView.setAdapter(adapter);

        // Set item count
        countTv = v.findViewById(R.id.SubTitle);
        if(mainActivity.getCurrentOrder()!=null) {
            count = mainActivity.getCurrentOrder().get_item_purchased().size();
        }
        countTv.setText(count + " Items");
        checkoutBtn = v.findViewById(R.id.confirmBtn);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Singleton.get(mainActivity).completeOrder();
                NavDirections action = CheckoutFragmentDirections.actionCheckoutFragmentToUserFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
        adapter.startListening();
        return v;
    }


}
