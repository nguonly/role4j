package net.role4j.runtime;

import net.role4j.CallableMethod;
import net.role4j.DumpHelper;
import net.role4j.Registry;
import net.role4j.Relation;
import net.role4j.view.NodeInfo;
import net.role4j.view.NodeInfoUI;
import net.role4j.view.RelationToTree;
import net.role4j.view.Tree;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by nguonly on 10/19/16.
 */
public class RTCommon {
    static Object[][] data;
    static Object[] col = new Object[] {"Comp", "Core", "Player", "Role", "Bound Time", "Unbound Time", "Lvl", "Seq"};
    public static DefaultTableModel modelRelation = new DefaultTableModel(data, col);

    public static DefaultMutableTreeNode rootRelationTree = new DefaultMutableTreeNode("Root");
    public static DefaultTreeModel modelRelationTree = new DefaultTreeModel(rootRelationTree);

    //For HashCallable
    public static DefaultMutableTreeNode rootHashCallable = new DefaultMutableTreeNode("Root");
    public static DefaultTreeModel modelHashCallable = new DefaultTreeModel(rootHashCallable);

    //For TransHashCallable
    public static DefaultMutableTreeNode rootTransHashCallable = new DefaultMutableTreeNode("Root");
    public static DefaultTreeModel modelTransHashCallable = new DefaultTreeModel(rootTransHashCallable);

    //For Active Transaction Table
    public static DefaultTableModel modelTransactions = new DefaultTableModel(new Object[][]{}, new Object[]{"Thread Id","Trans Id", "Entering Time"});

    public static void performRefresh(){
        Registry reg = Registry.getRegistry();

        reloadRelationTable(reg.getRelations());

        reloadHashCallableTree(reg.getHashCallables(), modelHashCallable, rootHashCallable);

        reloadHashCallableTree(reg.getHashTransCallables(), modelTransHashCallable, rootTransHashCallable);

        rootRelationTree.removeAllChildren();
        modelRelationTree.reload();
        Tree<NodeInfoUI> root = RelationToTree.convertRelationToTreeWithCompartment();

        reloadRelationTree(root, rootRelationTree);
        modelRelationTree.reload(rootRelationTree);

        reloadTransactions(reg.getTransactions());
    }

    private static void reloadRelationTable(ArrayDeque<Relation> relations){
        modelRelation.setNumRows(0); //clear table

        relations.forEach(r -> {
            Vector<String> d = new Vector<>();
            String comp = r.compartment==null?"":r.proxyCompartment.hashCode() + ":" + r.compartmentType.getSimpleName();
            String strBoundTime = r.boundTime!=null?r.boundTime.toLocalTime().toString():"";
            String strUnBoundTime = r.unboundTime!=null?r.unboundTime.toLocalTime().toString():"";

            d.add(comp);
            d.add(r.proxyObject.hashCode() + ":" + r.objectType.getSimpleName());
            d.add(r.proxyPlayer.hashCode() + ":" + r.playerType.get(0).getSimpleName());
            d.add(r.proxyRole.hashCode() + ":" + r.roleType.getSimpleName());

            d.add(strBoundTime);
            d.add(strUnBoundTime);

            d.add(String.valueOf(r.level));
            d.add(String.valueOf(r.sequence));
            modelRelation.addRow(d);
        });
    }

    private static void reloadHashCallableTree(HashMap<Integer, HashMap<Integer, CallableMethod>> callables,
                                               DefaultTreeModel model, DefaultMutableTreeNode root){
        root.removeAllChildren();
        model.reload();

        callables.forEach((k, v) -> {
            DefaultMutableTreeNode lvl1 = new DefaultMutableTreeNode(k);
            v.forEach((ik, iv) -> {
                lvl1.add(new DefaultMutableTreeNode(iv.method + ":" + iv.invokingObject.hashCode()));
            });
            root.add(lvl1);

        });

        model.reload(root);
    }

    private static void reloadRelationTree(Tree<NodeInfoUI> node, DefaultMutableTreeNode uiNode){
        List<Tree<NodeInfoUI>> children = node.getChildren();
        for(Tree<NodeInfoUI> n : children){
            DefaultMutableTreeNode uiNodeNextLevel = new DefaultMutableTreeNode(n.getData());
            uiNode.add(uiNodeNextLevel);
            reloadRelationTree(n, uiNodeNextLevel);
        }
    }

    private static void reloadTransactions(HashMap<Long, AbstractMap.SimpleEntry<Integer, LocalDateTime>> trans){
        modelTransactions.setNumRows(0);

        trans.forEach((k, v) -> {
            Vector d = new Vector();
            d.add(k);
            d.add(v.getKey());
            d.add(v.getValue());

            modelTransactions.addRow(d);
        });
    }

}
