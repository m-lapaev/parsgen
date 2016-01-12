package ru.itmo.gui.screens;

import ru.itmo.Test;
import ru.itmo.controller.Controller;
import ru.itmo.gui.components.DataRenderer;
import ru.itmo.gui.components.ScraperSettings;
import ru.itmo.gui.components.Screen;
import ru.itmo.model.Field;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Администратор on 15.10.15.
 */
public class CodeGenerator extends Screen {
    JTextField url = new JTextField();
    JList<Field> list = new JList<Field>();
    JScrollPane scroll;
    ScraperSettings settings;
    JButton ok = new JButton();
    JLabel variable = new JLabel();
    public static final String OK_ICON = Test.DEFAULT_RESOURCE_PATH + "ok.png";

    @Override
    public boolean processData() {
        return false;
    }

    public CodeGenerator() {
        initComponents();
        setActions();
    }

    private void setActions() {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && url.getText().length() > 0) {
                    showSettings(true, (Field) ((JList) e.getSource()).getSelectedValue());
                }
            }
        });
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSettings(false, null);
                settings.acceptChanges();
            }
        });
    }

    private void showSettings(boolean isToShow, Field field) {
        remove(isToShow ? scroll : settings);
        add(isToShow ? settings : scroll, BorderLayout.CENTER);
        ok.setVisible(isToShow);
        url.setEnabled(!isToShow);
        if (isToShow) {
            variable.setText("[Variable: " + field.getName() + "]; [Data type: " + field.getDataType() + "]");
            settings.displayPage(url.getText(), field);
        }
        revalidate();
        repaint();
    }

    private void initComponents() {
        settings = new ScraperSettings();
        setLayout(new BorderLayout());
        JPanel north = new JPanel(new GridLayout(2, 1));
        JPanel urlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        urlPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        urlPanel.add(new JLabel("Insert data source instance link here:"));
        url.setColumns(50);
        urlPanel.add(url);
        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        settingsPanel.add(variable);
        settingsPanel.add(ok);
        ok.setVisible(false);
        ok.setIcon(new ImageIcon(OK_ICON));
        north.add(urlPanel);
        north.add(settingsPanel);
        variable.setHorizontalAlignment(JLabel.CENTER);
        add(north, BorderLayout.NORTH);
        list.setCellRenderer(new DataRenderer());
        scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);
    }

    public void doPrerequisits() {
        list.setListData(Controller.getInstance().getFields());
    }
}
