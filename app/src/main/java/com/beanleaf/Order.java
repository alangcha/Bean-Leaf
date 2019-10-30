package com.beanleaf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Serializable {

    int id;
    User consumer;
    Cafe owner;
    ArrayList<Item> item_purchased;
    Date date_purchased;

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
    public User get_consumer() {
        return consumer;
    }

    // Set consumer
    public void set_consumer(User consumer) {
        this.consumer = consumer;
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
    public void delete_item(Item item){
        item_purchased.remove(item);
    }

    // getter date purchased
    public Date get_date_purchased() {
        return date_purchased;
    }

    // Setter for date purchased
    public void set_date_purchased(Date date_purchased) {
        this.date_purchased = date_purchased;
    }

    // Calculate order before tax
    public double calculate_order_total_before_tax(){
        double order_total = 0;
        for (Item i : item_purchased) {
            order_total += i.get_actual_price();
        }
        return order_total;
    }

    public double calculate_total_discount(){
        double discount_total = 0;
        for (Item i : item_purchased) {
            discount_total += get_tax_percentage() * i.get_normal_price();
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
