package com.syp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Cafe;
import com.syp.model.Item;

public class AddShopFragment extends Fragment {

    public static int RESULT_LOAD_IMAGE_SHOP = 3;
    public static int RESULT_LOAD_IMAGE_REG = 4;

    private Cafe newCafe;
    private MainActivity mainActivity;
    private EditText shopName;
    private EditText shopAddress;
    private EditText shopHours;
    private Button addItem;
    private Button addCafeImage;
    private Button addRegistrationForm;
    private Button registerShop;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Item, MenuViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();
        View v = inflater.inflate(R.layout.fragment_add_shop, container, false);

        newCafe = mainActivity.getNewCafe();

        shopName = v.findViewById(R.id.addshop_shop_name);
        shopAddress = v.findViewById(R.id.addshop_shop_address);
        shopHours = v.findViewById(R.id.addshop_shop_hours);

        addItem = v.findViewById(R.id.addshop_add_item);
        addCafeImage = v.findViewById(R.id.addshop_add_cafe_images);
        addRegistrationForm = v.findViewById(R.id.addshop_add_registration_form_image);
        registerShop = v.findViewById(R.id.addshop_register_cafe);


        //Set up recycler view
        recyclerView = v.findViewById(R.id.addShop_currentItems);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Query
        Query query = FirebaseDatabase.getInstance().getReference().child("Menu");

        // Firebase Options
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(query, new SnapshotParser<Item>() {
                            @NonNull
                            @Override
                            public Item parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Item item = new Item();
                                item.set_name(snapshot.child("beverage_name").getValue().toString());
                                item.set_price(Double.parseDouble(snapshot.child("beverage_price").getValue().toString()));
                                item.set_id(snapshot.child("beverage_caffeine_amt").getValue().toString());
                                return item;
                            }
                        })
                        .build();


        //Firebase Recycler View
        adapter = new FirebaseRecyclerAdapter<Item, MenuViewHolder>(options) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_menu_row_item, parent, false);

                return new MenuViewHolder(view);
            }
            @NonNull
            @Override
            protected void onBindViewHolder(MenuViewHolder holder, final int position, Item item) {
                holder.setBeverageName(item.get_name());
                holder.setBeveragePrice(Double.toString(item.getPrice()));
//                holder.setCaffeineAmt(Double.toString(item.get_caffeine_amt_in_mg()));
//                holder.setBeverageImage();
            }
        };


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = AddShopFragmentDirections.actionAddShopFragmentToAddItemNewFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });

        addCafeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE_SHOP);
            }
        });

        addRegistrationForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE_REG);
            }
        });

        registerShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCafe.set_name(shopName.getText().toString());
                newCafe.set_address(shopAddress.getText().toString());
                newCafe.set_hours(shopHours.getText().toString());

                NavDirections action = AddShopFragmentDirections.actionAddShopFragmentToUserFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });

        return v;
    }
}
