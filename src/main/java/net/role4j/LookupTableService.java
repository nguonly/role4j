package net.role4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by nguonly on 4/27/16.
 */
public class LookupTableService {
    static Registry _reg = Registry.getRegistry();
//    private static ArrayDeque<Relation> _relations = _reg.getRelations(); //This cause errors

    public static Relation getLevelAndSequence(ICompartment compartment, IPlayer node){
//        System.out.println(node.hashCode());
        ArrayDeque<Relation> relations = _reg.getRelations();
        Optional<Relation> optRelation = relations.stream()
                .filter(p -> p.proxyCompartment.equals(compartment) && p.proxyRole.equals(node))
                .findFirst();

        Relation r = new Relation();

        if(optRelation.isPresent()){
            Relation relation = optRelation.get();

//        System.out.println(relation);
//            List<Relation> list = relations.stream()
//                    .filter(p -> p.compartment.equals(compartment) && p.proxyPlayer.equals(node) && p.level == relation.level +1)
//                    .collect(Collectors.toList());

            Optional<Relation> list = relations.stream()
                    .filter(p -> p.proxyCompartment.equals(compartment) && p.proxyPlayer.equals(node) && p.level == relation.level +1)
                    .max(Comparator.comparing(p->p.sequence));

//        System.out.println(list.size());
//        list.forEach(c-> c.display());


            r.level = relation.level + 1;
//            r.sequence = list.size() + 1;
            r.sequence = (list.isPresent()?list.get().sequence:0) + 1;

        }else{
            r.level = 1;
            r.sequence = 1;
        }
//        Relation relation = optRelation.get();
//
////        System.out.println(relation);
//        List<Relation> list = _relations.stream()
//                .filter(p -> p.player.equals(node) && p.level == relation.level +1)
//                .collect(Collectors.toList());
//
////        System.out.println(list.size());
////        list.forEach(c-> c.display());
//
//
//        r.level = relation.level + 1;
//        r.sequence = list.size() + 1;

        return r;
    }


    public static Relation getLevelAndSequenceFromRole(ICompartment compartment, IPlayer node){
        ArrayDeque<Relation> relations = _reg.getRelations();
        Optional<Relation> optRelation = relations.stream()
                .filter(p -> p.proxyCompartment.equals(compartment) && p.role.equals(node))
                .findFirst();

        Relation r = new Relation();

        if(optRelation.isPresent()){
            Relation relation = optRelation.get();

            Optional<Relation> list = relations.stream()
                    .filter(p -> p.proxyCompartment.equals(compartment) && p.player.equals(node) && p.level == relation.level +1)
                    .max(Comparator.comparing(p->p.sequence));

            r.level = relation.level + 1;
//            r.sequence = list.size() + 1;
            r.sequence = (list.isPresent()?list.get().sequence:0) + 1;

        }else{
            r.level = 1;
            r.sequence = 1;
        }


        return r;
    }

    public static List<Relation> getLatestVersionRelations(int proxyCompartment){
        ArrayDeque<Relation> relations = _reg.getRelations();

        int maxVersion = getLatestVersion(proxyCompartment);

        List<Relation> maxRelations = relations.stream()
                .filter(p -> p.proxyCompartment.hashCode()==proxyCompartment && p.versionCounter==maxVersion)
                .collect(Collectors.toList());

        return maxRelations;
    }

    public static int getLatestVersion(int proxyCompartment){
        ArrayDeque<Relation> relations = _reg.getRelations();
        OptionalInt max = relations.stream()
                .filter(p -> p.proxyCompartment.hashCode()==proxyCompartment)
                .mapToInt(p->p.versionCounter).max();

        return max.isPresent()? max.getAsInt() : 0;
    }
}
