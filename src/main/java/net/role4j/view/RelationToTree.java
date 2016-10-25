package net.role4j.view;

import net.role4j.Compartment;
import net.role4j.Registry;
import net.role4j.Relation;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by nguonly on 5/11/16.
 */
public class RelationToTree {
    public static Tree<NodeInfo> convertRelationToTree(){
        Registry reg = Registry.getRegistry();
        ArrayDeque<Relation> relations = reg.getRelations();

        NodeInfo root = new NodeInfo(0, Object.class);

        Tree<NodeInfo> tree = new Tree<>(root);

        //1. Select all cores
        List<Relation> coreRelations = relations.stream()
                .filter(p -> p.proxyObject == p.proxyRole && p.level==0 && p.sequence==0)
                .collect(Collectors.toList());

        //2. for each core, converting to Tree
        for(Relation core: coreRelations){
            List<Relation> traversedRelations = reg.traverseRelation(core);

            NodeInfo coreInfo = new NodeInfo(core.proxyObject.hashCode(), core.objectType);
            //3. add the core object to tree
            Tree<NodeInfo> coreNode = tree.addChild(coreInfo);

            //4. traversing the all the roles belong the each core object
            assignTraversedRelationToTree(coreNode, traversedRelations);
        }

        return tree;
    }

    private static void assignTraversedRelationToTree(Tree<NodeInfo> tree, List<Relation> relations){
        for(Relation r: relations){
            NodeInfo n = new NodeInfo(r.proxyPlayer.hashCode(), r.player.getClass());
            Tree<NodeInfo> node = tree.find(n, tree);
            node.addChild(new Tree<>(new NodeInfo(r.proxyRole.hashCode(), r.roleType, r.proxyCompartment.hashCode(), r.compartmentType)));
        }
    }

    public static Tree<NodeInfoUI> convertRelationToTreeWithCompartment(){
        Registry reg = Registry.getRegistry();
        ArrayDeque<Relation> relations = reg.getRelations();

        NodeInfoUI root = new NodeInfoUI("Root", 0, 0, 0);

        Tree<NodeInfoUI> tree = new Tree<>(root);

        //1. select compartment
        Map<Object, Long> compartments =  relations.stream().collect(
                Collectors.groupingBy(p->p.proxyCompartment, Collectors.counting())
        );

        compartments.forEach((k, v) -> {
            List<Relation> coreRelations = relations.stream()
                    .filter(p -> p.proxyCompartment.equals(k) && p.proxyObject == p.proxyRole && p.level==0 && p.sequence==0)
                    .collect(Collectors.toList());

            Object realComparment = null;
            try {
                 realComparment = k.getClass().getField("_real").get(k);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            NodeInfoUI compartmentNode = new NodeInfoUI(realComparment.getClass().getName(), realComparment.hashCode(), k.hashCode(), k.hashCode());
            Tree<NodeInfoUI> compartmentTree = new Tree<>(compartmentNode);
            tree.addChild(compartmentTree);

            //2. for each core, converting to Tree
            for(Relation core: coreRelations){
                List<Relation> traversedRelations = reg.traverseRelation(core);

                NodeInfoUI coreInfo = new NodeInfoUI(core.objectType.getName(), core.object.hashCode(), core.proxyObject.hashCode(), k.hashCode());
                //3. add the core object to tree
                Tree<NodeInfoUI> coreNode = compartmentTree.addChild(coreInfo);

                //4. traversing the all the roles belong the each core object
                assignTraversedRelationToTreeWithCompartment(coreNode, traversedRelations);
            }
        });
        return tree;
    }

    private static void assignTraversedRelationToTreeWithCompartment(Tree<NodeInfoUI> tree, List<Relation> relations){
        for(Relation r: relations){
            NodeInfoUI n = new NodeInfoUI(r.objectType.getName(), r.player.hashCode(), r.proxyPlayer.hashCode(), r.proxyCompartment.hashCode());
            Tree<NodeInfoUI> node = tree.find(n, tree);
            node.addChild(new Tree<>(new NodeInfoUI(r.roleType.getName(), r.role.hashCode(), r.proxyRole.hashCode(), r.proxyCompartment.hashCode())));
        }
    }
}
