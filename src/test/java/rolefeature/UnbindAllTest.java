package rolefeature;

import net.role4j.*;
import net.role4j.trans.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Created by nguonly on 10/18/16.
 */
public class UnbindAllTest extends BaseTest {
    interface INaming{
        void setName(String name);
        String getName();
    }

    public static class Any implements IPlayer {
        private String _name;
        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class A implements IRole, INaming {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class B implements IRole, INaming {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class C implements IRole{

    }

    public static class D implements IRole{

    }

    public static class E implements IRole{
        private String _name;
        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    @Test
    public void testUnbindAllOnSingleObjectInCompartment() throws Throwable{
        Registry reg = Registry.getRegistry();

        Compartment comp = reg.newCompartment(Compartment.class);
        Any any = reg.newCore(Any.class);
        any.setName("Any");
        comp.activate();

        any.bind(C.class).bind(D.class);
        any.bind(E.class);

        any.setName("E");

        Assert.assertEquals("E", any.getName());

//        DumpHelper.dumpRelations();
        ArrayDeque<Relation> relationList = reg.getRelations();
        int rolesCount = (int) relationList.stream().filter(p -> p.proxyObject.equals(any) && !p.proxyRole.equals(any)).count();

        Assert.assertEquals(3, rolesCount);

        any.unbindAll();

        Assert.assertEquals("Any", any.getName());
    }

    @Test
    public void testUnbindAllOnSingleObjectInTransaction() throws Throwable{
        Registry reg = Registry.getRegistry();

        Compartment comp = reg.newCompartment(Compartment.class);
        Any any = reg.newCore(Any.class);
        any.setName("Any");
        comp.activate();

        any.bind(C.class).bind(D.class);
        any.bind(E.class);

        any.setName("E");

        try(Transaction tx = new Transaction()){
            Assert.assertEquals("E", any.getName());

            any.unbindAll();

            Assert.assertEquals("E", any.getName());
        }

        Assert.assertEquals("Any", any.getName());
    }

    @Test
    public void testUnbindAllOnMultipleObjectsInCompartment() throws Throwable{
        Registry reg = Registry.getRegistry();

        Compartment comp = reg.newCompartment(Compartment.class);
        Any any1 = reg.newCore(Any.class);
        any1.setName("Any1");

        Any any2 = reg.newCore(Any.class);
        any2.setName("Any2");

        comp.activate();

        any1.bind(C.class).bind(D.class);
        any1.bind(E.class);
        any1.setName("E");

        any2.bind(A.class);
        any2.setName("A");

        Assert.assertEquals("E", any1.getName());
        Assert.assertEquals("A", any2.getName());

        any1.unbindAll();
        Assert.assertEquals("Any1", any1.getName());

//        DumpHelper.dumpRelations();
        ArrayDeque<Relation> relationList = reg.getRelations();
        int rolesCount = (int) relationList.stream().filter(p -> p.proxyObject.equals(any2) && !p.proxyRole.equals(any2)).count();

        Assert.assertEquals(1, rolesCount);

    }

    @Test
    public void testUnbindAllOnMultipleObjectsInTransaction() throws Throwable{
        Registry reg = Registry.getRegistry();
        _reg.setRelations(new ArrayDeque<>());

        Compartment comp = reg.newCompartment(Compartment.class);
        Any any1 = reg.newCore(Any.class);
        any1.setName("Any1");

        Any any2 = reg.newCore(Any.class);
        any2.setName("Any2");

        comp.activate();

        any1.bind(C.class).bind(D.class);
        any1.bind(E.class);
        any1.setName("E");

        any2.bind(A.class);
        any2.setName("A");

        try(Transaction tx = new Transaction()) {
            Assert.assertEquals("E", any1.getName());
            Assert.assertEquals("A", any2.getName());

            Thread.sleep(1); //prevent from the time of transaction and unbound roles is the same

            any1.unbindAll();
            any2.unbindAll();

            Assert.assertEquals("E", any1.getName());
            Assert.assertEquals("A", any2.getName());
        }
        DumpHelper.dumpRelations();
        System.out.println(reg.getTransactions());

        ArrayDeque<Relation> relationList = reg.getRelations();
        int rolesCount = (int) relationList.stream().filter(p -> p.proxyObject.equals(any2) && !p.proxyRole.equals(any2)).count();

        Assert.assertEquals(0, rolesCount);

    }
}
