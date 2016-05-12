package net.role4j.bytebuddy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by nguonly on 4/30/16.
 */
public class ProxyInvocationHandler implements InvocationHandler {
    public ProxyInvocationHandler(Object core){}

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
