package rolefeature;

import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import net.role4j.Registry;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by nguonly on 4/29/16.
 */
public class InvokeViaInterfaceTest extends BaseTest{

    @Test
    public void testStateSpecific() throws Throwable{
        Person p1 = _reg.newCore(Person.class);
        Person p2 = _reg.newCore(Person.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        p1.setName("P1");
        p2.setName("P2");

        p1.bind(Student.class);
        p2.bind(Student.class);

        Assert.assertEquals("P1 is a Student", p1.speak());
        Assert.assertEquals("P2 is a Student", p2.speak());
    }

    @Test
    public void testInterfaceInvocation() throws Throwable{
        Person person = _reg.newCore(Person.class);

        MyCompartment compartment = _reg.newCompartment(MyCompartment.class);
        compartment.activate();

        person.bind(Student.class);

        Human human = _reg.newCore(Human.class);
        human.bind(Student.class);

//        _reg.displayCallables(_reg.callables);
        person.setName("Person");
        human.setName("Human");

        Assert.assertEquals("Person is a Student", person.speak());
        Assert.assertEquals("Human is a Student", human.speak());

    }

    public interface IHuman{
        String getName();

    }

    public static class Person implements IPlayer, IHuman {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }

        public String speak(){
            return "Person";
        }
    }

    public static class Student implements IRole {
        public String speak(){
            String name = interfaceOf(IHuman.class).getName();
            return name + " is a Student";
        }
    }

    public static class Human implements IPlayer, IHuman{
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }

        public String speak(){
            return "Human";
        }
    }

    public static class MyCompartment implements ICompartment{

    }

}
