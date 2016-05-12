package es.uniovi.reflection.invokedynamic.interfaces;

/**
 * Defines the method that performs a dynamic invocation modifying the value of a field. 
 * The generated classes implement this interface, overriding the set method. 
 * This overridden method has the same signature that the set method in java.lang.reflect.Field.
 * 
 * @version 0.3
 * @author Computational Reflection Research Group. University of Oviedo
 * @see java.lang.reflect.Field#set(Object, Object)
 */
public interface SetProperty{
	
	/**
	 * Assigns a value to a filed, using invokedynamic.
	 * @param object The object that contains the field. In case the field is static, this value could be <code>null</code>.
	 * @param newValue The new value of the field.
	 */
	 Void set(Object object, Object newValue);
}
