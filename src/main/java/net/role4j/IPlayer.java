package net.role4j;

import java.util.HashMap;

/**
 * Created by nguonly on 4/27/16.
 */
public interface IPlayer {
    Registry _reg = Registry.getRegistry();

    IPlayer _real = null;

    public HashMap<Integer, Relation> _callables = new HashMap<>();

    int _level = 0;
    int _sequence = 0;

    default <T> T newPlayer(Class<T> klazz) throws Throwable{
        return _reg.newCore(klazz);
    }

    default <T extends IRole> IRole bind(Class<T> klass, Object... args) throws Throwable{
        return _reg.bindCore(this, klass, args);
    }

    default <T extends IRole> IRole bind(ICompartment compartment, Class<T> clazz, Object... args) throws Throwable{
        return _reg.bindCore(compartment, this, clazz, args);
    }

    default <T> void unbind(Class<T> klazz) throws Throwable{
//        _reg.unbind(this, klazz);
        _reg.unbind(this, true, klazz);
    }

    default void unbindAll() throws Throwable{
        _reg.unbindAll(this);
    }

    default <T> T interfaceOf(Class<T> clazz){
        try {
            return _reg.interfaceOf(this, true, clazz);
        }catch(Throwable e){
            System.out.println(e);
        }
        return null;
    }

    default void transfer(Object role, Object toPlayer, ICompartment toCompartment){
        try {
            _reg.transfer(role, this, toPlayer, toCompartment);
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    /**
     * The core object of specific role or compartment instance
     * @param object is either role or compartment
     * @return
     */
    default Object getCore(Object object){
        try{
            return _reg.getCore(object);
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;
    }

    default <T> T getRoleInstance(Class<T> roleType){
        return _reg .getRoleInstance(this, roleType);
    }

    default <T> T getCompartment(Class<T> compartmentType){
        return _reg.getCompartment(this, compartmentType);
    }
}
