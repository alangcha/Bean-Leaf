package com.syp.model;

import android.media.Image;
import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Map;

@IgnoreExtraProperties
public class Cafe implements Serializable {

    String id;
    double rating;
    int phone_number;
    ArrayList<String> details;
    ArrayList<Item> menu;
    String name;
    String address;
    String hours;
    User owner;
    double longitude;
    double latitude;
    Uri image;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("address", address);
        result.put("name", name);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        return result;
    }

    public Cafe() {
        menu = new ArrayList<Item>();
    }

    // Getter for id
    public String get_id() {
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
    public void add_detail(String s) {
        details.add(s);
    }

    // Remove detail
    public void remove_detail(String s) {
        details.remove(s);
    }

    // Getter for firstName
    public String get_name() {
        return name;
    }

    public void set_hours(String s) {
        hours = s;
    }

    public String get_hours() {
        return this.hours;
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

    // Setter for id
    public void set_id(String id) {
        this.id = id;
    }

    // Setter for firstName
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
    public Uri getImage() {
        return image;
    }

    public void removeItem(Item i) {menu.remove(i);}

    // Equals object to compare two cafes
    public boolean equals(Object o) {
        Cafe other = (Cafe) o;

        // Check firstName
        if (!this.name.equalsIgnoreCase(other.name))
            return false;

        // Check address
        if (!this.address.equalsIgnoreCase(other.address))
            return false;

        // Check Latitude
        if (!(this.latitude == other.latitude))
            return false;

        // Check Longitude
        if (!(this.longitude == other.longitude))
            return false;

        return true;
    }

    public boolean distance_from(double latitude, double longitude) {
        if (this.longitude <= longitude + .5 || this.longitude >= longitude - .5) {
            if (this.latitude <= latitude + .5 || this.latitude >= latitude - .5)
                return true;
        }
        return false;
    }
}
