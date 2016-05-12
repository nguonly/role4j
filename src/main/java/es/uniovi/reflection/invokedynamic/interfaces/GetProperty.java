package es.uniovi.reflection.invokedynamic.interfaces;

/**
 * 
 * Defines the method that performs a dynamic invocation returning a field value.
 * The generated classes implement this interface, overriding the get method. 
 * This overridden method has the same signature that the get method in java.lang.reflect.Field.
 * 
 * @version 0.3
 * @author Computational Reflection Research Group. University of Oviedo
 * @param <T> El tipo T se corresponde con el tipo de la propiedad.
 * @see java.lang.reflect.Field#get(Object)
 */
public interface GetProperty<T> {
	
	/**
	 * Obtains the value of an instance field using invokedynamic.
	 * @param object The object that contains the field. In case the field is static, this value could be <code>null</code>.
	 * @return The value of the field.
	 */
	T get(Object object);
}
