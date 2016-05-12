package xtras;

import java.util.HashMap;

/**
 * Created by nguonly on 5/5/16.
 */
public class DefaultMethodTest {

    static interface Compartment{
        HashMap<Integer, String> _hash = new HashMap<>();
        String _name = new String();




    }

    public static class Person implements Compartment{

    }
}
