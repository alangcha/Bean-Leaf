// Package
package com.syp.ui;

// View & Nav Imports
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

// Package class imports
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Item;
import com.syp.model.Singleton;

// ------------------------------------------------------------------------------------
// Row representing a Item with Statistics ( connected to Recycle View )
// ------------------------------------------------------------------------------------
public class RowUserItemFragment extends RecyclerView.ViewHolder {

    // Views
    private TextView userItemRowItemName;
    private TextView userItemRowItemPrice;
    private TextView userItemRowItemCaffeine;
    private ImageView userItemRowItemImage;
    private View userItemRow;   // View for On Click

    // Item associated with row
    private Item item;

    // -----------------------------
    // Constructor of User Item Row
    // -----------------------------
    public RowUserItemFragment(View itemView) {
        super(itemView);

        // Find Views
        userItemRowItemName = itemView.findViewById(R.id.userItemRowItemName);
        userItemRowItemPrice = itemView.findViewById(R.id.userItemRowItemPrice);
        userItemRowItemCaffeine = itemView.findViewById(R.id.userItemRowItemCaffeine);
        userItemRowItemImage = itemView.findViewById(R.id.userItemRowItemImage);
        userItemRow = itemView.findViewById(R.id.userItemRow);
    }

    public void setUserItemInfo(Item item, MainActivity mainActivity, NavDirections action){
        // Set Item
        this.item = item;

        // Setters for UI
        setItemName();
        setItemPrice();
        setItemCaffeine();

        // If Action is required set On Click for Row
        if(action != null)
            setItemImageOnClickListener(mainActivity, action);
    }

    private void setItemName() {
        userItemRowItemName.setText(item.getName());
    }
    private void setItemPrice() {
        userItemRowItemPrice.setText( "$" + item.getPrice());
    }
    private void setItemCaffeine() { userItemRowItemCaffeine.setText(item.getCaffeine() + " mg of caffeine"); }
    private void setItemImageOnClickListener(MainActivity mainActivity, NavDirections action){
        userItemRow.setOnClickListener((View v) -> {
            Singleton.get(mainActivity).setCurrentItemId(item.getId());
            Navigation.findNavController(v).navigate(action);
        });
    }
}
