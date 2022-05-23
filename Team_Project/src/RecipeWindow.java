package Foodie;

import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;

/*
 * Authors: Emily Williams and Thomas Zack 
 * Program: Team Foodie Project 
 * Date: 5-9-22 
 * CMIS 495
 */

public class RecipeWindow extends JFrame {

    private JScrollPane recipeRPane;
    private JTextArea recipeRTextArea;
    private JScrollPane missingRPane;
    private JTextArea missingRTextArea;
    private JButton editRButton;
    private JButton deleteRButton;
    private JButton closeButton;
    private JLabel missingRLabel;
    private JLabel recipeRLabel;

    private Recipe selectedRecipe;
    private Repository theRepository;
    static RecipeBooleanTableModel recTable = new RecipeBooleanTableModel();

    public RecipeWindow(Repository repository, Recipe recipe, RecipeBooleanTableModel rbtm) {
        theRepository = repository;
        selectedRecipe = recipe;
        recTable = rbtm;

        initComponentsRecipe();
    }

    private void initComponentsRecipe() {

        recipeRPane = new JScrollPane();
        recipeRTextArea = new JTextArea();
        missingRPane = new JScrollPane();
        missingRTextArea = new JTextArea();
        editRButton = new JButton();
        deleteRButton = new JButton();
        closeButton = new JButton();
        missingRLabel = new JLabel();
        recipeRLabel = new JLabel();

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Recipe");
        ImageIcon image = new ImageIcon("./files/Foodies Icon.png");
        this.setIconImage(image.getImage());
        
        //Recipe Text Area
        recipeRTextArea.setColumns(20);
        recipeRTextArea.setRows(5);
        recipeRTextArea.setLineWrap(true);
        recipeRTextArea.setWrapStyleWord(true);
        recipeRTextArea.setEditable(false);
        recipeRTextArea.setMargin(new Insets(10, 10, 10, 10));
        recipeRPane.setViewportView(recipeRTextArea);

        showRecipeIngredients();

        //Missing Ingredients Text Area
        missingRTextArea.setColumns(20);
        missingRTextArea.setRows(5);
        missingRTextArea.setMargin(new Insets(10, 10, 10, 10));
        missingRTextArea.setEditable(false);
        missingRPane.setViewportView(missingRTextArea);

        showMissingIngredients();

        //Edit Recipe Button
        editRButton.setText("Edit Recipe");
        editRButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                //Pop-up Windows
                JFrame popFrame = new JFrame();

                try {
                    String chgName = JOptionPane.showInputDialog(popFrame, "Yes / No ", "Change Recipe Name?", JOptionPane.PLAIN_MESSAGE);
                    if (chgName.equalsIgnoreCase("yes")) {
                        String recipeName = JOptionPane.showInputDialog(popFrame, "Change Recipe Name:", "New Name", JOptionPane.PLAIN_MESSAGE);
                        selectedRecipe.modifyRecipeName(recipeName);
                    }

                    String chgIngre = JOptionPane.showInputDialog(popFrame, "Yes / No", "Change Ingredients?", JOptionPane.PLAIN_MESSAGE);
                    if (chgIngre.equalsIgnoreCase("yes")) {
                        int ingredientNumber = Integer.parseInt(JOptionPane.showInputDialog(popFrame, "Number of Ingredients:", "New Ingredients", JOptionPane.PLAIN_MESSAGE));
                        ArrayList<Ingredient> IngredientList = new ArrayList<>();
                        do {
                            String ingredientName = JOptionPane.showInputDialog(popFrame, "Ingredient Name:", "Add Ingredients", JOptionPane.PLAIN_MESSAGE);
                            int ingredientAmount = Integer.parseInt(JOptionPane.showInputDialog(popFrame, "Quantity:", "Add Ingredients", JOptionPane.PLAIN_MESSAGE));
                            Ingredient rIngredient = new Ingredient(ingredientName, ingredientAmount);
                            IngredientList.add(rIngredient);
                            ingredientNumber--;
                        } while (ingredientNumber > 0);

                        selectedRecipe.changeIngredients(IngredientList);
                    }

                    String chgSteps = JOptionPane.showInputDialog(popFrame, "Yes / No ", "Change Steps?", JOptionPane.PLAIN_MESSAGE);
                    if (chgSteps.equalsIgnoreCase("yes")) {
                        int stepsNumber = Integer.parseInt(JOptionPane.showInputDialog(popFrame, "Number of Steps:", "Add Steps", JOptionPane.PLAIN_MESSAGE));
                        int stepNu = 0;
                        ArrayList<String> StepsList = new ArrayList<>();
                        do {
                            stepNu++;
                            String stepsInstruction = JOptionPane.showInputDialog(popFrame, "Step " + stepNu, "Add Step", JOptionPane.PLAIN_MESSAGE);
                            StepsList.add(stepsInstruction);
                            stepsNumber--;
                        } while (stepsNumber > 0);

                        selectedRecipe.modifyInstructions(StepsList);
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Input Error: Quantity Must be a number.");
                }

                recTable.updateTable();
                showRecipeIngredients();
                showMissingIngredients();
                try {
                    FoodAppGUI.repository.saveRepository();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        //Delete Recipe Button
        deleteRButton.setText("Delete Recipe");
        deleteRButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //Remove Recipe from list and close window
                theRepository.deleteRecipe(selectedRecipe.getRecipeName());

                try {
                    theRepository.saveRepository();
                } catch (Exception e) {

                }
                recTable.updateTable();

                //close window
                setVisible(false);
                dispose();
            }
        });

        //Close Window Button
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //close window
                setVisible(false);
                dispose();
            }
        });

        //Missing Ingredient Section Title
        missingRLabel.setText("Missing Ingredients");
        missingRLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 16));
        missingRLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //Recipe Section Title
        recipeRLabel.setText("Recipe");
        recipeRLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        recipeRLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //Window Layout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(missingRPane, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                                                .addGap(10, 10, 10))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(editRButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(deleteRButton, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                                        .addComponent(closeButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(recipeRPane, GroupLayout.PREFERRED_SIZE, 446, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(missingRLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(recipeRLabel)
                                .addGap(190, 190, 190))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(missingRLabel)
                                        .addComponent(recipeRLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(recipeRPane)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(missingRPane, GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(31, 31, 31)
                                                .addComponent(editRButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(deleteRButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                                                .addComponent(closeButton)))
                                .addContainerGap())
        );

        pack();
    }

    //Populate Recipe Section
    public void showRecipeIngredients() {
        String display = selectedRecipe.getRecipeName() + "\n";
        display += "\nIngredient list: \n";
        for (Ingredient item : selectedRecipe.getIngredients()) {
            display += item.toString() + "\n";
        }
        display += "\nSteps List: \n";
        for (String item : selectedRecipe.getSteps()) {
            display += item.toString() + "\n";
        }
        recipeRTextArea.setText(display);
    }

    //Populate Missing Ingredient Section
    public void showMissingIngredients() {
        String display = "";
        for (String item : FoodAppGUI.repository.makeGroceryList(selectedRecipe)) {
            display += item.toString() + "\n";
        }
        missingRTextArea.setText(display);
    }

}
