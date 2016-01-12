package ru.itmo.gui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Created by Администратор on 14.10.15.
 */
public abstract class Screen extends JPanel {
    public abstract boolean processData();
    public abstract void doPrerequisits();

    public Screen() {
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }
}
