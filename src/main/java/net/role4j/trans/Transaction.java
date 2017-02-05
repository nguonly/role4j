package net.role4j.trans;

import net.role4j.Registry;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Created by nguonly on 5/31/16.
 */
public class Transaction implements AutoCloseable{
    public Transaction(){
        beginTransaction();
    }

    @Override
    public void close()  {
        endTransaction();
    }

    private synchronized void beginTransaction(){
        Registry reg = Registry.getRegistry();
        LocalDateTime time = LocalDateTime.now();
        long threadId = Thread.currentThread().getId();
        int transId = this.hashCode();
        reg.registerTransaction(threadId, transId, time);
    }

    private void endTransaction(){
        Registry reg = Registry.getRegistry();
        long threadId = Thread.currentThread().getId();
        int transId = this.hashCode();
        reg.removeTransaction(threadId, transId);
    }
}
