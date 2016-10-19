package net.role4j.view;

/**
 * Created by nguonly on 10/19/16.
 */
public class NodeInfoUI {
    public int id; //real hash code
    public String className;
    public int proxyHashCode;


    public NodeInfoUI(){}

    public NodeInfoUI(String clsName, int realHashCode, int proxyHashCode){
        id = realHashCode;
        className = clsName;
        this.proxyHashCode = proxyHashCode;
    }


    public String toString() {
        return String.format("%s[%d:%d]", className, id, proxyHashCode);
    }

    public boolean equals(Object node) {
        NodeInfoUI n = (NodeInfoUI)node;
        return this.id == n.id;
    }
}
