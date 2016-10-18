package transaction;

import net.role4j.Compartment;
import net.role4j.Registry;
import net.role4j.trans.Transaction;
import org.junit.Test;
import org.openjdk.jmh.runner.RunnerException;
import rolefeature.BaseTest;

/**
 * Created by nguonly on 10/18/16.
 */
public class CompartmentActivationInTransactionTest extends BaseTest {

    @Test(expected = RuntimeException.class)
    public void testCompartmentActivatedInTransactionThrowsError() throws Throwable{
        Registry reg = Registry.getRegistry();

        Compartment comp = reg.newCompartment(Compartment.class);
        comp.activate();

        try(Transaction tx = new Transaction()){
            comp.activate();
        }

    }

    @Test(expected = RuntimeException.class)
    public void testCompartmentDeactivatedInTransactionThrowsError() throws Throwable{
        Registry reg = Registry.getRegistry();

        Compartment comp = reg.newCompartment(Compartment.class);
        comp.activate();

        try(Transaction tx = new Transaction()){
            comp.deactivate();
        }
    }
}
