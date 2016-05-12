package net.role4j.view;

/**
 * Created by nguonly on 5/11/16.
 */
public class NodeInfo {
    public int id;
    public Class className;

    public int compartmentId;
    public Class compartmentName;

    public NodeInfo() {
    }

    public NodeInfo(int id, Class className) {
        this.id = id;
        this.className = className;
    }

    public NodeInfo(int id, Class className, int compId, Class compClass) {
        this.id = id;
        this.className = className;
        this.compartmentId = compId;
        this.compartmentName = compClass;
    }

    public String toString() {
        return String.format("%s:%d [%d]", className.getSimpleName(), id, compartmentId);
    }

    public boolean equals(Object node) {
        NodeInfo n = (NodeInfo)node;
        return this.id == n.id;
    }
}