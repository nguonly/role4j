package es.uniovi.reflection.invokedynamic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import es.uniovi.reflection.invokedynamic.codegen.GeneratorUtil;
import es.uniovi.reflection.invokedynamic.codegen.InvokedynamicClassLoader;
import es.uniovi.reflection.invokedynamic.codegen.InvokedynamicBootstrap;
import es.uniovi.reflection.invokedynamic.interfaces.Callable;
import es.uniovi.reflection.invokedynamic.interfaces.Constructor;
import es.uniovi.reflection.invokedynamic.interfaces.GetProperty;
import es.uniovi.reflection.invokedynamic.interfaces.SetProperty;
import es.uniovi.reflection.invokedynamic.util.Bootstrap;
import es.uniovi.reflection.invokedynamic.util.MethodSignature;
import es.uniovi.reflection.invokedynamic.util.TypeModifier;

/**
 * The ProxyFactory is a factory method that allows using the invokedynamic opcode included in JVM 1.7. 
 * Using this class, it is possible to perform dynamic invocations to methods, fields and constructors.
 * <br>It is not possible to access to members of non-public types.
 * @author Computational Reflection Research Group. University of Oviedo 
 * @version 0.3
 * 
 */
public class ProxyFactory {

	
	private static HashMap<String, Class<?>> classDescriptor = new HashMap<String, Class<?>>();
	
	private static final HashMap<MemberBSClass<?>, Callable<?>> invokedynamicClass = new HashMap<MemberBSClass<?>, Callable<?>>();
	private static final HashMap<MemberBIClass<?>, MemberBIClass<?>> memberMethod = new HashMap<MemberBIClass<?>, MemberBIClass<?>>();
	
	private static final HashMap<Field, GetProperty<?>>  fieldGetClass = new HashMap<Field, GetProperty<?>>();
	private static final HashMap<Field, SetProperty>  fieldSetClass = new HashMap<Field, SetProperty>();
	private static final HashMap<Method,Callable<?>> methodsClass = new HashMap<Method,Callable<?>>();
		
	private static final HashMap<MemberClass<?>, Callable<?>> memberClass = new HashMap<MemberClass<?>,Callable<?> >();
	private static final HashMap<ConstructorClass<?>, Constructor<?>> constructorClass = new HashMap<ConstructorClass<?>,Constructor<?> >();
	private static final HashMap<PropertyClass<?>, GetProperty<?>> propertyGetClass = new HashMap<PropertyClass<?>, GetProperty<?>>();
	private static final HashMap<PropertyClass<?>, SetProperty> propertySetClass = new HashMap<PropertyClass<?>, SetProperty>();
			
	/**
	 * Performs dynamic invocations to methods, fields and constructors received as a parameter.
	 * If the member to be invoked is static, object is ignored (null should be passed).
	 * @param bootstrap Bootstrap responsible for generating a bootstrap handle.
	 * @param methodSignature The signature of the method to be invoked.
	 * @param object Implicit object used to perform the dynamic invocation.
	 * @param args Arguments of the method invocation.
	 * @return The value of the dynamic invocation to a method, field or constructor, passing the indicated parameters.
	 * @throws ClassCastException If the types of the actual arguments do not match the type of the formal parameters.
     * @throws NullPointerException If object is null and the method to be invoked is an instance method.
     * @throws NoSuchMethodException There is no method with the specified signature.
     * @throws InstantiationException The class could not be instantiated.
     * @throws IllegalAccessException If the class of the member to be invoked is not public.
     * @see es.uniovi.reflection.invokedynamic.util.Bootstrap
     * @see es.uniovi.reflection.invokedynamic.util.MethodSignature
	 */
	public final static<T> T  invokeDynamic(Bootstrap bootstrap, MethodSignature methodSignature, Object object, Object...args) throws InstantiationException, IllegalAccessException {
		if(bootstrap.getCache().eval()){		
		   Callable<T> call = getInvokedynamicCallable(bootstrap, methodSignature);								 
		   return call.invoke(object, args);
		}		
		return new MemberBSClass<T>(bootstrap,methodSignature).getCallable().invoke(object, args);
	}
	
	/**
	 * Generates a Callable instance to perform dynamic invocations to methods, fields and constructors received as a parameter.
	 * @param bootstrap Bootstrap responsible for generating a bootstrap handle.
	 * @param methodSignature The signature of the method to be invoked.
	 * @return A Callable instance to perform dynamic invocations to methods, fields and constructors.
	 * @throws NoSuchMethodException There is no method with the specified signature.
     * @throws InstantiationException The class could not be instantiated.
     * @see es.uniovi.reflection.invokedynamic.util.Bootstrap
     * @see es.uniovi.reflection.invokedynamic.util.MethodSignature
	 */
	public final static<T> Callable<T>  generateInvokeDynamicCallable(Bootstrap bootstrap, MethodSignature methodSignature) throws InstantiationException, IllegalAccessException {
		if(bootstrap.getCache().eval())
			return getInvokedynamicCallable(bootstrap, methodSignature);
	
		return new MemberBSClass<T>(bootstrap,methodSignature).getCallable();		
	}
	
	/**
	 * Generates an object to provide static method invocation using invokedynamic, passing the bootstrap method as parameter.
	 * <br> The generated object implements the interface passed as a parameter. 
	 * That interface must contain one single method with the same signature of the one to be dynamically invoked.
	 * The method could be named with any identifier.
	 * <br>T must be the interface.
	 * @param bootstrap Bootstrap responsible for generating a bootstrap handle.
	 * @param interfaceCallable The interface with one single method.
	 * @return An object to provide static method invocation using invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.	
	 * @see es.uniovi.reflection.invokedynamic.util.Bootstrap
	 */	
	public final static<T> T  generateStaticInvokeDynamic(Bootstrap bootstrap, Class<T> interfaceCallable) throws InstantiationException, IllegalAccessException {
		MemberBIClass<T> mb = new MemberBIClass<T>(bootstrap,interfaceCallable,TypeModifier.Static,null);
		if(bootstrap.getCache().eval())				
		    return getInstance(mb);
		return mb.getInstance();
	}
	
