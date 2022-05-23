package Foodie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

/*
 * Author: Brandon Schmidt and Chris Stolo
 * Program: Team Foodie Project
 * Date: 5-9-22
 * CMIS 495
 */

public class Repository {

    public static String directory = "./files/";	  // public for test purposes 
    private static ArrayList<Recipe> repository = new ArrayList<Recipe>();

    public Repository() {
        
    }

    // Repository methods
    public static ArrayList<Recipe> getRepository() {
        return repository;
    }

    public void setRepository(ArrayList<Recipe> repository) {
        this.repository = repository;
    }

    //Creates and adds new recipe
    public void createRecipe(String name, ArrayList<Ingredient> ingredients, ArrayList<String> instructions) {
        repository.add(new Recipe(name, ingredients, instructions));
    }

//    public void createRecipe(String name, ArrayList<Ingredient>ingredients){
//	repository.add(new Recipe(name,ingredients)) ;
//    }
    
    //Delte a recipe
    public void deleteRecipe(String recipeName) {
        ArrayList<Recipe> temp = repository;
        Recipe recipeRemove = null;
        for (Recipe current : temp) {
            if (current.getRecipeName().matches(recipeName)) {
                recipeRemove = current;
            }
        }
        temp.remove(recipeRemove);
        repository = temp;
    }

    public Recipe searchRecipe(String recipeName) {
        for (Recipe rec : repository) {
            if (rec.getRecipeName().matches(recipeName)) {
                return rec;
            }
        }
        return null;
    }

    //Save the repository
    public static void saveRepository() throws IOException {
        String filename = directory + "repository.txt";
        PrintWriter out = new PrintWriter(new FileOutputStream(filename, false));
        out.println(" NAME  %%  Ingredients   %%   Steps   ");

        for (Recipe recipe : repository) {
            out.println(recipe.toString());  // write the recipe data
        }

        out.flush();    // flush all the data to the file
        out.close();    // close the stream
        System.out.println("Done!");
    }

    //Import a recipe file
    public void importRecipeFiles(String file) throws FileNotFoundException {
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));
            String name = "";
            ArrayList<Ingredient> Ingredients = null;
            ArrayList<String> process = null;
            sc.nextLine();
            while (sc.hasNext()) {
                if (!sc.hasNext()) {
                    break;
                }
                String[] line = sc.nextLine().split("%%");
                name = line[0];
                
                if (!line[1].isBlank()) {
                    Ingredients = new ArrayList();
                    StringTokenizer token = new StringTokenizer(line[1].replaceAll("[\\]\\[]", ""), ",|:");
                    while (token.hasMoreTokens()) {
                        int qty = 0;
                        String IngredName = "";
                        String current = token.nextToken().trim();
                        IngredName = current;
                        current = token.nextToken().trim();
                        if (current.matches("[0-9]+")) {
                            qty = Integer.parseInt(current);
                            Ingredients.add(new Ingredient(IngredName, qty));
                        }
                    }
                    
                    if (!line[2].isBlank()) {
                        StringTokenizer stepScan = new StringTokenizer(line[2], ",");
                        process = new ArrayList();
                        while (stepScan.hasMoreTokens()) {
                            String Steps = stepScan.nextToken().trim();
                            Steps = Steps.replaceAll("[\\]\\[]", "");
                            process.add(Steps);
                        }
                    }
                }
                createRecipe(name, Ingredients, process);
            }
            saveRepository();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error Opening File ");
        } catch (IOException f) {
            JOptionPane.showMessageDialog(null, "Error Opening File ");
        }
    }

    
    public void importSingleRecipe(File file) throws IOException {
        //local variables
        String name = "";
        ArrayList<Ingredient> Ingredients = null;
        ArrayList<String> process = null;
        String section = null;
        Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));
        String current = null;
        StringTokenizer token = null;

        try {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (!line.isBlank()) {
                    token = new StringTokenizer(line);
                    current = token.nextToken().replace(",", "");
                }
                
                if (current.equalsIgnoreCase("recipe")) { 	    //Section to grab name
                    process = new ArrayList();
                    Ingredients = new ArrayList();
                    section = current;
                    token = new StringTokenizer(sc.nextLine(), ",");
                    name = token.nextToken();
                }

                if (searchRecipe(name) != null) { // breaks import if recipe already exists 
                    JOptionPane.showMessageDialog(null, "Recipe Name Must Be Unique");
                    return;
                }

                if (current.equalsIgnoreCase("ingredients")) { 	// Section to create ingredients
                    section = current;
                    while (section.equalsIgnoreCase("ingredients")) {
                        int qty = 0;
                        String IngredName = "";
                        token = new StringTokenizer(sc.nextLine(), ",");
                        current = token.nextToken();

                        if (current.equalsIgnoreCase("directions")) {
                            section = current;
                            break;
                        }

                        if (current.matches("[A-Za-z][A-Za-z0-9\\s]*_?[A-Za-z]*")) {
                            IngredName = current;
                            current = token.nextToken();
                            if (current.matches("[0-9]+")) {
                                qty = Integer.parseInt(current);
                                Ingredients.add(new Ingredient(IngredName, qty));
                            }
                        }
                    }
                }

                if (section.equalsIgnoreCase("directions")) { 		// Section to grab Directions for recipe
                    while (sc.hasNext()) {
                        token = new StringTokenizer(sc.nextLine(), ",");
                        
                        if (token.hasMoreTokens()) {
                            current = token.nextToken();
                            process.add(current);
                        }
                    }
                }
            }
            createRecipe(name, Ingredients, process);
            saveRepository();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error Opening File ");
        }
    }

    //Create Table
    public static Object[][] createTable() {
        if (repository.isEmpty()) {
            Object[][] rData = new Object[1][2];
            rData[0][0] = false;
            rData[0][1] = "Please add or import recipes";
            return rData;
        }
        Object[][] rData = new Object[repository.size()][2];
        for (Recipe item : repository) {
            rData[repository.indexOf(item)][0] = false;
            rData[repository.indexOf(item)][1] = item.getRecipeName();
        }
        return rData;
    }

    // Display Pantry on console	
    public static void showRepository() {
        for (Recipe recipe : repository) {
            System.out.println(recipe.toString());
        }
    }

    public ArrayList<String> makeRecipeList() {
        ArrayList<String> rList = new ArrayList<>();

        for (Recipe item : repository) {
            rList.add(item.getRecipeName());
        }
        return rList;
    }

    public List<String> makeGroceryList(Recipe target) {
        List<String> pList = new ArrayList<String>();
        List<String> groceryList = new ArrayList<String>();
        ArrayList<Ingredient> ingredients = target.getIngredients();
        ArrayList<String> rList = new ArrayList<>();

        for (Ingredient item : ingredients) {
            rList.add(item.getIngredientName().toLowerCase());
        }

        for (Ingredient food : Pantry.pantry) {
            pList.add(food.getIngredientName().toLowerCase());
        }

        groceryList = rList.stream().filter(aObject -> {
            return !pList.contains(aObject);
        })
        .collect(Collectors.toList());

        return groceryList;
    }
}
