package ru.itmo.gui.miscellaneous;

import org.jsoup.nodes.Document;
import ru.itmo.gui.components.OptionButton;
import ru.itmo.model.Field;
import ru.itmo.model.PageNode;
import ru.itmo.model.SelectedOption;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

/**
 * Created by Администратор on 16.09.15.
 */
public class Options extends JPanel {
    PageNode nodeData;
    Document doc;
    JTextArea data = new JTextArea();
    JButton generate = new JButton("Generate");
    JTextArea code = new JTextArea();
    JScrollPane codeScroll;
    JTextArea result = new JTextArea();
    JScrollPane dataScroll;
    JScrollPane resultScroll;
    final Field field;

    OptionButton[] buttons = {
            new OptionButton(SelectedOption.BY_AT_VALUE_STARTING),
            new OptionButton(SelectedOption.BY_AT_VALUE_ENDING),
            new OptionButton(SelectedOption.BY_AT_VALUE_CONTAINING),
            new OptionButton(SelectedOption.BY_CLASS_STARTING),
            new OptionButton(SelectedOption.BY_TAG),
            new OptionButton(SelectedOption.BY_ID_VALUE),
            new OptionButton(SelectedOption.BY_NAME_VALUE),
            new OptionButton(SelectedOption.BY_STYLE_CONTAINING)
    };

    ButtonGroup group = new ButtonGroup();

    public Options(final Document doc, PageNode nodeData, Dimension size, final Field field) {
        this.field = field;
        this.nodeData = nodeData;
        this.doc = doc;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(size);
        JPanel north = new JPanel(new GridLayout(8, 1));
        north.setPreferredSize(new Dimension((int) size.getWidth(), 150));
        for (int i = 0; i < buttons.length; i++) {
            group.add(buttons[i]);
            north.add(buttons[i]);
        }
        dataScroll = new JScrollPane(data);
        add(dataScroll);
        dataScroll.setPreferredSize(new Dimension((int) size.getWidth(), 180));
        data.setEditable(false);
        data.setText(nodeData.getNodeData());
        add(north);
        add(generate);
        codeScroll = new JScrollPane(code);
        codeScroll.setPreferredSize(new Dimension((int) size.getWidth(), 180));
        add(codeScroll);
        resultScroll = new JScrollPane(result);
        add(resultScroll);
        resultScroll.setPreferredSize(new Dimension((int) size.getWidth(), 100));
        code.setLineWrap(true);
        result.setLineWrap(true);
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectedOption o = getSelectedOption();
                switch (o) {
                    case BY_AT_VALUE_STARTING:
                        result.setText(doc.getElementsByAttributeValueStarting("class", data.getSelectedText()).text());
                        code.setText("item.set" + field.getName() + "(" +
                                "(el=doc.getElementsByAttributeValueStarting(\"class\", \"" + data.getSelectedText() + "\")).size() > 0 ? " +
                                "el.text() : null"
                                + ");");
                        break;
                    case BY_AT_VALUE_ENDING:
                        result.setText(doc.getElementsByAttributeValueEnding("class", data.getSelectedText()).text());
                        code.setText("item.set" + field.getName() + "(" +
                                "(el=doc.getElementsByAttributeValueEnding(\"class\", \"" + data.getSelectedText() + "\")).size() > 0 ? " +
                                "el.text() : null"
                                + ");");
                        break;
                    case BY_AT_VALUE_CONTAINING:
                        result.setText(doc.getElementsByAttributeValueContaining("class", data.getSelectedText()).text());
                        code.setText("item.set" + field.getName() + "(" +
                                "(el=doc.getElementsByAttributeValueContaining(\"class\", \"" + data.getSelectedText() + "\")).size() > 0 ? " +
                                "el.text() : null"
                                + ");");
                        break;
                    case BY_CLASS_STARTING:
                        result.setText(doc.getElementsByClass(data.getSelectedText()).text());
                        code.setText("item.set" + field.getName() + "(" +
                                "(el=doc.getElementsByClass(\"" + data.getSelectedText() + "\")).size() > 0 ? " +
                                "el.text() : null"
                                + ");");
                        break;
                    case BY_TAG:
                        result.setText(doc.getElementsByTag(data.getSelectedText()).text());
                        code.setText("item.set" + field.getName() + "(" +
                                "(el=doc.getElementsByTag(\"" + data.getSelectedText() + "\")).size() > 0 ? " +
                                "el.text() : null"
                                + ");");
                        break;
                    case BY_ID_VALUE:
                        result.setText(doc.getElementsByAttributeValueContaining("id", data.getSelectedText()).text());
                        code.setText("item.set" + field.getName() + "(" +
                                "(el=doc.getElementsByAttributeValueContaining(\"id\", \"" + data.getSelectedText() + "\")).size() > 0 ? " +
                                "el.text() : null"
                                + ");");
                        break;
                    case BY_NAME_VALUE:
                        result.setText(doc.getElementsByAttributeValueContaining("name", data.getSelectedText()).text());
                        code.setText("item.set" + field.getName() + "(" +
                                "(el=doc.getElementsByAttributeValueContaining(\"name\", \"" + data.getSelectedText() + "\")).size() > 0 ? " +
                                "el.text() : null"
                                + ");");
                        break;
                    case BY_STYLE_CONTAINING:
                        result.setText(doc.getElementsByAttributeValueContaining("style", data.getSelectedText()).text());
                        code.setText("item.set" + field.getName() + "(" +
                                "(el=doc.getElementsByAttributeValueContaining(\"style\", \"" + data.getSelectedText() + "\")).size() > 0 ? " +
                                "el.text() : null"
                                + ");");
                        break;
                    default:
                        break;
                }
                if (field.mustBeNotNull()) {
                    code.append("\n if(el.size() == 0) return null;");
                }
            }
        });
    }

    private SelectedOption getSelectedOption() {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isSelected()) {
                return buttons[i].getOption();
            }
        }
        return SelectedOption.NONE;
    }

    public ArrayList<String> getCodeSnippet() {
        List list = Arrays.asList(code.getText().split("\n"));
        ArrayList<String> code = new ArrayList<String>();
        for (Object o : list) {
            code.add((String) o);
        }
        return code;
    }

}
