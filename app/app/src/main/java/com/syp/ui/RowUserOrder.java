// Package
package com.syp.ui;

// View Imports
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

// Package class imports
import com.syp.R;
import com.syp.model.DateFormats;
import com.syp.model.Order;

// Data Structure Imports
import java.util.Locale;

// -----------------------------------------------------------
// Row representing a User Order ( connected to Recycle View)
// -----------------------------------------------------------
public class RowUserOrder extends RecyclerView.ViewHolder {

    // Views
    private TextView orderCafeName;
    private TextView orderCafeDate;
    private TextView orderTotal;
    private TextView caffeine;

    // Order associated with row
    private Order order;

    // --------------------------
    // Constructor of User Order
    // --------------------------
    public RowUserOrder(View itemView) {
        super(itemView);
        orderCafeName = itemView.findViewById(R.id.orderShopeName);
        orderCafeDate = itemView.findViewById(R.id.orderDate);
        orderTotal = itemView.findViewById(R.id.orderTotal);
        caffeine = itemView.findViewById(R.id.orderCaffeineTotal);
    }

    // ------------------------------------------------------
    // Configures cell with information from order passed in
    // ------------------------------------------------------
    public void setOrder(Order order){
        this.order = order;
        this.setOrderCafeName();
        this.setOrderCafeDate();
        this.setOrderPrice();
        this.setOrderCaffeine();
    }

    // -------------------------------------------
    // Setters for order informations on UI Views
    // -------------------------------------------
    private void setOrderCafeName() {
        orderCafeName.setText(order.getCafe());
    }
    private void setOrderCafeDate() {
        orderCafeDate.setText(DateFormats.getDateString(order.getTimestampAsDate()));
    }
    private void setOrderPrice() {
        orderTotal.setText(String.format(Locale.ENGLISH, "$%.2f", order.getTotalSpent()));
    }
    private void setOrderCaffeine() {
        caffeine.setText(String.format(Locale.ENGLISH, "%.2f mg", order.getTotalCaffeine()));
    }

}
