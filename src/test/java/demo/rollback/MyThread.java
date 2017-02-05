package demo.rollback;

import net.role4j.ICompartment;

/**
 * Created by nguonly on 1/11/17.
 */
public class MyThread extends Thread {
    private ICompartment comp;
    private Main.A a;

    public MyThread(ICompartment comp, Main.A a){
        this.comp = comp;
        this.a = a;
    }

    public void run(){
        try {
            comp.activate();
            a.bind(Main.C.class);
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
            }
            System.out.println(i);
            i++;

        }
    }
}
