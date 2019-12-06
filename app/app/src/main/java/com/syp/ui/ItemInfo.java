// Package
package com.syp.ui;

// View Imports
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

// Package class imports
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Item;
import com.syp.model.Order;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

// Data Structure Imports
import java.util.Locale;

// -----------------------------------------------------------
// Row representing a User Order ( connected to Recycle View)
// -----------------------------------------------------------
public class ItemInfo extends RecyclerView.ViewHolder {

    // Views
    private TextView itemName;
    private TextView itemCaffeine;
    private TextView itemCount;
    private TextView itemPrice;

    // Order associated with row
    private Item item;

    // --------------------------
    // Constructor of User Order
    // --------------------------
    public ItemInfo(View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.userOrderRowItemName);
        itemCaffeine = itemView.findViewById(R.id.userOrderRowOrderCaffeinePerItem);
        itemCount = itemView.findViewById(R.id.userOrderRowOrderCount);
        itemPrice = itemView.findViewById(R.id.userOrderRowOrderPricePerItem);
        itemView.setOnClickListener((View v) -> {
            NavDirections action = UserProfileFragmentDirections.actionUserFragmentToUserOrderFragment();
            Navigation.findNavController(v).navigate(action);
        });
    }

    // ------------------------------------------------------
    // Configures cell with information from order passed in
    // ------------------------------------------------------
        public void setItemInfo(Item item, MainActivity mainActivity, NavDirections action){
        // Set appropriate views
        setOrderName(item);
        itemCaffeine.setText(Double.toString(item.getCaffeine()));
        itemCount.setText(Integer.toString(item.getCount()));
        itemPrice.setText(Double.toString(item.getPrice()));
        if(action == null)
            return;

        //setViewCafeOnClickListener(mainActivity, action);
    }

    // -------------------------------------------
    // Setters for order informations on UI Views
    // -------------------------------------------
    private void setOrderName(Item item) {
        itemName.setText(item.getName());
    }
    private void setOrderCaffeine() {
        itemCaffeine.setText(String.format(Locale.ENGLISH, "%.2f mg per item", item.getCaffeine()));
    }
    private void setOrderCount() {
        itemCount.setText("Count: " + Integer.toString(item.getCount()));
    }
    private void setOrderPrice() {
        itemPrice.setText("$" + Double.toString(item.getPrice())+ " per item");
    }

}
