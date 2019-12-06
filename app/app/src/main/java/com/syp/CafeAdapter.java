package com.syp;

import com.syp.model.Cafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CafeAdapter extends ArrayAdapter<Cafe> {
    public CafeAdapter(Context context, ArrayList<Cafe> cafes) {
        super(context, 0, cafes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Cafe cafe = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_row, parent, false);
        }
        // Lookup view for data population
        TextView cafeName = (TextView) convertView.findViewById(R.id.cafeName);
        TextView cafeAddress = (TextView) convertView.findViewById(R.id.cafeAddress);
        TextView cafeHours = (TextView) convertView.findViewById(R.id.cafeHours);
        // Populate the data into the template view using the data object
        cafeName.setText(cafe.getName());
        cafeAddress.setText(cafe.getAddress());
        cafeHours.setText(cafe.getHours());
        // Return the completed view to render on screen
        return convertView;
    }
}
