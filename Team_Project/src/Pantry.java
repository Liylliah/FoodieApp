package Foodie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/*
 * Authors: Chris Stolo and John Cole
 * Program: Team Foodie Project
 * Date: 5-9-22
 * CMIS 495
 * */

public class Pantry {

    // Pantry Variables
    protected static ArrayList<Ingredient> pantry = new ArrayList<>();
    protected static ArrayList<Ingredient> removalList;
    public static File file;
    public static Boolean importedFile = false;
    public static String lineFormat = "NAME:QTY";

    public Pantry() {

    }

    //Add Ingredients to Pantry list
    public static void addIngredient(Ingredient item) {
        for (Ingredient food : pantry) {
            if (food.getIngredientName().equalsIgnoreCase(item.getIngredientName())) {
                JOptionPane.showMessageDialog(null, "This ingredient already exists. Updating Quantity");
                food.setQty((item.getQty() + food.getQty()));
                return;
            }
        }
        pantry.add(item);
    }

    // Remove items from list
    public static void removeIngredient(Ingredient item) {
        pantry.remove(item);
    }

    public void deleteIngredient(String ingredientName) {
        ArrayList<Ingredient> temp = pantry;
        Ingredient ingredientRemove = null;
        for (Ingredient current : temp) {
            if (current.getIngredientName().matches(ingredientName)) {
                ingredientRemove = current;
            }
        }
        temp.remove(ingredientRemove);
        pantry = temp;
    }

    // Overloaded Modify Ingredients
    public static void modifyIngredientName(Object data, int row) {
        String newValue = (String) data;
        pantry.get(row).setName(newValue);
    }

    public static void modifyIngredientQty(Object data, int row) {
        int newValue = (int) data;
        pantry.get(row).setQty(newValue);
    }

    public void importFoodFiles(File file) throws FileNotFoundException {
        try {
            Pantry.file = file;
            importedFile = true;
            ArrayList<String> tempList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                if (!line.isBlank() && !line.equalsIgnoreCase(lineFormat)) {
                    tempList.add(line);
                }
                line = reader.readLine();
            }
            reader.close();
            for (String newItem : tempList) {
                String[] ingTemp = newItem.split(":");
                Boolean isNew = checkPantryImport(ingTemp);
                if (!isNew) {
                    for (Ingredient item : removalList) {
                        item.setQty((item.getQty() + Integer.parseInt(ingTemp[1])));
                    }
                } else {
                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setName(ingTemp[0]);
                    newIngredient.setQty(Integer.parseInt(ingTemp[1]));
                    pantry.add(newIngredient);
                }

                if (removalList != null) {
                    removalList.clear();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error - Opening File ");
        }
    }

    //Get Pantry
    public static ArrayList<Ingredient> getPantry() {
        return pantry;
    }

    //Check Pantry
    public static Boolean checkPantryImport(String[] ingTemp) {
        for (Ingredient item : pantry) {
            if (item.getIngredientName().equalsIgnoreCase(ingTemp[0])) {
                removalList = new ArrayList<>();
                removalList.add(item);
                return false;
            }
        }
        return true;
    }

    //Create Table
    public static Object[][] createTable() {
        if (pantry.isEmpty()) {
            Object[][] pData = new Object[1][3];
            pData[0][0] = false;
            pData[0][1] = 0;
            pData[0][2] = "Please add or import ingredients";
            return pData;
        }
        Object[][] pData = new Object[pantry.size()][3];
        for (Ingredient item : pantry) {
            pData[pantry.indexOf(item)][0] = false;
            pData[pantry.indexOf(item)][1] = item.getQty();
            pData[pantry.indexOf(item)][2] = item.getIngredientName();
        }
        return pData;
    }

    // Save list to text file in default location 
    public static void savePantry() throws IOException {
        if (importedFile) {
            currentFile();
        } else {
            createFile();
        }
    }

    //Create a new file
    //Need to add check if Pantry.txt already exists.
    public static void createFile() throws IOException {
        String fileName = new File("./files/pantry.txt").getAbsolutePath();
        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        out.println(lineFormat);
        for (Ingredient food : pantry) {
            out.println(food.toString());  // write the Food data
        }
        out.flush();
        out.close();
        System.out.println("Done!");
    }

    //Save to current file
    public static void currentFile() throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("./files/pantry.txt"));
        out.println(lineFormat);
        for (Ingredient food : pantry) {
            out.println(food.toString());
        }
        out.flush();
        out.close();
        System.out.println("Done!");
    }

    public ArrayList<String> makeIngredientList() {
        ArrayList<String> iList = new ArrayList<>();
        for (Ingredient item : pantry) {
            iList.add(item.getIngredientName());
        }
        return iList;
    }
}
