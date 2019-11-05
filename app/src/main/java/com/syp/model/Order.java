package com.syp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.Optional;
import android.os.Parcelable.Creator;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Order implements Serializable {

    int id;
    User user;
    Cafe owner;
    ArrayList<Item> item_purchased;
    long timestamp;

    public Order(){
        item_purchased = new ArrayList<Item>();
    }

    // Getter id
    public int get_id() {
        return id;
    }

    // Setter id
    public void set_id(int id) {
        this.id = id;
    }

    // Getter user purchasing order
    public User getUser() {
        return user;
    }

    // Set consumer
    public void setUser(User consumer) {
        this.user = consumer;
    }

    // Getter owner of drink
    public Cafe get_owner() {
        return owner;
    }

    // Setter owner of drink
    public void set_owner(Cafe owner) {
        this.owner = owner;
    }

    // Getter for all items
    public ArrayList<Item> get_item_purchased() {
        return item_purchased;
    }

    // Add item to item
    public void add_item(Item item){
        item_purchased.add(item);
    }

    // Delete item from items
    public void removeItem(Item item){
        item_purchased.remove(item);
    }

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
    public double calculate_order_total_before_tax(){
        double order_total = 0;
        for (Item i : item_purchased) {
            order_total += i.getPrice();
        }
        return order_total;
    }

    public double calculate_total_discount(){
        double discount_total = 0;
        for (Item i : item_purchased) {
            discount_total += get_tax_percentage() * i.getPrice();
        }
        return discount_total;
    }

    // Calculate order after tax
    public double calculate_order_total_after_tax() {
        return (calculate_order_total_before_tax() * (get_tax_percentage() + 1));
    }

    // Get tax percentage
    // Should call api with location of cafe and get percentage as decimal double
    public double get_tax_percentage(){
        return 0;
    }

}
