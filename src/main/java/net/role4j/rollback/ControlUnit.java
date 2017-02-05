package net.role4j.rollback;

import com.esotericsoftware.kryo.Kryo;
import net.role4j.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by nguonly on 1/11/17.
 */
public class ControlUnit {

    /**
     * Apply checkpoint to a compartment once binding operations are finished.
     * @param proxyCompartment
     */
    public static void checkpoint(int proxyCompartment){
        Registry reg = Registry.getRegistry();
        //ArrayDeque<Relation> relations = reg.getRelations();

        //1. copy to shadowRelation
        ArrayDeque<Relation> conf = copyRelations(proxyCompartment);

        //2. push the copy relation to stack
        Hashtable<Integer, Stack<ArrayDeque<Relation>>> versioning = reg.getConfigurations();
        Stack<ArrayDeque<Relation>> stack = versioning.get(proxyCompartment);
        if(stack == null) stack = new Stack<>();
        stack.push(conf);
        versioning.put(proxyCompartment, stack);
    }

    /**
     * Apply ControlUnit to active compartment in a current thread
     */
    public static void checkpoint(){
        Registry reg = Registry.getRegistry();
        ICompartment activeComp = reg.getActiveCompartments().get(Thread.currentThread().getId());

        checkpoint(activeComp.hashCode());
    }

    private static ArrayDeque<Relation> copyRelations(int proxyCompartment){
        Kryo kryo = new Kryo();
        ArrayDeque<Relation> relations = Registry.getRegistry().getRelations();
        ArrayDeque<Relation> cloneRelations = new ArrayDeque<>();

        //1. clone a whole relations
        relations.forEach(k -> {
            Relation r = k.clone();
            cloneRelations.add(r);
        });

        //2. iterate over the clone relations to perform copy on role and its players
        cloneRelations.forEach(k -> {
            if(k.proxyRole instanceof IRole){
                //3. deep copy role
                Object role = kryo.copy(k.role);
                Object proxyRole = kryo.copy(k.proxyRole);

                //4. find player in relations and update
                List<Relation> playerRelations = cloneRelations.stream()
                        .filter(c -> c.proxyCompartment.hashCode() == proxyCompartment && c.proxyObject == k.proxyObject
                        && c.proxyPlayer==k.proxyRole).collect(Collectors.toList());

                playerRelations.forEach(z -> {
                    z.player = role;
                    z.proxyPlayer = proxyRole;
                });

                //5. update role to a copy version
                k.role = role;
                k.proxyRole = proxyRole;
            }
        });


        return cloneRelations;
    }

    /**
     * Rolling to the recent checkpoint.
     * @param proxyCompartment
     */
    public static void rollback(int proxyCompartment){
        Registry reg = Registry.getRegistry();
        ArrayDeque<Relation> relations = reg.getRelations();

        relations.removeIf(p->p.proxyCompartment.hashCode()==proxyCompartment);

        Stack<ArrayDeque<Relation>> stack = reg.getConfigurations().get(proxyCompartment);

        if(stack==null) return;

        ArrayDeque<Relation> shadowRelation = stack.pop();
        shadowRelation.stream()
                .filter(p -> p.proxyCompartment.hashCode()==proxyCompartment)
                .forEach(k -> relations.add(k));

        //To re-adjust the callable stack to the new role instances resulting from rollback.
        List<Relation> coreRelations = relations.stream()
                .filter(p->p.proxyCompartment.hashCode()==proxyCompartment && p.level==0).collect(Collectors.toList());

        coreRelations.forEach(k -> {
            reg.reRegisterCallable((ICompartment) k.proxyCompartment, k.proxyObject);
        });

    }

    public static void rollback(){
        Registry reg = Registry.getRegistry();
        ICompartment activeComp = reg.getActiveCompartments().get(Thread.currentThread().getId());

        rollback(activeComp.hashCode());
    }
}
