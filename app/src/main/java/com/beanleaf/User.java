package com.beanleaf;

import java.io.Serializable;
import java.util.ArrayList;


public class User implements Serializable {

    int user_id;

    String username;
    String password;

    ArrayList<Order> orders;
    UserInfo info;

    public User(){
        orders = new ArrayList<Order>();
    }

    public String get_username() {
        return username;
    }

    public void set_username(String username) {
        this.username = username;
    }

    public String get_password() {
        return password;
    }

    public void set_password(String password) {
        this.password = password;
    }

    public int get_user_id() {
        return user_id;
    }

    public void set_user_id(int user_id) {
        this.user_id = user_id;
    }

    public UserInfo get_info() {
        return info;
    }

    public void set_info(UserInfo info) {
        this.info = info;
    }

    public void add_order(Order order){
        orders.add(order);
    }

    public ArrayList<Order> get_all_orders(){
        return orders;
    }
}

