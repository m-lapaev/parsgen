package ru.itmo.model;

import ru.itmo.gui.screens.DBSettings;

import java.util.ArrayList;

/**
 * Created by Администратор on 14.10.15.
 */
public class Field {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DBSettings.DataType getDataType() {
        return dataType;
    }

    public void setDataType(DBSettings.DataType dataType) {
        this.dataType = dataType;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Field(String name, DBSettings.DataType dataType, String length, boolean mustBeNotNull) {
        this.name = name;
        this.dataType = dataType;
        this.length = length;
        this.mustBeNotNull = mustBeNotNull;
    }

    private String name;
    private DBSettings.DataType dataType;
    private String length;
    private boolean mustBeNotNull;

    public ArrayList<String> getCodeSnippet() {
        return codeSnippet;
    }

    public boolean mustBeNotNull() {
        return this.mustBeNotNull;
    }

    public void setCodeSnippet(ArrayList<String> codeSnippet) {
        this.codeSnippet = codeSnippet;
    }

    private ArrayList<String> codeSnippet;
}
