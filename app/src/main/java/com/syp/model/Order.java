package com.syp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Order implements Serializable {

    private String id;
    private long timestamp;
    private String user;
    private String cafe;
    private Map<String, Item> items;


    public Order(){
        items = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter user purchasing order
    public String getUser() {
        return user;
    }

    // Set consumer
    public void setUser(String user) {
        this.user = user;
    }

    // Getter owner of drink
    public String getCafe() {
        return cafe;
    }

    // Setter owner of drink
    public void setCafe(String owner) {
        this.cafe = owner;
    }

    // Getter for all items
    public Map<String, Item> getItems() {
        return items;
    }

    public List<Item> getItemsAsList() {
        List<Item> itemsList = new ArrayList<>();

        for(String key : items.keySet())
            itemsList.add(items.get(key));

        return itemsList;
    }

    public void setItems(Map<String, Item> items) { this.items = items; }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestampAsDate(){
        return new Date(timestamp);
    }

    // Calculate order before tax
//    public double calculate_order_total_before_tax(){
//        double order_total = 0;
//        for (Item i : items) {
//            order_total += i.getPrice();
//        }
//        return order_total;
//    }
//
//    public double calculate_total_discount(){
//        double discount_total = 0;
//        for (String i : items.values()) {
//            discount_total += get_tax_percentage() * i.getPrice();
//        }
//        return discount_total;
//    }

//    // Calculate order after tax
//    public double calculate_order_total_after_tax() {
//        return (calculate_order_total_before_tax() * (get_tax_percentage() + 1));
//    }
//
//    // Get tax percentage
//    // Should call api with location of cafe and get percentage as decimal double
//    public double get_tax_percentage(){
//        return 0;
//    }

}
