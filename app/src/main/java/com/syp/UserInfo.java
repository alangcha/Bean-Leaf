package com.syp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import org.apache.commons.lang3.time.DateUtils;


public class UserInfo implements Serializable {

    String email;
    String name;
    ArrayList<Order> previous_orders;
    ArrayList<Cafe> favorite_cafes;
    ArrayList<Item> favorite_items;

    public String get_email() {
        return email;
    }

    public void set_email(String email) {
        this.email = email;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public ArrayList<Order> get_previous_orders() {
        return previous_orders;
    }

    public void add_previous_order(Order previous_order){
        this.previous_orders.add(previous_order);
    }

    public void remove_previous_order(Order previous_order){
        this.previous_orders.remove(previous_order);
    }

    public ArrayList<Cafe> get_favorite_cafes() {
        return favorite_cafes;
    }

    public void add_favorite_cafes(Cafe favorite_cafe) {
        this.favorite_cafes.add(favorite_cafe);
    }

    public void remove_favorite_cafes(Cafe favorite_cafe) {
        this.favorite_cafes.remove(favorite_cafe);
    }

    public ArrayList<Item> get_favorite_items() {
        return favorite_items;
    }

    public void add_favorite_items(Item favorite_item) {
        this.favorite_items.add(favorite_item);
    }

    public void remove_favorite_items(Item favorite_item) {
        this.favorite_items.remove(favorite_item);
    }

    public ArrayList<Order> get_today_orders(){
        ArrayList<Order> today = new ArrayList<Order>();

        for(Order order: previous_orders){
            if(DateUtils.isSameDay(order.get_date_purchased(), new Date())){
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
                total += item.get_caffeine_amt_in_mg();
            }
        }

        return total;
    }

}

