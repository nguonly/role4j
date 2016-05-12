package net.role4j;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nguonly on 5/9/16.
 */
public interface IRelationship {
    Registry _reg = Registry.getRegistry();

    default <T> List<T> getRoles(Class<T> roleType){
        ArrayDeque<Relation> relations = _reg.getRelations();

        List<T> roles = relations.stream()
                .filter(p->p.compartment.equals(this) && p.roleType.equals(roleType))
                .map(p-> roleType.cast(p.proxyRole))
                .collect(Collectors.toList());

        return roles;
    }

    default <T, R> List<T> getCores(Class<T> coreType, Class<R> roleType){
        ArrayDeque<Relation> relations = _reg.getRelations();

        List<T> cores = relations.stream()
                .filter(p->p.compartment.equals(this) && p.objectType.equals(coreType) && p.roleType.equals(roleType))
                .map(p->coreType.cast(p.proxyObject))
                .collect(Collectors.toList());

        return cores;
    }

    default <T> List<IPlayer> getCores(Class<T> roleType){
        ArrayDeque<Relation> relations = _reg.getRelations();

        List<IPlayer> cores = relations.stream()
                .filter(p->p.compartment.equals(this) && p.roleType.equals(roleType))
                .map(p->IPlayer.class.cast(p.proxyObject))
                .collect(Collectors.toList());

        return cores;
    }
}
