// Package
package com.syp.ui;

// View & Nav Imports
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

// Package Class imports
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Item;

import java.util.Locale;

// ------------------------------------------------------------------------------------
// Row representing a Item with Statistics ( connected to Recycle View, Merchant Only )
// ------------------------------------------------------------------------------------
public class RowMerchantItemStatisticsFragment extends RecyclerView.ViewHolder {

    // Views
    private TextView merchantItemStatisticsRowItemName;
    private TextView merchantItemStatisticsRowItemTotal;
    private TextView merchantItemStatisticsRowItemTotalOrders;
    private ImageView merchantItemStatisticsRowItemImage;

    // Item assocaited with Row
    private Item item;

    // --------------------------------------------
    // Constructor of Merchant Item Statistics Row
    // --------------------------------------------
    public RowMerchantItemStatisticsFragment(View itemView) {
        super(itemView);
        merchantItemStatisticsRowItemName = itemView.findViewById(R.id.merchantItemStatisticsRowItemName);
        merchantItemStatisticsRowItemTotal = itemView.findViewById(R.id.merchantItemStatisticsRowItemTotal);
        merchantItemStatisticsRowItemTotalOrders = itemView.findViewById(R.id.merchantItemStatisticsRowItemTotalOrders);
        merchantItemStatisticsRowItemImage = itemView.findViewById(R.id.merchantItemStatisticsRowItemImage);
    }

    // ------------------------------------------
    // Sets row info according to item passed in
    // ------------------------------------------
    public void setCafeItemStatisticsInfo(Item item, MainActivity mainActivity, NavDirections action){
        this.item = item;
        setItemName();
        setItemTotal();
        setItemTotalOrders();
    }

    // --------------------------
    // Setters for views
    // --------------------------
    private void setItemName() {
        merchantItemStatisticsRowItemName.setText(item.getName());
    }
    private void setItemTotal() {
        merchantItemStatisticsRowItemTotal.setText(String.format(Locale.ENGLISH, "$%.2f", item.getPrice()));
    }
    private void setItemTotalOrders() {
        merchantItemStatisticsRowItemTotalOrders.setText(String.format(Locale.ENGLISH, "$%.2f", item.getCaffeine()));
    }
}
