package com.syp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Cafe;
import com.syp.model.Item;
import com.syp.model.Singleton;

public class MerchantShopFragment extends Fragment {

    public static int RESULT_LOAD_IMAGE_CHANGESHOP = 5;
    private MainActivity mainActivity;
    private TextView shopName;
    private TextView shopAddress;
    private TextView shopHours;
    private EditText shopHoursEdit;
    private Button addItem;
    private FloatingActionButton done;
    private FloatingActionButton edit;
    private RecyclerView recyclerView;
    private ImageView image;
    private Button changeImage;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        View v = inflater.inflate(R.layout.fragment_merchantshop, container, false);

        shopName = v.findViewById(R.id.merchantshop_shopname);
        shopAddress = v.findViewById(R.id.merchantshop_shopaddress);
        shopHours = v.findViewById(R.id.merchantshop_shophours);
        shopHoursEdit = v.findViewById(R.id.merchantshop_shophours_edit);
        addItem = v.findViewById(R.id.merchantshop_add_item);
        image = v.findViewById(R.id.shopImage);
        changeImage = v.findViewById(R.id.shopChangeImage);

        String userID = Singleton.get(mainActivity).getUserId();
        String cafeID = Singleton.get(mainActivity).getCurrentCafeId();
        DatabaseReference ref = Singleton.get(mainActivity).getDatabase().child("users").child(userID)
                .child("cafes").child(cafeID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Cafe curCafe = dataSnapshot.getValue(Cafe.class);

                shopName.setText(curCafe.getName());
                shopAddress.setText(curCafe.getAddress());
                shopHours.setText(curCafe.getHours());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit = v.findViewById(R.id.btnEditShop);
        done = v.findViewById(R.id.btnDoneShop);
        done.hide();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopHours.setVisibility(View.GONE);
                shopHoursEdit.setVisibility(View.VISIBLE);
                edit.hide();
                done.show();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopHours.setVisibility(View.VISIBLE);
                shopHoursEdit.setVisibility(View.GONE);
                boolean failure = false;

                if(shopHoursEdit.getText().toString().trim().length() == 0){
                    shopHoursEdit.setText("");
                    shopHoursEdit.setHint("Invalid Number");
                    shopHoursEdit.setHintTextColor(Color.RED);
                    failure = true;
                }
                else{
                    shopHours.setText(shopHoursEdit.getText());
                    Singleton.get(mainActivity).getDatabase()
                            .child("users").child(Singleton.get(mainActivity).getUserId())
                            .child("cafes").child(Singleton.get(mainActivity).getCurrentCafeId())
                            .child("hours").setValue(shopHoursEdit.getText().toString());

                    Singleton.get(mainActivity).getDatabase()
                            .child("cafes").child(Singleton.get(mainActivity).getCurrentCafeId())
                            .child("hours").setValue(shopHoursEdit.getText().toString());
                }

                if(!failure){
                    edit.show();
                    done.hide();
                }

                InputMethodManager imm = (InputMethodManager) done.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE_CHANGESHOP);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = MerchantShopFragmentDirections.actionMerchantShopFragmentToAddItemExistingFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });


        //Set up recycler view
        recyclerView = v.findViewById(R.id.recycler_view_merchantshopitems);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DatabaseReference cafeRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userID)
                .child("cafes").child(cafeID);

        cafeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Cafe mCafe = dataSnapshot.getValue(Cafe.class);
                shopName.setText(mCafe.getName());
                shopAddress.setText(mCafe.getAddress());
                shopHours.setText(mCafe.getHours());
                Log.d("CAFE NAME", mCafe.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Query
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userID)
                .child("cafes").child(cafeID)
                .child("items");

        // Firebase Options
        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(query, new SnapshotParser<Item>() {
                            @NonNull
                            @Override
                            public Item parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Item item = snapshot.getValue(Item.class);
                                Log.d("ITEM NAME", item.getName());
                                return item;
                            }
                        })
                        .build();


        //Firebase Recycler View
        adapter = new FirebaseRecyclerAdapter<Item, MenuViewHolder>(options) {
            @Override
            public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_cafe_menu_item, parent, false);

                return new MenuViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(MenuViewHolder holder, final int position, Item item) {
                holder.setBeverageName(item.getName());
                holder.setBeveragePrice(""+item.getPrice());
                holder.setCaffeineAmt(item.getCaffeine()+"");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Singleton.get(mainActivity).setCurrentItemId(item.getId());

                        // Navigate to OrderItemFragment
                        NavDirections action = MerchantShopFragmentDirections.actionMerchantShopFragmentToItemEditFragment();
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
