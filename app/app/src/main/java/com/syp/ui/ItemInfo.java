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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

// Data Structure Imports
import java.util.Locale;

// -----------------------------------------------------------
// Row representing a User Order ( connected to Recycle View)
// -----------------------------------------------------------
public class UserOrderRow extends RecyclerView.ViewHolder {

    // Views
    private TextView orderName;
    private TextView orderCaffeine;
    private TextView orderCount;
    private TextView orderPrice;

    // Order associated with row
    private Order order;

    // --------------------------
    // Constructor of User Order
    // --------------------------
    public UserOrderRow(View itemView) {
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
        public void setOrderInfo(Order order){
        // Set appropriate views
        orderName.setText(order.getItemsAsList().get(0).getName());
        orderCaffeine.setText(Double.toString(order.getItemsAsList().get(0).getCaffeine()));
        orderCount.setText(Integer.toString(order.getItemsAsList().get(0).getCount()));
        orderPrice.setText(Double.toString(order.getItemsAsList().get(0).getPrice()));
    }

    // -------------------------------------------
    // Setters for order informations on UI Views
    // -------------------------------------------
    private void setOrderName() {
        orderName.setText(order.getItemsAsList().get(0).getName());
    }
    private void setOrderCaffeine() {
        orderCaffeine.setText(Double.toString(order.getItemsAsList().get(0).getCaffeine()));
    }
    private void setOrderCount() {
        orderCount.setText(Integer.toString(order.getItemsAsList().get(0).getCount()));
    }
    private void setOrderPrice() {
        orderPrice.setText(Double.toString(order.getItemsAsList().get(0).getPrice()));
    }

}
