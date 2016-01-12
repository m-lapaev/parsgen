package ru.itmo.gui.screens;

import ru.itmo.Test;
import ru.itmo.controller.Controller;
import ru.itmo.db.DBEngine;
import ru.itmo.model.DBTableModel;
import ru.itmo.model.Field;
import ru.itmo.gui.components.Screen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Администратор on 14.10.15.
 */
public class DBSettings extends Screen {
    JTextField host = new JTextField("localhost");
    JTextField port = new JTextField("5432");
    JTextField user = new JTextField("postgres");
    JTextField pass = new JTextField("12345");
    JTextField dbName = new JTextField("postgres");
    JTextField tableName = new JTextField("test");
    JTable table = new JTable(new DBTableModel());
    JScrollPane scroll;
    JButton add = new JButton("Add field ...");
    JButton remove = new JButton("Remove selected");
    JPanel addDialog = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JTextField fieldName = new JTextField();
    JComboBox<DataType> dataType = new JComboBox<DataType>();
    JTextField dataSize = new JTextField();
    JButton ok = new JButton();
    JButton cancel = new JButton();
    JPanel west = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JCheckBox mustBeNotNull = new JCheckBox("Not NULL");
    public static final String OK_ICON = Test.DEFAULT_RESOURCE_PATH + "ok.png";
    public static final String CANCEL_ICON = Test.DEFAULT_RESOURCE_PATH + "cancel.png";
    public static final String ADD_ICON = Test.DEFAULT_RESOURCE_PATH + "add.png";
    public static final String REMOVE_ICON = Test.DEFAULT_RESOURCE_PATH + "remove.png";

    public DBSettings() {
        initComponents();
        setActions();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel dbSettings = new JPanel(new GridLayout(7, 2, 10, 10));
        dbSettings.setPreferredSize(new Dimension(430, 210));
        JPanel settings = new JPanel(new FlowLayout(FlowLayout.CENTER));
        settings.add(dbSettings);
        settings.setPreferredSize(new Dimension(450, 220));
        west.setPreferredSize(new Dimension(500, 1));
        west.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(west, BorderLayout.WEST);
        west.add(settings);
        settings.setBorder(BorderFactory.createEtchedBorder());
        dbSettings.add(new JLabel("Database", JLabel.RIGHT));
        dbSettings.add(new JLabel("settings"));
        dbSettings.add(new JLabel("Host:"));
        dbSettings.add(host);
        dbSettings.add(new JLabel("Port:"));
        dbSettings.add(port);
        dbSettings.add(new JLabel("User:"));
        dbSettings.add(user);
        dbSettings.add(new JLabel("Password:"));
        dbSettings.add(pass);
        dbSettings.add(new JLabel("Schema name:"));
        dbSettings.add(dbName);
        dbSettings.add(new JLabel("Table name:"));
        dbSettings.add(tableName);
        scroll = new JScrollPane(table);
        add(scroll);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.setPreferredSize(new Dimension(450, 80));
        buttons.add(add);
        buttons.add(remove);
        west.add(buttons);
        JPanel itemContainer = new JPanel(new GridLayout(5, 2, 10, 10));
        itemContainer.setPreferredSize(new Dimension(430, 160));
        addDialog.setPreferredSize(new Dimension(450, 240));
        addDialog.add(itemContainer);
        itemContainer.add(new JLabel("Field name:"));
        itemContainer.add(fieldName);
        itemContainer.add(new JLabel("Field data type:"));
        itemContainer.add(dataType);
        itemContainer.add(new JLabel("Field data size:"));
        itemContainer.add(dataSize);
        itemContainer.add(mustBeNotNull);
        addDialog.add(cancel);
        addDialog.add(ok);
        west.add(addDialog);
        for (DataType data : DataType.values()) {
            dataType.addItem(data);
        }
        displayDialog(false);
        ok.setIcon(new ImageIcon(OK_ICON));
        cancel.setIcon(new ImageIcon(CANCEL_ICON));
        add.setIcon(new ImageIcon(ADD_ICON));
        remove.setIcon(new ImageIcon(REMOVE_ICON));
        addDialog.setBorder(BorderFactory.createEtchedBorder());
    }

    private void setActions() {
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDialog(true);
            }
        });
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isDataValid()) {
                    ((DBTableModel) table.getModel()).addData(new Field(fieldName.getText(), (DataType) dataType.getSelectedItem(), dataSize.getText(), mustBeNotNull.isSelected()));
                    displayDialog(false);
                }
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDialog(false);
            }
        });

        dataType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dataType.getSelectedItem().equals(DataType.STRING)) {
                    dataSize.setEditable(true);
                } else {
                    dataSize.setEditable(false);
                    dataSize.setText("DEFAULT SIZE");
                }
            }
        });

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sel = table.getSelectedRow();
                if (sel >= 0) {
                    ((DBTableModel) table.getModel()).removeRow(sel);
                }
            }
        });
    }

    private void displayDialog(boolean isVisible) {
        addDialog.setVisible(isVisible);
        dataSize.setEditable(false);
        dataSize.setText("DEFAULT SIZE");
        fieldName.setText("");
        dataType.setSelectedIndex(0);

    }

    private boolean isDataValid() {
        return !fieldName.getText().equals("") &&
                !fieldName.getText().contains(" ") &&
                (dataSize.getText().equals("DEFAULT SIZE") || Integer.parseInt(dataSize.getText()) > 0);
    }

    @Override
    public boolean processData() {
        ArrayList<Field> fields = ((DBTableModel) table.getModel()).getData();
        String settings = "jdbc:postgresql://"
                + host.getText() + ":"
                + port.getText() + "/"
                + dbName.getText();
        Controller.getInstance().setFields(fields);
        Controller.getInstance().setConnectionSettings(settings);
        Controller.getInstance().setUser(user.getText());
        Controller.getInstance().setPassword(pass.getText());
        Controller.getInstance().setTableName(tableName.getText());
        return DBEngine.getInstance(settings, user.getText(), pass.getText()
        ).createTable(tableName.getText(), fields);
    }

    @Override
    public void doPrerequisits() {

    }

    public enum DataType {
        INTEGER,
        STRING,
        FLOAT,
        BOOLEAN
    }
}
