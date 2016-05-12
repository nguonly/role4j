package es.uniovi.reflection.invokedynamic;

import java.lang.invoke.MethodType;
import java.util.Arrays;

import es.uniovi.reflection.invokedynamic.codegen.ClassGenerator;
import es.uniovi.reflection.invokedynamic.codegen.GeneratorUtil;
import es.uniovi.reflection.invokedynamic.codegen.InvokedynamicBootstrap;
import es.uniovi.reflection.invokedynamic.interfaces.Callable;
import es.uniovi.reflection.invokedynamic.util.DefaultBoostrap;
import es.uniovi.reflection.invokedynamic.util.TypeModifier;

/**
 * Generates and stores the classes that perform a dynamic invocation, providing the information of a specific method. 
 * The generated classes implement the Callable interface.
 * 
 * @version 0.3
  * @author Computational Reflection Research Group. University of Oviedo
 * @param <T> The return type of the member to be invoked.
 */
class MemberClass<T> {
	
	private final String name;
	private final Class<?> clazz;
	private final Class<?> returnType;
	private final Class<?>[] parametersType;
	private final TypeModifier typeModifier;
	
	private Callable<T> call= null;
	
	/**
	 * 
	 * @param clazz Class of the method to be invoked. 
	 * @param name Name of the method to be invoked.
	 * @param returnType Return type of the method to be invoked.
	 * @param parametersType Type of the parameters of the method to be invoked.
	 * @param typeModifier Indicates if the member is instance or class (static).
	 */
	public MemberClass( Class<?> clazz,  String name, Class<?> returnType,  Class<?>[] parametersType,  TypeModifier typeModifier){
		this.clazz = clazz;
		this.name = name;
		this.returnType = returnType;
		if(parametersType == null)
			this.parametersType = new Class<?>[]{};
		else
			this.parametersType = parametersType;
		this.typeModifier = typeModifier;
		}
	
	/**
	*  Returns an instance of the generated class. This class implements the Callable interface.
	*  T is the type of the method to be invoked.
	 * @return An instance of the generated class.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */	
	public final Callable<T> getCallable() throws InstantiationException{
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
	private Callable<T> getInstance() throws InstantiationException{
		DefaultBoostrap db;
		MethodType methodType;
		String classSignature=null;
		MethodType interfaceBridgeSignature=null;
		MethodType interfaceSignature=GeneratorUtil.CallableSignature;
		String interfaceMethodName = GeneratorUtil.CallableMehtodName;
		String interfaceClassName = GeneratorUtil.CallableClassName;
		boolean hasBridge = returnType != Object.class;
		boolean interfaceOptimized = false;
		boolean useArray[] = {false,true};
		int aloadIndexParameter =1;
						
		if(hasBridge){
			String stringRtype =  GeneratorUtil.getReturnType( returnType);
			classSignature =GeneratorUtil.getCallableClassSignatureBridge(stringRtype);
			interfaceBridgeSignature = interfaceSignature;
			interfaceSignature = GeneratorUtil.getCallableMethodSignatureBridge(returnType);			
		}
		
		if(typeModifier == TypeModifier.Instance){
			db = new DefaultBoostrap(clazz,name,InvokedynamicBootstrap.INSTANCE_BOOTSTRAP_METHOD);
			methodType = MethodType.methodType(returnType,clazz,parametersType);
		}
		else{
			db = new DefaultBoostrap(clazz,name,InvokedynamicBootstrap.STATIC_BOOTSTRAP_METHOD);
			methodType = MethodType.methodType(returnType,parametersType);
			useArray[0] = true;
			aloadIndexParameter=2;
		}
						
		return new ClassGenerator<Callable<T>>(db, classSignature,
				interfaceClassName,interfaceMethodName ,interfaceSignature,	 methodType,
				interfaceOptimized,hasBridge,interfaceBridgeSignature,
				GeneratorUtil.CallablelengthAloadBridge,useArray,aloadIndexParameter).getInstance();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + (typeModifier.eval() ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(parametersType);
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
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
		MemberClass<?> other = (MemberClass<?>) obj;
		if (typeModifier.eval() != other.typeModifier.eval())
			return false;
		if (!name.equals(other.name))
			return false;
		if (returnType != other.returnType)
			return false;				
		if (clazz != other.clazz)
			return false;
		if (!Arrays.equals(parametersType, other.parametersType))
			return false;
		return true;
	}



	
	

}
