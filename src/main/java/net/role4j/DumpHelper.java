package net.role4j;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nguonly on 5/4/16.
 */
public class DumpHelper {

    public static void dumpRelations(){
        ArrayDeque<Relation> relations = Registry.getRegistry().getRelations();

        dumpRelations(relations.stream().collect(Collectors.toList()));
    }

    public static void dumpRelations(Object compartment){
        ArrayDeque<Relation> relations = Registry.getRegistry().getRelations();

        List<Relation> list = relations.stream()
                .filter(p->p.proxyCompartment.equals(compartment)).collect(Collectors.toList());

        dumpRelations(list);
    }

    public static void dumpRelations(List<Relation> relations){
        System.out.println("----------------------");
        System.out.format("%30s %30s %30s %30s %5s %5s\n",
                "Compartment", "Object", "Player", "Role", "Lvl", "Seq");

        relations.forEach(System.out::println);

        System.out.println("----------------------");
    }

    /**
     * RolePlaysRoleTest$Person$ByteBuddy$KMaOOa1R is the format of ByteBuddy Generated classes
     * @param methodName
     * @return
     */
    public static String getSimpleMethodName(String methodName){
        int first$ = methodName.indexOf("$");
        int second$ = methodName.substring(first$+1).indexOf("$");
//        System.out.println(first$ + " " + second$);
        if(second$>0) {
            return methodName.substring(first$+1, first$+second$+1);
        }else if(first$>0) {
            return methodName.substring(first$);
        }
        return methodName;
    }

    public static void displayCallables(HashMap<Integer, CallableMethod> hashCallables) {
        System.out.println("----------------------");
        System.out.format("%10s %10s %20s\n", "Method", "InvokingObj", "Callable");
        hashCallables.forEach((k, v) -> {
            System.out.format("%10d %10d %20s\n", k, v.invokingObject.hashCode(), v.method);
        });
    }
}
