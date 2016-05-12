package rolefeature;

import net.role4j.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by nguonly on 5/5/16.
 */
public class UnbindTest extends BaseTest {


    @Test
    public void testUnbindEdge() throws Throwable{
        Any any = _reg.newCore(Any.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        any.bind(A.class).bind(B.class);
        any.bind(C.class);

        any.unbind(B.class);

        Optional<Relation> coreRelation = _reg.getRelations().stream()
                .filter(f->f.proxyCompartment.equals(comp) && f.proxyRole.equals(any) && f.level==0 && f.sequence==0)
                .findFirst();

        List<Relation> relations = _reg.traverseRelation(coreRelation.get());

        List<Class<?>> expected = new ArrayList<>();
        expected.add(A.class);
        expected.add(C.class);

        for(int i=0;i<expected.size(); i++){
            Assert.assertEquals(relations.get(i).roleType, expected.get(i));
        }
    }

    @Test
    public void testUnbindMiddleNode() throws Throwable{
        Any any = _reg.newCore(Any.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        any.bind(A.class).bind(B.class);
        any.bind(C.class);

        any.unbind(A.class);

        Optional<Relation> coreRelation = _reg.getRelations().stream()
                .filter(f->f.proxyCompartment.equals(comp) && f.proxyRole.equals(any) && f.level==0 && f.sequence==0)
                .findFirst();

        List<Relation> relations = _reg.traverseRelation(coreRelation.get());

        List<Class<?>> expected = new ArrayList<>();
        expected.add(C.class);

        for(int i=0;i<expected.size(); i++){
            Assert.assertEquals(relations.get(i).roleType, expected.get(i));
        }

//        _reg.setRelations(new ArrayDeque<>());



    }

    @Test
    public void testUnbindMiddleNode2() throws Throwable{
        Any any2 = _reg .newCore(Any.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        //Binding any2
        IRole a = any2.bind(A.class);
        a.bind(B.class).bind(D.class);
        a.bind(C.class);
        any2.bind(G.class);

        a.unbind(B.class);

        DumpHelper.dumpRelations();

        Optional<Relation> coreRelation = _reg.getRelations().stream()
                .filter(f->f.proxyCompartment.equals(comp) && f.proxyObject.equals(any2) && f.proxyRole.equals(any2) && f.level==0 && f.sequence==0)
                .findFirst();

        List<Relation> relations = _reg.traverseRelation(coreRelation.get());
        List<Class> expected = new ArrayList<>();
        expected.add(A.class);
        expected.add(C.class);
        expected.add(G.class);

        for(int i=0;i<expected.size(); i++){
            Assert.assertEquals(relations.get(i).roleType, expected.get(i));
        }
    }

    @Test
    public void testCheckOrdering() throws Throwable{
        Any any = _reg.newCore(Any.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        IRole a = any.bind(A.class);
        any.bind(G.class);
        a.bind(B.class).bind(D.class);
        a.bind(C.class);

        DumpHelper.dumpRelations();

        a.unbind(B.class);

        DumpHelper.dumpRelations();
        a.bind(D.class);

        DumpHelper.dumpRelations();

        Optional<Relation> coreRelation = _reg.getRelations().stream()
                .filter(f->f.proxyCompartment.equals(comp) && f.proxyRole.equals(any) && f.level==0 && f.sequence==0)
                .findFirst();

        List<Relation> traversedList = _reg.traverseRelation(coreRelation.get());

        traversedList.forEach(System.out::println);
    }

    @Test
    public void testRoleStateWhenUnbind() throws Throwable{
        Any any = _reg.newCore(Any.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);

        comp.activate();

        any.bind(A.class);

        any.interfaceOf(A.class).setName("A");

        Assert.assertEquals("A", any.interfaceOf(A.class).getName());

        any.unbind(A.class);

        any.bind(A.class);

        Assert.assertEquals(null, any.interfaceOf(A.class).getName()); //no state because it's re-initialized
    }

    @Test
    public void testRoleStateWhenUnbind2() throws Throwable{
        Any any = _reg.newCore(Any.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);

        comp.activate();

        IRole a = any.bind(A.class);

        any.interfaceOf(INaming.class).setName("A");

        a.bind(B.class);

        any.interfaceOf(INaming.class).setName("B");

        Assert.assertEquals("B", any.interfaceOf(INaming.class).getName());

        a.unbind(B.class);

        Assert.assertEquals("A", any.interfaceOf(INaming.class).getName());
    }

    interface INaming{
        void setName(String name);
        String getName();
    }

    public static class Any implements IPlayer {

    }

    public static class A implements IRole , INaming{
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class B implements IRole, INaming{
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

    }

    public static class F implements IRole{

    }

    public static class G implements IRole{

    }

    public static class H implements IRole{

    }

    public static class MyCompartment implements ICompartment {

    }
}
