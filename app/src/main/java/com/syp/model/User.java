package com.syp.model;

import com.google.firebase.database.IgnoreExtraProperties;

import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class User implements Serializable {

    String email;
    String displayName;
    boolean merchant;
    boolean gender;
    ArrayList<Cafe> cafes;
    ArrayList<Order> orders;

    public User(){
        orders = new ArrayList<Order>();
    }

    public void add_order(Order order){
        orders.add(order);
    }

    public ArrayList<Order> get_all_orders(){
        return orders;
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

    public ArrayList<Order> get_previous_orders() {
        return orders;
    }

    public void add_previous_order(Order previous_order){
        this.orders.add(previous_order);
    }

    public void remove_previous_order(Order previous_order){
        this.orders.remove(previous_order);
    }

    public void addCafe(Cafe c){
        cafes.add(c);
    }

    public void removeCafe(Cafe c){
        this.cafes.remove(c);
    }

    public ArrayList<Cafe> getCafes(){
        return this.cafes;
    }

    public ArrayList<Order> get_today_orders(){
        ArrayList<Order> today = new ArrayList<Order>();

        for(Order order: orders){
            if(DateUtils.isSameDay(new Date(order.getTimestamp()), new Date())){
                today.add(order);
            }
        }
        return today;
    }

    public double get_caffeine_today_in_mg(){
        double total = 0;
        ArrayList<Order> today_order = get_today_orders();

        for(Order order: today_order){
            for(Item item: order.get_item_purchased()){
                total += item.get_caffeine();
            }
        }

        return total;
    }
}

