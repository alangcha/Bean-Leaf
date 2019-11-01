package com.syp;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {

    private int id;
    private String name;
    private boolean available;
    private boolean favorite;
    private ArrayList<String> attributes;
    private double price;
    private double rating;
    private int num_ratings;
    private double discount;
    private double caffeine_amt_in_mg;
    private String description;
    private ArrayList<Image> images;
    private NutritionInfo nutrition_info;
    private Cafe owner;
    private ArrayList<Order> past_orders;

    // Default Constructor
    public Item(){
        attributes = new ArrayList<String>();
        images = new ArrayList<Image>();
        past_orders = new ArrayList<Order>();
        num_ratings = 0;
    }

    // setter for id
    public void set_id(int id) {
        this.id = id;
    }

    // Getter for id
    public int get_id() {
        return id;
    }

    // setter for name
    public void set_name(String name){
        this.name = name;
    }

    // Getter for name
    public String get_name() {
        return name;
    }

    // Getter for attributes
    public ArrayList<String> get_all_attributes(){
        return attributes;
    }

    // add an attribute
    public void add_attribute(String attribute){
        attributes.add(attribute);
    }

    // Remove single ingredient from list
    public void remove_attribute(String attribute){
        attributes.remove(attribute);
    }

    // setter of price
    public void set_price(double price) {
        this.price = price;
    }

    // Getter for price
    public double get_normal_price() {
        return price;
    }

    // Get actual price after discount
    public double get_actual_price() { return get_discounted_price(); }

    // setter for discount
    public void set_discount(double discount) {
        this.discount = discount;
    }

    // Getter for discount
    public double get_discount() {
        return discount;
    }

    // Get price post discount
    public double get_discounted_price() { return discount * price; }

    // setter for caffeine amt
    public void set_caffeine_amt_in_mg(double caffeine_amt_in_mg) {
        this.caffeine_amt_in_mg = caffeine_amt_in_mg;
    }

    // Getter for caffeine amount
    public double get_caffeine_amt_in_mg() {
        return caffeine_amt_in_mg;
    }

    // setter for description
    public void set_description(String description) {
        this.description = description;
    }

    // Getter for description
    public String get_description() {
        return description;
    }

    // setter for cafe owner
    public void set_owner(Cafe owner) {
        this.owner = owner;
    }

    // Getter for rating
    public double get_rating() {
        return rating;
    }

    // Updating overall rating by adding a rating
    public void update_rating(double rating){
        double new_rating = rating * num_ratings + rating;
        num_ratings++;
        rating = new_rating / num_ratings;
    }

    // Getter for images
    public ArrayList<Image> get_all_images() {
        return images;
    }

    // Get specific image at index
    public Image get_image_at(int index){
        return images.get(index);
    }

    // Add image
    public void add_images(Image image) {
        this.images.add(image);
    }

    // Remove single ingredient from list
    public void remove_image(Image image){
        images.remove(image);
    }

    // Get Nutrition Info
    public NutritionInfo get_nutrition_info() {
        return nutrition_info;
    }

    // Getter for owner of item
    public Cafe get_owner() {
        return owner;
    }

    // Getter for past orders
    public ArrayList<Order> get_past_orders() {
        return past_orders;
    }

    // Add to past orders
    public void add_past_orders(Order past_order) {
        this.past_orders.add(past_order);
    }

    public boolean is_available() {
        return available;
    }

    public void set_available(boolean available) {
        this.available = available;
    }

    public boolean is_favorite() { return favorite; }

    public void set_favorite(boolean favorite) { this.favorite = favorite; }

    // Equals operator overload for Item
    public boolean equals(Object o){
        Item other = (Item) o;

        // Check name
        if(!this.name.equals(other.name))
            return false;

        // Check attributes
        for(String attribute: attributes){
            for(String other_attribute: other.attributes){
                if(!attribute.equalsIgnoreCase(other_attribute))
                    return false;
            }
        }

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

        if(attributes.contains(search_term))
            return true;

        if(("discount".contains(search_term) || search_term.contains("discount")) && discount != 0)
            return true;

        if(description.contains(search_term))
            return true;

        return false;
    }

}
