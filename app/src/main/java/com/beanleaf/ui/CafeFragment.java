package com.beanleaf.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.beanleaf.Cafe;
import com.beanleaf.MapsActivity;
import com.beanleaf.R;

public class CafeFragment extends Fragment {
    private FloatingActionButton fabtn;
    Cafe cafe;
    MapsActivity mapsActivity;
    private TextView cafeNameTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cafe, container, false);

        fabtn = v.findViewById(R.id.btnCart);
        mapsActivity = (MapsActivity) getActivity();
        cafeNameTv = v.findViewById(R.id.cafe_name);

        cafe = mapsActivity.getCafeByPos(mapsActivity.getCurrentCafeIndex());
        cafeNameTv.setText(cafe.get_name());
        

        fabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections action = CafeFragmentDirections.actionCafeFragmentToCheckoutFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
        return v;

    }
}
