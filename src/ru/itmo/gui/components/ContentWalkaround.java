package ru.itmo.gui.components;

import ru.itmo.Test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Администратор on 20.10.15.
 */
public class ContentWalkaround extends WalkaroundPanel {
    JTextField contentLink = new JTextField();
    JTextField match = new JTextField();
    JTextArea description = new JTextArea();
    JTextField baseURL = new JTextField();
    public static final String URL_ICON = Test.DEFAULT_RESOURCE_PATH + "url.png";
    public static final String MATCH_ICON = Test.DEFAULT_RESOURCE_PATH + "regex.png";
    public static final String CONTENT_ICON = Test.DEFAULT_RESOURCE_PATH + "content.png";

    public ContentWalkaround() {
        initComponents();
        setActions();
    }

    private void setActions() {

    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel north = new JPanel(new GridLayout(3, 2, 5, 5));
        north.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(north, BorderLayout.NORTH);
        north.add(new JLabel("Content page: ", new ImageIcon(CONTENT_ICON), JLabel.LEFT));
        north.add(contentLink);
        north.add(new JLabel("Content item template: ", new ImageIcon(MATCH_ICON), JLabel.LEFT));
        north.add(match);
        north.add(new JLabel("Base URL", new ImageIcon(URL_ICON), JLabel.LEFT));
        north.add(baseURL);
        add(description, BorderLayout.CENTER);
    }

    @Override
    public ArrayList<String> getWalkaroundCode() {
        ArrayList<String> walkaround = new ArrayList<String>();
        walkaround.add("public static void main(String[] args) {");
        walkaround.add("try {");
        walkaround.add("db = DBEngine.getInstance(args[0], args[1], args[2]);");
        walkaround.add("Document doc = Jsoup.connect(\"" + contentLink.getText() + "\").get();");
        walkaround.add("Elements els = doc.getElementsByTag(\"a\");");
        walkaround.add("Pattern p = Pattern.compile(\"" + match.getText() + "\");");
        walkaround.add("ArrayList<String> urls = new ArrayList<String>();");
        walkaround.add("Matcher m = null;");
        walkaround.add("for(Element url : els){");
        walkaround.add("m = p.matcher(url.attr(\"href\"));");
        walkaround.add("if (m.matches()) {");
        walkaround.add("urls.add(\"" + baseURL.getText() + "\"+url.attr(\"href\"));");
        walkaround.add("}");
        walkaround.add("}");
        walkaround.add("for (String u : urls) {");
        walkaround.add("System.out.println(u);");
        walkaround.add("Item ad = parseItem(u);");
        walkaround.add("if(ad != null){");
        walkaround.add("addToDB(ad);");
        walkaround.add("}else{");
        walkaround.add("System.out.println(\"FAILED: \"+ u)");
        walkaround.add("}");
        walkaround.add("}");
        walkaround.add("} catch (IOException e) {");
        walkaround.add("e.printStackTrace();");
        walkaround.add("}");
        walkaround.add("}");
        return walkaround;
    }
}
