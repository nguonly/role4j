package es.uniovi.reflection.invokedynamic;

import java.lang.invoke.MethodType;
import java.util.Arrays;

import es.uniovi.reflection.invokedynamic.codegen.ClassGenerator;
import es.uniovi.reflection.invokedynamic.codegen.GeneratorUtil;
import es.uniovi.reflection.invokedynamic.codegen.InvokedynamicBootstrap;
import es.uniovi.reflection.invokedynamic.interfaces.Constructor;
import es.uniovi.reflection.invokedynamic.util.DefaultBoostrap;

/**
 * Generates and stores the classes that perform a dynamic invocation, providing the signature of a constructor. 
 * The generated classes implement the Constructor interface.
 * 
 * @version 0.3
 * @author Computational Reflection Research Group. University of Oviedo
 * @param <T> Type of the class to be instantiated..
 */
class ConstructorClass<T> {
	
	private final Class<T> clazz;
	private final Class<?>[] parametersType;
	
	private Constructor<T> constructor;
	
	/**
	 * 
	 * @param clazz Type of the instance to be created.
	 * @param parametersType Type of the parameters of the constructor to be invoked.
	 */
	public ConstructorClass(Class<T> clazz,Class<?>...parametersType ){
		this.clazz = clazz;
		this.parametersType = parametersType;
	}
	
	 /**
	 * Returns an instance of the generated class. This class implements the Constructor interface. 
	 * T is the type of the instances the constructor initializes.
	 * @return An instance of the generated class.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */	
	public Constructor<T> getConstructor() throws InstantiationException{
		if(constructor == null)
			constructor =  getInstance();
		return constructor;
	}
	
	/**
	* Gets the class to extend the Constructor interface. T is the return type of the method to be invoked.
	 * @return The generated class that implements the Constructor interface.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
	private Constructor<T> getInstance() throws InstantiationException{		
		String classSignature=null;
		MethodType signatureBridge=null;
		MethodType interfaceSignature= GeneratorUtil.ConstructorSignature;
		String interfaceMethodName =  GeneratorUtil.ConstructorMehtodName;
		String interfaceClassName =GeneratorUtil.ConstructorClassName;
		boolean hasBridge = clazz != Object.class;
		boolean interfaceOptimized = false;
		boolean useArray[]= {true};
		int aloadIndexParameter =1;
		
		if(hasBridge){
			String stringRtype =  GeneratorUtil.getReturnType(clazz);
			classSignature =  GeneratorUtil.getConstructorClassSignatureBridge(stringRtype);
			signatureBridge =interfaceSignature;
			interfaceSignature =GeneratorUtil.getConstructorMethodSignatureBridge(clazz);			
		}
					
		DefaultBoostrap db = new DefaultBoostrap(clazz,"<init>",InvokedynamicBootstrap.CONSTRUCTOR_BOOTSTRAP);
		MethodType methodType = MethodType.methodType(clazz,parametersType);
			
		return new ClassGenerator<Constructor<T>>(db, classSignature,
				interfaceClassName,interfaceMethodName ,interfaceSignature,	 methodType,
				 interfaceOptimized,hasBridge,signatureBridge
				 , GeneratorUtil.ConstructorlengthAloadBridge,useArray
				 ,aloadIndexParameter).getInstance();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result +  clazz.hashCode();
		result = prime * result + Arrays.hashCode(parametersType);
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
		ConstructorClass<?> other = (ConstructorClass<?>) obj;
		if (clazz != other.clazz)
			return false;
		if (!Arrays.equals(parametersType, other.parametersType))
			return false;
		return true;
	}
	
	
	

}
