package Foodie;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Authors: Emily Williams and John Cole 
 * Program: Team Foodie Project 
 * Date: 5-9-22 
 * CMIS 495
 */

public class FoodAppGUI extends JFrame {

    /* Attributes */
    static Pantry pantry = new Pantry();
    static boolean isSelected = false;
    static Repository repository = new Repository();
    ArrayList<Recipe> recipeData = repository.getRepository();
    static File file;
    PantryBooleanTableModel PantryBooleanTableModel = new PantryBooleanTableModel();
    RecipeBooleanTableModel RecipeBooleanTableModel = new RecipeBooleanTableModel();

    FoodAppGUI() {
        this.setTitle("Foodies App");
        ImageIcon image = new ImageIcon("./files/Foodies Icon.png");
        this.setIconImage(image.getImage());
        this.setVisible(true);
        initComponentsMain();
    }

    private void initComponentsMain() {
        repository.setRepository(recipeData);

        JPanel mainPanel = new JPanel();
        JPanel recipePanel = new JPanel();
        JButton buttonImportRecipe = new JButton();
        JButton buttonRemoveRecipe = new JButton();
        JButton buttonAddRecipe = new JButton();
        JButton buttonOpenRecipe = new JButton();
        JButton buttonEditIng = new JButton();
        JPanel ingredientPanel = new JPanel();
        JLabel pantryLabel = new JLabel();
        JLabel recipeLabel = new JLabel();
        JButton buttonAddIng = new JButton();
        JButton buttonRemoveIng = new JButton();
        JButton buttonImportIng = new JButton();
        JScrollPane pTableScrollPane1 = new JScrollPane();
        JScrollPane rTableScrollPane1 = new JScrollPane();
//        JMenuBar menuBar = new JMenuBar();
//        JMenu menuFile = new JMenu();
//        JMenuItem menuImportI = new JMenuItem();
//        JMenuItem menuImportR = new JMenuItem();
//        JMenu menuHelp = new JMenu();
//        JMenu menuExit = new JMenu();

        //Main Panel Layout
        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 304, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(945, 650));

        /* Pantry Panel */
        //Pantry Title
        pantryLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        pantryLabel.setText("Pantry");
        pantryLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //Table of Ingredients
        JTable pTable = new JTable(PantryBooleanTableModel);

        //Column Sizes
        TableColumn pColumn;
        for (int i = 0; i < 3; i++) {
            pColumn = pTable.getColumnModel().getColumn(i);
            switch (i) {
                case 2 ->
                    pColumn.setPreferredWidth(290);
                case 0 ->
                    pColumn.setPreferredWidth(40);
                default ->
                    pColumn.setPreferredWidth(70);
            }
        }
        pTableScrollPane1.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pTableScrollPane1.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        pTableScrollPane1.setViewportView(pTable);
        pTable.setFillsViewportHeight(true);

        //Pop-up Windows
        JFrame popFrame = new JFrame();

