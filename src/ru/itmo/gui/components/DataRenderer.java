package ru.itmo.gui.components;

import ru.itmo.Test;
import ru.itmo.model.Field;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created by Администратор on 15.10.15.
 */
public class DataRenderer implements ListCellRenderer {
    DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    Color SELECTION = Color.BLUE;
    public static final String DATA_SIZE = Test.DEFAULT_RESOURCE_PATH + "size.png";
    public static final String FIELD_NAME = Test.DEFAULT_RESOURCE_PATH + "field_name.png";
    public static final String DATA_TYPE = Test.DEFAULT_RESOURCE_PATH + "type.png";
    public static final String CODE = Test.DEFAULT_RESOURCE_PATH + "code.png";

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel layout = null;
        if ((value != null) && (value instanceof Field)) {
            Field field = ((Field) value);
            layout = new JPanel();
            layout.setLayout(new BorderLayout());
            layout.setPreferredSize(new Dimension(800, 100));
            JLabel catName = new JLabel(field.getName());
            JPanel label = new JPanel(new GridLayout(3, 2, 5, 5));
            label.setPreferredSize(new Dimension(150, 100));
            label.add(catName);
            JLabel dataType = new JLabel(field.getDataType().toString());
            label.add(dataType);
            JLabel size = new JLabel(field.getLength());
            label.add(size);
            layout.add(label, BorderLayout.WEST);
            JTextArea code = new JTextArea();
            code.setEditable(false);
            JPanel codeContainer = new JPanel(new BorderLayout());
            JLabel numbers = new JLabel();
            numbers.setPreferredSize(new Dimension(200, 95));
            numbers.setIcon(new ImageIcon(CODE));
            codeContainer.add(numbers, BorderLayout.WEST);
            codeContainer.add(code, BorderLayout.CENTER);
            layout.add(codeContainer, BorderLayout.CENTER);
            code.setText("");
            if (field.getCodeSnippet() != null) {
                for (String str : field.getCodeSnippet()) {
                    code.append(str + "\n");
                }
            }
            size.setIcon(new ImageIcon(DATA_SIZE));
            dataType.setIcon(new ImageIcon(DATA_TYPE));
            catName.setIcon(new ImageIcon(FIELD_NAME));
            if (isSelected) {
                layout.setBorder(BorderFactory.createLineBorder(SELECTION));
            } else {
                layout.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        }

        if (layout == null) {
            return defaultRenderer.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);
        }
        return layout;
    }
}
