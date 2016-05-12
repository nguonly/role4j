package es.uniovi.reflection.invokedynamic.codegen;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
/**
 * Performs the dynamic generation of CallSites, used in the dynamic invocations.
 * @author Computational Reflection Research Group. University of Oviedo
 * @version 0.3
 *
 */
public class InvokedynamicBootstrap {

	public static final String BOOTSTRAP_CLASS = "es/uniovi/reflection/invokedynamic/codegen/InvokedynamicBootstrap";
	
	public static final String CONSTRUCTOR_BOOTSTRAP = "constructorBootstrap";
	public static final String STATIC_BOOTSTRAP_METHOD = "staticBootstrapMethod";
	public static final String INSTANCE_BOOTSTRAP_METHOD = "instanceBootstrapMethod";
	public static final String GET_STATIC_BOOTSTRAP_ATTRIBUTE = "getStaticBootstrapAttribute";
	public static final String SET_STATIC_BOOTSTRAP_ATTRIBUTE = "setStaticBootstrapAttribute";
	public static final String GET_INSTANCE_BOOTSTRAP_ATTRIBUTE = "getInstanceBootstrapAttribute";
	public static final String SET_INSTANCE_BOOTSTRAP_ATTRIBUTE = "setInstanceBootstrapAttribute";
		
	public static  Lookup Mhlookup = null;
		
	/**
	 * Bootstrap used to generate CallSite for constructors.
	 * @param lookup lookup. It only searches for public members in classes.
	 * @param name Name of the class that calls the Bootstrap.
	 * @param type Signature of the constructor to be invoked.
	 * @param nameClass Name of the class we want to create an instance of.
	 * @param member Name of the constructor. In this case it will be the empty string.
	 * @return A CallSite for constructors.
	 * @throws NoSuchMethodException There is no method with the specified signature.
     * @throws IllegalAccessException If the class of the member to be invoked is not public.
	 */
	public static CallSite constructorBootstrap(Lookup lookup, String name, MethodType type, Class<?> nameClass, String member) throws NoSuchMethodException, IllegalAccessException {
		//System.out.println("> bootstrap constructor: lookup = " + lookup.lookupClass() + ", name = " + name + ", methodtype = " + type);
		mh = Mhlookup.findConstructor(nameClass,MethodType.methodType(void.class, type.parameterArray()));
		return new ConstantCallSite(mh);
	}
	
	/**
	 * Bootstrap used to generate CallSite for static methods.
	 * @param lookup lookup. It only searches for public members in classes.
	 * @param name Name of the class that calls the Bootstrap.
	 * @param type Signature of the method to be invoked.
	 * @param nameClass Name of the class that contains the method to be dynamically invoked.
	 * @param member Name of the method to be invoked.
	 * @return A CallSite for static methods.
	 * @throws NoSuchMethodException There is no method with the specified signature.
     * @throws IllegalAccessException If the class of the member to be invoked is not public.
	 */
	public static CallSite staticBootstrapMethod(Lookup lookup, String name,MethodType type, Class<?> nameClass, String member) throws NoSuchMethodException, IllegalAccessException{
		//System.out.println("> >bootstrap method: lookup = " + lookup	+ ", name = " + name + ", methodtype = " + type);
		
		mh = Mhlookup.findStatic(nameClass,member,type);		
		return new ConstantCallSite(mh);
	}
	
	/**
	 * Bootstrap used to generate CallSite for instance methods, which can implement an interface or override an abstract method.
	 * @param lookup Public lookup. It only searches for public members in classes.
	 * @param name Name of the class that calls the Bootstrap.
	 * @param type Signature of the method to be invoked.
	 * @param nameClass Name of the class that contains the method to be dynamically invoked.
	 * @param member Name of the method to be invoked.
	 * @return A CallSite for instance methods, which can implement an interface or override an abstract method.
	 * @throws NoSuchMethodException There is no method with the specified signature.
     * @throws IllegalAccessException If the class of the member to be invoked is not public.
	 */
	public static CallSite instanceBootstrapMethod(Lookup lookup, String name, MethodType type, Class<?> nameClass, String member) throws NoSuchMethodException, IllegalAccessException {
		//System.out.println("> >bootstrap method: lookup = " + lookup + ", name = " + name + ", methodtype = " + type);
		mh = Mhlookup.findVirtual(nameClass, member,type.dropParameterTypes(0, 1));
		return new ConstantCallSite(mh);
	}
	
	/**
	 * Bootstrap used to generate CallSite for static fields to get their value.
	 * @param lookup Public lookup. It only searches for public members in classes.
	 * @param name Name of the class that calls the Bootstrap.
	 * @param type Signature of the field to be invoked.
	 * @param nameClass Name of the class that contains the field to be dynamically invoked.
	 * @param member Name of the field to be invoked.
	 * @return A CallSite for static fields to get their value.
	 * @throws NoSuchFieldException When a member with the specified signature does not exist.
     * @throws IllegalAccessException If the class of the member to be invoked is not public.
	 */
	public static CallSite getStaticBootstrapAttribute(Lookup lookup,String name, MethodType type,Class<?> nameClass,String member) throws NoSuchFieldException, IllegalAccessException {
		//System.out.println("> >static bootstrap attribute : lookup = " + lookup + ", name = " + name + ", methodtype = " + type);
		mh = Mhlookup.findStaticGetter(nameClass,	member,type.returnType());
		return new ConstantCallSite(mh);
	}

