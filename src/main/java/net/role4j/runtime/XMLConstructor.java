package net.role4j.runtime;

import net.role4j.view.NodeInfoUI;

/**
 * Created by nguonly on 10/24/16.
 */
public class XMLConstructor {
    private StringBuffer buffer = new StringBuffer();

    public void openClosure(){
        buffer.append("<?xml version=\"1.0\"?>\n");
        buffer.append("<adaptation>\n");
    }

    public void closeClosure(){
        buffer.append("</adaptation>\n");
    }

    public void append(String text){
        buffer.append(text + "\n");
    }

    @Override
    public String toString(){
        return buffer.toString();
    }

    public static String getXMLCoreBindOperation(NodeInfoUI node){
        return getXMLBindingBaseOperation(node, true, true);
    }

    public static String getXMLCoreRebindOperation(NodeInfoUI node){
        return getXMLBindingBaseOperation(node, true, false);
    }

    public static String getXMLRoleBindingOperation(NodeInfoUI node){
        return getXMLBindingBaseOperation(node, false, true);
    }

    public static String getXMLRoleRebindOperation(NodeInfoUI node){
        return getXMLBindingBaseOperation(node, false, false);
    }

    public static String getXMLCoreUnbindOperation(NodeInfoUI node, NodeInfoUI parentNode){
        return getXMLUnbindBaseOperation(node, parentNode, true);
    }

    public static String getXMLRoleUnbindOperation(NodeInfoUI node, NodeInfoUI parentNode){
        return getXMLUnbindBaseOperation(node, parentNode, false);
    }

    private static String getXMLBindingBaseOperation(NodeInfoUI node, boolean isCore, boolean isForBinding){
        XMLConstructor xml = new XMLConstructor();

        String strOperation = isForBinding?"bind":"rebind";
        String strActor = isCore?"coreId":"roleId";

        xml.openClosure();
        xml.append("<compartment id=\"" + node.getCompartmentId() + "\" >");
        xml.append("<" + strOperation + " " + strActor + "=\"" + node.getProxyHashCode() +"\" roleType=\"\" />");
        xml.append("</compartment>");
        xml.closeClosure();

        return xml.toString();
    }

    private static String getXMLUnbindBaseOperation(NodeInfoUI node, NodeInfoUI parentNode, boolean isCore){
        XMLConstructor xml = new XMLConstructor();

        String strActor = isCore?"coreId":"roleId";

        xml.openClosure();
        xml.append("<compartment id=\"" + node.getCompartmentId() + "\" >");
        xml.append("<unbind " + strActor + "=\"" + parentNode.getProxyHashCode() +"\" roleType=\"" + node.getClassName() + "\" />");
        xml.append("</compartment>");
        xml.closeClosure();

        return xml.toString();
    }
}
