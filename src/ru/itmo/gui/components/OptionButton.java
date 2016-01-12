package ru.itmo.gui.components;

import ru.itmo.model.SelectedOption;

import javax.swing.*;

public class OptionButton extends JRadioButton {
    SelectedOption option;

    public OptionButton(SelectedOption option) {
        super(option.getValue());
        this.option = option;
    }

    public SelectedOption getOption() {
        return option;
    }
}

