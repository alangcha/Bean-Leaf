package com.syp;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

public class NutritionInfo implements Serializable {

    // Constant ArrayList of most common allergen ingredients
    List<String> allergy_ingredients = new ArrayList<String>(
            Arrays.asList("milk, eggs, peanuts, nuts, shellfish, wheat, soy, fish"));

    // Hashmap of String : Double value that will store Label : Value
    // i.e. Calories : 400
    ArrayList<NutritionLabel> nutrition_labels_and_values;

    // List of Ingredients
    ArrayList<String> ingredients;

    // Default Constructor creates instances of Vector / HashMap
    public NutritionInfo(){
        nutrition_labels_and_values = new ArrayList<NutritionLabel>();
        ingredients = new ArrayList<String>();
    }

    // Add a set of Label : Value pair into nutrition info hashmap
    void add_nutrition_label_and_value(String label, double value){
        nutrition_labels_and_values.add(new NutritionLabel(label, value));
    }

    // Remove specific label from nutrition container
    void remove_nutrition_label_and_value(String label) {
        nutrition_labels_and_values.remove(label);
    }

    // Get Label : Value pair by label
    public NutritionLabel get_label(String label){

        for(NutritionLabel info: nutrition_labels_and_values) {
            if (info.get_label().contains(label))
                return info;
        }
        return null;
    }

    // Get all nutrition information in form of ArrayList<Pair<String,Double>>
    public ArrayList<NutritionLabel> get_all_nutrition_info(){


        ArrayList<NutritionLabel> info = new ArrayList<NutritionLabel>();

        for(NutritionLabel pair: nutrition_labels_and_values){
            info.add(pair);
        }

        return info;
    }

    // Get all ingredients in form of ArrayList<String>
    public ArrayList<String> get_ingredients() {
        return ingredients;
    }

    // Remove single ingredient from list
    public void remove_ingredient(String ingredient) {
        ingredients.remove(ingredient);
    }

    // Add ingredient to ingredients
    public void add_ingredient(String ingredient) {
        ingredients.add(ingredient);
    }

    // Get list of allergens
    public ArrayList<String> get_allergy_information() {
        ArrayList<String> allergens = new ArrayList<String>();

        for (String ingredient : ingredients) {
            for (String allergen : allergy_ingredients) {

                if (ingredient.equalsIgnoreCase(allergen)) {
                    allergens.add(ingredient);
                }
            }
        }

        return allergens;
    }
}