	/**
	 * Generates an object to provide instance method invocation using invokedynamic, passing the bootstrap method as parameter.
	 * <br> The generated object implements the interface passed as a parameter. 
	 * That interface must contain one single method with the same signature of the one to be dynamically invoked.
	 * The method could be named with any identifier.
	 * <br>T must be the interface.
	 * @param bootstrap Bootstrap responsible for generating a bootstrap handle.
	 * @param interfaceCallable The interface with one single method.
	 * @return An object to provide instance method invocation using invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @see es.uniovi.reflection.invokedynamic.util.Bootstrap	
	 */	
	public final static<T> T  generateInvokeDynamic(Bootstrap bootstrap, Class<T> interfaceCallable) throws InstantiationException, IllegalAccessException {
		MemberBIClass<T> mb = new MemberBIClass<T>(bootstrap,interfaceCallable,TypeModifier.Instance,null);		
		if(bootstrap.getCache().eval())
			return getInstance(mb);
		
		return mb.getInstance();				
	}

	/**
	 * Generates a Constructor instance of the class and parameters passed as arguments. T is the type of the constructor class.
	 * <p>
	 * The following code dynamically invokes a constructor of the Counter class with no parameter:<br>
	 * <code>Constructor&lt;Counter&gt; constructor = ProxyFactory.&lt;Contructor&lt;Counter&gt;&gt;generateConstructor(Counter.class);</code><br>
	 * <code>Counter counter = constructor.newInstance();</code>
	 * 
	 * @param clazz The class we want to create a new instance of.
	 * @param parameterstype Type of the parameters of the constructor to be invoked.
	 * @return A constructor with the signature passed as a parameter.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException There is no method with the specified signature.
	 */
	public final static<T> Constructor<T> generateConstructor(Class<T> clazz, Class<?>...parameterstype) throws InstantiationException{
		ConstructorClass<T> consclass = new ConstructorClass<T>(clazz, parameterstype);		 				
		@SuppressWarnings("unchecked")
		Constructor<T> constructor =  (Constructor<T>) constructorClass.get(consclass);
		if (constructor != null) {
			return  constructor;
		}
		constructor = consclass.getConstructor();	
		constructorClass.put(consclass,constructor);
		return constructor;			
	}
	
	
	/**
	 * Generates a Callable instance to perform dynamic invocations to a static method, indicating its signature. T is the return type of the static method.
	 *  <p>
	 * The following code shows how to dynamically invoke the static method  <code>concat</code> of the <code>Counter</code> class. The method returns 
	 * a <code>String</code> and receives two  <code>String</code> as parameters:<br>
	 * <code>Callable&lt;String&gt; callable = ProxyFactory.&lt;Callable&lt;String&gt;&gt;generateStaticCallable(counter,"concat",String.class, String.class,String.class);</code><br>
	 * <code>String cad = callable.invoke(null,"Hello", "World");</code>
	 * @param object Object of the class of the method to be invoked.
	 * @param methodName Name of the method to be invoked. It must be static.
	 * @param returnType The return type of the method to be invoked.
	 * @param parametersType Type of the parameters of the method to be invoked.
	 * @return A Callable to perform dynamic invocations to a static method, indicating its signature.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException There is no method with the specified signature.
	 */
	public final static<T> Callable<T> generateStaticCallable(Object object,String methodName, Class<T> returnType, Class<?>...parametersType) throws InstantiationException {
		Class<?> clazz = object.getClass();		
		return getCallable(clazz, methodName, returnType,parametersType, TypeModifier.Static);
	}
	
	
	/**
	 * Generates a Callable instance to perform dynamic invocations to an instance method, indicating its signature. 
	 * T is the return type of the instance method.
	 * <p>
	 * The following code shows how to dynamically invoke the instance method  <code>increase</code> of the  <code>Counter</code> class. The method return a <code>long</code> and receives
	 * no parameters:<br>
	 * <code>Callable&lt;Long&gt; callable = ProxyFactory.&lt;Callable&lt;Long&gt;&gt;generateCallable(counter,"increase", long.class);</code><br>
	 * <code>long value = callable.invoke(counter);</code>
	 * @param object Object of the class of the method to be invoked.
	 * @param methodName Name of the instance method to be invoked.
	 * @param returnType The return type of the method to be invoked.
	 * @param parametersType Type of the parameters of the method to be invoked.
	 * @return A Callable to perform dynamic invocations to an instance method, indicating its signature.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException There is no method with the specified signature.
	 */	
	public final static<T> Callable<T> generateCallable(Object object,String methodName, Class<T> returnType, Class<?>...parametersType) throws InstantiationException {
		Class<?> clazz = object.getClass();		
		return getCallable(clazz, methodName, returnType,parametersType, TypeModifier.Instance);
	}
	
	
	/**
	 * Generates a Callable instance to perform dynamic invocations to a static method, indicating its signature. T is the return type of the static method.
     *  <p>
	 * The following code shows how to dynamically invoke the static method  <code>concat</code> of the <code>Counter</code> class. The method returns a <code>String</code> and
	 * receives two <code>String</code> as parameters:<br>
	 * <code>Callable&lt;String&gt; callable = ProxyFactory.&lt;Callable&lt;String&gt;&gt;generateStaticCallable(Counter.class,"concat",String.class, String.class,String.class);</code><br>
	 * <code>String cad = callable.invoke(null,"Hello", "World");</code>
	 * @param clazz Class of the method to be invoked.
	 * @param methodName Name of the method to be invoked. It must be static.
	 * @param returnType The return type of the method to be invoked.
	 * @param parametersType Type of the parameters of the method to be invoked.
	 * @return A Callable to perform dynamic invocations to a static method, indicating its signature.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException There is no method with the specified signature.
	 */	
	public final static<T> Callable<T> generateStaticCallable( Class<?> clazz, String methodName,  Class<T> returnType,  Class<?>... parametersType) throws InstantiationException {
		return getCallable(clazz, methodName, returnType,parametersType, TypeModifier.Static);
	}
	
