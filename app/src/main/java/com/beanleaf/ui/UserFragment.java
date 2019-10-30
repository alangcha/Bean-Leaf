package com.beanleaf.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.beanleaf.R;

// hardcoded data:

public class UserFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        //LineChart chart = (LineChart) getView().findViewById(R.id.chart); //retrieve from fragment
//
//        //hard coded user jordan:
//
//        User jordan = new User();
//
//
//        ArrayList<Order> orders = getData(jordan);
//        List<Entry> entries = new ArrayList<Entry>();
//
//        for (Order order : orders) {
//            for(Item item : order.get_item_purchased()) {
//                // turn your data into Entry objects
//                Date d = order.get_date_purchased();
//                long millis = d.getTime();
//
//
//                entries.add(new Entry(millis, (float) item.get_normal_price())); //casted double to float
//            }
//        }
//
//        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
////        dataSet.setColor(...);
////        dataSet.setValueTextColor(...); // styling, ...
//
//        LineData lineData = new LineData(dataSet);
//        //chart.setData(lineData);
//        //chart.invalidate(); // refresh
//    }
//
//    // For purposes of implementation later, this returns an array of Item objects.
//    //Query the database for array of Item objects
//    public ArrayList<Order> getData(User user){
//        //Query database for orders
//        return user.get_info().get_previous_orders();
//
//    }
}