package ru.itmo.gui.components;

import ru.itmo.Test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Администратор on 20.10.15.
 */
public class ListWalkaround extends WalkaroundPanel {
    JList list = new JList();
    JTextField link = new JTextField();
    JButton add = new JButton("");
    JScrollPane scroll;
    JButton clear = new JButton("Clear the list");
    JButton remove = new JButton("Remove selected item");
    public static final String ADD_ICON = Test.DEFAULT_RESOURCE_PATH + "add.png";

    public ListWalkaround() {
        initComponents();
        setActions();
    }

    private void setActions() {

    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER));
        north.add(new JLabel("Insert a link to add: "));
        north.add(link);
        north.add(add);
        add.setIcon(new ImageIcon(ADD_ICON));
        add(north, BorderLayout.NORTH);
        north.setBorder(new EmptyBorder(5, 5, 5, 5));
        scroll = new JScrollPane(list);
        add(scroll, BorderLayout.CENTER);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        south.add(clear);
        south.add(remove);
        add(south, BorderLayout.SOUTH);
        link.setColumns(50);
    }

    @Override
    public ArrayList<String> getWalkaroundCode() {
        ArrayList<String> walkaround = new ArrayList<String>();
        walkaround.add("public static void main(String[] args) {");
        walkaround.add("try {");
        walkaround.add("db = DBEngine.getInstance(args[0], args[1], args[2]);");
        walkaround.add("ArrayList<String> urls = new ArrayList<String>();");
        for (int i = 0; i < list.getModel().getSize(); i++) {
            walkaround.add("urls.add(" + list.getModel().getElementAt(i) + ");");
        }
        walkaround.add("for (String u : urls) {");
        walkaround.add("System.out.println(u);");
        walkaround.add("Item ad = parseItem(u);");
        walkaround.add("if(ad != null){");
        walkaround.add("addToDB(ad);");
        walkaround.add("}else{");
        walkaround.add("System.out.println(\"FAILED: \"+ u)");
        walkaround.add("}");
        walkaround.add("} catch (IOException e) {");
        walkaround.add("e.printStackTrace();");
        walkaround.add("}");
        walkaround.add("}");
        return walkaround;
    }
}
