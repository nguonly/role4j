package net.role4j.view;

import net.role4j.common.StringHelper;

/**
 * Created by nguonly on 10/19/16.
 */
public class NodeInfoUI {
    public int id; //real hash code
    public String className;
    public int proxyHashCode;
    private int compartmentId;
    private int coreId;

    public NodeInfoUI(){}

    public NodeInfoUI(String clsName, int realHashCode, int proxyHashCode, int compartmentId){
        id = realHashCode;
        className = clsName;
        this.proxyHashCode = proxyHashCode;
        this.compartmentId = compartmentId;
//        this.coreId = coreId;
    }

    public int getId(){return id;}

    public int getCompartmentId(){return compartmentId;}

    public int getProxyHashCode(){return proxyHashCode;}

    public String getClassName(){return className;}

    public String toString() {
        return String.format("%s[%d:%d]", StringHelper.getSimpleClassName(className), id, proxyHashCode);
    }

    public boolean equals(Object node) {
        NodeInfoUI n = (NodeInfoUI)node;
        return (this.id == n.id) && (this.compartmentId == n.compartmentId);
    }
}