	/**
	 * Generates a Callable instance to perform dynamic invocations to an instance method, indicating its signature. T is the return type of the static method.
	 * <p>
	 * The following code shows how to dynamically invoke the instance method  <code>increase</code> of the <code>Counter</code> class. The method returns a <code>long</code>
	 * and receives no parameter:<br>
	 * <code>Callable&lt;Long&gt; callable = ProxyFactory.&lt;Callable&lt;Long&gt;&gt;generateCallable(Counter.class,"increase", long.class);</code><br>
	 * <code>long value = callable.invoke(counter);</code>
	 * @param clazz Class of the method to be invoked.
	 * @param methodName Name of the instance method to be invoked.
	 * @param returnType The return type of the method to be invoked.
	 * @param parametersType Type of the parameters of the method to be invoked.
	 * @return A Callable to perform dynamic invocations to an instance method, indicating its signature.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException There is no method with the specified signature.
	 */	
	public final static<T> Callable<T> generateCallable(Class<?> clazz,String methodName, Class<T> returnType, Class<?>...parametersType) throws InstantiationException {
		return getCallable(clazz, methodName, returnType,parametersType, TypeModifier.Instance);
	}
	
	/**
	 * Generates a Callable instance to perform dynamic invocations to a static method, indicating its signature.
	 * T is the return type of the static method.
	 *  <p>
	 * The following code shows how to dynamically invoke the static method <code>concat</code> of the <code>Counter</code> class. The method returns a <code>String</code>
	 * and receives two <code>String</code> as parameters:<br>
	 * <code>Callable&lt;String&gt; callable = ProxyFactory.&lt;Callable&lt;String&gt;&gt;generateStaticCallable("paquete.Counter","concat",String.class, String.class,String.class);</code><br>
	 * <code>String cad = callable.invoke(null,"Hello", "World");</code>
	 * @param clazzDescriptor Class descriptor of the method to be invoked.
	 * @param methodName Name of the method to be invoked. It must be static.
	 * @param returnType The return type of the method to be invoked.
	 * @param parametersType Type of the parameters of the method to be invoked.
	 * @return A Callable to perform dynamic invocations to a static method, indicating its signature.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException There is no method with the specified signature.
	 * @throws ClassNotFoundException If the class specified by the descriptor cannot be found.
	 */	
	public final static<T> Callable<T> generateStaticCallable(String clazzDescriptor,String methodName, Class<T> returnType, Class<?>... parametersType)throws InstantiationException, ClassNotFoundException {
		final Class<?> clazz = getClassToDescriptor(clazzDescriptor);			
		return getCallable(clazz, methodName, returnType, parametersType, TypeModifier.Static);
	}
	
	/**
	 * Generates a Callable instance to perform dynamic invocations to an instance method, indicating its signature.
	 *  T is the return type of the instance method.
	 * <p>
	 * The following code shows how to dynamically invoke the instance method <code>increase</code> of the <code>Counter</code> class. The method returns a <code>long</code> and receives no parameter<br>
	 * <code>Callable&lt;Long&gt; callable = ProxyFactory.&lt;Callable&lt;Long&gt;&gt;generateCallable("paquete.Counter","increase", long.class);</code><br>
	 * <code>long value = callable.invoke(counter);</code>
	 * @param clazzDescriptor Class descriptor of the method to be invoked.
	 * @param methodName Name of the method to be invoked. It must be an instance method.
	 * @param returnType The return type of the method to be invoked.
	 * @param parametersType Type of the parameters of the method to be invoked.
	 * @return A Callable to perform dynamic invocations to an instance method, indicating its signature.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException There is no method with the specified signature.
	 * @throws ClassNotFoundException If the class specified by the descriptor cannot be found.
	 */	
	public static<T> Callable<T> generateCallable(String clazzDescriptor,String methodName, Class<T> returnType, Class<?>... parametersType) throws InstantiationException, ClassNotFoundException {
		final Class<?> clazz = getClassToDescriptor(clazzDescriptor);						
		return getCallable(clazz, methodName, returnType, parametersType, TypeModifier.Instance );
	}
	
		
	/**
	 * Generates a Callable instance to perform dynamic invocations to an instance method, indicating a Method instance. T is the return type of the instance method.
	 * <p>
	 * The following code shows how to dynamically invoke the method represented by the <code>method</code> Method:<br>
	 * <code>Callable&lt;T&gt; callable = ProxyFactory.generateCallable(method);</code><br>
	 * <code>Object value = callable.invoke(counter);</code>
	 * @param method An instance of Method representing the method to be invoked.
	 * @return A Callable instance to perform dynamic invocations to an instance method.
	 * @throws InstantiationException If the class could not be instantiated.
	 */
	public final static<T> Callable<T> generateCallable(final Method method) throws InstantiationException {
					
		@SuppressWarnings("unchecked")
		Callable<T>  call = (Callable<T>) methodsClass.get(method);

		if (call == null) {
			call = new MethodClass<T>(method).getCallable();
			methodsClass.put(method,call);
		}				
		return call;
	}	
	
