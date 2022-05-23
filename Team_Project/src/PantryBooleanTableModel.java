package Foodie;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/*
 * Authors: Emily Williams and John Cole
 * Program: Team Foodie Project
 * Date: 5-9-22
 * CMIS 495
 * */

public class PantryBooleanTableModel extends AbstractTableModel {

    //Attributes for Default
    String[] pColumnNames = {"", "Qty", "Ingredients"};
    Object[][] pData = Pantry.createTable();

    public void createDataTable() {
        pColumnNames = new String[]{"", "Qty", "Ingredients"};
        pData = Pantry.createTable();
    }

    @Override
    public int getRowCount() {
        return pData.length;
    }

    @Override
    public int getColumnCount() {
        return pColumnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return pData[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return pColumnNames[column];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0 || FoodAppGUI.isSelected == true) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteRow(ArrayList<Ingredient> pantry) {
        for (int rowIndex = pantry.size() - 1; rowIndex >= 0; rowIndex--) {
            if ((Boolean) getValueAt(rowIndex, 0) == true) {
                pantry.remove(rowIndex);
            }
        }
        updateTable();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        pData[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    // This method is used by the JTable to define the default
    // renderer or editor for each cell. For example if you have
    // a boolean data it will be rendered as a checkbox. A
    // number value is right aligned.
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return pData[0][columnIndex].getClass();
    }

    public void updateTable() {
        createDataTable();
        fireTableDataChanged();
    }
}
