package com.syp.model;

import java.util.Optional;

public class Database {

    public static void createUser(User u){
        // TODO - Should push user to databse
    }

    public static Optional<User> checkExistingUser(String email){
        // TODO - Should check if email has already been registered
        return Optional.empty();
    }

    public static void addOrderToUser(Order o, User u){
        // TODO - Should push order o to user u in database
    }

    public static void editUserEmail(String email){
        // TODO - Change user email to new email
    }

    public static void editUserPassword(String password){
        // TODO - Change user email to new email
    }

    public static void editFirstName(String firstName){
        // TODO - Change user email to new email
    }

    public static void editLastName(String lastName){
        // TODO - Change user email to new email
    }

    public static void editCafeName(Cafe c, String name){
        // TODO - Should replace original cafe with edited cafe
    }

    public static void editCafeAddress(Cafe c, String address){
        // TODO - Should replace original cafe with edited cafe
    }

    public static void editCafeHours(Cafe c, String hours){
        // TODO - Should replace original cafe with edited cafe
    }

    public static void addItemToCafe(Cafe c, Item i){
        // TODO - Should replace original cafe with edited cafe
    }

    public static void addNewCafe(Cafe c){
        // TODO - Should push to list of cafes

        // TODO - Should also push cafe to list of cafes in User object
    }


}
