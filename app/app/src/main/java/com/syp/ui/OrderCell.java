package com.syp.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.syp.MainActivity;
import com.syp.R;

import org.w3c.dom.Text;

public class OrderCell extends RecyclerView.ViewHolder {

    // for any view that will be set as you render a row
    private TextView orderCafeName;
    private TextView orderCafeDate;
    private RecyclerView items;
    private TextView price;
    private TextView caffeine;

    public OrderCell(View itemView) {
        super(itemView);
        orderCafeName = itemView.findViewById(R.id.orderShopeName);
        orderCafeDate = itemView.findViewById(R.id.orderDate);
        price = itemView.findViewById(R.id.orderTotal);
        caffeine = itemView.findViewById(R.id.orderCaffeineTotal);
    }

    public void setOrderCafeName(String title) {
        orderCafeName.setText(title);
    }
    public void setOrderCafeDate(String date) {
        orderCafeDate.setText(date);
    }
    public void setOrderPrice(String title) {
        price.setText(title);
    }
    public void setOrderCaffeine(String title) {
        caffeine.setText(title);
    }
    public RecyclerView getIems() {return items;}

}
