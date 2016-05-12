package net.role4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nguonly on 5/10/16.
 */
public class ReflectionHelper {
    public static <T> T newInstance(Class<T> clazz, Object... args){
        try {
            T obj;

            Class<?>[] paramTypes = null;
            if (args != null && args.length > 0) {
                paramTypes = getParameterTypes(args);
                java.lang.reflect.Constructor<T> constr = clazz.getConstructor(paramTypes);
                obj = constr.newInstance(args);
            } else {
                obj = clazz.newInstance();
            }

            return obj;
        }catch(Throwable e){
            e.printStackTrace();
        }

        return null;
    }

    public static List<Class<?>> getAllSuperClassesAndInterfaces(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        Class<?>[] ifs = clazz.getInterfaces();
        //add itself
        classes.add(clazz);

        //super classes
        while (true) {
            Class<?> superClass = clazz.getSuperclass();
            classes.add(superClass);
            if (superClass.equals(Object.class)) break;
            clazz = superClass;
        }

        //get all interfaces
        for (Class<?> i : ifs) {
            classes.add(i);
        }

        return classes;
    }

    private static Class<?>[] getParameterTypes(Object... args) {
        Class<?>[] params=new Class<?>[args.length];
        for (int i=0;i<args.length;i++){
            params[i]=args[i].getClass();
        }
        return params;
    }
}
