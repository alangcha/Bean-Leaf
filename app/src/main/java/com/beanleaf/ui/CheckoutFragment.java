package com.beanleaf.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beanleaf.Item;
import com.beanleaf.MapsActivity;
import com.beanleaf.MyAdapter;
import com.beanleaf.R;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {
    private MapsActivity mapsActivity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView countTv;
    private ArrayList<Item> items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);

        recyclerView = v.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Get data
        mapsActivity = (MapsActivity) getActivity();
        items = mapsActivity.getOrder();

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(items);
        recyclerView.setAdapter(mAdapter);

        mapsActivity = (MapsActivity) getActivity();

        // Set item count
        countTv = v.findViewById(R.id.SubTitle);
        countTv.setText(mAdapter.getItemCount() + " Items");

        return v;
    }
}
