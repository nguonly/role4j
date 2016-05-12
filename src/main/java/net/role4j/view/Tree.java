package net.role4j.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nguonly on 5/11/16.
 */
public class Tree<T> {
    private List<Tree<T>> children = new ArrayList<>();
    private Tree<T> parent = null;
    private T data = null;

    public Tree(T data) {
        this.data = data;
    }

//    public Tree(T data, Tree<T> parent) {
//        this.data = data;
//        this.parent = parent;
//    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public void setParent(Tree<T> parent) {
        this.parent = parent;
    }

    public Tree<T> getParent() {
        return this.parent;
    }

    public Tree<T> addChild(T data) {
        Tree<T> child = new Tree<T>(data);
        child.setParent(this);
        this.children.add(child);

        return child;
    }

    public void addChild(Tree<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if (this.children.size() == 0)
            return true;
        else
            return false;
    }

    public void removeParent() {
        this.parent = null;
    }

    public Tree<T> find(T item, Tree<T> node){
        if (node.getData().equals(item))
            return node;

        List<Tree<T>> children = node.getChildren();
        Tree<T> res = null;
        for (int i = 0; res == null && i < children.size(); i++) {
            res = find(item, children.get(i));
        }
        return res;
    }

    public void printNode(String prefix){
        System.out.format("%s + %s\n", prefix, data);
        for(Tree<T> n : children){
            if(children.indexOf(n) == children.size() -1){
                n.printNode(prefix + "    ");
            }else{
                n.printNode(prefix + "   |");
            }
        }
    }
}