	/**
	* Generates a GetProperty instance to obtain the value of a static field by means of invokedynamic. T is the type of the field.
	 * <p>
	 * The following code shows how to obtain the <code>STATIC_COUNTER</code> static field of the <code>Counter</code> class,
	 *  whose type is <code>long</code>:<br>
	 * <code>GetProperty&lt;Long&gt; getProperty = ProxyFactory.&lt;GetProperty&lt;Long&gt;&gt;generateStaticGetProperty(counter,"STATIC_COUNTER", long.class);</code><br>
	 * <code>long value = getProperty.get(null);</code>
	 * @param object The object of the class the property belongs to. 
	 * @param property Name of the static property to be invoked.
	 * @param type Type of the static property to be invoked.
	 * @return A GetProperty instance to obtain the value of a static field by means of invokedynamic
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException If no method with the appropriate signature belongs to the class.
	 */
	public final static<T> GetProperty<T> generateStaticGetProperty(Object object,String property, Class<T> type) throws InstantiationException {
		final Class<?> clazz = object.getClass();		
		return getGetProperty(clazz, property, type, TypeModifier.Static );
	}
	
	/**
	* Generates a GetProperty instance to obtain the value of an instance field by means of invokedynamic. T is the type of the field.
	 * <p>
	 * The following code shows how to obtain the <code>instance_counter</code> field of the Counter class, whose type is <code>long</code>:<br>
	 * <code>GetProperty&lt;Long&gt; getProperty = ProxyFactory.&lt;GetProperty&lt;Long&gt;&gt;generateGetProperty(counter,"instance_counter", long.class);</code><br>
	 * <code>long value = getProperty.get(counter);</code>
	 * @param object The object of the class the property belongs to.
	 * @param property Name of the instance property to be invoked.
	 * @param type Type of the static property to be invoked.
	 * @return A GetProperty instance to obtain the value of an instance field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException There is no method with the specified signature.
	 */
	public final static<T> GetProperty<T> generateGetProperty(Object object,String property, Class<T> type) throws InstantiationException {
		final Class<?> clazz = object.getClass();				
		return getGetProperty(clazz, property, type,TypeModifier.Instance );
	}
	
	/**
	 * Generates a GetProperty instance to obtain the value of a static field by means of invokedynamic. T is the type of the field.
	 * <p>
	 * The following code shows how to obtain the <code>STATIC_COUNTER</code> static field of the Counter class, whose type is <code>Long</code>:
	 * <code>GetProperty&lt;Long&gt; getProperty = ProxyFactory.&lt;GetProperty&lt;Long&gt;&gt;generateStaticGetProperty("paquete.Counter","STATIC_COUNTER", "java.lang.Long");</code><br>
	 * <code>long value = getProperty.get(null);</code>
	 * @param clazzDescriptor Descriptor of the class the property belongs to.
	 * @param property Name of the static property to be invoked.
	 * @param typeDescriptor Descriptor of the type of the static property to be invoked.
	 * @return A GetProperty instance to obtain the value of a static field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException There is no method with the specified signature.
	 * @throws ClassNotFoundException If the class specified by the descriptor cannot be found.
	 */
	public final static<T> GetProperty<T> generateStaticGetProperty(String clazzDescriptor,String property, String typeDescriptor) throws InstantiationException, ClassNotFoundException {
		final Class<?> clazz = getClassToDescriptor(clazzDescriptor);
		final Class<?> type = getClassToDescriptor(typeDescriptor);	
		return getGetProperty(clazz, property, type, TypeModifier.Static );
	}
	
	/**
	 * Generates a GetProperty instance to obtain the value of an instance field by means of invokedynamic. T is the type of the field.
	 *  <p>
	 * The following code shows how to obtain the <code>instance_counter</code> field of the Counter class, whose type is <code>Long</code>:<br>
	 * <code>GetProperty&lt;Long&gt; getProperty = ProxyFactory.&lt;GetProperty&lt;Long&gt;&gt;generateGetProperty("paquete.Counter","instance_counter", "java.lang.Long");</code><br>
	 * <code>long value = getProperty.get(counter);</code>
	 * @param clazzDescriptor Descriptor of the class the property belongs to.
	 * @param property Name of the instance property to be invoked.
	 * @param typeDescriptor Descriptor or the type of the static property to be invoked.
	 * @return A GetProperty instance to obtain the value of an instance field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	 * @throws NoSuchMethodException There is no method with the specified signature.
	 * @throws ClassNotFoundException If the class specified by the descriptor cannot be found.
	 */
	public static<T> GetProperty<T> generateGetProperty(String clazzDescriptor,	String property, String typeDescriptor) throws InstantiationException, ClassNotFoundException {
		final Class<?> clazz = getClassToDescriptor(clazzDescriptor);
		final Class<?> type = getClassToDescriptor(typeDescriptor);				
		return getGetProperty(clazz, property, type, TypeModifier.Instance);
	}
		
	/**
	 * Generates a GetProperty instance to obtain the value of a Field by means of invokedynamic. T is the type of the field.<br>
	 * The following code shows how to obtain the value of a filed using the appropriate Field:<br>
	 * <code>GetProperty&lt;?&gt; getProperty = ProxyFactory.generateGetProperty(field);</code><br>
	 * <code>Object value = getProperty.get(counter);</code>
	 * @param field Objeto de la clase Field que representa la propiedad a invocar.
	 * @return A GetProperty instance to obtain the value of a Field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	
	 */
	@SuppressWarnings("unchecked")
	public final static<T>  GetProperty<T> generateGetProperty( final Field field) throws InstantiationException {
	
		GetProperty<T> get =  (GetProperty<T>) fieldGetClass.get(field);

		if (get  != null)
			return   get;
		
		get = new FieldClass<T>(field).getGetProperty();		
		fieldGetClass.put(field, get);
		return get;
	}
	

