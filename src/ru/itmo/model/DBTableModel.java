package ru.itmo.model;


import ru.itmo.gui.screens.DBSettings;
import ru.itmo.model.Field;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class DBTableModel extends AbstractTableModel {

    private String[] columnNames = new String[]{
            "Column name",
            "Data type",
            "Field size"
    };

    private Class<?>[] columnClasses = new Class[]{
            String.class,
            DBSettings.DataType.class,
            String.class,
    };

    ArrayList<Field> fields = new ArrayList<Field>();

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }


    public int getColumnCount() {
        return columnNames.length;
    }


    public int getRowCount() {
        return fields.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        String value = "";

        switch (columnIndex) {
            case 0:
                value = fields.get(rowIndex).getName();
                break;
            case 1:
                value = fields.get(rowIndex).getDataType().toString();
                break;
            case 2:
                value = fields.get(rowIndex).getLength();
                break;
            default:
                break;
        }
        return value;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return columnClasses[i];
    }

    public void addData(Field field) {
        fields.add(field);
        fireTableDataChanged();
    }

    public void removeRow(int row){
        fields.remove(row);
        fireTableDataChanged();
    }

    public ArrayList<Field> getData(){
        return fields;
    }
}

