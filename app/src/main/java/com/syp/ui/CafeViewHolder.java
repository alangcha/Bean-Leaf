package com.syp.ui;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.syp.R;

public class CafeViewHolder extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    private TextView cafeName;
    private TextView cafeAddress;
    private ImageView cafeImage;
    private Button editButton;

    public CafeViewHolder(View cafeView) {
        super(cafeView);
        cafeName = cafeView.findViewById(R.id.cell_shop_name);
        cafeAddress = cafeView.findViewById(R.id.cell_shop_address);
        cafeImage = cafeView.findViewById(R.id.cell_shop_image);
        editButton = cafeView.findViewById(R.id.shopEditButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setCafeName(String name) {
        cafeName.setText(name);
    }
    public void setCafeAddress(String address) {
        cafeAddress.setText(address);
    }
//    public void setCafeImage(Image image) { cafeImage.setImageDrawable(image); }
}
