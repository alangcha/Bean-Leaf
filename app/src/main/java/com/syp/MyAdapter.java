package com.syp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView itemTitleTv;
        public TextView itemSubTitleTv;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            itemTitleTv = itemView.findViewById(R.id.itemTitle);
            itemSubTitleTv = itemView.findViewById(R.id.itemSubTitle);
        }
    }

    // Store a member variable for the contacts
    private ArrayList<Item> items;

    // Pass in the contact array into the constructor
    public MyAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.fragment_menu_row_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Item item = items.get(position);

        // Set item views based on your views and data model
        TextView drinkTitleTv = viewHolder.itemTitleTv;
        drinkTitleTv.setText(item.get_name());
        TextView priceTv = viewHolder.itemSubTitleTv;
        priceTv.setText(Double.toString(item.get_actual_price()));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return items.size();
    }
}
