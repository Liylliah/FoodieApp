package Foodie;

import java.util.ArrayList;
import java.util.List;
/**
 * Authors: Brandon Schmidt, Chris Stolo and Thomas Zack
 * Program: Team Foodie Project
 * Date: 5-9-22
 * CMIS 495
 * */

public class Recipe {

    private String name;
    private List<ArrayList> recipe = new ArrayList<ArrayList>();

    //Constructor (no values)
    public Recipe() {

    }

    public Recipe(String Name, ArrayList ingredients, ArrayList steps) {
        this.name = Name;
        this.recipe.add(0, ingredients);
        this.recipe.add(1, steps);
    }

    public Recipe(String Name, ArrayList ingredients) {
        this.name = Name;
        this.recipe.add(0, ingredients);
    }

    public void modifyRecipeName(String newRecipeName) {
        this.name = newRecipeName;
    }

    public ArrayList<Ingredient> getIngredients() {
        ArrayList<Ingredient> ingredients = null;
        ingredients = recipe.get(0);
        return ingredients;
    }

    public ArrayList<String> getSteps() {
        ArrayList<String> steps = null;
        steps = recipe.get(1);
        return steps;
    }

    public void changeIngredients(ArrayList<Ingredient> Ingredients) {
        recipe.set(0, Ingredients);
    }

    public void deleteIngredient(Ingredient Ingredient) {
        ArrayList<Ingredient> temp = recipe.get(0);
        for (Ingredient food : temp) {
            if (food.getIngredientName().matches(Ingredient.getIngredientName())) {

                temp.remove(food);
            }
        }
    }

    public void addIngredient(Ingredient Ingredient) {
        ArrayList<Ingredient> temp = recipe.get(0);
        temp.add(Ingredient);
        recipe.set(0, temp);
    }

    public void modifyInstructions(ArrayList<String> newInstructions) {
        recipe.set(1, newInstructions);
    }

    public String getRecipeName() {
        return name;
    }

    public String toString() {
        String rec = "";
        for (ArrayList item : recipe) {
            rec += item + "%%";
        }
        return name + "%%" + rec;
    }

    // Display Pantry on console	
    public void showRecipe() {
        System.out.println(name);
        for (ArrayList item : recipe) {
            System.out.println(item.toString());
        }
    }

}
