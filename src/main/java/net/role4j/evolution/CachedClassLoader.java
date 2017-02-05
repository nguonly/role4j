package net.role4j.evolution;

import java.util.Hashtable;

/**
 * Created by nguonly on 1/14/17.
 */
public class CachedClassLoader {
    //Used to cache already defined classes
    public static Hashtable<String, Class<?>> classes = new Hashtable<>();
}
