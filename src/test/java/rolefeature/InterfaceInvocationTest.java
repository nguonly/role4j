package rolefeature;

import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by nguonly on 5/1/16.
 */
public class InterfaceInvocationTest extends BaseTest{

    @Test
    public void testInterfaceCallFromCore() throws Throwable{
        Person p = _reg.newCore(Person.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        p.bind(Student.class);

        p.interfaceOf(Student.class).setStudentId("STU-22222");
        Assert.assertEquals("STU-22222", p.interfaceOf(Student.class).getStudentId());
    }

    @Test
    public void testInterfaceCallFromRole() throws Throwable{
        Person p = _reg.newCore(Person.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();
        p.setName("lycog");
        p.bind(Student.class);

        String pay = p.interfaceOf(Student.class).pay();
        Assert.assertEquals("lycog pays", pay);
    }

    @Test
    public void testInterfaceCallFromCoreAndRole() throws Throwable{
        Person p = _reg.newCore(Person.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();
        p.setName("lycog");
        p.bind(Student.class);

        String takeCourse = p.interfaceOf(Student.class).takeCourse();
        Assert.assertEquals("lycog takes a course", takeCourse);
    }

    @Test
    public void testInterfaceSharedByCoreAndRole() throws Throwable{
        Person p = _reg.newCore(Person.class);

        String fatherSpeak = "Father speaks";
        String personSpeak = "Person speaks";

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        Assert.assertEquals(personSpeak, p.speak());

        p.bind(Father.class);

        Assert.assertEquals(fatherSpeak, p.speak());
        Assert.assertEquals(fatherSpeak, p.interfaceOf(Person.class).speak());
        Assert.assertEquals(fatherSpeak, p.interfaceOf(ITalkable.class).speak());
        Assert.assertEquals(fatherSpeak, p.interfaceOf(Father.class).speak());
        Assert.assertEquals(fatherSpeak, p.interfaceOf(Father.class).whoSpeak());
    }

    @Test
    public void testAbstractClassSharedByCoreAndRole() throws Throwable{

    }

    static interface IHuman{
        String getName();
    }

    static interface ITalkable{
        String speak();
    }

    public static class Person implements IPlayer, IHuman, ITalkable{
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }

        public String speak(){
            return "Person speaks";
        }
    }

    public static class Student implements IRole{

        private String _id;

        public void setStudentId(String id){
            _id = id;
        }

        public String getStudentId(){
            return _id;
        }

        public String pay(){
            String name = interfaceOf(Person.class).getName();
            return name + " pays";
//            return "pay";
        }

        public String takeCourse(){
            String name = interfaceOf(IHuman.class).getName();
            return name + " takes a course";
        }
    }

    public static class Father implements IRole,ITalkable{
        public String speak(){
            return "Father speaks";
        }

        public String whoSpeak(){
            return interfaceOf(ITalkable.class).speak();
        }
    }

    public static class MyCompartment implements ICompartment{

    }
}

