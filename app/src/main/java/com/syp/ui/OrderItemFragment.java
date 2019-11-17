package com.syp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.syp.ExceedCaffeineActivity;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Cafe;
import com.syp.model.Item;
import com.syp.model.Singleton;
import com.syp.model.User;

import java.util.ArrayList;
import java.util.Map;

public class OrderItemFragment extends Fragment {

    private MainActivity mainActivity;
    private ImageView itemImage;
    private TextView itemTitle;
    private TextView itemPrice;
    private TextView itemCaffine;
    private ArrayList<CheckBox> iceButtons;
    private ArrayList<CheckBox> sugarButtons;
    private ArrayList<CheckBox> toppingsButtons;
    private Button addToCartButton;
    private ElegantNumberButton stepper;
    private Singleton singleton;
    private Item currentItem;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orderitem, container, false);
        mainActivity = (MainActivity) getActivity();
        singleton = Singleton.get(mainActivity);


        // Get current item from database
        DatabaseReference cafeRef = Singleton.get(mainActivity).getDatabase().child("cafes").child(Singleton.get(mainActivity).getCurrentCafeId()).child("items").child(Singleton.get(mainActivity).getCurrentItemId());
        cafeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentItem = dataSnapshot.getValue(Item.class);
                // load data into views
                itemTitle.setText(currentItem.getName());
                itemPrice.setText((Double.toString(currentItem.getPrice())));
                itemCaffine.setText(((currentItem.getCaffeine()) + " mg of caffeine"));

                addToCartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference orderRef = singleton.getDatabase().child("users").child(singleton.getUserId())
                                .child("currentOrder").child(currentItem.getId());
                        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int stepperCount = Integer.parseInt(stepper.getNumber());

                                if(stepperCount <= 0) {
                                    return;
                                };

                                if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                                        dataSnapshot.getChildren().iterator().hasNext()) {
                                    Toast.makeText(mainActivity, "Current item exists", Toast.LENGTH_SHORT).show();
                                    DatabaseReference ref = singleton.getDatabase().child("users").child(singleton.getUserId())
                                            .child("currentOrder").child(currentItem.getId()).child("count");

                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            int itemCount = Integer.parseInt(dataSnapshot.getValue().toString());
                                            ref.setValue(itemCount + stepperCount);
                                            Toast.makeText(mainActivity, stepperCount + " \"" + currentItem.getName() + "\" " + "added to your order", Toast.LENGTH_SHORT).show();

                                            NavDirections action = OrderItemFragmentDirections.actionOrderItemFragmentToCafeFragment();
                                            Navigation.findNavController(view).navigate(action);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(mainActivity, "Current item does not exist", Toast.LENGTH_SHORT).show();
                                    DatabaseReference ref = singleton.getDatabase().child("users").child(singleton.getUserId())
                                            .child("currentOrder").child(currentItem.getId());
                                    currentItem.setCount(stepperCount);
                                    ref.setValue(currentItem);
                                    Toast.makeText(mainActivity, stepperCount + " \"" + currentItem.getName() + "\" " + "added to your order", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(OrderItemFragment.this.getContext(), FinishOrderPopUp.class));

                                    NavDirections action = OrderItemFragmentDirections.actionOrderItemFragmentToCafeFragment();
                                    Navigation.findNavController(view).navigate(action);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("CANCEL", "Failed to read value.", error.toException());
            }
        });

        // Connect views to variables
        itemImage = v.findViewById(R.id.item_image);
        itemTitle = v.findViewById(R.id.item_title);
        itemPrice = v.findViewById(R.id.item_price);
        itemCaffine = v.findViewById(R.id.item_caffine);
//        iceButtons = new ArrayList<CheckBox>();
//        iceButtons.add((CheckBox) v.findViewById(R.id.ice0CheckBox));
//        iceButtons.add((CheckBox) v.findViewById(R.id.ice25CheckBox));
//        iceButtons.add((CheckBox) v.findViewById(R.id.ice50CheckBox));
//        iceButtons.add((CheckBox) v.findViewById(R.id.ice100CheckBox));
//        sugarButtons = new ArrayList<CheckBox>();
//        sugarButtons.add((CheckBox) v.findViewById(R.id.sugar0CheckBox));
//        sugarButtons.add((CheckBox) v.findViewById(R.id.sugar25CheckBox));
//        sugarButtons.add((CheckBox) v.findViewById(R.id.sugar50CheckBox));
//        sugarButtons.add((CheckBox) v.findViewById(R.id.sugar100CheckBox));
//        toppingsButtons = new ArrayList<CheckBox>();
//        toppingsButtons.add((CheckBox) v.findViewById(R.id.bobaCheckBox));
//        toppingsButtons.add((CheckBox) v.findViewById(R.id.jellyCheckBox));
//        toppingsButtons.add((CheckBox) v.findViewById(R.id.miniBobaCheckBox));
        stepper = v.findViewById(R.id.item_stepper);
        addToCartButton = v.findViewById(R.id.addToCart);


        return v;
    }

}