	/**
	 * Generates a SetProperty instance to modify the value of a static field by means of invokedynamic.
	 * <p>
	 * The following code shows how to modify the value of the <code>STATIC_COUNTER</code> static field of the <code>Counter</code> class, whose type is <code>long</code>:<br>
	 * <code>SetProperty setProperty = ProxyFactory.&lt;SetProperty&gt;generateStaticSetProperty(counter,"STATIC_COUNTER", long.class);</code><br>
	 * <code>setProperty.set(null,newValue);</code>
	 * @param object Object of the class the property belongs to.
	 * @param property Name of the static property to be invoked.
	 * @param type Type of the static property to be invoked.
	 * @return A SetProperty instance to modify the value of a static field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	
	 */	
	public final static SetProperty generateStaticSetProperty(Object object,String property, Class<?> type) throws InstantiationException {
		final Class<?> clazz = object.getClass();	
		return getSetProperty(clazz,property, type, TypeModifier.Static);
	}
	
	/**
	 * Generates a SetProperty instance to modify the value of an instance field by means of invokedynamic.
	 * <p>
	 * The following code shows how to modify the value of the <code>instance_counter</code> field of the <code>Counter</code> class, whose type is <code>long</code>:<br>
	 * <code>SetProperty setProperty = ProxyFactory.&lt;SetProperty&gt;generateSetProperty(counter,"instance_counter", long.class);</code><br>
	 * <code>setProperty.set(counter,newValue);</code>
	 * @param object Object of the class the property belongs to.
	 * @param property Name of the instance property to be invoked.
	 * @param type Type of the static property to be invoked.
	 * @return A SetProperty instance to modify the value of an instance field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	
	 */	
	public final static SetProperty generateSetProperty(Object object,String property, Class<?> type) throws InstantiationException {
		final Class<?> clazz = object.getClass();		
		return getSetProperty(clazz,property, type, TypeModifier.Instance);
	}
	
	/**
	 * Generates a SetProperty instance to modify the value of a static field by means of invokedynamic.
	 *  <p>
	 * The following code shows how to modify the value of the <code>STATIC_COUNTER</code> static field of the <code>Counter</code> class, whose type is <code>Long</code>:<br>
	 * <code>SetProperty setProperty = ProxyFactory.&lt;SetProperty&gt;generateStaticSetProperty("paquete.Counter","STATIC_COUNTER", "java.lang.Long");</code><br>
	 * <code>setProperty.set(null,newValue);</code>
	 * @param clazzDescriptor Descriptor of the class the property belongs to.
	 * @param property Name of the static property to be invoked.
	 * @param typeDescriptor Type of the static property to be invoked.
	 * @return A SetProperty instance to modify the value of a static field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	* @throws ClassNotFoundException If the class specified by the descriptor cannot be found.
	 */
	public final static SetProperty generateStaticSetProperty(String clazzDescriptor,String property, String typeDescriptor) throws InstantiationException, ClassNotFoundException {
		final Class<?> clazz = getClassToDescriptor(clazzDescriptor);
		final Class<?> type = getClassToDescriptor(typeDescriptor);				
		return getSetProperty(clazz, property, type, TypeModifier.Static );
	}
	
	/**
	 * Generates a SetProperty instance to modify the value of an instance field by means of invokedynamic.
	 * <p>
	 * The following code shows how to modify the value of the <code>instance_counter</code> field of the <code>Counter</code> class, whose type is <code>Long</code>:<br>
	 * <code>SetProperty setProperty = ProxyFactory.&lt;SetProperty&gt;generateSetProperty("paquete.Counter","instance_counter", "java.lang.Long");</code><br>
	 * <code>setProperty.set(counter,newValue);</code>
	 * @param clazzDescriptor Descriptor of the class the property belongs to.
	 * @param property Name of the instance property to be invoked.
	 * @param typeDescriptor Type of the static property to be invoked.
	 * @return A SetProperty instance to modify the value of an instance field by means of invokedynamic.
	* @throws InstantiationException If the class could not be instantiated.
	 * @throws ClassNotFoundException If the class specified by the descriptor cannot be found.
	 */
	public static SetProperty generateSetProperty(String clazzDescriptor,String property, String typeDescriptor) throws InstantiationException, ClassNotFoundException {
		final Class<?> clazz = getClassToDescriptor(clazzDescriptor);
		final Class<?> type = getClassToDescriptor(typeDescriptor);				
		return getSetProperty(clazz, property, type, TypeModifier.Instance );
	}

