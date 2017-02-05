package dynclassloading;

import dynclassloading.subject.MyObject;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.role4j.ReflectionHelper;
import net.role4j.evolution.ClassReloader;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by nguonly on 1/14/17.
 */
public class ByteBuddyClassReloadingTest {

    //@Test
    public void testClassReloading() throws InstantiationException, IllegalAccessException, InterruptedException, NoSuchMethodException, InvocationTargetException {
        ByteBuddyAgent.install();

        int i=0;
        while(i<10) {
            MyObject o = createObject(MyObject.class);
            o.doThing();

//            Class<?> cls = new ClassReloader().loadClass(MyObject.class);
//            Object o = cls.newInstance();
//
//            Method m = cls.getDeclaredMethod("doThing");
//            m.invoke(o);


            Thread.sleep(3000);
            i++;
        }
    }

    private <T> T createObject(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        Class<T> aa = (Class<T>)new ClassReloader().loadClass(clazz);
        Class<? extends T> proxyType = new ByteBuddy(ClassFileVersion.ofThisVm())
                .rebase(clazz)

                .make()
                .load(clazz.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded();

        Class<? extends T> sub = new ByteBuddy()
                .subclass(proxyType)
                .make()
                .load(proxyType.getClassLoader(), ClassReloadingStrategy.Default.WRAPPER)
                .getLoaded();

//        return proxyType.newInstance();
        return sub.newInstance();
    }
}
