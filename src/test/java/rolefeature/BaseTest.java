package rolefeature;

import net.role4j.Registry;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayDeque;

/**
 * Created by nguonly on 4/29/16.
 */
public abstract class BaseTest {
    //delta - the maximum delta between expected and actual for which both numbers are still considered equal.
    static final double DELTA = 1e-15; //for assertion of double value

    public Registry _reg = Registry.getRegistry();

    @Before
    public void beforeEachTest(){
        _reg.setRelations(new ArrayDeque<>());
    }

    @After
    public void afterEachTest(){
        _reg.setRelations(null);
    }
}
