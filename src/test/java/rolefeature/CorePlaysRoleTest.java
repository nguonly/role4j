package rolefeature;

import net.role4j.*;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by nguonly on 4/29/16.
 */
public class CorePlaysRoleTest extends BaseTest{

    @Test
    public void testBehaviorDependent() throws Throwable{

        Person p = _reg.newCore(Person.class);

        DumpHelper.displayCallables(_reg.coreCallable);
        Assert.assertEquals("Person", p.speak());

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        p.bind(Student.class);
        Assert.assertEquals("Student", p.speak());


        Person p2 = _reg.newCore(Person.class);
        p2.bind(Father.class);
        Assert.assertEquals("Father", p2.speak());

    }

    @Test
    public void testCallMethodOfCore() throws Throwable{

        Person p = _reg.newCore(Person.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();
        p.setName("lycog");

        p.bind(Student.class);

//        _reg.displayCallables(_reg.callables);
        p.setName("lycog");

        Assert.assertEquals("lycog", p.getName());
    }

    public static class Person implements IPlayer{
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

    public static class Student implements IRole{
        public String speak(){
            return "Student";
        }

    }

    public static class Father implements IRole{
        public String speak(){
            return "Father";
        }
    }

    public static class MyCompartment implements ICompartment{

    }
}
