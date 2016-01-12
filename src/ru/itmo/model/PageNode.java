package ru.itmo.model;

import org.jsoup.nodes.Element;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Администратор on 16.09.15.
 */
public class PageNode extends DefaultMutableTreeNode {
    private final long id;
    private final long parentId;
    private final String nodeData;
    private final Element element;

    public PageNode(long id, long parentId, String nodeData, Element element) {
        this.id = id;
        this.parentId = parentId;
        this.nodeData = nodeData;
        this.element = element;
    }

    public long getId() {
        return id;
    }

    public long getParentId() {
        return parentId;
    }

    public String getNodeData() {
        return nodeData;
    }


    public String toString() {
        return nodeData;
    }

    public int hashCode() {
        return (int) id;
    }

    public boolean equals(Object obj) {
        if (((PageNode) obj).getId() == id) {
            return true;
        }
        return false;
    }

}
