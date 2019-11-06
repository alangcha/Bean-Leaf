package com.syp.ui;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.syp.MainActivity;
import com.syp.model.Item;
import com.syp.model.Order;
import com.syp.R;
import com.syp.model.Singleton;
import com.syp.model.User;

public class StatisticsFragment extends Fragment {

    final float dailyLimit = 400;
    final String[] weekDays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu",
            "Fri", "Sat"};

    final String[] monthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul"
            , "Aug", "Sep", "Oct", "Nov", "Dec"};

    private MainActivity mainActivity;
    private View v;
    private List<Order> dailyOrders;
    private List<Order> weeklyOrders;
    private List<Order> monthlyOrders;
    private TextView dailyTotal;
    private TextView weeklyTotal;
    private TextView monthlyTotal;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.fragment_statistics, container, false);
        dailyTotal = v.findViewById(R.id.totalDay);
        weeklyTotal = v.findViewById(R.id.totalWeek);
        monthlyTotal = v.findViewById(R.id.totalMonth);

        DatabaseReference ref = Singleton.get(mainActivity).getDatabase().child("users").child(Singleton.get(mainActivity).getUserId());

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                setupPieChart(user.getCaffeineTodayInMg());

                configureDailyBarChart((ArrayList<Order>)user.getOrdersAsList());
                configureWeeklyBarChart((ArrayList<Order>)user.getOrdersAsList());
                configureMonthlyBarChart((ArrayList<Order>)user.getOrdersAsList());

                ArrayList<Order> orders = (ArrayList<Order>)user.getOrdersAsList();
                ArrayList<Order> dailyOrders = returnDailyList(orders);
                ArrayList<Order> weeklyOrders = returnWeeklyList(orders);
                ArrayList<Order> monthlyOrders = returnWeeklyList(orders);

                dailyTotal.setText("$" + Double.toString(sumMoney(dailyOrders)));
                weeklyTotal.setText("$" + Double.toString(sumMoney(weeklyOrders)));
                monthlyTotal.setText("$" + Double.toString(sumMoney(monthlyOrders)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
       });

        return v;
    }


    public void configureDailyBarChart(ArrayList<Order> userOrders) {

        ArrayList<Order> userTodayOrder = returnDailyList(userOrders);
        setupDailyBarChart(userTodayOrder);

    }

    public void configureWeeklyBarChart(ArrayList<Order> userOrders)
    {
        ArrayList<Order> userThisWeekOrder = returnWeeklyList(userOrders);
        setupWeeklyBarChart(userThisWeekOrder);

    }

    public void configureMonthlyBarChart(ArrayList<Order> userOrders)
    {
        ArrayList<Order> userThisMonthOrder = returnMonthlyList(userOrders);
        setupMonthlyBarChart(userThisMonthOrder);
    }


    private void setupPieChart(double intake)
    {
        // Populating a list of PieEntries
        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry((float)intake , ""));
        pieEntries.add(new PieEntry(dailyLimit - (float)intake, ""));

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(new int[] { Color.rgb(250, 250, 250),
                Color.rgb(102, 255, 178)});
        PieData data = new PieData(dataSet);
        PieChart chart = (PieChart) v.findViewById(R.id.pieChartCaffeine);
        Description description = chart.getDescription();
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        description.setEnabled(false);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void setupDailyBarChart(ArrayList<Order> userOrders)
    {
        BarChart mChart;
        mChart = v.findViewById(R.id.barChartPriceDaily);
        mChart.getDescription().setEnabled(false);
        ArrayList<BarEntry> yVals = new ArrayList<>();
        HashMap <String, Float> chartMap = new HashMap<>();

        for(int i = 0; i < userOrders.size(); i++)
        {
            for(int j = 0; j < userOrders.get(i).getItemsAsList().size(); j++)
            {
                Log.d("myTag", "here the ITEM: " + userOrders.get(i).getItemsAsList().get(j).getName());
                if(!chartMap.containsKey(userOrders.get(i).getItemsAsList().get(j).getName()))
                {
                    chartMap.put(userOrders.get(i).getItemsAsList().get(j).getName(),
                            (float) userOrders.get(i).getItemsAsList().get(j).getPrice()); // change to actual price later
                }
                else
                {
                    float currentPrice = chartMap.get(userOrders.get(i).getItemsAsList().get(j).getName());
                    currentPrice += userOrders.get(i).getItemsAsList().get(j).getPrice();
                    chartMap.put(userOrders.get(i).getItemsAsList().get(j).getName(), currentPrice);
                }
            }

        }

        Log.d("myTag", "here comes the hashMAP: ");
        ArrayList<String> chartItemNames = new ArrayList<>();
        ArrayList<Float> chartItemPrices = new ArrayList<>();



        for(Map.Entry<String, Float> entry : chartMap.entrySet())
        {
            Log.d("myTag", entry.getKey()+ " " + entry.getValue());
            chartItemNames.add(entry.getKey());
            chartItemPrices.add(entry.getValue());
        }

        for(int i = 0; i < chartItemPrices.size(); i++)
        {
            yVals.add(new BarEntry(i, chartItemPrices.get(i)));
        }
        BarDataSet set = new BarDataSet(yVals, "daily total spending");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);



        BarData data = new BarData(set);
        //BarData data = new BarData(labels, set);

        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(chartItemNames);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(chartItemNames.size());
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis lAxis = mChart.getAxisLeft();
        YAxis rAxis = mChart.getAxisRight();
        lAxis.setEnabled(false);
        rAxis.setEnabled(false);

        mChart.setData(data);
        mChart.invalidate();
    }

    private void setupWeeklyBarChart(ArrayList<Order> userOrders) {
        BarChart mChart;
        mChart = v.findViewById(R.id.barChartPriceWeekly);
        mChart.getDescription().setEnabled(false);
        ArrayList<BarEntry> yVals = new ArrayList<>();
        HashMap <String, Float> chartMap = new HashMap<>();

        for(int i = 0; i < userOrders.size(); i++)
        {
            String weekdayOfOrder = getWeekDay(userOrders.get(i).getTimestampAsDate());
            if(!chartMap.containsKey(weekdayOfOrder))
            {
                float price = 0;
                for(int j = 0; j < userOrders.get(i).getItemsAsList().size(); j++)
                {
                    price += userOrders.get(i).getItemsAsList().get(j).getPrice();
                }
                chartMap.put(weekdayOfOrder, price);
            }
            else
            {
                float price = chartMap.get(weekdayOfOrder);
                for(int j = 0; j < userOrders.get(i).getItemsAsList().size(); j++)
                {
                    price += userOrders.get(i).getItemsAsList().get(j).getPrice();
                }
                chartMap.put(weekdayOfOrder, price);
            }

        }


        ArrayList<String> weekDayNames = new ArrayList<>();
        ArrayList<Float> chartWeekSum = new ArrayList<>();


        for(int i = 0; i < weekDays.length; i++)
        {

            if(chartMap.containsKey(weekDays[i]))
            {
                weekDayNames.add(weekDays[i]);
                chartWeekSum.add(chartMap.get(weekDays[i]));
            }
        }



        for(int i = 0; i < chartWeekSum.size(); i++)
        {
            yVals.add(new BarEntry(i, chartWeekSum.get(i)));
        }
        BarDataSet set = new BarDataSet(yVals, "weekly total spending");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);



        BarData data = new BarData(set);
        //BarData data = new BarData(labels, set);

        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(weekDayNames);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(chartWeekSum.size());
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis lAxis = mChart.getAxisLeft();
        YAxis rAxis = mChart.getAxisRight();
        lAxis.setEnabled(false);
        rAxis.setEnabled(false);

        mChart.setData(data);
        mChart.invalidate();
    }

    private void setupMonthlyBarChart(ArrayList<Order> userOrders) {
        BarChart mChart;
        mChart = v.findViewById(R.id.barChartPriceMonthly);
        mChart.getDescription().setEnabled(false);
        ArrayList<BarEntry> yVals = new ArrayList<>();
        HashMap <String, Float> chartMap = new HashMap<>();
        // PriorityQueue<String> pQueue = new PriorityQueue<>();

        for(int i = 0; i < userOrders.size(); i++)
        {
            String monthdayOfOrder = returnDate(userOrders.get(i).getTimestampAsDate());;
            if(!chartMap.containsKey(monthdayOfOrder))
            {
                float price = 0;
                for(int j = 0; j < userOrders.get(i).getItems().size(); j++)
                {
                    price += userOrders.get(i).getItemsAsList().get(j).getPrice();
                }
                chartMap.put(monthdayOfOrder, price);
            }
            else
            {
                float price = chartMap.get(monthdayOfOrder);
                for(int j = 0; j < userOrders.get(i).getItemsAsList().size(); j++)
                {
                    price += userOrders.get(i).getItemsAsList().get(j).getPrice();
                }
                chartMap.put(monthdayOfOrder, price);
            }

        }

        Date todayDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayDate);

        Integer month = calendar.get(calendar.MONTH);
        ArrayList<String> MonthDayNames = new ArrayList<>();
        ArrayList<Float> chartMonthDaySum = new ArrayList<>();

        Log.d("myTag", "iterating month: ");
        for(int i = 1; i <= 31; i++)
        {
            StringBuilder iterateMonth = new StringBuilder();
            iterateMonth.append(monthNames[month]);
            iterateMonth.append(" ");
            iterateMonth.append(i);
            if(chartMap.containsKey(iterateMonth.toString()))
            {
                MonthDayNames.add(iterateMonth.toString());
                chartMonthDaySum.add(chartMap.get(iterateMonth.toString()));
            }
            Log.d("myTag", iterateMonth.toString());
        }

        Log.d("myTag", "here comes the hashMAP: ");


        Log.d("myTag", "Hello Bruhs read through the week :D ");



        for(int i = 0; i < chartMonthDaySum.size(); i++)
        {
            yVals.add(new BarEntry(i, chartMonthDaySum.get(i)));
        }
        BarDataSet set = new BarDataSet(yVals, "monthly total spending");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);





        BarData data = new BarData(set);
        //BarData data = new BarData(labels, set);

        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(MonthDayNames);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(chartMonthDaySum.size());
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis lAxis = mChart.getAxisLeft();
        YAxis rAxis = mChart.getAxisRight();
        lAxis.setEnabled(false);
        rAxis.setEnabled(false);

        mChart.setData(data);
        mChart.invalidate();
    }


    /////////////////////////Return Order List by Specified Period ////////////////////

    public ArrayList<Order> returnDailyList(ArrayList<Order> userOrder)
    {
        Date todayDate = new Date();

        ArrayList<Order> userTodayOrder = new ArrayList<>();
        for(int i = 0; i < userOrder.size(); i++)
        {
            if(isSameDay(todayDate, userOrder.get(i).getTimestampAsDate()))
            {
                userTodayOrder.add(userOrder.get(i));
            }
        }
        return userTodayOrder;
    }



    public ArrayList<Order> returnDailyList(ArrayList<Order> userOrder, Date todayDate)
    {

        ArrayList<Order> userTodayOrder = new ArrayList<>();
        for(int i = 0; i < userOrder.size(); i++)
        {
            if(isSameDay(todayDate, userOrder.get(i).getTimestampAsDate()))
            {
                userTodayOrder.add(userOrder.get(i));
            }
        }
        return userTodayOrder;
    }

    public ArrayList<Order> returnWeeklyList(ArrayList<Order> userOrder)
    {
        Date todayDate = new Date();
        ArrayList<Order> userThisWeekOrder = new ArrayList<>();
        for(int i = 0; i < userOrder.size(); i++)
        {
            if(isSameWeek(todayDate, userOrder.get(i).getTimestampAsDate()))
            {
                userThisWeekOrder.add(userOrder.get(i));
                //Log.d("myTag" , "adding... " + Integer.toString(i) );
            }
        }
        return userThisWeekOrder;
    }



    public ArrayList<Order> returnMonthlyList(ArrayList<Order> userOrder)
    {


        Date todayDate = new Date();


        ArrayList<Order> userThisMonthOrder = new ArrayList<>();
        for(int i = 0; i < userOrder.size(); i++)
        {
            if(isSameMonth(todayDate, userOrder.get(i).getTimestampAsDate()))
            {
                userThisMonthOrder.add(userOrder.get(i));
            }

        }
        return userThisMonthOrder;
    }

    ///////////////////////// Time Logics ////////////////////


    private boolean isSameDay(Date date1, Date date2)
    {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        boolean sameYear = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
        boolean sameMonth = calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
        boolean sameDay = calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
        return (sameDay && sameMonth && sameYear);
    }


    private boolean isSameWeek(Date date1, Date date2)
    {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        Integer year1 = calendar1.get(calendar1.YEAR);
        Integer week1 = calendar1.get(calendar1.WEEK_OF_YEAR);
        Integer year2 = calendar2.get(calendar2.YEAR);
        Integer week2 = calendar2.get(calendar2.WEEK_OF_YEAR);

        if(year1.equals(year2))
        {
            if(week1.equals(week2))
            {
                Log.d("myTag" , "Am I ever here in the check same week?");
                return true;
            }
        }

        return false;
    }

    private String getWeekDay(Date date)
    {

        //Log.d("myTag", "Hellos are u here?");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //Log.d("myTag", "Hellos, here is Day of Week: " + weekDays[calendar.get(calendar.DAY_OF_WEEK)-1]);
        return weekDays[calendar.get(calendar.DAY_OF_WEEK)-1];
    }

    private boolean isSameMonth(Date date1, Date date2)
    {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        Integer year1 = calendar1.get(calendar1.YEAR);
        Integer month1 = calendar1.get(calendar1.MONTH);
        Integer year2 = calendar2.get(calendar2.YEAR);
        Integer month2 = calendar2.get(calendar2.MONTH);


        Log.d("myTag" , "this is the year1: " + year1);
        Log.d("myTag" , "this is the year2: " + year2);
        Log.d("myTag" , "this is the month1: " + month1);
        Log.d("myTag" , "this is the month2: " + month2);

        if(year1.equals(year2))
        {
            if(month1.equals(month2))
            {
                Log.d("myTag" , "Am I ever here in the check same month?");
                return true;
            }
        }

        return false;
    }


    String returnDate(Date date)
    {
        StringBuilder dateBuilder = new StringBuilder();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Integer month = calendar.get(calendar.MONTH);
        Integer day =calendar.get(calendar.DAY_OF_MONTH);
        Log.d("myTag" , "this is month: " + month.toString());
        dateBuilder.append(monthNames[month]);
        dateBuilder.append(" ");
        dateBuilder.append(day);
        //Log.d("myTag" , "this is date about to return: " + dateBuilder.toString());
        return dateBuilder.toString();
    }

    double sumMoney(ArrayList<Order> orders){
        double total = 0;
        for(Order o: orders){
            for(Item i: o.getItemsAsList()){
                total+=i.getPrice();
            }
        }
        return total;
    }

}
