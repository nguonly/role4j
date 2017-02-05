package rolefeature;

import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * Created by nguonly on 5/17/16.
 */
public class ParallelExecutionTest extends BaseTest{

    public static class Person implements IPlayer {
        public String speak(){
            return "Person";
        }
    }

    public static class Student implements IRole{
        public String speak(){
            return "Student";
        }
    }

    public static class Father implements IRole{
        public String speak(){
            return "Father";
        }
    }

    public static class MyCompartment implements ICompartment{

    }

    @Test
    public void testBehaviorOfObjectExecutingInDifferentThreads() throws Throwable{
        Person ly = _reg.newCore(Person.class);

        MyCompartment school = _reg.newCompartment(MyCompartment.class);
        MyCompartment home = _reg.newCompartment(MyCompartment.class);

        school.activate();
        ly.bind(Student.class);
        school.deactivate();

        home.activate();
        ly.bind(Father.class);
        home.deactivate();

        Runnable r1 = () -> {
            school.activate();
            for(int i=0; i<1_000_000; i++) {
                String ret = ly.speak();
                Assert.assertEquals("Student", ret);
            }
            school.deactivate();
        };

        Runnable r2 = () -> {
            home.activate();
            for(int i=0; i<1_000_000; i++) {
                String ret = ly.speak();
                Assert.assertEquals("Father", ret);
            }
            home.deactivate();
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(r1);
        executor.execute(r2);

        executor.shutdown();

        //wait for all threads finish their tasks
        while(!executor.isTerminated()){ }
    }

    @Test
    public void testBehaviorOfObjectInsideAnActiveCompartment() throws Throwable{
        Person ly = _reg.newCore(Person.class);

        MyCompartment world = _reg.newCompartment(MyCompartment.class);
        world.activate();
        ly.bind(Student.class);

        final boolean[] bindingFinished = {false};

        Runnable binding = () -> {
            world.activate();
            try {
                ly.bind(Father.class);
                bindingFinished[0] =true;
//                System.out.println(":::::::::::: binding completed :::::::::::");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.schedule(binding, 100, TimeUnit.MILLISECONDS);

        for (int i = 0; i < 1_00; i++) {
            Thread.sleep(50);
            if(ly.getRoleInstance(Father.class)==null) {
                Assert.assertEquals("Student", ly.speak());
            }else{
                Assert.assertEquals("Father", ly.speak());
            }

        }


        pool.shutdown();

        //waiting
        while(!pool.isTerminated()) {}

    }
}
