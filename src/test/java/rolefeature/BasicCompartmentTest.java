package rolefeature;

import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import net.role4j.Registry;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.ArrayDeque;

import static org.junit.Assert.fail;

/**
 * Created by nguonly on 4/29/16.
 */
public class BasicCompartmentTest extends BaseTest{
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testCompartmentInstantiation() throws Throwable{
        Faculty inf = _reg.newCompartment(Faculty.class);

        Assert.assertEquals("Default Faculty", inf.getName());

        Faculty fe = _reg.newCompartment(Faculty.class, "FE");
        Assert.assertEquals("FE", fe.getName());

        Faculty fs = _reg.newCompartment(Faculty.class, "FS", 1);
        Assert.assertEquals("FS", fs.getName());
    }

    @Test//(expected = RuntimeException.class)
    public void testRuntimeExceptionWhenNoCompartment() throws Throwable{
//        exception.expect(RuntimeException.class);
//        exception.expectMessage("No compartment was found");
        Person p = _reg.newCore(Person.class);
//        p.bind(Student.class);

        try {
            p.bind(Student.class);
//            fail("No compartment was found");
        }catch(RuntimeException e){
            Assert.assertEquals("No compartment was found", e.getMessage());
        }
    }

    @Test
    public void testBindingMustBeInsideCompartment() throws Throwable{
        Person p = _reg.newCore(Person.class);

        Faculty inf = _reg.newCompartment(Faculty.class);
        inf.activate();

        p.bind(Student.class);

//        _reg.displayRelations();
    }

    @Test
    public void testCompartmentActivation() throws Throwable{
        Person lycog = _reg .newCore(Person.class);
        lycog.setName("lycog");

        Faculty inf = _reg.newCompartment(Faculty.class, "Inf"); //informatik
        inf.activate();
        lycog.bind(Student.class);

//        _reg.displayRelations();

        Faculty ifl = _reg.newCompartment(Faculty.class, "IFL"); //language
        ifl.activate();
        lycog.bind(Student.class);

//        _reg.displayRelations();

        //start activation test
        inf.activate();
        lycog.setId("INF-11");

        ifl.activate();
        lycog.setId("IFL-22");

        inf.activate();
        Assert.assertEquals("INF-11", lycog.getId());

        ifl.activate();
        Assert.assertEquals("IFL-22", lycog.getId());
        Assert.assertEquals("IFL-22 takes Java", lycog.interfaceOf(Student.class).takeCourse("Java"));
    }

    @Test
    public void testCompartmentDeActivation() throws Throwable{
        Person lycog = _reg.newCore(Person.class);
        lycog.setName("lycog");

        Faculty inf = _reg.newCompartment(Faculty.class, "Inf");
        inf.activate();

        lycog.setId("lycogId88888"); //original behavior

        Assert.assertEquals("lycogId88888", lycog.getId());

        lycog.bind(Student.class);
        lycog.setId("lycogSTUDENTId"); //student id
        Assert.assertEquals("lycogSTUDENTId", lycog.getId());

        inf.deactivate();

        //original behavior
        Assert.assertEquals("lycogId88888", lycog.getId());

        //role behavior
        inf.activate();
        Assert.assertEquals("lycogSTUDENTId", lycog.getId());

    }

    @Test
    public void testActivationOfTwoCompartments() throws Throwable{
        Person p = _reg.newCore(Person.class);
        p.setId("Person88");

        Faculty inf = _reg.newCompartment(Faculty.class, "INF");
        Faculty tud = _reg.newCompartment(Faculty.class, "TUD");

        tud.activate();
        p.bind(LanguageStudent.class);
        p.setId("Lang99");
        Assert.assertEquals("Lang99", p.getId());

        inf.activate();
        p.bind(Student.class);
        p.setId("Inf77");
        Assert.assertEquals("Inf77", p.getId());

        tud.activate();
        Assert.assertEquals("Lang99", p.getId());
        tud.deactivate();

        Assert.assertEquals("Person88", p.getId());

        inf.activate();
        Assert.assertEquals("Inf77", p.getId());

    }

    public static class Faculty implements ICompartment{
        private String _name;

        public Faculty(){
            _name = "Default Faculty";
        }

        public Faculty(String name){
            _name = name;
        }

        public Faculty(String name, Integer id){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class Person implements IPlayer{
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }

        private String _id;

        public void setId(String id){
            _id = id;
        }

        public String getId(){
            return _id;
        }
    }

    public static class Student implements IRole{
        private String _id;

        public void setId(String id){
            _id = id;
        }

        public String getId(){
            return _id;
        }

        public String takeCourse(String course){
            return _id + " takes " + course;
        }
    }

    public static class LanguageStudent implements IRole{
        private String _id;

        public void setId(String id){
            _id = id;
        }

        public String getId(){
            return _id;
        }
    }
}
