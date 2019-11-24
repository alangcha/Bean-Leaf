package com.syp.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Item;
import com.syp.model.Singleton;

public class RowMerchantItemStatisticsFragment extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    private TextView merchantItemStatisticsItemName;
    private TextView merchantItemStatisticsItemTotal;
    private TextView merchantItemStatisticsItemTotalOrders;
    private ImageView merchantItemStatisticsItemImage;
    private Item item;

    public RowMerchantItemStatisticsFragment(View itemView) {
        super(itemView);
        merchantItemStatisticsItemName = itemView.findViewById(R.id.merchantItemStatisticsRowItemName);
        merchantItemStatisticsItemTotal = itemView.findViewById(R.id.merchantItemStatisticsRowItemTotal);
        merchantItemStatisticsItemTotalOrders = itemView.findViewById(R.id.merchantItemStatisticsRowItemTotalOrders);
        merchantItemStatisticsItemImage = itemView.findViewById(R.id.merchantItemStatisticsRowItemImage);
    }

    public void setCafeItemStatisticsInfo(Item item, MainActivity mainActivity, NavDirections action){
        this.item = item;
        setBeverageName();
        setBeveragePrice();
        setCaffeineAmt();
    }
    private void setBeverageName() {
        merchantItemStatisticsItemName.setText(item.getName());
    }
    private void setBeveragePrice() {
        merchantItemStatisticsItemTotal.setText( "$" + item.getPrice());
    }
    private void setCaffeineAmt() { merchantItemStatisticsItemTotalOrders.setText(item.getCaffeine() + " orders"); }
}
