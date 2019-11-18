package com.syp.ui;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.syp.R;

public class CafeViewHolder extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    private TextView cafeName;
    private ImageButton editButton;

    public CafeViewHolder(View cafeView) {
        super(cafeView);
        cafeName = cafeView.findViewById(R.id.cellShopName);
        editButton = cafeView.findViewById(R.id.shopEditButton);
    }

    public void setCafeName(String name) {
        cafeName.setText(name);
    }
    public ImageButton getEditButton(){
        return editButton;
    }
}
