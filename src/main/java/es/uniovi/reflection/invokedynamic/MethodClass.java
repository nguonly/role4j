package es.uniovi.reflection.invokedynamic;

import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import es.uniovi.reflection.invokedynamic.codegen.ClassGenerator;
import es.uniovi.reflection.invokedynamic.codegen.GeneratorUtil;
import es.uniovi.reflection.invokedynamic.codegen.InvokedynamicBootstrap;
import es.uniovi.reflection.invokedynamic.interfaces.Callable;
import es.uniovi.reflection.invokedynamic.util.DefaultBoostrap;

/**
 * Generates and stores the classes that perform a dynamic invocation, providing a Method instance. 
 * The generated classes implement the Callable interface.
 * 
 * @version 0.3
 * @author Computational Reflection Research Group. University of Oviedo.
 * @param <T> The return type of the member to be invoked.
 */
public class MethodClass<T> {
	
	private final Method method;
	private Callable<T> call;
	
	/**
	 * 
	 * @param method The method to be dynamically invoked.
	 */
	public MethodClass(Method method){
		this.method = method;
	}
	
	/**
	 * Returns an instance of the generated class. This class implements the Callable interface. 
	 * T is the type of the method to be invoked.
	 * @return An instance of the generated class.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */	
	public Callable<T> getCallable() throws InstantiationException{
		if(call == null)
			call = getInstance();		
		return call;
	}

	/**
	 * Gets the class to extend the Callable interface. 
	 * T is the return type of the method to be invoked.
	 * @return The generated class that implements the Callable interface.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
	private Callable<T> getInstance() throws InstantiationException {
		DefaultBoostrap db;
		MethodType methodType;
		String classSignature=null;
		MethodType interfaceBridgeSignature=null;
		MethodType interfaceSignature=GeneratorUtil.CallableSignature;
		String interfaceMethodName = GeneratorUtil.CallableMehtodName;
		String interfaceClassName = GeneratorUtil.CallableClassName;
		boolean hasBridge = method.getReturnType() != Object.class;
		boolean interfaceOptimized = false;
		boolean useArray[] = {false,true};
		int aloadIndexParameter =1;
						
		if(hasBridge){
			String stringRtype =  GeneratorUtil.getReturnType( method.getReturnType());
			classSignature =GeneratorUtil.getCallableClassSignatureBridge(stringRtype);
			interfaceBridgeSignature = interfaceSignature;
			interfaceSignature = GeneratorUtil.getCallableMethodSignatureBridge(method.getReturnType());			
		}
		
		if(! Modifier.isStatic(method.getModifiers())){
			db = new DefaultBoostrap( method.getDeclaringClass(),method.getName(),InvokedynamicBootstrap.INSTANCE_BOOTSTRAP_METHOD);
			methodType = MethodType.methodType( method.getReturnType(),method.getDeclaringClass(),method.getParameterTypes());
		}
		else{
			db = new DefaultBoostrap(method.getDeclaringClass(),method.getName(),InvokedynamicBootstrap.STATIC_BOOTSTRAP_METHOD);
			methodType = MethodType.methodType( method.getReturnType(),method.getParameterTypes());
			useArray[0] = true;
			aloadIndexParameter=2;
		}
						
		return new ClassGenerator<Callable<T>>(db, classSignature,
				interfaceClassName,interfaceMethodName ,interfaceSignature,	 methodType,
				interfaceOptimized,hasBridge,interfaceBridgeSignature,
				GeneratorUtil.CallablelengthAloadBridge, 
				useArray,aloadIndexParameter).getInstance();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result +  method.hashCode();
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
		MethodClass<?> other = (MethodClass<?>) obj;
		 if (!method.equals(other.method))
			return false;
		return true;
	}
	
	
	
	

}
