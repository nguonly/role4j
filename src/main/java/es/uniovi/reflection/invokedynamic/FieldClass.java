package es.uniovi.reflection.invokedynamic;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import es.uniovi.reflection.invokedynamic.codegen.ClassGenerator;
import es.uniovi.reflection.invokedynamic.codegen.GeneratorUtil;
import es.uniovi.reflection.invokedynamic.codegen.InvokedynamicBootstrap;
import es.uniovi.reflection.invokedynamic.interfaces.GetProperty;
import es.uniovi.reflection.invokedynamic.interfaces.SetProperty;
import es.uniovi.reflection.invokedynamic.util.DefaultBoostrap;

/**
   * Generates and stores the classes that perform a dynamic invocation, providing a Field instance. 
  * The generated classes implement the GetProperty and SetProperty interfaces.
 * @version 0.3
 * @author Computational Reflection Research Group. University of Oviedo
 * @param <T> Return type of the property to be invoked.
 */
public class FieldClass<T> {
	
	private final Field field;	
	private GetProperty<T> get = null;
	private SetProperty set = null;
	
	/**
	 * @param field Contains the field to be access dynamically.
	 */
	public FieldClass(Field field){
		this.field = field;
	}
	
	/**
	* Returns an instance of the generated class. This class implements the GetProperty interface.
	* T is the type of the field.
	 * @return An instance of the class that implements GetProperty.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
	public GetProperty<T> getGetProperty() throws InstantiationException{
		if(get == null);
			get = getInstanceGet();
		return get;
	}
	
	/**
	* Returns an instance of the generated class. This class implements the SetProperty interface.
	* @return An instance of the class that implements GetProperty.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
	public SetProperty getSetProperty() throws InstantiationException{
		if(set == null);
			set = getInstanceSet();
		return set;
	}

	/**
	 * Gets the class that implements GetProperty.
	* @return An instance of the class that implements GetProperty
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
		boolean hasBridge = field.getType() != Object.class;
		boolean interfaceOptimized = false;
		boolean useArray[] = {false,false};
		int aloadIndexParameter = 1;
				
		if(hasBridge){
			String stringRtype =  GeneratorUtil.getReturnType(field.getType());
			classSignature = GeneratorUtil.getGetPropertyClassSignatureBridge(stringRtype);
			interfaceBridgeSignature = interfaceSignature;
			interfaceSignature = GeneratorUtil.getGetPropertyMethodSignatureBridge(field.getType());
		}
			
		if(! Modifier.isStatic(field.getModifiers())){
			bootstrap = new DefaultBoostrap(field.getDeclaringClass(),field.getName(),InvokedynamicBootstrap.GET_INSTANCE_BOOTSTRAP_ATTRIBUTE);
			methodType = MethodType.methodType(field.getType(),field.getDeclaringClass());
		}
		else{
			bootstrap = new DefaultBoostrap(field.getDeclaringClass(),field.getName(),InvokedynamicBootstrap.GET_STATIC_BOOTSTRAP_ATTRIBUTE);
			methodType = MethodType.methodType(field.getType());
		}
	
		return new ClassGenerator<GetProperty<T>>(bootstrap, classSignature,
				interfaceClassName,interfaceMethodName ,interfaceSignature,	 methodType,
				 interfaceOptimized,hasBridge,interfaceBridgeSignature,
				 GeneratorUtil.GetPropertylengthAloadBridge,
				 useArray,aloadIndexParameter).getInstance();
	}
	
	
	/**
	* Gets the class that implements SetProperty.
	* @return An instance of the class that implements SetProperty
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
	
		
		if(! Modifier.isStatic(field.getModifiers())){
			bootstrap = new DefaultBoostrap(field.getDeclaringClass(),field.getName(),InvokedynamicBootstrap.SET_INSTANCE_BOOTSTRAP_ATTRIBUTE);
			methodType = MethodType.methodType(void.class,field.getDeclaringClass(),field.getType());
		}
		else{
			bootstrap = new DefaultBoostrap(field.getDeclaringClass(),field.getName(),InvokedynamicBootstrap.SET_STATIC_BOOTSTRAP_ATTRIBUTE);
			methodType = MethodType.methodType(void.class,field.getType());
			aloadIndexParameter=2;
		}
	
		return new ClassGenerator<SetProperty>(bootstrap, classSignature,
				interfaceClassName, interfaceMethodName,interfaceSignature, methodType,
				 interfaceOptimized,hasBridge,interfaceBridgeSignature
				 ,lenghtAloadBridge, useArray,aloadIndexParameter).getInstance();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + field.hashCode();
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
		FieldClass<?> other = (FieldClass<?>) obj;
		if (!field.equals(other.field))
			return false;
		return true;
	}
	
	

}
