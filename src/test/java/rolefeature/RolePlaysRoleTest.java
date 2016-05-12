package rolefeature;

import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by nguonly on 5/2/16.
 */
public class RolePlaysRoleTest extends BaseTest {

    @Test
    public void testBasicRolePlaysRoleTest() throws Throwable{
        Person p = _reg.newCore(Person.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        p.bind(Student.class).bind(TeachingAssistant.class);

        p.interfaceOf(Student.class).setId(11111);
        Assert.assertEquals(11111, p.interfaceOf(Student.class).getId());

        String whoPaid = p.interfaceOf(TeachingAssistant.class).pay();

        Assert.assertEquals("11111 pays", whoPaid);
    }

    @Test
    public void testRoleSpecificState() throws Throwable{
        Person p1 = _reg.newCore(Person.class);
        Person p2 = _reg.newCore(Person.class);

        p1.setName("P1");
        p2.setName("P2");

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        p1.bind(Student.class).bind(TeachingAssistant.class);
        p2.bind(Student.class).bind(TeachingAssistant.class);

        p1.interfaceOf(Student.class).setId(11111);
        p2.interfaceOf(Student.class).setId(22222);

        Assert.assertEquals("11111 pays", p1.interfaceOf(TeachingAssistant.class).pay());
        Assert.assertEquals("22222 pays", p2.interfaceOf(TeachingAssistant.class).pay());
    }

    @Test
    public void testRolePlaysRoleAndMultipleCompartments() throws Throwable{
        Person p = _reg.newCore(Person.class);

        p.setName("lycog");

        MyCompartment school = _reg.newCompartment(MyCompartment.class);
        MyCompartment home = _reg.newCompartment(MyCompartment.class);

        Assert.assertEquals("lycog as a person speaks", p.speak());

        school.activate();
        p.bind(Student.class).bind(TeachingAssistant.class);

        Assert.assertEquals("lycog as a student speaks", p.speak());

        home.activate();
        p.bind(Father.class);
        Assert.assertEquals("lycog as a father speaks", p.speak());

    }

    public static class Person implements IPlayer {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }

        public void display(){
            System.out.println("Person");

        }

        public String speak(){
            return getName() + " as a person speaks";
        }

    }

    public static class Student implements IRole {
        private static int COUNTER = 0;

        private int _id;

        public Student(){
            COUNTER++; //avoid this proxying might return weird results
            //_id = COUNTER;
        }

        public void display(){
            System.out.println("Student");
        }

        public void setId(int id){
            _id = id;
        }

        public int getId(){
            return _id;
        }

        public String speak(){
            String name = interfaceOf(Person.class).getName();
            return name + " as a student speaks";
        }
    }

    public static class TeachingAssistant implements IRole{
        public void display(){
            System.out.println("Teaching Assistant");
        }

        public String pay(){
            //System.out.println(this);
//            Student s = (Student)getPlayer();
            int id = interfaceOf(Student.class).getId();
            return id + " pays";

        }
    }

    public static class Father implements IRole{
        public String speak(){
            String name = interfaceOf(Person.class).getName();
            return name + " as a father speaks";
        }
    }

    public static class MyCompartment implements ICompartment{

    }
}
