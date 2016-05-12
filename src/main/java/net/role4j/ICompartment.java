package net.role4j;

import java.util.List;

/**
 * Created by nguonly on 4/29/16.
 */
public interface ICompartment extends IPlayer{
    Registry _reg = Registry.getRegistry();

    default void activate(){
        _reg.activateCompartment(this);
    }

    default void deactivate(){
        _reg.deactivateCompartment(this);
    }

}
