package com.syp;

import java.io.Serializable;

public class NutritionLabel implements Serializable {
    String label;
    double value;

    public NutritionLabel(){}

    public NutritionLabel(String label, double val){
        this.label = label;
        value = val;
    }

    public String get_label() {
        return label;
    }

    public void set_label(String label) {
        this.label = label;
    }

    public double get_value() {
        return value;
    }

    public void set_value(double value) {
        this.value = value;
    }
}