	/**
	 * Generates a SetProperty instance to modify the value of a Field by means of invokedynamic. T is the type of the field.<br>
	 * The following code shows how to modify the value of a filed using the appropriate Field<br>
	 * <code>SetProperty setProperty = ProxyFactory.&lt;SetProperty&gt;generateSetProperty(field);</code><br>
	 * <code>setProperty.set(counter, newValue);</code>
	 * @param field A Field instance representing que the field whose value is going to be modified.
	 * @return A SetProperty instance to modify the value of a Field by means of invokedynamic. 
	 * @throws InstantiationException If the class could not be instantiated.	
	 */
	@SuppressWarnings("rawtypes")
	public final static SetProperty generateSetProperty(final Field field) throws InstantiationException {
			SetProperty set =  fieldSetClass.get(field);

			if (set  != null)
				return   set;
						
			set = new FieldClass(field).getSetProperty();	
			fieldSetClass.put(field, set);
			return set;
	}	
	
	
	/**
	 * Generates an object to provide instance method invocation using invokedynamic, passing the name of the method and class as parameters.
	 * <br>The generated object implements the interface passed as a parameter. That interface must contain one single method.
	 * The method of the interface must have the same return type of method to invoke, the first  parameter must be the method class, then put the other parameters of the method.
	 *  The method could be named with any identifier.
	 * <br>T must be the interface.
	 * <p>
	 *  The next example shows how to declare an interface and invoke its method using invokedynamic.<br>
	 *  We want to invoke the <code>increase</code> instance method in the <code>Counter</code> class. This method returns a <code>long</code> and receives no parameter.
	 *   We create the following interface:<br>
	 * <code> public interface CallablelongCounter{</code><br>
	 * <code> 	public long invoke(Counter counter);</code><br>
	 * <code> }</code>
	 * <p>
	 * Then, we generate the Callable instance:<br>
	 * <code> CallablelongCounter callable = ProxyFactory.&lt;CallablelongCounter&gt; generateCallable(Counter.class, "increase", CallablelongCounter.class);</code><br>
	 * And the method is invoked with invokedynamic:<br>
	 * <code> long value = callable.invoke(counter);</code>
	 * @param clazz Class of the method to be invoked.
	 * @param methodName Name of the method to be invoked. It must be an instance method.
	 * @param interfaceCallable The interface with one single method.
	 * @return An object to provide instance method invocation using invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.	
	 */
	public final static <T> T generateCallable(Class<?> clazz,String methodName, Class<T> interfaceCallable) throws InstantiationException {
    	MemberBIClass<T> mb = new MemberBIClass<T>(interfaceCallable,TypeModifier.Instance,clazz,methodName,InvokedynamicBootstrap.INSTANCE_BOOTSTRAP_METHOD);
		return getInstance(mb);
	}
	
	/**
	 * Generates an object to provide static method invocation using invokedynamic, passing the name of the method and class as parameters.
	 * <br> The generated object implements the interface passed as a parameter. 
	 * That interface must contain one single method with the same signature of the one to be dynamically invoked.
	 * The method could be named with any identifier.
	 * <br>T must be the interface.
	 *  <p>
	 *  The next example shows how to declare an interface and invoke its method using invokedynamic.<br>
	 *  We want to invoke the <code>concat</code> static method in the <code>Counter</code> class. This method returns a <code>String</code> and receives two <code>String</code> parameter.
	 *  We create the following interface:
	 *  <br>
	 * <code> public interface CallableString2String{</code><br>
	 * <code> 	public String invoke(String cad1, String cad2);</code><br>
	 * <code> }</code>
	 * <p>
	 * Then, the Callable is generated:<br>
	 * <code> CallableString2String callable = ProxyFactory.&lt;CallableString2String&gt; generateStaticCallable(Counter.class, "concat", CallableString2String.class);</code><br>
	 * And the method can be dynamically invoked:<br>
	 * <code> String cad = callable.invoke("Hello", "World");</code>
	 * @param clazz Class of the method to be invoked.
	 * @param methodName Name of the method to be invoked. It must be an static method.
	 * @param interfaceCallable The interface with one single method.
	 * @return An object to provide static method invocation using invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.	
	 */	
	public final static <T> T generateStaticCallable(Class<?> clazz,String methodName, Class<T> interfaceCallable) throws InstantiationException {
		MemberBIClass<T> mb = new MemberBIClass<T>(interfaceCallable,TypeModifier.Static,clazz,methodName,InvokedynamicBootstrap.STATIC_BOOTSTRAP_METHOD);
		return getInstance(mb);
	}


	/**
	 *  Generates an object to obtain the value of an instance field by means of invokedynamic.
	 * <br>The generated object implements the interface passed as a parameter.
	 * That interface must contain one single method. The method of the interface must have the same return type of field
	 *  and the property class as the only parameter.
	 *  The method could be named with any identifier.
	 * <br>T must be the interface.
	 * <p>
	 *  The next example shows how to declare an interface and obtain the value of an instance field by means of invokedynamic.<br>
	 *  We want to the value of <code>instance_concat</code> a instance field in the <code>Counter</code> class with <code>long</code> type.
	 *  We create the following interface:<br>	
	 * <code> public interface CallablelongCounter{</code><br>
	 * <code> 	public long invoke(Counter counter);</code><br>
	 * <code> }</code>
	 * <p>
	 * Then, the Callable is generated:<br>
	 * <code> CallablelongCounter callable = ProxyFactory.&lt;CallablelongCounter&gt; generateGetProperty(Counter.class, "instance_concat", CallablelongCounter.class);</code><br>
	  * And the field can be dynamically invoked:<br>
	 * <code> long value = callable.invoke(counter);</code>
	 * @param clazz Class of the field to be invoked.
	 * @param propertyName Name of the instance field to be invoked.
	 * @param interfaceCallable The interface with one single method.
	 * @return An object to obtain the value field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.	
	 */
	public final static <T> T generateGetProperty(Class<?> clazz,String propertyName, Class<T> interfaceCallable) throws InstantiationException {
		MemberBIClass<T> mb = new MemberBIClass<T>(interfaceCallable,TypeModifier.Instance,clazz,propertyName,InvokedynamicBootstrap.GET_INSTANCE_BOOTSTRAP_ATTRIBUTE);
		return getInstance(mb);
	}
	
