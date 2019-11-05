package com.syp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.syp.MainActivity;
import com.syp.R;
import com.syp.model.Item;
import com.syp.model.Singleton;

public class AddItemNewFragment extends Fragment {

    public static int RESULT_LOAD_IMAGE_NEW = 2;
    private MainActivity mainActivity;
    private EditText itemName;
    private EditText itemPrice;
    private EditText itemCaffeine;
    private ImageView image;
    private Button addImage;
    private Button addItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_item_new, container, false);

        mainActivity = (MainActivity) getActivity();
        itemName = v.findViewById(R.id.additemnew_item_name);
        itemPrice = v.findViewById(R.id.additemnew_item_price);
        itemCaffeine = v.findViewById(R.id.addnewitem_item_caffeine);
        addImage = v.findViewById(R.id.additemnew_add_image);
        addItem = v.findViewById(R.id.additemnew_add_item);
        image = v.findViewById(R.id.additemnew_image);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE_NEW);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item i = new Item();
                i.set_name(itemName.getText().toString());
                i.set_price(Double.parseDouble(itemPrice.getText().toString()));
                i.set_caffeine_amt_in_mg(Double.parseDouble(itemCaffeine.getText().toString()));
                Singleton.get(mainActivity).addItemNew(i);
                NavDirections action = AddItemNewFragmentDirections.actionAddItemNewFragmentToAddShopFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });

        return v;
    }
}
