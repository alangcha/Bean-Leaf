package com.syp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.syp.MainActivity;
import com.syp.R;

public class AddItemExistingFragment extends Fragment {

    public static int RESULT_LOAD_IMAGE_EXISTING = 1;
    private MainActivity mainActivity;
    private EditText itemName;
    private EditText itemPrice;
    private EditText itemCaffeine;
    private Button addImage;
    private Button addItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_item_existing, container, false);

        mainActivity = (MainActivity) getActivity();
        itemName = v.findViewById(R.id.additemexisting_item_name);
        itemPrice = v.findViewById(R.id.additemexisting_item_price);
        itemCaffeine = v.findViewById(R.id.additemexisting_item_caffeine);
        addImage = v.findViewById(R.id.additemexisting_add_image);
        addItem = v.findViewById(R.id.additemexisting_add_item);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE_EXISTING);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = AddItemExistingFragmentDirections.actionAddItemExistingFragmentToMerchantShopFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });


        return v;
    }
}
