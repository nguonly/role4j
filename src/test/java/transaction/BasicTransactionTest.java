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
 * Created by nguonly on 5/31/16.
 */
public class BasicTransactionTest extends BaseTest {

    public static class Person implements IPlayer{
        public String getName(){
            return this.getClass().getSimpleName();
        }
    }

    public static class Student implements IRole{
        public String getName(){
            return this.getClass().getSimpleName();
        }
    }

    public static class TeachingAssistant implements IRole{
        public String getName(){
            return this.getClass().getSimpleName();
        }
    }

    public static class Employee implements IRole {
        public String getName(){
            return this.getClass().getSimpleName();
        }
    }

    @Test
    public void testNewBindingNotAffectTransaction() throws Throwable{
        Registry reg = Registry.getRegistry();

        Person p = reg.newCore(Person.class);
        Compartment comp = reg.newCompartment(Compartment.class);

        comp.activate();
        p.bind(Student.class);

        Runnable newBinding = () -> {
            try {
                p.bind(comp, Employee.class);

                DumpHelper.dumpRelations();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.schedule(newBinding, 100, TimeUnit.MILLISECONDS);

        try(Transaction tx = new Transaction()) {
            DumpHelper.displayTransactionCallables();
            for(int i=0; i<100; i++) {
                String ret = p.getName();
                Assert.assertEquals("Student", ret);
//                System.out.println(ret);

                Thread.sleep(20);
            }
        }
    }

    @Test
    public void testTwoTransactionsRunInParallelWithDifferentBindings() throws Throwable{
        Registry reg = Registry.getRegistry();

        Person p = reg.newCore(Person.class);
        Compartment comp = reg.newCompartment(Compartment.class);

        comp.activate();
        p.bind(Student.class);

        Runnable newBinding = () -> {
            try {
                p.bind(comp, Employee.class);

                DumpHelper.dumpRelations();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        Runnable newTxRunnable = () -> {
            comp.activate();
            try(Transaction newTx = new Transaction()) {
                int counter = 0;
                for (int i = 0; i < 50; i++) {
                    String ret = p.getName();
                    System.out.println(ret);

                    //Mock to assert
                    if(ret.equals("Employee")){
                        counter++;
                    }
                    Thread.sleep(20);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            comp.deactivate();
        };

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.schedule(newBinding, 100, TimeUnit.MILLISECONDS);
        pool.schedule(newTxRunnable, 150, TimeUnit.MILLISECONDS);

        try(Transaction tx = new Transaction()) {
            DumpHelper.displayTransactionCallables();
            for(int i=0; i<100; i++) {
                String ret = p.getName();
                Assert.assertEquals("Student", ret);
                System.out.println(ret);

                Thread.sleep(20);
            }
        }
    }

    @Test
    public void testTwoTransactionsInRolePlaysRole() throws Throwable{
        Registry reg = Registry.getRegistry();

        Person p = reg.newCore(Person.class);
        Compartment comp = reg.newCompartment(Compartment.class);

        comp.activate();
        p.bind(Student.class).bind(TeachingAssistant.class);

        Runnable newBinding = () -> {
            try {
                p.bind(comp, Employee.class);

                DumpHelper.dumpRelations();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        final int[] counter = new int[1];
        Runnable newTxRunnable = () -> {
            comp.activate();
            try(Transaction newTx = new Transaction()) {
                int c = 0;
                for (int i = 0; i < 50; i++) {
                    String ret = p.getName();
//                    System.out.println(ret);

                    //Mock to assert
                    if(ret.equals("Employee")){
                        c++;
                    }
                    Thread.sleep(20);
                }
                counter[0] = c;
            } catch (Exception e) {
                e.printStackTrace();
            }
            comp.deactivate();
        };

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.schedule(newBinding, 100, TimeUnit.MILLISECONDS);
        pool.schedule(newTxRunnable, 150, TimeUnit.MILLISECONDS);

        try(Transaction tx = new Transaction()) {
            DumpHelper.displayTransactionCallables();
            for(int i=0; i<100; i++) {
                String ret = p.getName();
                Assert.assertEquals("TeachingAssistant", ret);
//                System.out.println(ret);

                Thread.sleep(20);
            }
        }

        //Assert the thread result
        Assert.assertEquals(50, counter[0]);
    }
}
