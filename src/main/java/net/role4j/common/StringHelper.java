package net.role4j.common;

/**
 * Created by nguonly on 10/24/16.
 */
public class StringHelper {

    /**
     * Get simplified class name by removing package name space
     * @return String of the class name without package name
     */
    public static String getSimpleClassName(String fullClassName){
        int idx = fullClassName.lastIndexOf(".");
        return fullClassName.substring(idx+1);
    }
}
