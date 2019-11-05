package com.syp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Cafe;
import com.syp.model.Item;
import com.syp.model.Singleton;

import java.util.ArrayList;

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
    private Item currentItem;
    private Cafe currentCafe;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orderitem, container, false);
        mainActivity = (MainActivity) getActivity();

        // Get current cafe & item
        currentCafe = Singleton.get(mainActivity).getCurrentCafe();
        currentItem = Singleton.get(mainActivity).getCurrentItem();

        // Connect views to variables
        itemImage = v.findViewById(R.id.item_image);
        itemTitle = v.findViewById(R.id.item_title);
        itemPrice = v.findViewById(R.id.item_price);
        itemCaffine = v.findViewById(R.id.item_caffine);
        iceButtons = new ArrayList<CheckBox>();
        iceButtons.add((CheckBox) v.findViewById(R.id.ice0CheckBox));
        iceButtons.add((CheckBox) v.findViewById(R.id.ice25CheckBox));
        iceButtons.add((CheckBox) v.findViewById(R.id.ice50CheckBox));
        iceButtons.add((CheckBox) v.findViewById(R.id.ice100CheckBox));
        sugarButtons = new ArrayList<CheckBox>();
        sugarButtons.add((CheckBox) v.findViewById(R.id.sugar0CheckBox));
        sugarButtons.add((CheckBox) v.findViewById(R.id.sugar25CheckBox));
        sugarButtons.add((CheckBox) v.findViewById(R.id.sugar50CheckBox));
        sugarButtons.add((CheckBox) v.findViewById(R.id.sugar100CheckBox));
        toppingsButtons = new ArrayList<CheckBox>();
        toppingsButtons.add((CheckBox) v.findViewById(R.id.bobaCheckBox));
        toppingsButtons.add((CheckBox) v.findViewById(R.id.jellyCheckBox));
        toppingsButtons.add((CheckBox) v.findViewById(R.id.miniBobaCheckBox));
        stepper = v.findViewById(R.id.item_stepper);
        addToCartButton = v.findViewById(R.id.addToCart);

        itemTitle.setText(currentItem.get_name());
        itemPrice.setText((Double.toString(currentItem.getPrice())));
        itemCaffine.setText(((currentItem.get_caffeine()) + "mg"));

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Item> items = new ArrayList<>();
                for(int i = 0; i < Integer.parseInt(stepper.getNumber()); i++){
                    items.add(currentItem);
                }

                Singleton.get(mainActivity).addItemToOrder(items);
                //startActivity(new Intent(OrderItemFragment.this.getContext(), FinishOrderPopUp.class));

                NavDirections action = OrderItemFragmentDirections.actionOrderItemFragmentToCafeFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });

        return v;
    }

}
