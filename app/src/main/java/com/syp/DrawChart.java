//package com.syp;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.PriorityQueue;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.charts.BarLineChartBase;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.AxisBase;
//import com.github.mikephil.charting.components.Description;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//
//
//import com.github.mikephil.charting.formatter.IAxisValueFormatter;
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
//import com.github.mikephil.charting.formatter.ValueFormatter;
//import com.github.mikephil.charting.utils.ColorTemplate;
//import com.google.firebase.database.DatabaseReference;
//import com.syp.model.Item;
//import com.syp.model.Order;
//import com.syp.R;
//import com.syp.model.Singleton;
//
//public class DrawChart extends AppCompatActivity {
//
//    final float dailyLimit = 400;
//    final String[] weekDays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu",
//            "Fri", "Sat"};
//
//    final String[] monthNames = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul"
//                                        , "Aug", "Sep", "Oct", "Nov", "Dec"};
//
//    ArrayList<Order> testOrderList = new ArrayList<>();
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_draw_chart);
//
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            Date orderOneDate = sdf.parse("4/11/2019");
//            Date orderTwoDate = sdf.parse("6/11/2019");
//            orderOne.setTimestamp(orderOneDate.getTime());
//            orderTwo.setTimestamp(orderTwoDate.getTime());
//            Date orderThreeDate = sdf.parse("1/11/2019");
//            orderThree.setTimestamp(orderThreeDate.getTime());
//        }
//        catch (Exception e)
//        {
//            Log.d("dateExceptionTag" , e.getMessage());
//        }
//        for(int i = 0; i < orderOne.get_item_purchased().size(); i++)
//        {
//
//
//            Log.d("myTag", "this is items of order one: " + orderOne.get_item_purchased().get(i).get_name() + " , " +
//                    orderOne.get_item_purchased().get(i).getPrice());
//        }
//
//        for(int i = 0; i < orderTwo.get_item_purchased().size(); i++)
//        {
//
//
//            Log.d("myTag", "this is items of order two: " + orderTwo.get_item_purchased().get(i).get_name() + " , " +
//                    orderTwo.get_item_purchased().get(i).getPrice());
//        }
//
//
//            Log.d("myTag" , "this is the time of" +
//                    " order one: " + (new Date(orderOne.getTimestamp())).toString());
//
//            Log.d("myTag" , "this is the time of" +
//                " order two: " + (new Date(orderTwo.getTimestamp())).toString());
//
//        Log.d("myTag" , "this is the time of" +
//                " order three: " + (new Date(orderThree.getTimestamp())).toString());
//
//            orderOne.setId(1);
//            orderTwo.setId(2);
//            orderThree.setId(3);
//        testOrderList.add(orderOne);
//        testOrderList.add(orderTwo);
//        testOrderList.add(orderThree);
//
////        setupPieChart(257.123);
//        // Testing ends, delete later
//
//        configureDailyBarChart(testOrderList);
//        //configureWeeklyBarChart(testOrderList);
//        //configureMonthlyBarChart(testOrderList);
//
//    }
//
//
//    public void configureDailyBarChart(ArrayList<Order> userOrders)
//    {
//
//        ArrayList<Order> userTodayOrder = returnDailyList(userOrders);
//        for(int i = 0; i < userTodayOrder.size(); i++)
//        {
//            Log.d("myTag" , "this is the id in daily Order: " + Integer.toString(userTodayOrder.get(i).getId()));
//        }
//
//        setupDailyBarChart(userTodayOrder);
//
//    }
//
//    public void configureWeeklyBarChart(ArrayList<Order> userOrders)
//    {
//        ArrayList<Order> userThisWeekOrder = returnWeeklyList(userOrders);
//        for(int i = 0; i < userThisWeekOrder.size(); i++)
//        {
//            Log.d("myTag" , "this is the id in daily Order: " + Integer.toString(userThisWeekOrder.get(i).getId()));
//        }
//
//        setupWeeklyBarChart(userThisWeekOrder);
//
//    }
//
//    public void configureMonthlyBarChart(ArrayList<Order> userOrders)
//    {
//        Log.d("myTag" , "Are you here bro? ");
//
//        ArrayList<Order> userThisMonthOrder = returnMonthlyList(userOrders);
//        for(int i = 0; i < userThisMonthOrder.size(); i++)
//        {
//            Log.d("myTag" , "this is the id in monthly Order: " + Integer.toString(userThisMonthOrder.get(i).getId()));
//
//        }
//        setupMonthlyBarChart(userThisMonthOrder);
//    }
//
//
//    private void setupPieChart(double intake)
//    {
//    // Populating a list of PieEntries
//        List<PieEntry> pieEntries = new ArrayList<>();
//        pieEntries.add(new PieEntry((float)intake , "Caffeine Consumed"));
//        pieEntries.add(new PieEntry(dailyLimit - (float)intake, "Caffiene Remaining"));
//
//        PieDataSet dataSet = new PieDataSet(pieEntries, "");
//        dataSet.setColors(new int[] { Color.rgb(255, 102, 102),
//                Color.rgb(102, 255, 178)});
//        PieData data = new PieData(dataSet);
//        PieChart chart = (PieChart) findViewById(R.id.PieChart);
//        Description description = chart.getDescription();
//        Legend legend = chart.getLegend();
//        legend.setEnabled(false);
//        description.setEnabled(false);
//        chart.setData(data);
//        chart.animateY(1000);
//        chart.invalidate();
//    }
//
//    private void setupDailyBarChart(ArrayList<Order> userOrders)
//    {
//        BarChart mChart;
//        mChart = findViewById(R.id.BarChart);
//        mChart.getDescription().setEnabled(false);
//        ArrayList<BarEntry> yVals = new ArrayList<>();
//        HashMap <String, Float> chartMap = new HashMap<>();
//
//        for(int i = 0; i < userOrders.size(); i++)
//        {
//            for(int j = 0; j < userOrders.get(i).get_item_purchased().size(); j++)
//            {
//                Log.d("myTag", "here the ITEM: " + userOrders.get(i).get_item_purchased().get(j).get_name());
//                if(!chartMap.containsKey(userOrders.get(i).get_item_purchased().get(j).get_name()))
//                {
//                    chartMap.put(userOrders.get(i).get_item_purchased().get(j).get_name(),
//                            (float) userOrders.get(i).get_item_purchased().get(j).getPrice()); // change to actual price later
//                }
//                else
//                {
//                    float currentPrice = chartMap.get(userOrders.get(i).get_item_purchased().get(j).get_name());
//                    currentPrice += userOrders.get(i).get_item_purchased().get(j).getPrice();
//                    chartMap.put(userOrders.get(i).get_item_purchased().get(j).get_name(), currentPrice);
//                }
//            }
//
//        }
//
//        Log.d("myTag", "here comes the hashMAP: ");
//        ArrayList<String> chartItemNames = new ArrayList<>();
//        ArrayList<Float> chartItemPrices = new ArrayList<>();
//
//
//
//        for(Map.Entry<String, Float> entry : chartMap.entrySet())
//        {
//            Log.d("myTag", entry.getKey()+ " " + entry.getValue());
//            chartItemNames.add(entry.getKey());
//            chartItemPrices.add(entry.getValue());
//        }
//
//        for(int i = 0; i < chartItemPrices.size(); i++)
//        {
//            yVals.add(new BarEntry(i, chartItemPrices.get(i)));
//        }
//        BarDataSet set = new BarDataSet(yVals, "daily total spending");
//        set.setColors(ColorTemplate.MATERIAL_COLORS);
//        set.setDrawValues(true);
//
//
//
//        BarData data = new BarData(set);
//        //BarData data = new BarData(labels, set);
//
//        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(chartItemNames);
//
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1f);
//        xAxis.setLabelCount(chartItemNames.size());
//        xAxis.setValueFormatter(xAxisFormatter);
//
//        mChart.setData(data);
//        mChart.invalidate();
//    }
//
//    private void setupWeeklyBarChart(ArrayList<Order> userOrders)
//    {
//        BarChart mChart;
//        mChart = findViewById(R.id.BarChart);
//        mChart.getDescription().setEnabled(false);
//        ArrayList<BarEntry> yVals = new ArrayList<>();
//        HashMap <String, Float> chartMap = new HashMap<>();
//
//        for(int i = 0; i < userOrders.size(); i++)
//        {
//            String weekdayOfOrder = getWeekDay(userOrders.get(i).getTimestampAsDate());
//            if(!chartMap.containsKey(weekdayOfOrder))
//            {
//                float price = 0;
//                for(int j = 0; j < userOrders.get(i).get_item_purchased().size(); j++)
//                {
//                    price += userOrders.get(i).get_item_purchased().get(j).getPrice();
//                }
//                chartMap.put(weekdayOfOrder, price);
//            }
//            else
//            {
//                float price = chartMap.get(weekdayOfOrder);
//                for(int j = 0; j < userOrders.get(i).get_item_purchased().size(); j++)
//                {
//                    price += userOrders.get(i).get_item_purchased().get(j).getPrice();
//                }
//                chartMap.put(weekdayOfOrder, price);
//            }
//
//        }
//
//
//        ArrayList<String> weekDayNames = new ArrayList<>();
//        ArrayList<Float> chartWeekSum = new ArrayList<>();
//
//
//        for(int i = 0; i < weekDays.length; i++)
//        {
//
//            if(chartMap.containsKey(weekDays[i]))
//            {
//                weekDayNames.add(weekDays[i]);
//                chartWeekSum.add(chartMap.get(weekDays[i]));
//            }
//        }
//
//
//
//        for(int i = 0; i < chartWeekSum.size(); i++)
//        {
//            yVals.add(new BarEntry(i, chartWeekSum.get(i)));
//        }
//        BarDataSet set = new BarDataSet(yVals, "weekly total spending");
//        set.setColors(ColorTemplate.MATERIAL_COLORS);
//        set.setDrawValues(true);
//
//
//
//        BarData data = new BarData(set);
//        //BarData data = new BarData(labels, set);
//
//        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(weekDayNames);
//
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1f);
//        xAxis.setLabelCount(chartWeekSum.size());
//        xAxis.setValueFormatter(xAxisFormatter);
//
//        mChart.setData(data);
//        mChart.invalidate();
//    }
//
//    private void setupMonthlyBarChart(ArrayList<Order> userOrders)
//    {
//        BarChart mChart;
//        mChart = findViewById(R.id.BarChart);
//        mChart.getDescription().setEnabled(false);
//        ArrayList<BarEntry> yVals = new ArrayList<>();
//        HashMap <String, Float> chartMap = new HashMap<>();
//       // PriorityQueue<String> pQueue = new PriorityQueue<>();
//
//        for(int i = 0; i < userOrders.size(); i++)
//        {
//            String monthdayOfOrder = returnDate(userOrders.get(i).getTimestampAsDate());;
//            if(!chartMap.containsKey(monthdayOfOrder))
//            {
//                float price = 0;
//                for(int j = 0; j < userOrders.get(i).getItems().size(); j++)
//                {
//                    price += userOrders.get(i).get_item_purchased().get(j).getPrice();
//                }
//                chartMap.put(monthdayOfOrder, price);
//            }
//            else
//            {
//                float price = chartMap.get(monthdayOfOrder);
//                for(int j = 0; j < userOrders.get(i).get_item_purchased().size(); j++)
//                {
//                    price += userOrders.get(i).get_item_purchased().get(j).getPrice();
//                }
//                chartMap.put(monthdayOfOrder, price);
//            }
//
//        }
//
//        Date todayDate = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(todayDate);
//
//        Integer month = calendar.get(calendar.MONTH);
//        ArrayList<String> MonthDayNames = new ArrayList<>();
//        ArrayList<Float> chartMonthDaySum = new ArrayList<>();
//
//        Log.d("myTag", "iterating month: ");
//        for(int i = 1; i <= 31; i++)
//        {
//            StringBuilder iterateMonth = new StringBuilder();
//            iterateMonth.append(monthNames[month]);
//            iterateMonth.append(" ");
//            iterateMonth.append(i);
//            if(chartMap.containsKey(iterateMonth.toString()))
//            {
//                MonthDayNames.add(iterateMonth.toString());
//                chartMonthDaySum.add(chartMap.get(iterateMonth.toString()));
//            }
//            Log.d("myTag", iterateMonth.toString());
//        }
//
//        Log.d("myTag", "here comes the hashMAP: ");
//
//
//        Log.d("myTag", "Hello Bruhs read through the week :D ");
//
//
//
//        for(int i = 0; i < chartMonthDaySum.size(); i++)
//        {
//            yVals.add(new BarEntry(i, chartMonthDaySum.get(i)));
//        }
//        BarDataSet set = new BarDataSet(yVals, "monthly total spending");
//        set.setColors(ColorTemplate.MATERIAL_COLORS);
//        set.setDrawValues(true);
//
//
//
//        BarData data = new BarData(set);
//        //BarData data = new BarData(labels, set);
//
//        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(MonthDayNames);
//
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1f);
//        xAxis.setLabelCount(chartMonthDaySum.size());
//        xAxis.setValueFormatter(xAxisFormatter);
//
//        mChart.setData(data);
//        mChart.invalidate();
//    }
//
//
//    /////////////////////////Return Order List by Specified Period ////////////////////
//
//    public ArrayList<Order> returnDailyList(ArrayList<Order> userOrder)
//    {
//        Date todayDate = new Date();
//
//        ArrayList<Order> userTodayOrder = new ArrayList<>();
//        for(int i = 0; i < userOrder.size(); i++)
//        {
//            if(isSameDay(todayDate, userOrder.get(i).getTimestampAsDate()))
//            {
//                userTodayOrder.add(userOrder.get(i));
//            }
//        }
//        return userTodayOrder;
//    }
//
//
//
//    public ArrayList<Order> returnDailyList(ArrayList<Order> userOrder, Date todayDate)
//    {
//
//        ArrayList<Order> userTodayOrder = new ArrayList<>();
//        for(int i = 0; i < userOrder.size(); i++)
//        {
//            if(isSameDay(todayDate, userOrder.get(i).getTimestampAsDate()))
//            {
//                userTodayOrder.add(userOrder.get(i));
//            }
//        }
//        return userTodayOrder;
//    }
//
//    public ArrayList<Order> returnWeeklyList(ArrayList<Order> userOrder)
//    {
//        Date todayDate = new Date();
//        ArrayList<Order> userThisWeekOrder = new ArrayList<>();
//        for(int i = 0; i < userOrder.size(); i++)
//        {
//            if(isSameWeek(todayDate, userOrder.get(i).getTimestampAsDate()))
//            {
//                userThisWeekOrder.add(userOrder.get(i));
//                //Log.d("myTag" , "adding... " + Integer.toString(i) );
//            }
//        }
//        return userThisWeekOrder;
//    }
//
//
//
//    public ArrayList<Order> returnMonthlyList(ArrayList<Order> userOrder)
//    {
//
//
//        Date todayDate = new Date();
//
//
//        ArrayList<Order> userThisMonthOrder = new ArrayList<>();
//        for(int i = 0; i < userOrder.size(); i++)
//        {
//            if(isSameMonth(todayDate, userOrder.get(i).getTimestampAsDate()))
//            {
//                userThisMonthOrder.add(userOrder.get(i));
//            }
//
//        }
//        return userThisMonthOrder;
//    }
//
//    ///////////////////////// Time Logics ////////////////////
//
//
//    private boolean isSameDay(Date date1, Date date2)
//    {
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTime(date1);
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.setTime(date2);
//        boolean sameYear = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
//        boolean sameMonth = calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
//        boolean sameDay = calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
//        return (sameDay && sameMonth && sameYear);
//    }
//
//
//    private boolean isSameWeek(Date date1, Date date2)
//    {
//
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTime(date1);
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.setTime(date2);
//
//        Integer year1 = calendar1.get(calendar1.YEAR);
//        Integer week1 = calendar1.get(calendar1.WEEK_OF_YEAR);
//        Integer year2 = calendar2.get(calendar2.YEAR);
//        Integer week2 = calendar2.get(calendar2.WEEK_OF_YEAR);
//
//        if(year1.equals(year2))
//        {
//            if(week1.equals(week2))
//            {
//                Log.d("myTag" , "Am I ever here in the check same week?");
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private String getWeekDay(Date date)
//    {
//
//        //Log.d("myTag", "Hellos are u here?");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        //Log.d("myTag", "Hellos, here is Day of Week: " + weekDays[calendar.get(calendar.DAY_OF_WEEK)-1]);
//        return weekDays[calendar.get(calendar.DAY_OF_WEEK)-1];
//    }
//
//    private boolean isSameMonth(Date date1, Date date2)
//    {
//
//        Calendar calendar1 = Calendar.getInstance();
//        calendar1.setTime(date1);
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.setTime(date2);
//
//        Integer year1 = calendar1.get(calendar1.YEAR);
//        Integer month1 = calendar1.get(calendar1.MONTH);
//        Integer year2 = calendar2.get(calendar2.YEAR);
//        Integer month2 = calendar2.get(calendar2.MONTH);
//
//
//        Log.d("myTag" , "this is the year1: " + year1);
//        Log.d("myTag" , "this is the year2: " + year2);
//        Log.d("myTag" , "this is the month1: " + month1);
//        Log.d("myTag" , "this is the month2: " + month2);
//
//        if(year1.equals(year2))
//        {
//            if(month1.equals(month2))
//            {
//                Log.d("myTag" , "Am I ever here in the check same month?");
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//
//    String returnDate(Date date)
//    {
//        StringBuilder dateBuilder = new StringBuilder();
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//
//        Integer month = calendar.get(calendar.MONTH);
//        Integer day =calendar.get(calendar.DAY_OF_MONTH);
//        Log.d("myTag" , "this is month: " + month.toString());
//        dateBuilder.append(monthNames[month]);
//        dateBuilder.append(" ");
//        dateBuilder.append(day);
//        //Log.d("myTag" , "this is date about to return: " + dateBuilder.toString());
//        return dateBuilder.toString();
//    }
//
//}
