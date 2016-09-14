package transaction;

import net.role4j.*;
import net.role4j.trans.Transaction;
import org.junit.Assert;
import org.junit.Test;
import rolefeature.BaseTest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by nguonly on 6/1/16.
 */
public class InvocationWithTransactionTest extends BaseTest {
    public static class Any implements IPlayer {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class A implements IRole {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class B implements IRole  {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class C implements IRole{
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class D implements IRole{
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    @Test
    public void testInvocationWithoutBinding() throws Throwable{
        Registry reg = Registry.getRegistry();

        Any any = reg.newCore(Any.class);
        any.setName("Any");
        Compartment comp = reg.newCompartment(Compartment.class);
        comp.activate();

        Assert.assertEquals("Any", any.getName());

        try(Transaction tx = new Transaction()){
            for(int i=0;i<50;i++) {
                Assert.assertEquals("Any", any.getName());

                Thread.sleep(20);
            }
        }
    }


    @Test
    public void testThreeTransactionRunningWithDifferentResults() throws Throwable{
        Registry reg = Registry.getRegistry();

        Any any = reg.newCore(Any.class);
        Compartment comp = reg.newCompartment(Compartment.class);

        comp.activate();
        //before any binding
        any.setName("Any");
//        any.bind(D.class);
//        any.setName("D");

        final int[] counter = new int[3]; //measurement variable for thread

        Runnable bindingThread = () -> {
            comp.activate();
            try {
                IRole a = any.bind(A.class);
//                any.setName("A");
//                a.bind(B.class);
//                any.setName("B");
//                System.out.println("Unbinding: " + any.getName());
                System.out.println("Unbinding completed");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            comp.deactivate();
        };

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
        pool.schedule(bindingThread, 100, TimeUnit.MILLISECONDS);

        try(Transaction tx = new Transaction()){
//            DumpHelper.displayTransactionCallables();
            for(int i=0; i<100; i++){
                String ret = any.getName();
//                System.out.println(ret);
                Assert.assertEquals("Any", ret);
                Thread.sleep(30);
            }
        }

        pool.shutdown();

        //Wait for all threads to finish
        while(!pool.isTerminated()){}

        //Assertion the thread
//        Assert.assertEquals(100, counter[0]);
//        Assert.assertEquals(100, counter[1]);
    }
}
