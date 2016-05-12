package net.role4j;

import es.uniovi.reflection.invokedynamic.interfaces.Callable;

/**
 * Created by nguonly on 4/29/16.
 */
public class CallableMethod {

    public String method;
    public Callable<?> callable;
    public Object invokingObject;

    public CallableMethod(){

    }

    public CallableMethod(String method, Object invokingObject, Callable<?> callable){
        this.method = method;
        this.invokingObject = invokingObject;
        this.callable = callable;
    }
}
