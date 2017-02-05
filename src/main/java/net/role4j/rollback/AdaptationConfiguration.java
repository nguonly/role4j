package net.role4j.rollback;

/**
 * Created by nguonly on 1/12/17.
 */
public class AdaptationConfiguration implements AutoCloseable {
    public AdaptationConfiguration(){
        enter();
    }

    @Override
    public void close() {
        leave();
    }

    private void enter(){
        ControlUnit.checkpoint();
    }

    private void leave(){

    }
}
