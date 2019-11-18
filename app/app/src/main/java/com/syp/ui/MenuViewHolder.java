package com.syp.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.syp.R;

public class MenuViewHolder extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    private TextView itemBeverageName;
    private TextView itemBeveragePrice;
    private TextView itemCaffeineAmt;
    private ImageView itemImage;

    public MenuViewHolder(View itemView) {
        super(itemView);
        itemBeverageName = itemView.findViewById(R.id.food_name);
        itemBeveragePrice = itemView.findViewById(R.id.food_price);
        itemCaffeineAmt = itemView.findViewById(R.id.food_caffine);
//        itemImage = itemView.findViewById(R.id.itemImage);
    }

    public void setBeverageName(String title) {
        itemBeverageName.setText(title);
    }
    public void setBeveragePrice(String price) {
        itemBeveragePrice.setText( "$" + price);
    }
    public void setCaffeineAmt(String title) {
        itemCaffeineAmt.setText(title + " mg of caffeine");
    }
//    public void setBeverageImage(String price) { itemImage.setImageDrawable(price); }
}
