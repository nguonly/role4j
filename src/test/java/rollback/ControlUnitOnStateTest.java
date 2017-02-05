package rollback;

import net.role4j.Compartment;
import net.role4j.DumpHelper;
import net.role4j.IPlayer;
import net.role4j.IRole;
import net.role4j.rollback.AdaptationConfiguration;
import net.role4j.rollback.ControlUnit;
import org.junit.Assert;
import org.junit.Test;
import rolefeature.BaseTest;

/**
 * Created by nguonly on 1/13/17.
 */
public class ControlUnitOnStateTest extends BaseTest {

    public static class Person implements IPlayer{
        private String name;

//        public Person(){
//
//        }
//
//        public Person(String name){
//            this.name = name;
//        }

        public void setName(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    public static class A implements IRole {
        private String name = "AA";

        public A(){

        }

        public A(String name){
            this.name = name;
        }

        public void setName(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    public static class B implements IRole {
        private String name;

        public B(){

        }

        public B(String name){
            this.name = name;
        }

        public void setName(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    public static class C implements IRole {
        private String name;

        public C(){

        }

        public C(String name){
            this.name = name;
        }

        public void setName(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    @Test
    public void statePreserveTest() throws Throwable {
        Compartment comp = _reg.newCompartment(Compartment.class);
        Person person = _reg.newCore(Person.class);
        person.setName("Lycog");

        comp.activate();

        Assert.assertEquals("Lycog", person.getName());

        IRole a;
        try(AdaptationConfiguration ac = new AdaptationConfiguration()){
            a = person.bind(A.class, "myA");
        }

        Assert.assertEquals("myA", person.getName());

        try(AdaptationConfiguration ac = new AdaptationConfiguration()){
            a.bind(B.class, "myB");
        }

        Assert.assertEquals("myB", person.getName());

        try(AdaptationConfiguration ac = new AdaptationConfiguration()){
            a.bind(C.class, "myC");
        }
        Assert.assertEquals("myC", person.getName());

        DumpHelper.dumpRelations();

        ControlUnit.rollback();

        DumpHelper.dumpRelations();
        Assert.assertEquals("myB", person.getName());

        ControlUnit.rollback();

        Assert.assertEquals("myA", person.getName());
    }
}