        //Add Ingredient Button
        buttonAddIng.setText("Add Ingredient");
        buttonAddIng.addActionListener(evt -> {
            Ingredient ingredient = new Ingredient();
            try {
                ingredient.setName(JOptionPane.showInputDialog(popFrame, "Ingredient:", "Add Ingredient", JOptionPane.PLAIN_MESSAGE));
                ingredient.setQty(Integer.parseInt(JOptionPane.showInputDialog(popFrame, "Quantity:", "Add Ingredient", JOptionPane.PLAIN_MESSAGE)));
                Pantry.addIngredient(ingredient);
                PantryBooleanTableModel.updateTable();
                Pantry.savePantry();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error - Saving File ");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input Error: Quantity Must be a number.");
            }
        });

        //Remove Ingredient Button
        buttonRemoveIng.setText("Remove Ingredient");
        buttonRemoveIng.addActionListener(evt -> {
            PantryBooleanTableModel.deleteRow(Pantry.getPantry());
            try {
                Pantry.savePantry();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error - Saving File ");
            }
        });

        //Import Ingredients Button
        buttonImportIng.setText("Import Ingredients");
        buttonImportIng.addActionListener(evt -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/documents"));
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    pantry.importFoodFiles(file);
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "Error - Getting file path");
                }
            }
            PantryBooleanTableModel.updateTable();
        });

        //Edit Ingredient Button
        buttonEditIng.setText("Edit Ingredient");
        buttonEditIng.addActionListener(evt -> {
            TableModelListener modelListener = e -> {
                int row = e.getFirstRow();
                int column = e.getColumn();
                TableModel model = (TableModel) e.getSource();
                Object data = model.getValueAt(row, column);
                if (column == 1) {
                    Pantry.modifyIngredientQty(data, row);
                } else if (column == 2) {
                    Pantry.modifyIngredientName(data, row);
                }
            };
            if (isSelected == true) {
                isSelected = false;
                try {
                    pTable.getModel().removeTableModelListener(modelListener);
                    buttonEditIng.setText("Edit Ingredient");
                    Pantry.savePantry();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error - Saving File ");
                }
            } else if (isSelected == false) {
                isSelected = true;
                buttonEditIng.setText("Save Change");
                pTable.getModel().addTableModelListener(modelListener);
            }
        });

        //Pantry Panel Layout
        GroupLayout ingredientPanelLayout = new GroupLayout(ingredientPanel);
        ingredientPanel.setLayout(ingredientPanelLayout);
        ingredientPanelLayout.setHorizontalGroup(
                ingredientPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(ingredientPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(ingredientPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(ingredientPanelLayout.createSequentialGroup()
                                                .addComponent(buttonAddIng, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonRemoveIng, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(pTableScrollPane1, GroupLayout.PREFERRED_SIZE, 400, Short.MAX_VALUE)
                                        .addGroup(ingredientPanelLayout.createSequentialGroup()
                                                .addComponent(buttonImportIng, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonEditIng, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(pantryLabel, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                                .addContainerGap(12, Short.MAX_VALUE))
        );

        ingredientPanelLayout.setVerticalGroup(
                ingredientPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(ingredientPanelLayout.createSequentialGroup()
                                .addComponent(pantryLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pTableScrollPane1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ingredientPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonAddIng)
                                        .addComponent(buttonRemoveIng))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ingredientPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonImportIng)
                                        .addComponent(buttonEditIng))
                        )
        );

        /* Recipe Panel */
        //Recipe Title
        recipeLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        recipeLabel.setText("Recipes");
        recipeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //Table of Recipes
        JTable rTable = new JTable(RecipeBooleanTableModel);

        //Column Sizes
        TableColumn rColumn;
        for (int i = 0; i < 2; i++) {
            rColumn = rTable.getColumnModel().getColumn(i);
            if (i == 1) {
                rColumn.setPreferredWidth(360);
            } else {
                rColumn.setPreferredWidth(40);
            }
        }
        rTableScrollPane1.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rTableScrollPane1.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        rTableScrollPane1.setViewportView(rTable);
        rTable.setFillsViewportHeight(true);

        //Import Recipe Button
        buttonImportRecipe.setText("Import Recipe");
        buttonImportRecipe.addActionListener(evt -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/documents"));
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    repository.importSingleRecipe(file);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error - Getting file path");
                }
            }
            try {
                repository.saveRepository();
            } catch (Exception e) {
                e.printStackTrace();
            }
            RecipeBooleanTableModel.updateTable();
            rTable.setModel(RecipeBooleanTableModel);
            rTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            rTable.getColumnModel().getColumn(1).setPreferredWidth(360);
        });

        //Remove Recipe Button
        buttonRemoveRecipe.setText("Remove Recipe");
        buttonRemoveRecipe.addActionListener(evt -> {
            RecipeBooleanTableModel.deleteRow(Repository.getRepository());
            try {
                Repository.saveRepository();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error - Saving File ");
            }
        });

        //Add Recipe Button
        buttonAddRecipe.setText("Add Recipe");
        buttonAddRecipe.addActionListener(evt -> {
            try {
                String recipeName = JOptionPane.showInputDialog(popFrame, "Recipe:", "Add Recipe", JOptionPane.PLAIN_MESSAGE);
                int ingredientNumber = Integer.parseInt(JOptionPane.showInputDialog(popFrame, "Number of Ingredients:", "Add Ingredients", JOptionPane.PLAIN_MESSAGE));
                ArrayList<Ingredient> IngredientList = new ArrayList<>();
                do {
                    String ingredientName = JOptionPane.showInputDialog(popFrame, "Ingredient Name:", "Add Ingredients", JOptionPane.PLAIN_MESSAGE);
                    int ingredientAmount = Integer.parseInt(JOptionPane.showInputDialog(popFrame, "Quantity:", "Add Ingredients", JOptionPane.PLAIN_MESSAGE));
                    Ingredient rIngredient = new Ingredient(ingredientName, ingredientAmount);
                    IngredientList.add(rIngredient);
                    ingredientNumber--;
                } while (ingredientNumber > 0);

                int stepsNumber = Integer.parseInt(JOptionPane.showInputDialog(popFrame, "Number of Steps:", "Add Steps", JOptionPane.PLAIN_MESSAGE));
                int stepNu = 0;
                ArrayList<String> StepsList = new ArrayList<>();
                do {
                    stepNu++;
                    String stepsInstruction = JOptionPane.showInputDialog(popFrame, "Step " + stepNu, "Add Step", JOptionPane.PLAIN_MESSAGE);
                    StepsList.add(stepsInstruction);
                    stepsNumber--;
                } while (stepsNumber > 0);

                Recipe recipe = new Recipe(recipeName, IngredientList, StepsList);

                repository.createRecipe(recipeName, IngredientList, StepsList);

                repository.saveRepository();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input Error: Quantity Must be a number.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, " Error: Saving File");
            }

            RecipeBooleanTableModel.updateTable();
            rTable.setModel(RecipeBooleanTableModel);
            rTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            rTable.getColumnModel().getColumn(1).setPreferredWidth(360);
        });

        //Open Recipe Button
        buttonOpenRecipe.setText("Open Recipe");
