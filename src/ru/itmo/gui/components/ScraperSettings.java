package ru.itmo.gui.components;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.itmo.gui.components.Screen;
import ru.itmo.gui.miscellaneous.Options;
import ru.itmo.model.Field;
import ru.itmo.model.PageNode;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Администратор on 06.08.15.
 */
public class ScraperSettings extends JPanel {
    JScrollPane scroll;
    JTree dom = new JTree();
    DefaultTreeModel model;
    ArrayList<PageNode> els = new ArrayList<PageNode>();
    private long idCounter = 0;
    JEditorPane web = new JEditorPane();
    JScrollPane editorScroll;
    Document doc;
    Options options;
    Field field;
    String lastUrl = "";

    public ScraperSettings() {
        setGui();
        setActions();
    }

    private void setGui() {
        setSize(new Dimension(1000, 800));
        dom = new JTree();
        setLayout(new BorderLayout());
        JPanel north = new JPanel(new FlowLayout());
        scroll = new JScrollPane(dom);
        add(north, BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout());
        web = new JEditorPane();
        editorScroll = new JScrollPane(web);
        editorScroll.setPreferredSize(new Dimension(1, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - 100)));
        center.add(editorScroll, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        add(center);
        web.setContentType("text/html");
        web.setEditable(false);
        dom.setModel(null);
        dom.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    private void setActions() {
        dom.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (options != null) {
                    remove(options);
                }
                if (dom.getSelectionPath() != null) {
                    add(options = new Options(doc, (PageNode) dom.getSelectionPath().getLastPathComponent()
                            , new Dimension(400, 800), field), BorderLayout.EAST);
                }
                revalidate();
            }
        });
    }

    private void insertIntoTree(PageNode currentNode) {
        for (PageNode category : els) {
            if (category.getParentId() == currentNode.getId()) {
                currentNode.add(category);
                insertIntoTree(category);
            }
        }
    }

    private void walkAround(Element element, long parent) {
        els.add(new PageNode(idCounter, parent, element.toString(), element));
        final long nextParent = idCounter;
        idCounter++;
        for (Element el : element.children()) {
            walkAround(el, nextParent);
        }
    }

    private PageNode getRoot(ArrayList<PageNode> nodes) {
        for (PageNode el : nodes) {
            if (el.getParentId() == -1) {
                return el;
            }
        }
        return null;
    }

    public void displayPage(String uri, Field field) {
        try {
            if (!lastUrl.equals(uri)) {
                web.setPage(uri);
                els.clear();
                idCounter = 0;
                doc = Jsoup.connect(uri).get();
                walkAround(doc, -1);
                PageNode root = getRoot(els);
                model = new DefaultTreeModel(root);
                insertIntoTree(root);
                dom.setModel(model);
                this.field = field;
                dom.setSelectionRow(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptChanges() {
        field.setCodeSnippet(options.getCodeSnippet());
    }
}
