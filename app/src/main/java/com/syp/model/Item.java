package com.syp.model;

import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Item implements Serializable {

    private String id;
    private String name;
    private double price;
    private double caffeine;
    private String image;
    private int count; // Only used to keep track of how many items ordered

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("price", price);
        result.put("caffeine", caffeine);
        return result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // Default Constructor
    public Item(){ }

    // setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // setter for firstName
    public void setName(String name){
        this.name = name;
    }

    // Getter for firstName
    public String getName() {
        return name;
    }

    // setter of price
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter for price
    public double getPrice() {
        return price;
    }

    // setter for caffeine amt
    public void setCaffeine(double caffeine_amt_in_mg) {
        this.caffeine = caffeine_amt_in_mg;
    }

    // Getter for caffeine amount
    public double getCaffeine() {
        return caffeine;
    }

    // Getter for images
    public String getImage() {
        return image;
    }

    // Add image
    public void setImage(String image) {
        this.image = image;
    }

    // Equals operator overload for Item
    public boolean equals(Object o){
        Item other = (Item) o;

        // Check firstName
        if(!this.name.equals(other.name))
            return false;

        return true;
    }

    // Contains method to check if item is related to serach term
    public boolean contains(String search_term){

        if(name.contains(search_term))
            return true;

        if(String.valueOf(id).contains(search_term))
            return true;

        return false;
    }

}
