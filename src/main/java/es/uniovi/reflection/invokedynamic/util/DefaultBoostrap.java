package es.uniovi.reflection.invokedynamic.util;

import es.uniovi.reflection.invokedynamic.codegen.InvokedynamicBootstrap;

/**
 * Defines the default bootstrap class to be invoked.
 * 
  * @author Computational Reflection Research Group. University of Oviedo
  * @version 0.3
 *
 */
public class DefaultBoostrap extends Bootstrap {

	/**
	 * @param clazz The class whose member is going to be dynamically invoked.
	 * @param memberName Name of the member to be dynamically invoked.
	 * @param bootstrapMethod Name of the method in the bootstrap to be invoked.
	 */
	public DefaultBoostrap(Class<?> clazz, String memberName,String bootstrapMethod) {
		super(InvokedynamicBootstrap.BOOTSTRAP_CLASS, bootstrapMethod , clazz, memberName);
	}
	
	
		

}
