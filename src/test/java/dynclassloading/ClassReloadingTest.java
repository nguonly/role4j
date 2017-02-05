package dynclassloading;

import net.role4j.evolution.ClassReloader;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by nguonly on 1/14/17.
 */
public class ClassReloadingTest {

//    @Test
    public void canClassReloaderTest() throws IllegalAccessException, InstantiationException {
        Class<?> cls = new ClassReloader().loadClass(Person.class);

        Person p = (Person)cls.newInstance();
        Assert.assertNotNull(p);

        Assert.assertEquals("Person", p.getName());
    }

    public static class Person{
        public String getName(){
            return this.getClass().getSimpleName();
        }
    }
}
