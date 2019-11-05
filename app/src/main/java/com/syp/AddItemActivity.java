package com.syp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;
import com.syp.model.Item;
import com.syp.model.Singleton;
import com.syp.ui.AddItemExistingFragment;
import com.syp.ui.AddItemNewFragment;
import com.syp.ui.AddItemNewFragmentDirections;
import com.syp.ui.AddShopFragment;
import com.syp.ui.MerchantShopFragment;

public class AddItemActivity extends Activity {

    public static int RESULT_LOAD_IMAGE_NEW = 2;
    private EditText itemName;
    private EditText itemPrice;
    private EditText itemCaffeine;
    private ImageView image;
    private Button addImage;
    private Button addItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set content view
        setContentView(R.layout.fragment_add_item_new);

        itemName = findViewById(R.id.additemnew_item_name);
        itemPrice = findViewById(R.id.additemnew_item_price);
        itemCaffeine = findViewById(R.id.addnewitem_item_caffeine);
        addImage = findViewById(R.id.additemnew_add_image);
        addItem = findViewById(R.id.additemnew_add_item);
        image = findViewById(R.id.additemnew_image);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OnActivityResult", "Success");

        // Image Code Range
        if (requestCode > 0 && requestCode < 6 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            ImageView imageView;
            if(resultCode == AddItemNewFragment.RESULT_LOAD_IMAGE_NEW)
                imageView = (ImageView) findViewById(R.id.additemnew_image);
            else if(resultCode == AddItemExistingFragment.RESULT_LOAD_IMAGE_EXISTING)
                imageView = (ImageView) findViewById(R.id.additemexisting_image);
            else if(resultCode == AddShopFragment.RESULT_LOAD_IMAGE_REG)
                imageView = (ImageView) findViewById(R.id.addshop_reg_image);
            else if(resultCode == MerchantShopFragment.RESULT_LOAD_IMAGE_CHANGESHOP)
                imageView = (ImageView) findViewById(R.id.shopImage);
            else
                imageView = (ImageView) findViewById(R.id.addshop_cafe_image);

            if(imageView != null){
                dataInstance.uploadFile(selectedImage, getFileExtension(selectedImage));
            }

            Picasso.get().load(selectedImage).into(imageView);

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
