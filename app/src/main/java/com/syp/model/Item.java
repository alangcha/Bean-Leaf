package com.syp.model;

import android.media.Image;
import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Item implements Serializable {

    private String id;
    private String name;
    private double price;
    private double caffeine_amt_in_mg;
    private Uri image;
    private Cafe owner;
    private User user;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("price", price);
        result.put("caffeine", caffeine_amt_in_mg);
        return result;
    }

    // Default Constructor
    public Item(){ }

    // setter for id
    public void set_id(String id) {
        this.id = id;
    }

    // Getter for id
    public String get_id() {
        return id;
    }

    // setter for firstName
    public void set_name(String name){
        this.name = name;
    }

    // Getter for firstName
    public String get_name() {
        return name;
    }

    // setter of price
    public void set_price(double price) {
        this.price = price;
    }

    // Getter for price
    public double getPrice() {
        return price;
    }

    // setter for caffeine amt
    public void set_caffeine(double caffeine_amt_in_mg) {
        this.caffeine_amt_in_mg = caffeine_amt_in_mg;
    }

    // Getter for caffeine amount
    public double get_caffeine() {
        return caffeine_amt_in_mg;
    }

    // Getter for images
    public Uri getImage() {
        return image;
    }

    // Add image
    public void setImage(Uri image) {
        this.image = image;
    }

    // Getter for owner of item
    public Cafe getOwner() {
        return owner;
    }

    public void setOwner(Cafe c) { owner = c; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Equals operator overload for Item
    public boolean equals(Object o){
        Item other = (Item) o;

        // Check firstName
        if(!this.name.equals(other.name))
            return false;

        // Check location
        if(!this.owner.equals(other.owner))
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
