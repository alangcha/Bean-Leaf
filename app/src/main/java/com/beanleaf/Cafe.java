package com.beanleaf;

import android.media.Image;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Cafe implements Serializable {

    int id;
    double rating;
    int phone_number;
    ArrayList<String> details;
    String name;
    ArrayList<Date> start_hours;
    ArrayList<Date> end_hours;
    String address;
    User owner;
    double longitude;
    double latitude;
    Menu menu;
    ArrayList<Order> orders;
    ArrayList<Image> images;

    public Cafe(){
        menu = new Menu();
    }

    // Getter for id
    public int get_id() {
        return id;
    }

    // Getter for rating
    public double get_rating() {
        return rating;
    }

    // Getter for phone number
    public int get_phone_number() {
        return phone_number;
    }

    // Getter for Details in form of ArrayList<Pair<String, Boolean>>
    public ArrayList<String> get_details() {
        return details;
    }

    // Add detail
    public void add_detail(String s){
        details.add(s);
    }

    // Remove detail
    public void remove_detail(String s){
        details.remove(s);
    }

    // Getter for name
    public String get_name() {
        return name;
    }

    // Get hours
    public ArrayList<Date> get_start_hours() {
        return start_hours;
    }

    public ArrayList<Date> get_end_hours() {
        return end_hours;
    }

    public ArrayList<String> get_end_hours_string() {
        ArrayList<String> end_hours_string = new ArrayList<String>();
        DateFormat df = new SimpleDateFormat("hh:mm aa", Locale.US);

        for(Date d: end_hours){
            String open_time = df.format(d);
            end_hours_string.add(open_time);
        }

        return end_hours_string;
    }

    public ArrayList<String> get_open_hours_string() {
        ArrayList<String> start_hours_string = new ArrayList<String>();
        DateFormat df = new SimpleDateFormat("hh:mm aa", Locale.US);

        for(Date d: start_hours){
            String open_time = df.format(d);
            start_hours_string.add(open_time);
        }

        return start_hours_string;
    }

    // Get address
    public String get_address() {
        return address;
    }

    // Get owner
    public User get_owner() {
        return owner;
    }

    // Get longitude
    public double get_longitude() {
        return longitude;
    }

    // Get latitude
    public double get_latitude() {
        return latitude;
    }

    // Get menu
    public Menu get_menu() {
        return menu;
    }

    // Get previous orders
    public ArrayList<Order> get_orders() {
        return orders;
    }

    // Setter for id
    public void set_id(int id) {
        this.id = id;
    }

    // Setter for rating
    public void set_rating(double rating) {
        this.rating = rating;
    }

    // Setter for phone number
    public void set_phone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    // Setter for name
    public void set_name(String name) {
        this.name = name;
    }

    // Setter for address
    public void set_address(String address) {
        this.address = address;
    }

    // Setter for owner
    public void set_owner(User owner) {
        this.owner = owner;
    }

    // Setter for longitude
    public void set_longitude(double longitude) {
        this.longitude = longitude;
    }

    // Setter for latitude
    public void set_latitude(double latitude) {
        this.latitude = latitude;
    }

    // get all images
    public ArrayList<Image> get_all_images() {
        return images;
    }

    // get single image by image
    public Image get_image(int index){
        return images.get(index);
    }

    // Add single image
    public void add_image(Image image){
        images.add(image);
    }

    // Equals object to compare two cafes
    public boolean equals(Object o){
        Cafe other = (Cafe) o;

        // Check name
        if(!this.name.equalsIgnoreCase(other.name))
            return false;

        // Check address
        if(!this.address.equalsIgnoreCase(other.address))
            return false;

        // Check Latitude
        if(!(this.latitude == other.latitude))
            return false;

        // Check Longitude
        if(!(this.longitude == other.longitude))
            return false;

        return true;
    }

    // Contains method to check if cafe contains any related terms to a given search term
    public boolean contains(String search_term){

        if(String.valueOf(id).equalsIgnoreCase(search_term))
            return true;

        if(String.valueOf(phone_number).equalsIgnoreCase(search_term))
            return true;

        for(String detail: details){
            if(detail.contains(search_term))
                return true;
        }

        if(address.contains(search_term))
            return true;

        if(!menu.find_item(search_term).isEmpty())
            return true;

        return false;

    }

    public boolean distance_from(double latitude, double longitude){
        if(this.longitude <= longitude + .5 || this.longitude >= longitude - .5){
            if(this.latitude <= latitude + .5 || this.latitude >= latitude - .5)
                return true;
        }
        return false;
    }
}
