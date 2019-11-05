package com.syp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Item;
import com.syp.model.Singleton;

public class ItemEditFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;
    private TextView itemName;
    private TextView itemPrice;
    private TextView itemCaffeine;
    private EditText itemNameEdit;
    private EditText itemPriceEdit;
    private EditText itemCaffeineEdit;
    private FloatingActionButton edit;
    private FloatingActionButton done;
    private MainActivity mainActivity;
    private Button changeImage;
    private ImageView image;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_itemedit, container, false);
        mainActivity = (MainActivity) getActivity();
        Item i = Singleton.get(mainActivity).getCurrentMerchantItem();

        itemName = v.findViewById(R.id.merchantitem_title);
        itemPrice = v.findViewById(R.id.merchantitem_price);
        itemCaffeine = v.findViewById(R.id.merchantitem_caffeine);
        itemNameEdit = v.findViewById(R.id.merchantitem_title_edit);
        itemPriceEdit = v.findViewById(R.id.merchantitem_price_edit);
        itemCaffeineEdit = v.findViewById(R.id.merchantitem_caffeine_edit);
        image = v.findViewById(R.id.item_image);

        changeImage = v.findViewById(R.id.edititem_addimage);
        edit = v.findViewById(R.id.btnEditShop);
        done = v.findViewById(R.id.btnDoneShop);
        done.hide();

        itemName.setText(i.get_name());
        itemPrice.setText(String.valueOf(i.getPrice()));
        itemCaffeine.setText(String.valueOf(i.get_caffeine_amt_in_mg()));
        itemNameEdit.setText(i.get_name());
        itemPriceEdit.setText(String.valueOf(i.getPrice()));
        itemCaffeineEdit.setText(String.valueOf(i.get_caffeine_amt_in_mg()));
        image.setImageURI(i.getImage());

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemName.setVisibility(View.GONE);
                itemPrice.setVisibility(View.GONE);
                itemCaffeine.setVisibility(View.GONE);
                itemNameEdit.setVisibility(View.VISIBLE);
                itemPriceEdit.setVisibility(View.VISIBLE);
                itemCaffeineEdit.setVisibility(View.VISIBLE);
                edit.hide();
                done.show();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set visibility
                itemName.setVisibility(View.VISIBLE);
                itemPrice.setVisibility(View.VISIBLE);
                itemCaffeine.setVisibility(View.VISIBLE);
                itemCaffeineEdit.setVisibility(View.GONE);
                itemPriceEdit.setVisibility(View.GONE);
                itemNameEdit.setVisibility(View.GONE);

                // Set Text
                itemName.setText(itemNameEdit.getText());
                itemPrice.setText(itemPriceEdit.getText());
                itemCaffeine.setText(itemCaffeineEdit.getText());

                // Visibility
                edit.show();
                done.hide();

                // Edit in Singleton
                Item i = Singleton.get(mainActivity).getCurrentMerchantItem();
                i.set_name(itemNameEdit.getText().toString());
                i.set_price(Double.parseDouble(itemPriceEdit.getText().toString()));
                i.set_caffeine_amt_in_mg(Double.parseDouble((itemNameEdit.getText().toString())));
                i.setImage(Singleton.get(mainActivity).getCurrentMerchantItem().getImage());
                Singleton.get(mainActivity).editItem(i);

                InputMethodManager imm = (InputMethodManager) done.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        return v;
    }
}
