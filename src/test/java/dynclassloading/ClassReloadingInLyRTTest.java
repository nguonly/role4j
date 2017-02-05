package dynclassloading;

import net.role4j.IPlayer;
import org.junit.Test;
import rolefeature.BaseTest;

/**
 * Created by nguonly on 1/14/17.
 */
public class ClassReloadingInLyRTTest extends BaseTest {

    @Test
    public void basicInitializationTest() throws Throwable {
        Person p = _reg.newCore(Person.class);
    }

    public static class Person implements IPlayer{

    }
}
