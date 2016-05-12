package es.uniovi.reflection.invokedynamic;

import java.lang.invoke.MethodType;

import es.uniovi.reflection.invokedynamic.codegen.ClassGenerator;
import es.uniovi.reflection.invokedynamic.codegen.GeneratorUtil;
import es.uniovi.reflection.invokedynamic.codegen.InvokedynamicBootstrap;
import es.uniovi.reflection.invokedynamic.interfaces.GetProperty;
import es.uniovi.reflection.invokedynamic.interfaces.SetProperty;
import es.uniovi.reflection.invokedynamic.util.DefaultBoostrap;
import es.uniovi.reflection.invokedynamic.util.TypeModifier;

/**
 * Generates and stores the classes that perform a dynamic invocation, providing the information of a specific field. 
 * The generated classes implement the GetProperty and SetProperty interfaces.
 * @version 0.3
  * @author Computational Reflection Research Group. University of Oviedo
 * @param <T> Return type of the property to be invoked.
 */
class PropertyClass<T> {
	
	private final String name;
	private final Class<?> clazz;
	private final Class<?> type;
	private final TypeModifier typeModifier;
	
	private GetProperty<T> get=null;
	private SetProperty    set=null;
	
	/**
	 * 
	 * @param clazz The class the method belongs to.
	 * @param name Name of the property
	 * @param type Type of the property
	 * @param typeModifier Indicates if the member is instance or class (static).
	 */
	public PropertyClass( Class<?> clazz, String name, Class<?> type, TypeModifier typeModifier){
		this.clazz = clazz;
		this.name = name;
		this.type = type;
		this.typeModifier = typeModifier;
		
	}
	
	/**
	 * Returns an object that implements GetProperty to perform dynamic invocations to the property. 
	 * T is the type of the property.
	 * @return Returns an object that implements GetProperty.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
	public final GetProperty<T> getProperty() throws InstantiationException{
		if(get == null){
			get = getInstanceGet();
		}		
		return get;
	}
	
	/**
	 *Returns an object that implements SetProperty to perform dynamic invocations to the property. 
	 * @return An object that implements SetProperty
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
	public final SetProperty setProperty() throws InstantiationException{
		if(set == null){
			set = getInstanceSet();
		}		
		return set;
	}

	/**
	* Gets the class that implements GetProperty.
	* @return An object that implements GetProperty
	* @throws InstantiationException If there is an error instantiating the class.
	*/
	private GetProperty<T> getInstanceGet() throws InstantiationException{		
		DefaultBoostrap bootstrap;
		MethodType methodType;
		String classSignature=null;
		MethodType interfaceBridgeSignature=null;
		MethodType interfaceSignature= GeneratorUtil.GetPropertySignature;
		String interfaceMethodName = GeneratorUtil.GetPropertyMehtodName;
		String interfaceClassName = GeneratorUtil.GetPropertyClassName;
		boolean hasBridge = type != Object.class;
		boolean interfaceOptimized = false;
		boolean useArray[] = {false};
		int aloadIndexParameter =1;
				
		if(hasBridge){
			String stringRtype =  GeneratorUtil.getReturnType(type);
			classSignature = GeneratorUtil.getGetPropertyClassSignatureBridge(stringRtype);
			interfaceBridgeSignature = interfaceSignature;
			interfaceSignature = GeneratorUtil.getGetPropertyMethodSignatureBridge(type);
		}
			
		if(typeModifier == TypeModifier.Instance){
			bootstrap = new DefaultBoostrap(clazz,name,InvokedynamicBootstrap.GET_INSTANCE_BOOTSTRAP_ATTRIBUTE);
			methodType = MethodType.methodType(type,clazz);
		}
		else{
			bootstrap = new DefaultBoostrap(clazz,name,InvokedynamicBootstrap.GET_STATIC_BOOTSTRAP_ATTRIBUTE);
			methodType = MethodType.methodType(type);
		}
	
		return new ClassGenerator<GetProperty<T>>(bootstrap, classSignature,
				interfaceClassName,interfaceMethodName ,interfaceSignature,	 methodType,
				 interfaceOptimized,hasBridge,interfaceBridgeSignature,
				 GeneratorUtil.GetPropertylengthAloadBridge, useArray,aloadIndexParameter).getInstance();
	}
	
	
	/**
	 * Gets the class that implements SetProperty
	* @return An object that implements SetProperty.
	* @throws InstantiationException If there is an error instantiating the class.
	*/
	private SetProperty getInstanceSet() throws InstantiationException{
		
		DefaultBoostrap bootstrap;
		MethodType methodType;
		String classSignature=null;
		MethodType interfaceBridgeSignature=null;
		String interfaceClassName = GeneratorUtil.SetPropertyClassName;
		MethodType interfaceSignature=GeneratorUtil.SetPropertySignature;
		String interfaceMethodName = GeneratorUtil.SetPropertyMethodName;	
		boolean interfaceOptimized = false;
		boolean hasBridge =false;
		int lenghtAloadBridge = 0;
		boolean useArray[] = {false,false};
		int aloadIndexParameter=1;
	
		
		if(typeModifier == TypeModifier.Instance){
			bootstrap = new DefaultBoostrap(clazz,name,InvokedynamicBootstrap.SET_INSTANCE_BOOTSTRAP_ATTRIBUTE);
			methodType = MethodType.methodType(void.class,clazz,type);
		}
		else{
			bootstrap = new DefaultBoostrap(clazz,name,InvokedynamicBootstrap.SET_STATIC_BOOTSTRAP_ATTRIBUTE);
			methodType = MethodType.methodType(void.class,type);
			aloadIndexParameter=2;
		}
	
		return new ClassGenerator<SetProperty>(bootstrap, classSignature,
				interfaceClassName, interfaceMethodName,interfaceSignature, methodType,
				 interfaceOptimized,hasBridge,interfaceBridgeSignature,
				 lenghtAloadBridge, useArray,aloadIndexParameter).getInstance();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result +  clazz.hashCode();
		result = prime * result +  name.hashCode();
		result = prime * result +  type.hashCode();
		result = prime * result	+ (typeModifier.eval() ? 1231 : 1237);;
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
		PropertyClass<?> other = (PropertyClass<?>) obj;
		if (type != other.type)
			return false;
		if (!name.equals(other.name))
			return false;
		 if (clazz != other.clazz)
			return false;		
		if (typeModifier.eval() != other.typeModifier.eval())
			return false;
		return true;
	}

	


	

}
