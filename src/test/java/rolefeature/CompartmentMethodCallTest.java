package rolefeature;

import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by nguonly on 5/2/16.
 */
public class CompartmentMethodCallTest extends BaseTest {

    @Test
    public void testInvokeCompartmentMethod() throws  Throwable{
        Person p = _reg.newCore(Person.class);
        p.setName("lycog");

        University tud = _reg.newCompartment(University.class);
        tud.activate();

        tud.setName("INF");
        p.bind(Student.class);

        Assert.assertEquals("INF", p.getCompartment(University.class).getName());

        String display = p.interfaceOf(Student.class).display();
        Assert.assertEquals(display, "lycog is in INF");
    }

    @Test
    public void testInvokeCompartmentMethodFromSharedInterface() throws Throwable{
        Person p = _reg.newCore(Person.class);
        p.setName("lycog");

        University tud = _reg.newCompartment(University.class);
        tud.activate();

        tud.setName("INF");
        p.bind(Student.class);
        String display = p.interfaceOf(Student.class).display();
        Assert.assertEquals(display, "lycog is in INF");

        LanguageSchool langSchool = _reg.newCompartment(LanguageSchool.class);
        langSchool.activate();
        langSchool.setName("Language School");
        p.bind(Student.class);
        display = p.interfaceOf(Student.class).display();
        Assert.assertEquals(display, "lycog is in Language School");
    }

    public static class Person implements IPlayer{
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class Student implements IRole {
        public String display(){
            String compName = getCompartment(INaming.class).getName();
            String name = interfaceOf(Person.class).getName();

            return name + " is in " + compName;
        }
    }

    public static class University implements ICompartment, INaming{
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class LanguageSchool implements INaming, ICompartment{
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    static interface INaming {
        void setName(String name);
        String getName();
    }

}