	/**
	 *  Generates an object to obtain the value of an static field by means of invokedynamic.
	 * <br>The generated object implements the interface passed as a parameter.
	 * That interface must contain one single method. The method of the interface must have the same return type of field and not have any parameters.
	 * The method could be named with any identifier.
	 * <br>T must be the interface.
	 *  <p>
	 *  The next example shows how to declare an interface and obtain the value of an static field by means of invokedynamic.<br>
	 *  We want to the value of <code>STATIC_COUNTER</code> a static field in the <code>Counter</code> class with <code>long</code> type.
	 *  We create the following interface:<br>	
	  * <code> public interface Callablelong{</code><br>
	 * <code> 	public long invoke();</code><br>
	 * <code> }</code>
	 * <p>
	 * Then, the Callable is generated:<br>
	 * <code> Callablelong callable = ProxyFactory.&lt;Callablelong&gt; generateStaticGetProperty(Counter.class, "STATIC_COUNTER", Callablelong.class);</code><br>
	 * And the field can be dynamically invoked:<br>
	 * <code> long value = callable.invoke();</code>
	 * @param clazz Class of the field to be invoked.
	 * @param propertyName Name of the static field to be invoked.
	 * @param interfaceCallable The interface with one single method.
	 * @return An object to obtain the value field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	 */
	public final static <T> T generateStaticGetProperty(Class<?> clazz,String propertyName, Class<T> interfaceCallable) throws InstantiationException {
		MemberBIClass<T> mb = new MemberBIClass<T>(interfaceCallable,TypeModifier.Static,clazz,propertyName,InvokedynamicBootstrap.GET_STATIC_BOOTSTRAP_ATTRIBUTE);
		return getInstance(mb);
	}
	
	/**
	 * Generates an object to modify the value of an instance field by means of invokedynamic.
	 * <br>The generated object implements the interface passed as a parameter.
	 * That interface must contain one single method. The method of the interface must have a void return type,
	 *  the property class as the first parameter and the second parameter is the field type. 
	 * The method could be named with any identifier.
	 * <br>T must be the interface.
	 *  <p>
	 *  The next example shows how to declare an interface and modify the value of an instance field by means of invokedynamic<br>
	 *  We want to modify the value of <code>instance_concat</code> a instance field in the <code>Counter</code> class with <code>long</code> type.
	 *  We create the following interface:<br>	
	 * <code> public interface CallablevoidCounterlong{</code><br>
	 * <code> 	public void invoke(Counter counter, long newValue);</code><br>
	 * <code> }</code>
	 * <p>
	 * Then, the Callable is generated:<br>
	 * <code> CallablevoidCounterlong callable = ProxyFactory.&lt;CallablevoidCounterlong&gt; generateSetProperty(Counter.class, "instance_concat", CallablevoidCounterlong.class);</code><br>
	 * And the field can be dynamically invoked:<br>
	 * <code>callable.invoke(counter,newValue);</code>
	 * @param clazz Class of the field to be invoked.
	 * @param propertyName Name of the instance field to be invoked.
	 * @param interfaceCallable The interface with one single method.
	 * @return An object to modify the value of an instance field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	 */
	public final static <T> T generateSetProperty(Class<?> clazz,String propertyName, Class<T> interfaceCallable) throws InstantiationException {
		MemberBIClass<T> mb = new MemberBIClass<T>(interfaceCallable,TypeModifier.Instance,clazz,propertyName,InvokedynamicBootstrap.SET_INSTANCE_BOOTSTRAP_ATTRIBUTE);
		return getInstance(mb);
	}
	
	/**
	 * Generates an object to modify the value of an static field by means of invokedynamic.
	 * <br>The generated object implements the interface passed as a parameter.
	 * That interface must contain one single method. The method of the interface must have a void return type, and the field type as a only parameter. 
	 * The method could be named with any identifier.
	 * <br>T must be the interface.
	 *  <p>
	 *  The next example shows how to declare an interface and modify the value of an static field by means of invokedynamic<br>
	 *  We want to modify the value of <code>STATIC_COUNTER</code> a static field in the <code>Counter</code> class with <code>long</code> type.
	 *  We create the following interface:<br>
	 * <code> public interface Callablevoidlong{</code><br>
	 * <code> 	public void invoke(long newValue);</code><br>
	 * <code> }</code>
	 * <p>
	  * Then, the Callable is generated:<br>
	 * <code> Callablevoidlong callable = ProxyFactory.&lt;Callablevoidlong&gt; generateStaticSetProperty(Counter.class, "STATIC_COUNTER", Callablevoidlong.class);</code><br>
	  * And the field can be dynamically invoked:<br>
	 * <code> callable.invoke(newValue);</code>
	 * @param clazz Class of the field to be invoked.
	 * @param propertyName Name of the static field to be invoked.
	 * @param interfaceCallable The interface with one single method.
	 * @return An object to modify the value of an static field by means of invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.
	 */
	public final static <T> T generateStaticSetProperty(Class<?> clazz,String propertyName, Class<T> interfaceCallable) throws InstantiationException {
		MemberBIClass<T> mb = new MemberBIClass<T>(interfaceCallable,TypeModifier.Static,clazz,propertyName,InvokedynamicBootstrap.SET_STATIC_BOOTSTRAP_ATTRIBUTE);
		return getInstance(mb);
	}
	
		
	/**
	 * Generates an object to provide a constructor invocation using invokedynamic.
	 * <br> The generated object implements the interface passed as a parameter. 
	 * That interface must contain one single method. The method of the interface must have the type class to return type
	 * and as parameters, the arguments of the constructor to invoke.
	 * The method could be named with any identifier.
	 * <br>T must be the interface.
	 *  <p>
	 *  The next example shows how to declare an interface and dynamically invokes a constructor<br>
	 *  we want to create a new instance of <code>Counter</code> class and invoke a constructor with a <code>long</code> type parameter.
	 *  We create the following interface:<br>
	  * <code> public interface CallableCounterlong{</code><br>
	 * <code> 	public Counter invoke(long value);</code><br>
	 * <code> }</code>
	 * <p>
	 * Then, the Callable is generated:<br>
	* <code> CallableCounterlong callable = ProxyFactory.&lt;CallableCounterlong&gt; generateConstructor(Counter.class, CallableCounterlong.class);</code><br>
	 * And the method can be dynamically invoked:<br>
	 * <code> Counter counter = callable.invoke(value);</code>
	 * @param clazz Class of the constructor to be invoked.
	 * @param interfaceCallable The interface with one single method.
	 * @return An object to provide a constructor invocation using invokedynamic.
	 * @throws InstantiationException If the class could not be instantiated.	
	 */	
	public final static <T> T generateConstructor(Class<?> clazz,Class<T> interfaceCallable) throws InstantiationException {
		MemberBIClass<T> mb = new MemberBIClass<T>(interfaceCallable,TypeModifier.Static,clazz,"<init>",InvokedynamicBootstrap.CONSTRUCTOR_BOOTSTRAP);
		return getInstance(mb);
	}

