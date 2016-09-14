package xtras;

import net.role4j.*;
import org.junit.Assert;
import org.junit.Test;
import rolefeature.BaseTest;
import rolefeature.BasicCompartmentTest;
import rolefeature.InterfaceInvocationTest;

/**
 * Created by nguonly on 6/3/16.
 */
public class TestTest extends BaseTest{

    public static class Person implements IPlayer{
        public String getName(){
            return "Person";
        }


    }

    public static class Student implements IRole {
        public String getName(){
            return "Student";
        }
    }

    public static class Father implements IRole{
        public String getName(){
            return "Father";
        }
    }

    public static class Faculty implements ICompartment{

    }

    public static class Home implements ICompartment{

    }

    @Test
    public void test() throws Throwable{
        Registry reg = Registry.getRegistry();

        Person ly = reg.newCore(Person.class);
        Faculty inf = reg.newCompartment(Faculty.class);
        inf.activate();
        ly.bind(Student.class);
        inf.deactivate();

        Home home = reg.newCompartment(Home.class);
        home.activate();
        ly.bind(Father.class);
        home.deactivate();

        Assert.assertEquals("Person", ly.getName());
        home.activate();
        Assert.assertEquals("Father", ly.getName());

        inf.activate();
        Assert.assertEquals("Student", ly.getName());

    }
}
