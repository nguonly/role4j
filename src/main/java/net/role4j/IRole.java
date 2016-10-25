package net.role4j;

import java.util.List;

/**
 * Created by nguonly on 4/27/16.
 */
public interface IRole extends IPlayer {
    default <T extends IRole> IRole bind(Class<T> klass, Object... args) throws Throwable{
        return _reg.bindRole(this, klass, args);
    }

    default Object getPlayer(){
        return _reg.getPlayer(this);
    }

    default <T> T interfaceOf(Class<T> clazz){
        try {
            return _reg.interfaceOf(this, false, clazz); //false means role
        }catch(Throwable e){
            System.out.println(e);
        }
        return null;
    }

    default <T> void unbind(Class<T> klazz) throws Throwable{
//        _reg.unbind(this, klazz);
        _reg.unbind(null, this, false, klazz);
    }

    default <T> T coreOf(Class<T> clazz){
        try{

        }catch(Throwable e){
            System.out.println(e);
        }
        return null;
    }

    default <T> T roleOf(Class<T> clazz){
        try {
            return clazz.newInstance();
        }catch(Throwable e){
            System.out.println(e);
        }
        return null;
    }

    default <T> List<IPlayer> getCores(Class<T> roleType){
        try{
            return _reg.getCores(roleType);
        }catch(Throwable e){
            System.out.println(e);
        }
        return null;
    }

    default <T> T getPlayer(Class<T> playerType){
        return _reg.getPlayer(this, playerType);
    }
}
