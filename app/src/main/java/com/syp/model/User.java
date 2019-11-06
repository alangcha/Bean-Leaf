package com.syp.model;

import com.google.firebase.database.IgnoreExtraProperties;

import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class User implements Serializable {
    String email;
    String displayName;
    boolean merchant;
    boolean gender;
    Map<String, Cafe> cafes;
    Map<String, Order> orders;
    Order currentOrder;

    public User(){
        cafes = new HashMap<>();
        orders = new HashMap<>();
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Map<String, Order> getOrders(){
        return orders;
    }

    public void setOrders(Map<String, Order> orders){
        this.orders = orders;
    }

    public List<Order> getOrdersAsList(){
        ArrayList<Order> ordersList = new ArrayList<>();

        for(String key : orders.keySet()){
            ordersList.add(orders.get(key));
        }

        return ordersList;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMerchant() {
        return merchant;
    }

    public void setMerchant(boolean merchant) {
        this.merchant = merchant;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Map<String, Cafe> getCafes(){
        return this.cafes;
    }

    public void setCafes(Map<String, Cafe> cafes) {this.cafes = cafes;}

    public ArrayList<Order> getTodayOrders(){
        ArrayList<Order> today = new ArrayList<Order>();

        for(Order order: this.getOrdersAsList()){
            if(DateUtils.isSameDay(new Date(order.getTimestamp()), new Date())){
                today.add(order);
            }
        }
        return today;
    }

    public double getCaffeineTodayInMg(){
        double total = 0;
        ArrayList<Order> today_order = getTodayOrders();

        for(Order order: today_order){
            for(Item item: order.getItemsAsList()){
                total += item.getCaffeine();
            }
        }

        return total;
    }
}

