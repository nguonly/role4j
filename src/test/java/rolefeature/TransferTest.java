package rolefeature;

import net.role4j.DumpHelper;
import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by nguonly on 5/5/16.
 */
public class TransferTest extends BaseTest {

    @Test
    public void testTransferSingleRoleFromCore() throws Throwable{
        Any any = _reg.newCore(Any.class);
        Any newAny = _reg.newCore(Any.class);

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        any.setName("Any");
        newAny.setName("Any2");

        IRole a = any.bind(A.class);
        any.setName("A");
        a.bind(C.class);
        Assert.assertEquals("A", any.getName());

        DumpHelper.dumpRelations(comp);

        any.transfer(a, newAny, comp);

        Assert.assertEquals("Any", any.getName());

        any.bind(B.class);

        Assert.assertEquals("A", newAny.getName());
        DumpHelper.dumpRelations(comp);
    }

    @Test
    public void testTransferMiddleRolesToExistingRelations() throws Throwable{
        Any any = _reg.newCore(Any.class);
        Any newAny = _reg.newCore(Any.class);

        any.setName("Any");
        newAny.setName("NewAny");

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        IRole a = any.bind(A.class);
        a.bind(B.class);
        IRole d = a.bind(D.class);

        DumpHelper.dumpRelations(comp);
        any.interfaceOf(D.class).setId("D111");

        newAny.bind(B.class).bind(C.class);

        any.transfer(d, newAny, comp);
        DumpHelper.dumpRelations(comp);

        String actual = newAny.interfaceOf(D.class).getId();
        Assert.assertEquals("D111", actual);

    }

    @Test
    public void testTransferRoleToDifferentCompartment() throws Throwable{
        Any any = _reg.newCore(Any.class);
        Any newAny = _reg.newCore(Any.class);

        MyCompartment oldCompartment = _reg.newCompartment(MyCompartment.class);
        MyCompartment newCompartment = _reg.newCompartment(MyCompartment.class);

        oldCompartment.activate();
        any.setName("Any");
        IRole a = any.bind(A.class);
        any.bind(F.class);
        IRole b = a.bind(B.class);
        b.bind(C.class);
        a.bind(D.class);

        any.setName("Any of B");

        any.transfer(b, newAny, newCompartment);

        newCompartment.activate();
        String actual = newAny.getName(); //Any of B
        Assert.assertEquals("Any of B", actual);
    }

    @Test
    public void testRoleTransferRole() throws Throwable{
        Any any = _reg.newCore(Any.class);
        Any newAny = _reg.newCore(Any.class);

        any.setName("Any");
        newAny.setName("NewAny");

        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        comp.activate();

        IRole a = any.bind(A.class);
        a.bind(B.class);
        IRole d = a.bind(D.class);

        any.interfaceOf(D.class).setId("D111");

        a.transfer(d, newAny, comp);

        String actual = newAny.interfaceOf(D.class).getId();
        Assert.assertEquals("D111", actual);
    }

    interface INaming{
        void setName(String name);
        String getName();
    }

    public static class Any implements IPlayer, INaming {
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
        public String _id;

        public void  setId(String id){
            _id = id;
        }

        public String getId(){
            return _id;
        }
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
