package demo.rollback;

import net.role4j.Compartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import net.role4j.Registry;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nguonly on 1/11/17.
 */
public class Main {
    public static void main(String... args) throws Throwable {
        BugSensor sensor = new BugSensor();
//        Thread.setDefaultUncaughtExceptionHandler(sensor);

        Registry reg = Registry.getRegistry();

        A a = reg.newCore(A.class);

        Compartment comp = reg.newCompartment(Compartment.class);
        System.out.println("Compartment: " + comp.hashCode());
        comp.activate();
        IRole b = a.bind(B.class);

        System.out.println(a.doThing());


        Runnable r1 = () -> {
            try {
                comp.activate();
                b.bind(C.class);
            } catch (Throwable throwable) {
//                throwable.printStackTrace();
                throw new RuntimeException("error");

            }
            int i=0;
            while(i<10) {

                    System.out.println(a.doThing());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    try {
//                        throw e;
//                    } catch (InterruptedException e1) {
//                        e1.printStackTrace();
//                    }
                }
                System.out.println(i);
                i++;

            }
        };

//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        executorService.execute(r1);
//
//        executorService.shutdownNow();

        MyThread t = new MyThread(comp, a);
        t.start();

        while(true){
            Thread.sleep(1000);
        }
    }

    public static class A implements IPlayer{
        public String doThing(){
            return this.getClass().getName();
        }
    }

    public static class B implements IRole{
        public String doThing(){
            return this.getClass().getName();
        }
    }

    public static class C implements IRole{
        public String doThing(){
            int i=1/0;
            return this.getClass().getName();
        }
    }
}

