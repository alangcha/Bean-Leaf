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
    private TextView userOrderRowCafeName;
    private TextView userOrderRowOrderDate;
    private TextView userOrderRowOrderTotal;
    private TextView userOrderRowOrderCaffeineTotal;

    // Order associated with row
    private Order order;

    // --------------------------
    // Constructor of User Order
    // --------------------------
    public RowUserOrder(View itemView) {
        super(itemView);
        userOrderRowCafeName = itemView.findViewById(R.id.userOrderRowCafeName);
        userOrderRowOrderDate = itemView.findViewById(R.id.userOrderRowOrderDate);
        userOrderRowOrderTotal = itemView.findViewById(R.id.userOrderRowOrderTotal);
        userOrderRowOrderCaffeineTotal = itemView.findViewById(R.id.userOrderRowOrderCaffeineTotal);
        itemView.setOnClickListener((View v) -> {
            NavDirections action = UserProfileFragmentDirections.actionUserFragmentToUserOrderFragment();
            Navigation.findNavController(v).navigate(action);
        });
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
        userOrderRowCafeName.setText(order.getCafe());
    }
    private void setOrderCafeDate() {
        userOrderRowOrderDate.setText(DateFormats.getDateString(order.getTimestampAsDate()));
    }
    private void setOrderPrice() {
        userOrderRowOrderTotal.setText(String.format(Locale.ENGLISH, "$%.2f", order.getTotalSpent()));
    }
    private void setOrderCaffeine() {
        userOrderRowOrderCaffeineTotal.setText(String.format(Locale.ENGLISH, "%.2f mg", order.getTotalCaffeine()));
    }

}
