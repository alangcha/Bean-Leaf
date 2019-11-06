package com.syp.model;

import android.media.Image;
import android.net.Uri;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Map;

@IgnoreExtraProperties
public class Cafe implements Serializable {

    private String id;
    private Map<String, Item> items;
    private String name;
    private String address;
    private String hours;
    private double longitude;
    private double latitude;
    private String image;


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
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void setItems(Map<String, Item> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Exclude
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

    @Exclude
    public boolean distance_from(double latitude, double longitude) {
        if (this.longitude <= longitude + .5 || this.longitude >= longitude - .5) {
            if (this.latitude <= latitude + .5 || this.latitude >= latitude - .5)
                return true;
        }
        return false;
    }
}
