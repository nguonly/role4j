package net.role4j;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
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
    public int sequence; //Ordering of binding
    public int level; //Depth of the deep role relation

    public LocalDateTime boundTime; //role bound time
    public LocalDateTime unboundTime; //role unbound time: Phantom state

    public int versionCounter; //version counter added on 11.02.2017

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
        String strBoundTime = boundTime!=null?boundTime.toLocalTime().toString():"";
        String strUnBoundTime = unboundTime!=null?unboundTime.toLocalTime().toString():"";
        String str = String.format("%30s %12s %12s %30s %30s %30s %5s %5s %5s",
                comp,
                strBoundTime, strUnBoundTime,
                proxyObject.hashCode()+ ":" + objectType.getSimpleName(),
                proxyPlayer.hashCode()+ ":" + (playerType.get(0)).getSimpleName(),
                proxyRole.hashCode()+ ":" + roleType.getSimpleName(),
                level, sequence, versionCounter);
        return str;
    }

    public void display(){
        System.out.println("Obj=" + object.hashCode() + " Role="+role.hashCode());
    }

    public List<Relation> nextBindingLevelRoles(ICompartment compartment){
        ArrayDeque<Relation> dequeRelations = Registry.getRegistry().getRelations();

//        Predicate<Relation> transTime = (p) -> p.boundTime.compareTo(ct)<=0 && (p.unboundTime==null || p.unboundTime.isAfter(ct));

        List<Relation> list = dequeRelations.stream()
                .filter(p -> p.proxyCompartment.equals(compartment) && p.proxyPlayer.equals(proxyRole)
                        && p.level==level+1 && p.unboundTime==null)
                .collect(Collectors.toList());

        return list;
    }

    public static Relation findMaxBindingSequence(List<Relation> relations){
        Optional<Relation> relation = relations.stream()
                .max(Comparator.comparing(p->p.sequence));

        return relation.isPresent()?relation.get():null;
    }

    public static Object getProxyCompartment(ArrayDeque<Relation> relations, int proxyCompartmentId){
        Optional<Relation> relation = relations.stream()
                .filter(p -> p.proxyCompartment.hashCode() == proxyCompartmentId)
                .findFirst();

        return relation.isPresent()?relation.get().proxyCompartment : null;
    }

    public static Object getCompartment(ArrayDeque<Relation> relations, int compartmentId){
        Optional<Relation> relation = relations.stream()
                .filter(p -> p.compartment.hashCode() == compartmentId)
                .findFirst();

        return relation.isPresent()?relation.get().compartment : null;
    }

    public static Object getProxyCore(ArrayDeque<Relation> relations, int proxyCoreId){
        Optional<Relation> relation = relations.stream()
                .filter(p -> p.proxyObject.hashCode() == proxyCoreId)
                .findFirst();

        return relation.isPresent() ? relation.get().proxyObject : null;
    }

    public static Object getProxyPlayer(ArrayDeque<Relation> relations, int proxyPlayerId){
        Optional<Relation> relation = relations.stream()
                .filter(p -> p.proxyPlayer.hashCode() == proxyPlayerId)
                .findFirst();

        return relation.isPresent() ? relation.get().proxyPlayer : null;
    }

    public static Object getProxyRole(ArrayDeque<Relation> relations, int proxyRoleId){
        Optional<Relation> relation = relations.stream()
                .filter(p -> p.proxyRole.hashCode() == proxyRoleId)
                .findFirst();

        return relation.isPresent() ? relation.get().proxyRole : null;
    }

    public Relation clone(){
        Relation r = new Relation();
        r.compartment = this.compartment;
        r.proxyCompartment = this.proxyCompartment;
        r.compartmentType = this.compartmentType;

        r.object = this.object;
        r.proxyObject = this.proxyObject;
        r.objectType = this.objectType;

        r.player = this.player;
        r.proxyPlayer = this.proxyPlayer;
        r.playerType = this.playerType;

        r.role = this.role;
        r.proxyRole = this.proxyRole;
        r.roleType = this.roleType;

        r.level = this.level;
        r.sequence = this.sequence;

        r.unboundTime = this.unboundTime;
        r.boundTime = this.boundTime;

        return r;
    }
}
