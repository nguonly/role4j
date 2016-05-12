package es.uniovi.reflection.invokedynamic.interfaces;

/**
 * 
 * Defines the method that performs a dynamic constructor invocation. 
 * The generated classes implement this interface, overriding the newInstance method.
 * This overridden method has the same signature that the newInstance method in java.lang.reflect.Constructor.
 * 
 * @version 0.3
 *   @author Computational Reflection Research Group. University of Oviedo
 * @param <T> Type of the class to be instantiated.
 * @see java.lang.reflect.Constructor#newInstance(Object...)
 */
public interface Constructor<T> {
	
	/**
	 * Creates an object using invokedynamic.
	 * @param args Arguments of the constructor invocation.
	 * @return The instance created using invokedynamic.
	 */
	T newInstance(Object...args);

}