	/**
	 * Bootstrap used to generate CallSite for static fields to set their value.
	 * @param lookup Public lookup. It only searches for public members in classes.
	 * @param name Name of the class that calls the Bootstrap.
	 * @param type Signature of the field to be invoked.
	 * @param nameClass Name of the class that contains the field to be dynamically invoked.
	 * @param member Name of the field to be invoked.
	 * @return A CallSite for static fields to set their value.
	 * @throws NoSuchFieldException When a member with the specified signature does not exist.
     * @throws IllegalAccessException If the class of the member to be invoked is not public.
	 */
	public static CallSite setStaticBootstrapAttribute(Lookup lookup,String name, MethodType type, Class<?> nameClass, String member) throws NoSuchFieldException, IllegalAccessException {
		//System.out.println("> >static bootstrap attribute : lookup = " + lookup	+ ", name = " + name + ", methodtype = " + type);
		mh = Mhlookup.findStaticSetter(nameClass,	member, type.parameterType(0));
		return new ConstantCallSite(mh);
	}

	/**
	 * Bootstrap used to generate CallSite for instance fields to get their value.
	  * @param lookup Public lookup. It only searches for public members in classes.
	 * @param name Name of the class that calls the Bootstrap
	 * @param type Signature of the field to be invoked.
	 * @param nameClass Name of the class that contains the field to be dynamically invoked
	 * @param member Name of the field to be invoked.
	 * @return A CallSite for instance fields to get their value.
	 * @throws NoSuchFieldException When a member with the specified signature does not exist.
     * @throws IllegalAccessException If the class of the member to be invoked is not public.
	 */
	public static CallSite getInstanceBootstrapAttribute(Lookup lookup,	String name, MethodType type,Class<?> nameClass,String member) throws NoSuchFieldException, IllegalAccessException {
		//System.out.println("> > instance bootstrap attribute : lookup = " + lookup	+ ", name = " + name + ", methodtype = " + type);
		mh  =Mhlookup.findGetter(nameClass, member, type.returnType());
		return new ConstantCallSite(mh);
	}

	/**
	 * Bootstrap used to generate CallSite for instance fields to set their value.
	 * @param lookup Public lookup. It only searches for public members in classes.
	 * @param name Name of the class that calls the Bootstrap.
	 * @param type Signature of the field to be invoked.
	 * @param nameClass Name of the class that contains the field to be dynamically invoked.
	 * @param member Name of the field to be invoked.
	 * @return A CallSite for instance fields to set their value.
	 * @throws NoSuchFieldException If the class of the member to be invoked is not public.
     * @throws IllegalAccessException When a member with the specified signature does not exist.
	 */
	public static CallSite setInstanceBootstrapAttribute(Lookup lookup,	String name, MethodType type, Class<?> nameClass, String member) throws  IllegalAccessException, NoSuchFieldException {
		//System.out.println("> > instance bootstrap attribute : lookup = " + lookup	+ ", name = " + name + ", methodtype = " + type);
		mh = Mhlookup.findSetter(nameClass, member, type.parameterType(1));
		return new ConstantCallSite(mh);
	}
	
	/**
	 * Method handle that holds the member to be invoked.
	 */
	private static MethodHandle mh;
	
	/**
	 * Field used to search for methods and fields without visibility constraints.
	 */
	private static int TRUSTED = -1;
	
	static{
		
		try {
			/**
			 * Para poder realizar bquedas de mtodos y propiedades sin restricciones de visibilidad,
			 * se crea una nueva instancia del Lookup. Para ello se llama al contructor privado que recibe
			 * como parmetros, la clase desde la que se va a realizar las bsquedas (esto dato no tiene
			 * mucha importancia) y el nivel de visibilidad que queremos para realizar las bsquedas. En
			 * este caso se pasa el valor TRUSTED que no da una visibilidad total.
			 */
		Constructor<?>[] cons = Lookup.class.getDeclaredConstructors();
		for(int i=0; i<cons.length;i++){
		if(cons[i].getParameterTypes().length==2 && cons[i].getParameterTypes()[0] == Class.class){
			cons[i].setAccessible(true);
			Mhlookup = (Lookup) cons[i].newInstance(new Object[]{InvokedynamicBootstrap.class, TRUSTED});
			break;
		   }
		}
				
		} catch (Throwable e) {
					
		}finally{
			/**
			 * Si ocurre algn error durante la creacin del bootstrap, se crear un lookup pblico.
			 * Un lookup pblico slo puede ver los mtodos pblicos de las clases.
			 */
			if(Mhlookup == null){
				Mhlookup = MethodHandles.lookup();
			}
		}
		
	}
	
	
}
