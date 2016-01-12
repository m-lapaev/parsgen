package ru.itmo.gui.screens;

import ru.itmo.controller.Controller;
import ru.itmo.gui.components.*;
import ru.itmo.model.SelectedOption;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Администратор on 20.10.15.
 */
public class WalkaroundSettings extends Screen {
    OptionButton[] options = {
            new OptionButton(SelectedOption.WITHIN_INTERVAL),
            new OptionButton(SelectedOption.WITHIN_CONTENT),
            new OptionButton(SelectedOption.WITHIN_THE_LIST)
    };
    ButtonGroup group = new ButtonGroup();

    WalkaroundPanel[] panels = {
            new ContentWalkaround(),
            new ListWalkaround(),
            new IntervalWalkaround()
    };
    WalkaroundPanel currentPanel = new ContentWalkaround();

    public WalkaroundSettings() {
        setActions();
        initComponents();
    }

    private void setActions() {
        for (int i = 0; i < options.length; i++) {
            options[i].addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (((OptionButton) e.getSource()).isSelected()) {
                        System.out.println(((OptionButton) e.getSource()).getOption());
                        displayOptions(((OptionButton) e.getSource()).getOption());
                    }
                }
            });
        }
    }

    private void initComponents() {
        JPanel west = new JPanel(new GridLayout(3, 1, 5, 5));
        west.setPreferredSize(new Dimension(300, 100));
        for (int i = 0; i < options.length; i++) {
            group.add(options[i]);
            west.add(options[i]);
        }
        setLayout(new BorderLayout());
        JPanel westContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        westContainer.add(west);
        add(westContainer, BorderLayout.WEST);
        setActions();
        add(currentPanel, BorderLayout.CENTER);
        options[0].setSelected(true);
    }

    @Override
    public boolean processData() {
        Controller.getInstance().setParser(currentPanel.getWalkaroundCode());
        return true;
    }

    @Override
    public void doPrerequisits() {

    }

    private void displayOptions(SelectedOption option) {
        remove(currentPanel);
        switch (option) {
            case WITHIN_CONTENT:
                currentPanel = panels[0];
                break;
            case WITHIN_THE_LIST:
                currentPanel = panels[1];
                break;
            case WITHIN_INTERVAL:
                currentPanel = panels[2];
                break;
            default:
                throw new IllegalArgumentException();
        }
        add(currentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

}
