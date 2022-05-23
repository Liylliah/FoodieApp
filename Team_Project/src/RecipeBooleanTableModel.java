package Foodie;

/*
 * Author: Brandon Schmidt
 * Based off template created by Emily Williams and John Cole
 * Program: Team Foodie Project
 * Date: 5-9-22
 * CMIS 495
 */

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

class RecipeBooleanTableModel extends AbstractTableModel {

    String[] rColumnNames = {"", "Recipes"};
    Object[][] rData = Repository.createTable();

    public void createDataTable() {
        rColumnNames = new String[]{"", "Recipes"};
        rData = Repository.createTable();
    }

    @Override
    public int getRowCount() {
        return rData.length;
    }

    @Override
    public int getColumnCount() {
        return rColumnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rData[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return rColumnNames[column];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 0;
    }

    public void deleteRow(ArrayList<Recipe> repository) {
        for (int rowIndex = repository.size() - 1; rowIndex >= 0; rowIndex--) {
            if ((Boolean) getValueAt(rowIndex, 0) == true) {
                repository.remove(rowIndex);
            }
        }
        updateTable();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        rData[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    // This method is used by the JTable to define the default
    // renderer or editor for each cell. For example if you have
    // a boolean data it will be rendered as a checkbox. A
    // number value is right aligned.
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return rData[0][columnIndex].getClass();
    }

    public void updateTable() {
        createDataTable();
        fireTableDataChanged();
    }

}
