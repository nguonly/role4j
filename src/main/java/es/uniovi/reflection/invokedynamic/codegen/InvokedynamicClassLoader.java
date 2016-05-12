package es.uniovi.reflection.invokedynamic.codegen;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * The class loader is responsible for loading the generating classes in the library.
 * @version 0.3
 * @author Computational Reflection Research Group. University of Oviedo
 *
 */
public class InvokedynamicClassLoader extends ClassLoader { // spring classLoader DecoratingClassLoader{

	//-classLoader spring - constructor
	//super(ClassUtils.getDefaultClassLoader());
	
	private static Hashtable<String, Class<?>> classes;
	private static InvokedynamicClassLoader instance= null;
	
	private InvokedynamicClassLoader(){		
		super(ClassLoader.getSystemClassLoader());
		classes = new Hashtable<String, Class<?>> () ;
	}
	
	public static InvokedynamicClassLoader getClassLoader(){
		if(instance == null)
			instance = new InvokedynamicClassLoader();
		return instance;
	}
	
		
	  public  Class<?> register(String name, byte[] code) throws Throwable{
		Class<?> c =  defineClass(name.replace("/", "."), code, 0, code.length);
		classes.put(name, c);
		return c;
		}
	  
	@Override
	 protected Class<?> findClass(String name) throws ClassNotFoundException {
		 byte classByte[];
	        Class<?> result=null;
	        result = (Class<?>)classes.get(name);
	        if(result != null){
	            return result;
	        }
	        
	        try{
	            return findSystemClass(name);
	        }catch(Exception e){
	        }
	        try{
	           String classPath =    ((String)ClassLoader.getSystemResource(name.replace('.',File.separatorChar)+".class").getFile()).substring(1);
	           
	           classByte = loadClassData(classPath);
	            result = defineClass(name,classByte,0,classByte.length,null);
	            classes.put(name,result);
	            return result;
	        }catch(Exception e){
	            return null;
	        } 
	 }
	
	 private byte[] loadClassData(String className) throws IOException{
		 
	        File f ;
	        f = new File(className);
	        int size = (int)f.length();
	        byte buff[] = new byte[size];
	        FileInputStream fis = new FileInputStream(f);
	        DataInputStream dis = new DataInputStream(fis);
	        dis.readFully(buff);
	        dis.close();
	        return buff;
	    }
	 
	  
	
}
