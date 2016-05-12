package es.uniovi.reflection.invokedynamic.util;

/**
 * Created by nguonly on 4/21/16.
 *
 * To emulate the external library of HiPerfTimer that relies on Kernel32 JNA in Windows OS
 */
public class HiPerfTimer {
    private static int calls = 0;
    private static long startTime;
    private static long stopTime;

    public HiPerfTimer(){

    }

    public static void Reset(){
        startTime = 0 ;
        stopTime = 0;
        calls = 0;
    }

    public static void Start(){
        ++calls;
        startTime = System.nanoTime();
    }

    public static void Stop(){
        stopTime = System.nanoTime();
    }

    public static long getElapsedNanoseconds(){
        return stopTime - startTime;
    }

    public static long getElapsedMicroseconds(){
        return getElapsedNanoseconds()/1000L;
    }

    public static long getElapsedMiliseconds(){
        return getElapsedNanoseconds()/1000000L;
    }

}
