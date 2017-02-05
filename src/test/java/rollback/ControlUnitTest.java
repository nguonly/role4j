package rollback;

import net.role4j.*;
import net.role4j.rollback.AdaptationConfiguration;
import net.role4j.rollback.ControlUnit;
import org.junit.Assert;
import org.junit.Test;
import rolefeature.BaseTest;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by nguonly on 1/11/17.
 */
public class ControlUnitTest extends BaseTest {

    @Test
    public void applyCheckpointInActiveCompartmentTest() throws Throwable {
        Person p = _reg.newCore(Person.class);
        Compartment comp = _reg.newCompartment(Compartment.class);

        comp.activate();
        p.bind(A.class);
        p.bind(C.class);

        ControlUnit.checkpoint();

        Stack<ArrayDeque<Relation>> stackVersion = _reg.getConfigurations().get(comp.hashCode());
        Assert.assertEquals(1, stackVersion.size());

        p.bind(B.class).bind(C.class);

        ArrayDeque<Relation> v2Relations = stackVersion.pop();
        List<Relation> v2 = v2Relations.stream()
                .filter(c->c.proxyCompartment.equals(comp))
                .collect(Collectors.toList());

        v2.forEach(r -> {
            String name = r.role.getClass().getSimpleName();
//            System.out.println(name);
            Assert.assertTrue(name.contains("Person") || name.contains("A") || name.contains("C"));
        });
    }

    @Test
    public void rollbackInCompartmentTest() throws Throwable {
        Person p = _reg.newCore(Person.class);
        Compartment comp = _reg.newCompartment(Compartment.class);

        comp.activate();
        p.bind(A.class).bind(B.class);

        ControlUnit.checkpoint(comp.hashCode());

        p.bind(C.class);
        p.bind(D.class);

        ControlUnit.rollback(comp.hashCode());

//        DumpHelper.dumpRelations();
        ArrayDeque<Relation> v1Relations = _reg.getRelations();
        List<Relation> v1 = v1Relations.stream()
                .filter(c->c.proxyCompartment.equals(comp) && c.level>0)
                .collect(Collectors.toList());

        v1.forEach(r -> {
            String name = r.role.getClass().getSimpleName();
            Assert.assertTrue(name.contains("A") || name.contains("B"));
        });
    }

    public static class Person implements IPlayer{
        public String doThing(){
            return this.getClass().getSimpleName();
        }
    }

    public static class A implements IRole {
        public String doThing(){
            return this.getClass().getSimpleName();
        }
    }

    public static class B implements IRole{
        public String doThing(){
            return this.getClass().getSimpleName();
        }
    }

    public static class C implements IRole{
        public String doThing(){
            return this.getClass().getSimpleName();
        }
    }

    public static class D implements IRole{
        public String doThing(){
            return this.getClass().getSimpleName();
        }
    }

    @Test
    public void newCheckpointRemovingRoleTest() throws Throwable {
        Person p = _reg.newCore(Person.class);

        Compartment comp = _reg.newCompartment(Compartment.class);

        comp.activate();

        try(AdaptationConfiguration ac = new AdaptationConfiguration()) {
            p.bind(A.class);
            p.bind(B.class);
        }

        Assert.assertEquals("B", p.doThing());

        try(AdaptationConfiguration ac = new AdaptationConfiguration()) {
            p.unbind(B.class);
        }

        Assert.assertEquals("A", p.doThing());

        ControlUnit.rollback();

        Assert.assertEquals("B", p.doThing());

        try(AdaptationConfiguration ac = new AdaptationConfiguration()) {
            p.bind(C.class);
        }

        Assert.assertEquals("C", p.doThing());

        ControlUnit.rollback();

        Assert.assertEquals("B", p.doThing());

        comp.deactivate();

        Assert.assertEquals("Person", p.doThing());

    }
}
