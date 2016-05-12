package relation;

import net.role4j.*;
import org.junit.Assert;
import org.junit.Test;
import rolefeature.BaseTest;
import rolefeature.RolePlaysRoleTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by nguonly on 5/4/16.
 */
public class TraversingCoreTest extends BaseTest{

    @Test
    public void testTraversingCore() throws Throwable{
        Any p = _reg.newCore(Any.class);
        Any any = _reg.newCore(Any.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        IRole a = p.bind(A.class);
        p.bind(C.class).bind(D.class).bind(E.class);
        a.bind(B.class).bind(G.class);
        a.bind(H.class);

        IRole d = any.bind(D.class);
        IRole h = any.bind(H.class);
        IRole c = h.bind(C.class);
        d.bind(G.class);
        h.bind(F.class);

        Optional<Relation> relation = _reg.getRelations().stream()
                .filter(f->f.proxyCompartment.equals(comp) && f.proxyRole.equals(p) && f.level==0 && f.sequence==0)
                .findFirst();

//        _reg.findRelation(relation.get());

        DumpHelper.dumpRelations();
//
//        System.out.println("----------------------");

        List<Relation> list = _reg.traverseRelation(relation.get());

//        System.out.println("--------------");
        List<Class<?>> expected = new ArrayList<>();
        expected.add(A.class);
        expected.add(B.class);
        expected.add(G.class);
        expected.add(H.class);
        expected.add(C.class);
        expected.add(D.class);
        expected.add(E.class);

        for(int i=0;i<expected.size(); i++){
            Assert.assertEquals(list.get(i).roleType, expected.get(i));
        }

        relation = _reg.getRelations().stream()
                .filter(f->f.proxyCompartment.equals(comp) && f.proxyRole.equals(any) && f.level==0 && f.sequence==0)
                .findFirst();

        list = _reg.traverseRelation(relation.get());

        expected = new ArrayList<>();
        expected.add(D.class);
        expected.add(G.class);
        expected.add(H.class);
        expected.add(C.class);
        expected.add(F.class);

        for(int i=0;i<expected.size(); i++){
            Assert.assertEquals(list.get(i).roleType, expected.get(i));
        }
    }

    /**
     * This is to confirm that Any object implement IRole can be both IPlayer and IRole
     * @throws Throwable
     */
    @Test
    public void testInstanceOf() throws Throwable{
        Any any = _reg.newCore(Any.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        IRole a = any.bind(A.class);
//
        Assert.assertEquals(true, any instanceof IPlayer);
        Assert.assertEquals(false, any instanceof IRole);
        Assert.assertEquals(true, a instanceof IRole); //a can be IRole
        Assert.assertEquals(true, a instanceof IPlayer);//a can be IPlayer

    }

    public static class Any implements IPlayer{

    }

    public static class A implements IRole {

    }

    public static class B implements IRole{

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

    public static class MyCompartment implements ICompartment{

    }
}
