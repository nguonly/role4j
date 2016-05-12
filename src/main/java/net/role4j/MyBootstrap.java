package net.role4j;

import java.lang.invoke.*;

/**
 * Created by nguonly on 4/22/16.
 */
public class MyBootstrap {
    private static MethodHandle mhToString = null;

    public static CallSite callDispatch(MethodHandles.Lookup lookup, String name, MethodType methodType, Class<?> paramClass, String methodName) throws Throwable{

//        if(callSite != null)
//            return callSite;

        //mhToString  = lookup.findVirtual(paramClass, methodName, methodType.dropParameterTypes(0, 1));
        mhToString  = lookup.findVirtual(paramClass, methodName, methodType.dropParameterTypes(0, 1));

        //MethodType mtProfile = MethodType.methodType(methodType.returnType(),	MethodHandle.class, paramClass);
        //mhProfile = lookup.findStatic(MyBootstrap.class, "profile", mtProfile);
        //mhProfile = MethodHandles.insertArguments(mhProfile, 0, mhToString);


        //return callSite =  new MutableCallSite(mhToString);
        return new ConstantCallSite(mhToString);

    }

    public static CallSite anotherDispatch(MethodHandles.Lookup lookup, String name, MethodType methodType, Class<?> paramClass, String methodName) throws Throwable{

//        if(callSite != null)
//            return callSite;

        //mhToString  = lookup.findVirtual(paramClass, methodName, methodType.dropParameterTypes(0, 1));
        mhToString  = lookup.findVirtual(paramClass, methodName, methodType.dropParameterTypes(0, 1));

        //MethodType mtProfile = MethodType.methodType(methodType.returnType(),	MethodHandle.class, paramClass);
        //mhProfile = lookup.findStatic(MyBootstrap.class, "profile", mtProfile);
        //mhProfile = MethodHandles.insertArguments(mhProfile, 0, mhToString);


        //return callSite =  new MutableCallSite(mhToString);
        return new ConstantCallSite(mhToString);

    }
}
