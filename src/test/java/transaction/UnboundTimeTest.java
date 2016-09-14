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
public class UnboundTimeTest extends BaseTest {
    public static class Any implements IPlayer {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class A implements IRole  {
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

    public static class E implements IRole{

    }

    public static class F implements IRole{

    }

    public static class G implements IRole{

    }

    public static class H implements IRole{

    }

    @Test
    public void testUnboundInThePresenceOfTransaction() throws Throwable{
        Registry reg = Registry.getRegistry();

        Any any = reg.newCore(Any.class);
        Compartment comp = reg.newCompartment(Compartment.class);

        comp.activate();
        any.setName("Any");

        any.bind(C.class).bind(D.class);

        any.setName("MyD");

        Runnable unbindRunnable = () -> {
            comp.activate();
            try {
                any.unbind(C.class);
                DumpHelper.dumpRelations();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            comp.deactivate();
        };

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.schedule(unbindRunnable, 100, TimeUnit.MILLISECONDS);

        try(Transaction tx = new Transaction()){
            for(int i=0; i<50; i++){
                String ret = any.getName();
                System.out.println(ret);
                Assert.assertEquals("MyD", ret);

                Thread.sleep(10);
            }
        }
    }

    @Test
    public void testTwoTransactionsWithUnbinding() throws Throwable{
        Registry reg = Registry.getRegistry();

        Any any = reg.newCore(Any.class);
        Compartment comp = reg.newCompartment(Compartment.class);

        comp.activate();
        any.setName("Any");

        any.bind(C.class).bind(D.class);

        any.setName("MyD");

        Runnable unbindRunnable = () -> {
            comp.activate();
            try {
                any.unbind(C.class);
                DumpHelper.dumpRelations();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            comp.deactivate();
        };

        final int[] counter = new int [1];
        Runnable newTransRunnable = () -> {
            comp.activate();
            try(Transaction tx2 = new Transaction()) {
                int c = 0;
                for (int i = 0; i < 50; i++) {
                    String ret = any.getName();
//                System.out.println(ret);
                    if (ret.equals("Any")) c++;

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                counter[0] = c;
            } catch (Exception e) {
                e.printStackTrace();
            }
            comp.deactivate();
        };

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.schedule(unbindRunnable, 100, TimeUnit.MILLISECONDS);
        pool.schedule(newTransRunnable, 150, TimeUnit.MILLISECONDS);

        try(Transaction tx = new Transaction()){
            for(int i=0; i<50; i++){
                String ret = any.getName();
//                System.out.println(ret);
                Assert.assertEquals("MyD", ret);

                Thread.sleep(10);
            }
        }

        pool.shutdown();

        //wait for thread to finish
        while(!pool.isTerminated()){ }

        //Assert result in the thread
        Assert.assertEquals(50, counter[0]);
    }

    @Test
    public void testThreeTransactionRunningWithDifferentResults() throws Throwable{
        Registry reg = Registry.getRegistry();

        Any any = reg.newCore(Any.class);
        Compartment comp = reg.newCompartment(Compartment.class);

        comp.activate();
        //before any binding
//        any.setName("Any");
        any.bind(D.class);
        any.setName("D");

        final int[] counter = new int[3]; //measurement variable for thread

        Runnable bindingThread = () -> {
            comp.activate();
            try {
                IRole a = any.bind(A.class);
                any.setName("A");
                a.bind(B.class);
                any.setName("B");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            comp.deactivate();
        };

        Runnable tx2Thread = () -> {
            comp.activate();
            try(Transaction tx2 = new Transaction()){
                int c = 0;
                for(int i=0; i<100; i++) {
                    String ret = any.getName();
                    if (ret.equals("B")) c++;
                    Thread.sleep(20);
                }
                counter[0] = c;
            }catch (Exception e){
                e.printStackTrace();
            }
            comp.deactivate();
        };

        Runnable unbindingThread = () -> {
            comp.activate();
            try {
                any.unbind(B.class);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            comp.deactivate();
        };

        Runnable tx3Thread = () -> {
            comp.activate();
            try(Transaction tx3 = new Transaction()){
                int c = 0;
                for(int i=0; i<100; i++){
                    String ret = any.getName();
                    if(ret.equals("A")) c++;
                    Thread.sleep(20);
                }
                counter[1] = c;
            } catch (Exception e) {
                e.printStackTrace();
            }
            comp.deactivate();
        };

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
        pool.schedule(bindingThread, 100, TimeUnit.MILLISECONDS);
        pool.schedule(tx2Thread, 150, TimeUnit.MILLISECONDS);
        pool.schedule(unbindingThread, 180, TimeUnit.MILLISECONDS);
        pool.schedule(tx3Thread, 210, TimeUnit.MILLISECONDS);

        try(Transaction tx = new Transaction()){
            for(int i=0; i<100; i++){
                String ret = any.getName();
                Assert.assertEquals("D", ret);
                Thread.sleep(30);
            }
        }

        pool.shutdown();

        //Wait for all threads to finish
        while(!pool.isTerminated()){}

        //Assertion the thread
        Assert.assertEquals(100, counter[0]);
        Assert.assertEquals(100, counter[1]);
    }
}
