package es.uniovi.reflection.invokedynamic;

import java.lang.invoke.MethodType;

import es.uniovi.reflection.invokedynamic.codegen.ClassGenerator;
import es.uniovi.reflection.invokedynamic.codegen.GeneratorUtil;
import es.uniovi.reflection.invokedynamic.interfaces.Callable;
import es.uniovi.reflection.invokedynamic.util.Bootstrap;
import es.uniovi.reflection.invokedynamic.util.MethodSignature;

/**
 * Generates and stores the classes that perform a dynamic invocation, providing the method signature. 
 * The generated classes implement the Callable interface.
 * 
 * @version 0.3
 * @author Computational Reflection Research Group. University of Oviedo
 * @param <T> The return type of the member to be invoked.
 */
public class MemberBSClass<T> {
	
	private final Bootstrap bootstrap;
	private final MethodSignature methodSignature;
	
	private  Callable<T> call=null;
	
	/**
	 * 
	 * @param bootstrap Bootstrap Contains the necessary data to invoke the bootstrap method.
	 * @param methodSignature  Contains the signature of the method to be dynamically invoked.
	 */
	public MemberBSClass(Bootstrap bootstrap,MethodSignature methodSignature ){
		this.bootstrap =bootstrap;
		this.methodSignature = methodSignature;		
	}
	
	/**
	 * Returns an instance of the generated class. This class implements the Callable interface.
	 *  T is the return type of the method to be invoked.
	 * @return An instance of the generated class.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
	public Callable<T> getCallable() throws InstantiationException {
		if(call == null)
			call =  getInstance();
		return call;
	}
	
	/**
	 * Gets the class to extend the Callable interface. T is the return type of the method to be invoked.
	 * @return The generated class that implements the Callable interface.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
	private Callable<T> getInstance() throws InstantiationException{
		MethodType methodType;
		String classSignature=null;
		MethodType interfaceBridgeSignature=null;
		MethodType interfaceSignature=GeneratorUtil.CallableSignature;
		String interfaceMethodName = GeneratorUtil.CallableMehtodName;
		String interfaceClassName = GeneratorUtil.CallableClassName;
		boolean hasBridge = methodSignature.getRtype() != Object.class;
		boolean interfaceOptimized = false;
		boolean useArray[] ={false,true};
		int aloadIndexParameter =1;
						
		if(hasBridge){
			String stringRtype =  GeneratorUtil.getReturnType( methodSignature.getRtype());
			classSignature =GeneratorUtil.getCallableClassSignatureBridge(stringRtype);
			interfaceBridgeSignature = interfaceSignature;
			interfaceSignature = GeneratorUtil.getCallableMethodSignatureBridge(methodSignature.getRtype());			
		}
		
		if(!methodSignature.isStatic())			
			methodType = MethodType.methodType(methodSignature.getRtype(),methodSignature.getClazz(),methodSignature.getParametersType());
		else{
			methodType = MethodType.methodType(methodSignature.getRtype(),methodSignature.getParametersType());
			useArray[0] = true;
			aloadIndexParameter=2;
		}
								
		return new ClassGenerator<Callable<T>>(bootstrap, classSignature,
				interfaceClassName,interfaceMethodName ,interfaceSignature,	 methodType,
				interfaceOptimized,hasBridge,interfaceBridgeSignature,
				GeneratorUtil.CallablelengthAloadBridge, useArray,aloadIndexParameter).getInstance();
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bootstrap.hashCode();
		result = prime * result	+ methodSignature.hashCode();
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
		MemberBSClass<?> other = (MemberBSClass<?>) obj;
		 if (!bootstrap.equals(other.bootstrap))
			return false;
		if (!methodSignature.equals(other.methodSignature))
			return false;
		return true;
	}
	
	

}
