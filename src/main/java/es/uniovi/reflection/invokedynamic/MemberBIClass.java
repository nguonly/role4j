package es.uniovi.reflection.invokedynamic;

import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import es.uniovi.reflection.invokedynamic.codegen.ClassGenerator;
import es.uniovi.reflection.invokedynamic.util.Bootstrap;
import es.uniovi.reflection.invokedynamic.util.DefaultBoostrap;
import es.uniovi.reflection.invokedynamic.util.TypeModifier;

/**
 * Generates and stores the classes that perform a dynamic invocation, providing a specific interface.
 * The generated classes implement the interface pass as a parameter.
 * 
 * @version 0.3
 * @author Computational Reflection Research Group. University of Oviedo
 * @param <T> T is the type of the interface.
 */
class MemberBIClass<T> {
	
	
	private final Class<T> interfaceCallable;
	private final TypeModifier typeModifier;
	private final Class<?> clazz;
	private final String nameInvoke;
	private final String bootstrapType;
	private final Bootstrap bootstrap;
	
	private T call = null;
	
	
	public MemberBIClass(Class<T> interfaceCallable, TypeModifier typeModifier, Class<?> clazz, String nameInvoke, String bootstrapType ){
		 this(null,interfaceCallable,typeModifier,clazz,nameInvoke,bootstrapType);
	}	
	
	/**
	 * 
	 * @param bootstrap Bootstrap Contains the necessary data to invoke the bootstrap method.
	 * @param interfaceCallable An interface with a single method with the same signature as the method to be invoked.
	 * @param typeModifier Indicates if the member is instance or class (static).
	 * @param clazz The class that contains the method to be invoked.
	 */
	public MemberBIClass(Bootstrap bootstrap, Class<T> interfaceCallable, TypeModifier typeModifier, Class<?> clazz ){
	 this(bootstrap,interfaceCallable,typeModifier,clazz,null,null);	
	}
	
	private MemberBIClass(Bootstrap bootstrap, Class<T> interfaceCallable, TypeModifier typeModifier, Class<?> clazz, String nameInvoke, String bootstrapType ){
		this.interfaceCallable = interfaceCallable;
		this.typeModifier  = typeModifier;
		this.clazz = clazz;
		this.nameInvoke = nameInvoke;
		this.bootstrapType = bootstrapType;
		this.bootstrap = bootstrap;
	}
	
	/**
	 *Returns an instance of the generated class. This class implements the interface passed as a parameter.
	 * T is the type of the same interface.
	 * @return An instance of the generated class.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
		
	public T getInstance() throws InstantiationException{
		if(call == null)
			call = generateInstance();
		return call;
	}
	
	/**
	 * Gets the class that extends the interface. T is the type of the interface.
	 * El tipo T se corresponde con el tipo de la interfaz.
	 * @return The generated class that implements the interface.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
	private T generateInstance() throws InstantiationException{
		MethodType mtMethod=null;
		MethodType mtInterfaceSignature = null;
		int lenghtAloadBridge = 0;
		boolean interfaceOptimized = true;
		boolean hasBridge =false;
		boolean useArray[] ={};
		
		String classSignature=null;
		MethodType interfaceBridgeSignature=null;
		Method[] methods = interfaceCallable.getDeclaredMethods();
		int aloadIndexParameter =1;
		if(methods.length != 1)
			throw new IllegalArgumentException("The interface passed as parameter must contain only the method to invoke");
		
		Class<?> rtype =methods[0].getReturnType();
		Class<?>[] parametersType = methods[0].getParameterTypes();
		mtInterfaceSignature = MethodType.methodType(rtype, parametersType);
		String interfaceMethodName = methods[0].getName();		
				
		if(TypeModifier.Instance == typeModifier && clazz != parametersType[0] && parametersType[0] == Object.class){
			parametersType[0] = clazz;
			mtMethod = MethodType.methodType(rtype, parametersType);				
		}
		else
			mtMethod = mtInterfaceSignature;
		
		Bootstrap bt = bootstrap;
		if(bt == null)
			bt = new DefaultBoostrap(clazz,nameInvoke,bootstrapType);		
				
		return new ClassGenerator<T>(bt, classSignature,
				 interfaceCallable.getName(), interfaceMethodName,mtInterfaceSignature,
				 mtMethod,
				 interfaceOptimized,hasBridge,interfaceBridgeSignature,
				 lenghtAloadBridge,useArray,aloadIndexParameter).getInstance();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bootstrap == null) ? 0 : bootstrap.hashCode());
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + interfaceCallable.hashCode();
		result = prime * result	+ ((nameInvoke == null) ? 0 : nameInvoke.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;		
		MemberBIClass<?> other = (MemberBIClass<?>) obj;
		
		if (nameInvoke == null) {
			if (other.nameInvoke != null)
				return false;
		} else if (!nameInvoke.equals(other.nameInvoke))
			return false;
		
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		
		if (!interfaceCallable.equals(other.interfaceCallable))
			return false;
		
		if (bootstrap == null) {
			if (other.bootstrap != null)
				return false;
		} else if (!bootstrap.equals(other.bootstrap))
			return false;
			
		return true;
	}



		
	
	
}
