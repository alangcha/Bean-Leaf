// Package
package com.syp.ui;

// View Imports
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

// Package class imports
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.DateFormats;
import com.syp.model.Order;
import com.syp.model.Singleton;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

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

    }

    // ------------------------------------------------------
    // Configures cell with information from order passed in
    // ------------------------------------------------------
    public void setOrder(Order order, MainActivity mainActivity){
        this.order = order;
        this.setOrderCafeName();
        this.setOrderCafeDate();
        this.setOrderPrice();
        this.setOrderCaffeine();
        itemView.setOnClickListener((View v) -> {
            Singleton.get(mainActivity).setCurrentOrderId(order.getId());
            NavDirections action = UserProfileFragmentDirections.actionUserFragmentToUserOrderFragment();
            Navigation.findNavController(v).navigate(action);
        });
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
