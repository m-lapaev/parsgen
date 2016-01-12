package ru.itmo.gui.components;

import ru.itmo.Test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Администратор on 20.10.15.
 */
public class IntervalWalkaround extends WalkaroundPanel {
    JTextField baseURL = new JTextField();
    JTextField start = new JTextField();
    ImageIcon icon = new ImageIcon(DESCRIPTION);
    JLabel description = new JLabel() {
        public void doLayout() {
            super.doLayout();
            description.setIcon(resizeImage(icon, this.getWidth(),
                    (int) (this.getHeight() * this.getWidth() / icon.getIconWidth())
                    , false));
        }
    };
    JScrollPane descrScroll;
    JTextField end = new JTextField();
    JTextField before = new JTextField();
    JTextField after = new JTextField();
    JTextField complementTo = new JTextField();
    JCheckBox isToComplementByZeroes = new JCheckBox("Complement short numbers by zeros");
    public static final String URL_ICON = Test.DEFAULT_RESOURCE_PATH + "url.png";
    public static final String START_FROM = Test.DEFAULT_RESOURCE_PATH + "start.png";
    public static final String END_AT = Test.DEFAULT_RESOURCE_PATH + "end.png";
    public static final String COMPLEMENT_ICON = Test.DEFAULT_RESOURCE_PATH + "complement.png";
    public static final String EXTRA_ICON = Test.DEFAULT_RESOURCE_PATH + "extra.png";
    public static final String JUSTIFY_ICON = Test.DEFAULT_RESOURCE_PATH + "justify.png";
    public static final String DESCRIPTION = Test.DEFAULT_RESOURCE_PATH + "regex_ref.png";

    public IntervalWalkaround() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel north = new JPanel(new GridLayout(7, 2, 5, 5));
        north.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(north, BorderLayout.NORTH);
        north.add(new JLabel("Base ulr: ", new ImageIcon(URL_ICON), JLabel.LEFT));
        north.add(baseURL);
        north.add(new JLabel("Start from: ", new ImageIcon(START_FROM), JLabel.LEFT));
        north.add(start);
        north.add(new JLabel("End at: ", new ImageIcon(END_AT), JLabel.LEFT));
        north.add(end);
        north.add(new JLabel("Add characters before: ", new ImageIcon(COMPLEMENT_ICON), JLabel.LEFT));
        north.add(before);
        north.add(new JLabel("Add characters after:", new ImageIcon(COMPLEMENT_ICON), JLabel.LEFT));
        north.add(after);
        north.add(new JLabel("Extra settings: ", new ImageIcon(EXTRA_ICON), JLabel.LEFT));
        north.add(isToComplementByZeroes);
        north.add(new JLabel("Complement to ... digits: ", new ImageIcon(JUSTIFY_ICON), JLabel.LEFT));
        north.add(complementTo);
        descrScroll = new JScrollPane(description);
        add(descrScroll, BorderLayout.CENTER);
        description.setIcon(new ImageIcon(DESCRIPTION));

    }

    public static ImageIcon resizeImage(ImageIcon imageIcon, int width, int height, boolean max) {
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(-1, height, java.awt.Image.SCALE_SMOOTH);
        int width1 = newimg.getWidth(null);
        if ((max && width1 > width) || (!max && width1 < width)) {
            newimg = image.getScaledInstance(width, -1, java.awt.Image.SCALE_SMOOTH);
        }
        return new ImageIcon(newimg);
    }

    @Override
    public ArrayList<String> getWalkaroundCode() {
        ArrayList<String> walkaround = new ArrayList<String>();
        walkaround.add("public static void main(String[] args) {");
        walkaround.add("db = DBEngine.getInstance(args[0], args[1], args[2]);");
        walkaround.add("long start = " + start.getText() + "L;");
        walkaround.add("long end = " + end.getText() + "L;");
        walkaround.add("String url = \"\";");
        walkaround.add("for(long i = start; i<=end;i++){");
        //todo left padding value
        walkaround.add("url = \"" + baseURL.getText() + before.getText() +
                (isToComplementByZeroes.isSelected() ? "\" + zeroLeftPad(" + complementTo.getText() + ", i)+\"" : "+ i+ \"")
                + "" + after.getText() + "\";");
        walkaround.add("System.out.println(url);");
        walkaround.add("Item ad = parseItem(url);");
        walkaround.add("if(ad != null){");
        walkaround.add("addToDB(ad);");
        walkaround.add("}else{");
        walkaround.add("System.out.println(\"FAILED: \"+ url);");
        walkaround.add("}");
        walkaround.add("}");
        walkaround.add("}");
        if (isToComplementByZeroes.isSelected()) {
            walkaround.add("private static String zeroLeftPad(int digitNumber, long value) {");
            walkaround.add("StringBuffer sb = new StringBuffer();");
            walkaround.add("if (value < 0) {");
            walkaround.add("sb.append(\"-\");");
            walkaround.add("}");
            walkaround.add("String valueString = String.valueOf(Math.abs(value));");
            walkaround.add("while (sb.length() < digitNumber - valueString.length()) {");
            walkaround.add("sb.append(\"0\");");
            walkaround.add("}");
            walkaround.add("return sb.append(valueString).toString();");
            walkaround.add("}");
        }
        return walkaround;
    }
}
