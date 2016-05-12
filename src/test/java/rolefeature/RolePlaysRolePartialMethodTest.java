package rolefeature;

import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test on Partial Methods decorated over cascading calls in the role-playing-role relation.
 * This stimulates the proceed() method in the COP
 * Created by nguonly on 5/3/16.
 */
public class RolePlaysRolePartialMethodTest extends BaseTest{

    @Test
    public void testPartialMethodCalls() throws Throwable{
        Sender sender = _reg.newCore(Sender.class);

        Networking networking = _reg.newCompartment(Networking.class);
        networking.activate();

        sender.bind(Encryption.class);

        String data = sender.send();
        Assert.assertEquals(data, "<E>data<E>");
    }

    @Test
    public void testPartialMethodCallsInMultipleRoleRelations() throws Throwable{
        Sender sender = _reg.newCore(Sender.class);

        Networking networking = _reg.newCompartment(Networking.class);
        networking.activate();

        sender.bind(Encryption.class).bind(Compression.class);

        String data = sender.send();
        Assert.assertEquals(data, "<C><E>data<E><C>");

        Networking networking2 = _reg.newCompartment(Networking.class);
        networking2.activate();

        sender.bind(Compression.class).bind(Encryption.class);

        data = sender.send();
        Assert.assertEquals(data, "<E><C>data<C><E>");

        Networking networking3 = _reg.newCompartment(Networking.class);
        networking3.activate();

        sender.bind(Encryption.class).bind(Compression.class).bind(Logging.class);

        data = sender.send();
        Assert.assertEquals(data, "<L><C><E>data<E><C><L>");
    }

    @Test
    public void testPartialMethodCallsWithoutCascadingDownToCore() throws Throwable{
        Sender sender = _reg.newCore(Sender.class);

        Networking networking = _reg.newCompartment(Networking.class);
        networking.activate();

        sender.bind(SHA.class).bind(Compression.class);

        String data = sender.send();
        Assert.assertEquals(data, "<C><SHA><C>");
    }

    interface ISending{
        String send();
    }

    public static class Sender implements IPlayer, ISending{
        public String send(){
            return "data";
        }
    }

    public static class Encryption implements IRole, ISending {
        public String send(){
            String data = getPlayer(ISending.class).send();  //proceed() method in COP
            return "<E>" + data + "<E>";
        }
    }

    public static class Compression implements IRole, ISending{
        public String send(){
            String data = getPlayer(ISending.class).send();
            return "<C>" + data + "<C>";
        }
    }

    public static class Logging implements IRole, ISending{
        public String send(){
            String data = getPlayer(ISending.class).send();
            return "<L>" + data + "<L>";
        }
    }

    public static class SHA implements IRole, ISending{
        public String send(){
            //no cascading call back to core
            return "<SHA>";
        }
    }

    public static class Networking implements ICompartment{

    }
}
