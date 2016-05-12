package net.role4j;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nguonly on 4/24/16.
 */
public class Relation {
    public Object compartment;
    public Class compartmentType;
    public Object proxyCompartment;

    public Object proxyObject;
    public Object object;
    public Class objectType;
    public Object player;
    public Object proxyPlayer;
    public List<Class<?>> playerType;
    public Object role;
    public Object proxyRole;
    public Class roleType;
    public int sequence;
    public int level; //Depth of the deep role relation

    /**
     * Return the string of real object not proxy
     * @return
     */
//    @Override
//    public String toString(){
//        String comp = compartment==null?"":compartment.hashCode() + ":" + DumpHelper.getSimpleMethodName(compartmentType.getSimpleName());
//        String str = String.format("%30s %30s %30s %30s %5s %5s",
//                comp,
//                object.hashCode()+ ":" + DumpHelper.getSimpleMethodName(objectType.getSimpleName()),
//                player.hashCode()+ ":" + DumpHelper.getSimpleMethodName((playerType.get(0)).getSimpleName()),
//                role.hashCode()+ ":" + roleType.getSimpleName(),
//                level, sequence);
//        return str;
//    }

    /**
     * Return the string of proxy object not real
     * @return
     */
    @Override
    public String toString(){
        String comp = compartment==null?"":proxyCompartment.hashCode() + ":" + compartmentType.getSimpleName();
        String str = String.format("%30s %30s %30s %30s %5s %5s",
                comp,
                proxyObject.hashCode()+ ":" + objectType.getSimpleName(),
                proxyPlayer.hashCode()+ ":" + (playerType.get(0)).getSimpleName(),
                proxyRole.hashCode()+ ":" + roleType.getSimpleName(),
                level, sequence);
        return str;
    }

    public void display(){
        System.out.println("Obj=" + object.hashCode() + " Role="+role.hashCode());
    }

    public List<Relation> nextBindingLevelRoles(ICompartment compartment){
        ArrayDeque<Relation> dequeRelations = Registry.getRegistry().getRelations();

        List<Relation> list = dequeRelations.stream()
                .filter(p -> p.proxyCompartment.equals(compartment) && p.proxyPlayer.equals(proxyRole) && p.level==level+1)
                .collect(Collectors.toList());

        return list;
    }

    public static Relation findMaxBindingSequence(List<Relation> relations){
        Optional<Relation> relation = relations.stream()
                .max(Comparator.comparing(p->p.sequence));

        return relation.isPresent()?relation.get():null;
    }
}