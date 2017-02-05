package demo.rollback;

import demo.rollback.server.AppState;
import demo.rollback.server.ServiceHandler;
import net.role4j.DumpHelper;
import net.role4j.ICompartment;
import net.role4j.Registry;
import net.role4j.rollback.ControlUnit;

import java.net.Socket;

/**
 * Created by nguonly on 1/11/17.
 */
public class BugSensor implements Thread.UncaughtExceptionHandler {
    private Socket client;

    public BugSensor(){

    }

    public BugSensor(Socket client){
        this.client = client;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(t + " Throwable: " + e);
        System.out.println("Thread status: " + t.getState());
        Registry reg = Registry.getRegistry();
        ICompartment comp = reg.getActiveCompartments().get(t.getId());
//        System.out.println(comp.hashCode());
//        t.start();
//        AppState.resetAdaptation();
        ControlUnit.rollback(comp.hashCode());
        System.out.println("Rollback >>>");
        AppState.appendMessage(t + " throws " + e);
        AppState.appendMessage(t.getName() + ">>>>> Rollback >>>> ");
        DumpHelper.dumpRelations();
        ServiceHandler handler = new ServiceHandler(client);
        handler.start();
    }
}
