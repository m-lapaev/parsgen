package ru.itmo.gui;

import ru.itmo.controller.Controller;
import ru.itmo.gui.components.Screen;
import ru.itmo.gui.screens.CodeGenerator;
import ru.itmo.gui.screens.DBSettings;
import ru.itmo.gui.screens.WalkaroundSettings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Администратор on 14.10.15.
 */
public class Wizard extends JFrame {
    Screen[] frames = {new DBSettings(),
            new CodeGenerator(),
            new WalkaroundSettings()};
    JButton next = new JButton("Next >");
    JButton previous = new JButton("< Previous");
    JButton exit = new JButton("Quit");
    Screen currentScreen = frames[0];

    public Wizard() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultLookAndFeelDecorated(true);
        setMaximizedBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setResizable(false);
        initComponents();
        setActions();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 30, 10));
        buttonPanel.setPreferredSize(new Dimension(400, 35));
        add(south, BorderLayout.SOUTH);
        south.add(buttonPanel);
        buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        buttonPanel.add(exit);
        buttonPanel.add(previous);
        buttonPanel.add(next);
        add(currentScreen, BorderLayout.CENTER);
    }

    private int screenNumber = 0;

    private Screen getNext() {
        if (screenNumber < frames.length - 1) {
            screenNumber+=1;
            System.out.println(screenNumber);
            return frames[screenNumber];
        }
        return null;
    }

    private void setActions() {
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(screenNumber == frames.length-1){
                    next.setEnabled(false);
                }
                currentScreen.processData();
                remove(currentScreen);
                currentScreen = getNext();
                if (currentScreen != null) {
                    currentScreen.doPrerequisits();
                    add(currentScreen, BorderLayout.CENTER);
                    revalidate();
                }
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.getInstance().generateClasses();
            }
        });
    }
}
