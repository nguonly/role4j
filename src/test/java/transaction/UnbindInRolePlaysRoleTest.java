package transaction;

import net.role4j.*;
import net.role4j.trans.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.omg.IOP.TransactionService;
import rolefeature.BaseTest;

/**
 * Created by nguonly on 10/18/16.
 */
public class UnbindInRolePlaysRoleTest extends BaseTest {

    public static interface IChannel{
        String send(String data);
        String receive();
    }
    public static class Channel implements IChannel,IPlayer {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }

        @Override
        public String send(String data) {
            return data;
        }

        @Override
        public String receive() {
            return null;
        }
    }

    public static class Compression implements IChannel, IRole {

        @Override
        public String send(String data) {
            String fMsg = getPlayer(IChannel.class).send(data);
            return "<C>" + fMsg + "<C>";
        }

        @Override
        public String receive() {
            return null;
        }
    }

    public static class Encryption implements IChannel, IRole {

        @Override
        public String send(String data) {
            String fMsg = getPlayer(IChannel.class).send(data);
            return "<E>" + fMsg + "<E>";
        }

        @Override
        public String receive() {
            return null;
        }
    }

    @Test
    public void testRolesNotRemovedDuringTransaction() throws Throwable{
        Registry reg = Registry.getRegistry();
        Compartment comp = reg.newCompartment(Compartment.class);
        Channel channel = reg.newCore(Channel.class);

        comp.activate();

        channel.bind(Encryption.class).bind(Compression.class);
//        DumpHelper.dumpRelations();
//        DumpHelper.displayCallables(reg.coreCallable);

        try(Transaction tx = new Transaction()) {
            Assert.assertEquals("<C><E>Data<E><C>", channel.send("Data"));

            Thread.sleep(1); //prevent from unexpected result as the time of transaction and unbound role is the same

            channel.unbind(Compression.class);

//            DumpHelper.dumpRelations();
            Assert.assertEquals("<C><E>Data<E><C>", channel.send("Data"));
        }

        Assert.assertEquals("<E>Data<E>", channel.send("Data"));
//        System.out.println("After Transaction");
//        DumpHelper.dumpRelations();
    }
}