//        buttonOpenRecipe.addActionListener(evt -> new RecipeWindow(repository).setVisible(true));
        buttonOpenRecipe.addActionListener(evt -> {
            Object[] recipeOptions = repository.makeRecipeList().toArray();
            Object chosen = JOptionPane.showInputDialog(new JFrame(), "Choose a Recipe: ", "select recipe", JOptionPane.PLAIN_MESSAGE, null, recipeOptions, "Regular");

            new RecipeWindow(repository, repository.searchRecipe((String) chosen), RecipeBooleanTableModel).setVisible(true);
        });

        //Recipe Panel Layout
        GroupLayout recipePanelLayout = new GroupLayout(recipePanel);
        recipePanel.setLayout(recipePanelLayout);
        recipePanelLayout.setHorizontalGroup(
                recipePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(recipePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(recipePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(recipePanelLayout.createSequentialGroup()
                                                .addComponent(buttonAddRecipe, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonRemoveRecipe, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(rTableScrollPane1, GroupLayout.PREFERRED_SIZE, 400, Short.MAX_VALUE)
                                        .addGroup(recipePanelLayout.createSequentialGroup()
                                                .addComponent(buttonImportRecipe, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(buttonOpenRecipe, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(recipeLabel, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                                .addContainerGap(12, Short.MAX_VALUE))
        );

        recipePanelLayout.setVerticalGroup(
                recipePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(recipePanelLayout.createSequentialGroup()
                                .addComponent(recipeLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rTableScrollPane1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(recipePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonAddRecipe)
                                        .addComponent(buttonRemoveRecipe))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(recipePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonImportRecipe)
                                        .addComponent(buttonOpenRecipe))
                        )
        );

        /* Menu */
//        menuFile.setText("File");
//        menuImportI.setText("Import Ingredients");
//        menuImportR.setText("Import Recipe");
//        menuExit.setText("Exit");
//        menuHelp.setText("Help");
          //Add to Menu
//        menuFile.add(menuImportI);
//        menuFile.add(menuImportR);
//        menuFile.addSeparator();
//        menuFile.add(menuExit);
          //Set up Menu Bar
//        menuBar.add(menuFile);
//        menuBar.add(menuHelp);
//        setJMenuBar(menuBar);

        //Layout - Main
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(ingredientPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(recipePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(ingredientPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(recipePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
    }

    public static void main(String[] args) throws FileNotFoundException {
        repository.importRecipeFiles("./files/repository.txt");
        new FoodAppGUI().setVisible(true);
    }

}
