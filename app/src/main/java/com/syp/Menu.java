package com.syp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Collection;

public class Menu implements Serializable {


    ArrayList<Item> items;
    ArrayList<Item> promotional_items;

    public Menu(){
        items = new ArrayList<Item>();
        promotional_items = new ArrayList<Item>();
    }

    // Get all items
    public ArrayList<Item> get_items() {
        return items;
    }

    // Add single item to menu
    public void add_item(Item item){
        items.add(item);
    }

    // Remove item by item
    public void remove_item(Item item){
        items.remove(item);
        promotional_items.remove(item);
    }

    // Remove all items with name
    public void remove_item_by_name(String name){
        for(Item item: items){
            if(item.get_name().equalsIgnoreCase(name))
                items.remove(item);
                promotional_items.remove(item);
        }
    }

    // Get all items that are available
    public ArrayList<Item> get_actual_items(){
        ArrayList<Item> availible_items = new ArrayList<Item>();

        for(Item item: items){
            if(item.is_available())
                availible_items.add(item);
        }

        return availible_items;
    }

    // Set all items with ingredient to unavailable
    public void set_unavailable_by_ingredient(String ingredient){

        for(Item item: items){
            if(item.get_nutrition_info().get_ingredients().contains(ingredient))
                item.set_available(false);
        }
    }

    // Set all items with name to unavailable
    public void set_unavailable_by_name(String name){

        for(Item item: items){
            if(item.get_name().equalsIgnoreCase(name))
                item.set_available(false);
        }
    }

    // Find specific item
    public ArrayList<Item> find_item(String search_term){
        ArrayList<Item> searched_items = new ArrayList<Item>();

        for(Item item: items){
            if(item.contains(search_term))
                searched_items.add(item);
        }

        return searched_items;
    }

    // Get all promotional items
    public ArrayList<Item> get_promotional_items() {
        return promotional_items;
    }

    // Add promotional item
    public void add_promotional_item(Item item){
        items.add(item);
        promotional_items.add(item);
    }

    // Remove promotional item
    // If remove permanent is true remove from items as well
    public void remove_promotional_item(Item item, boolean remove_permanent){
        promotional_items.remove(item);
        if(remove_permanent)
            items.remove(item);
    }

    // Get favorite items
    public ArrayList<Item> get_favorites() {

        ArrayList<Item> favorites = new ArrayList<Item>();

        for(Item item: items){
            if(item.is_favorite())
                favorites.add(item);
        }

        return favorites;
    }

    // Add an item as favorite
    public void add_favorite_item(Item item){
        item.set_favorite(true);
    }

    // Add an item as favorite through name
    public void add_favorite_item(String name){
        for(Item item: items){
            if(item.get_name().equalsIgnoreCase(name))
                item.set_favorite(true);
        }
    }

    // Remove an favorite item
    // If remove permanent is true remove from items as well
    public void remove_favorite_item(Item item, boolean remove_permanent){

        if(remove_permanent)
            remove_item(item);
        else
            item.set_favorite(false);
    }

    // Remove an favorite item
    // If remove permanent is true remove from items as well
    public void remove_favorite_item(String name, boolean remove_permanent){

        for(Item item_i: items){
            if(item_i.get_name().equalsIgnoreCase(name)){
                if(remove_permanent)
                    remove_favorite_item(item_i, true);
                else
                    item_i.set_favorite(false);
            }
        }
    }

}
