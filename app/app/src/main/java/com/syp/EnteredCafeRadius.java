package com.syp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class EnteredCafeRadius extends Activity {

    private Button enteredCafeViewCafeMenu;
    private Button enteredCafeDismiss;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entered_cafe);

        enteredCafeViewCafeMenu = findViewById(R.id.enteredCafeViewCafeMenu);
        setViewCafeMenuOnClickListener();
        enteredCafeDismiss = findViewById(R.id.enteredCafeDismiss);
        setDismissOnClickListener();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));
    }

    private void setViewCafeMenuOnClickListener(){
        enteredCafeViewCafeMenu.setOnClickListener((View v)-> {
            getIntent().putExtra("action", "viewCafeMenu");
            finish();
        });
    }

    private void setDismissOnClickListener(){
        enteredCafeDismiss.setOnClickListener((View v)-> {
            getIntent().putExtra("action", "viewCheckout");
            finish();
        });
    }
}
