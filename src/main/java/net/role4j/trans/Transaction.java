package net.role4j.trans;

import net.role4j.Registry;

/**
 * Created by nguonly on 5/31/16.
 */
public class Transaction implements AutoCloseable{
    public Transaction(){
        beginTransaction();
    }

    @Override
    public void close() throws Exception {
        endTransaction();
    }

    private void beginTransaction(){
        Registry reg = Registry.getRegistry();
        long threadId = Thread.currentThread().getId();
        int transId = this.hashCode();
        reg.registerTransaction(threadId, transId);
    }

    private void endTransaction(){
        Registry reg = Registry.getRegistry();
        long threadId = Thread.currentThread().getId();
        int transId = this.hashCode();
        reg.removeTransaction(threadId, transId);
    }
}