		/**
		 * Returns a SetProperty object. If the object is in the internal cache, it is then returned. Otherwise, it is first created, then stored and finally returned.
		 * @param clazz Class the property belongs to.
		 * @param name Name of the property.
		 * @param type Type of the property.
		 * @param typeModifier Indicates if the member is instance or class (static).
		 * @return A SetProperty object.
		 * @throws InstantiationException If the class could not be instantiated.
		 */
	private  static<T> SetProperty getSetProperty(Class<?> clazz, String name,Class<?> type,TypeModifier typeModifier) throws InstantiationException {

		PropertyClass<T> pc = new PropertyClass<T>(clazz, name, type, typeModifier);		 
		SetProperty set =  propertySetClass.get(pc);
		if (set != null) {
			return set;
		}
		set = pc.setProperty();		
		propertySetClass.put(pc,set);
		return set;
	}
	
		 
	/**
	 * Returns a Callable object. If the object is in the internal cache, it is then returned. Otherwise, it is first created, then stored and finally returned.
	 * @param clazz Class the method belongs to.
	 * @param name Name of the method.
	 * @param rtype Return type of the method
	 * @param parametersType Type of the parameters of the method.
	 * @param typeModifier Indicates if the member is instance or class (static).
	 * @return A Callable instance.
	 * @throws InstantiationException If the class could not be instantiated.
	 */
	@SuppressWarnings("unchecked")
	private  static<T>  Callable<T> getCallable( Class<?> clazz,  String name,  Class<?> rtype, Class<?>[] parametersType, TypeModifier typeModifier) throws InstantiationException {

		MemberClass<T> mc = new MemberClass<T>(clazz, name, rtype,parametersType, typeModifier);		 				
		Callable<T> call =  (Callable<T>) memberClass.get(mc);
		if (call != null) 
			return  call;
		
		call = mc.getCallable();	
		memberClass.put(mc,call);
		return call;		
	}
	
	/**
	 * Returns a Callable object. If the object is in the internal cache, it is then returned. Otherwise, it is first created, then stored and finally returned.
	 * @param bootstrap responsible for generating the Handle used to invoke the appropriate bootstrap class.
	 * @param methodSignature The signature of the method to be invoked.
	 * @return A Callable instance.
	 * @throws InstantiationException If the class could not be instantiated.
	 */
	@SuppressWarnings("unchecked")
	private static <T> Callable<T> getInvokedynamicCallable(Bootstrap bootstrap, MethodSignature methodSignature) throws InstantiationException {
		MemberBSClass<T> idc = new MemberBSClass<T>(bootstrap,methodSignature);		
		
		Callable<T> call =  (Callable<T>) invokedynamicClass.get(idc);
		if (call != null)
			return call;
		
		call = idc.getCallable();	
		invokedynamicClass.put(idc,call);
		return call;
	}
	
	/**
	 * Returns a GetProperty object. If the object is in the internal cache, it is then returned. Otherwise, it is first created, then stored and finally returned.
	 * @param clazz Class the field belongs to.
	 * @param name Name of the property.
	 * @param type Type of the property.
	 * @param typeModifier Indicates if the member is instance or class (static).
	 * @return A GetProperty instance.
	 * @throws InstantiationException If the class could not be instantiated.
    */
	@SuppressWarnings("unchecked")
	private  static<T>  GetProperty<T> getGetProperty(Class<?> clazz, String name,Class<?> type, TypeModifier typeModifier) throws InstantiationException {
	 PropertyClass<T> pc = new PropertyClass<T>(clazz, name, type, typeModifier); 		
		GetProperty<T> get=  (GetProperty<T>) propertyGetClass.get(pc);

		if (get != null) {
			return   get;
		}
		get = pc.getProperty();
		propertyGetClass.put(pc,get);	
	
		return get;
	}
	
	/**
	 * Returns an object of type T. T is an interface that will be used in the dynamic invocation. . If the object is in the internal cache, it is then returned. Otherwise, it is first created, then stored and finally returned.
	 * @param member Represent a bootstrap and an interface.
	 * @return Returns an object of type T.
	 * @throws InstantiationException If the class could not be instantiated.
     */	
	private static <T> T getInstance(MemberBIClass<T> member)	throws InstantiationException {
		@SuppressWarnings("unchecked")
		MemberBIClass<T> mbget = (MemberBIClass<T>) memberMethod.get(member);

		if (mbget == null) {
			memberMethod.put(member, member);
			return member.getInstance();
		}
		return mbget.getInstance();
	}

	/**
	 * Returns an object whose type is represented by a type descriptor. If the object is in the internal cache, it is then returned. Otherwise, it is first created, then stored and finally returned.
	 * @param descriptor The class descriptor.
	 * @return Returns an class whose type is represented by a type descriptor.
	 * @throws ClassNotFoundException If the class specified by the descriptor cannot be found.
	 */
	private final static Class<?> getClassToDescriptor(String descriptor)throws ClassNotFoundException {
		Class<?> clazz = classDescriptor.get(descriptor);
		if(clazz == null){
			clazz = InvokedynamicClassLoader.getClassLoader().loadClass(descriptor);
		
			if(clazz == null) // es primitivo
				clazz = GeneratorUtil.getDescriptorPrimitive(descriptor);
				
			classDescriptor.put(descriptor,clazz);
		}
		return clazz;
	}
	
}
