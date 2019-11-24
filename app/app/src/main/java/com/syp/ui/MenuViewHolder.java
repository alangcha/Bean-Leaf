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

public class MenuViewHolder extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    private TextView itemBeverageName;
    private TextView itemBeveragePrice;
    private TextView itemCaffeineAmt;
    private ImageView itemImage;
    private Item item;
    private View cafeItemRow;

    public MenuViewHolder(View itemView) {
        super(itemView);
        itemBeverageName = itemView.findViewById(R.id.food_name);
        itemBeveragePrice = itemView.findViewById(R.id.food_price);
        itemCaffeineAmt = itemView.findViewById(R.id.food_caffine);
        cafeItemRow = itemView.findViewById(R.id.cafeItemRowButton);
//        itemImage = itemView.findViewById(R.id.itemImage);
    }

    public void setCafeItemInfo(Item item, MainActivity mainActivity, NavDirections action){
        this.item = item;
        setBeverageName();
        setBeveragePrice();
        setCaffeineAmt();

        if(action == null)
            return;

        setViewItemOnClickListener(mainActivity, action);
    }
    private void setBeverageName() {
        itemBeverageName.setText(item.getName());
    }
    private void setBeveragePrice() {
        itemBeveragePrice.setText( "$" + item.getPrice());
    }
    private void setCaffeineAmt() { itemCaffeineAmt.setText(item.getCaffeine() + "mg of caffeine"); }
    private void setViewItemOnClickListener(MainActivity mainActivity, NavDirections action){
        cafeItemRow.setOnClickListener((View v) -> {
            Singleton.get(mainActivity).setCurrentItemId(item.getId());
            Navigation.findNavController(v).navigate(action);
        });
    }
//    public void setBeverageImage(String price) { itemImage.setImageDrawable(price); }
}
