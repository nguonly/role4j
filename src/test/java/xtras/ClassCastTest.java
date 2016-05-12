package xtras;

import net.role4j.IPlayer;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by nguonly on 4/29/16.
 */
public class ClassCastTest {

    @Test
    public void testClassCast(){
        //classOf(Person.class).getName();
    }

    @Test
    public void testFindingInterface(){
        Person p = new Person();

        Class[] ifs = p.getClass().getInterfaces();
        Assert.assertEquals(IPlayer.class, ifs[0]);

        Class cls = p.getClass().getSuperclass();

        System.out.println(cls);
        Class ss = cls.getSuperclass();
        System.out.println(cls);
        List<Class> list;

    }

    private <T> T classOf(Class<T> clazz){
        Class ccc = Person.class;

        Human p = new Human();
        return (T)p;
    }

    public static class Person implements IPlayer{
        public String getName(){
            return "Person";
        }
    }

    public static class Human{
        public String getName(){
            return "Human";
        }
    }
}
