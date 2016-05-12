package es.uniovi.reflection.invokedynamic.interfaces;

/**
 * Defines the invoke method that performs a dynamic method invocation.
 * The generated classes implement this interface, overriding the invoke method.
 * This overridden method has the same signature that the invoke method in java.lang.reflect.Method.
 * 
 * @version 0.3
 *  @author Computational Reflection Research Group. University of Oviedo
 * @param <T> The return type of the method to be invoked.
 * @see java.lang.reflect.Method#invoke(Object, Object...)
 */
public interface Callable<T> {
	
	/**
	 * Invokes a method dynamically, using invokedynamic.
	 * @param object Implicit object used to perform the dynamic invocation. If the method to be invoked is static, this value should be null.
	 * @param arguments Arguments passed to the method invocation.
	 * @return The value returned by the method invoked dynamically.
	 */
	 T invoke( Object object,  Object...arguments);
	 

}
