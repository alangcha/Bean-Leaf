package com.syp.ui;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Cafe;
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
        Cafe curCafe = Singleton.get(mainActivity).getCurrentMerchantCafe();

        shopName = v.findViewById(R.id.merchantshop_shopname);
        shopAddress = v.findViewById(R.id.merchantshop_shopaddress);
        shopHours = v.findViewById(R.id.merchantshop_shophours);
        shopHoursEdit = v.findViewById(R.id.merchantshop_shophours_edit);
        addItem = v.findViewById(R.id.merchantshop_add_item);
        image = v.findViewById(R.id.shopImage);
        changeImage = v.findViewById(R.id.shopChangeImage);

        shopName.setText(curCafe.get_name());
        shopAddress.setText(curCafe.get_address());
        shopHours.setText(curCafe.get_hours());
        shopAddress.setText(curCafe.get_address());
        shopHours.setText(curCafe.get_hours());
        image.setImageURI(curCafe.getImage());

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

                shopHours.setText(shopHours.getText());

                Cafe c = Singleton.get(mainActivity).getCurrentMerchantCafe();
                c.set_hours(shopHoursEdit.getText().toString());

                Singleton.get(mainActivity).editCafeHours(shopHoursEdit.getText().toString());

                edit.show();
                done.hide();
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
        recyclerView = v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Query
        Query query = FirebaseDatabase.getInstance().getReference().child("Shops");

        // Firebase Options
        FirebaseRecyclerOptions<Cafe> options =
                new FirebaseRecyclerOptions.Builder<Cafe>()
                        .setQuery(query, new SnapshotParser<Cafe>() {
                            @NonNull
                            @Override
                            public Cafe parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Cafe cafe = new Cafe();
                                cafe.set_name(snapshot.child("cafe_name").getValue().toString());
                                cafe.set_address(snapshot.child("cafe_address").getValue().toString());
                                // cafe.add_image(snapshot.child("cafe_image").getValue());
                                return cafe;
                            }
                        })
                        .build();


        //Firebase Recycler View
        adapter = new FirebaseRecyclerAdapter<Cafe, CafeViewHolder>(options) {
            @Override
            public CafeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_merchant_shop, parent, false);

                return new CafeViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(CafeViewHolder holder, final int position, Cafe item) {
                holder.setCafeName(item.get_name());
                holder.setCafeAddress(item.get_address());
                // holder.setCafeImage();
            }
        };

        // specify an adapter
        recyclerView.setAdapter(adapter);


        return v;
    }
}
