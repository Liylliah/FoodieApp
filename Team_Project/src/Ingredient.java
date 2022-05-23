package Foodie;

/*
 * Author: Chris Stolo
 * Program: Team Foodie Project 
 * Date: 5-9-22 
 * CMIS 495
 */

public class Ingredient {

    //Attributes
    private String name;
    private int qty;

    //Constructor (no values)
    public Ingredient() {
        
    }

    //Constructor to make ingredient
    public Ingredient(String name, int qty) {
        this.name = name;
        this.qty = qty;
    }

    //Get methods
    public String getIngredientName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public String toString() {
        return this.getIngredientName() + ":"
                + this.getQty();
    }

    //Set methods
    public void setName(String name) {
        this.name = name;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void addQty() {
        qty++;
    }

    public void reduceQty() {
        qty--;
    }

}
