package es.uniovi.reflection.invokedynamic.util;

/**
 * Indicates if a member is instance or class (static).
 * 
   * @author Computational Reflection Research Group. University of Oviedo
  * @version 0.3
 *
 */
public enum TypeModifier {
	
	/**
	 * The method or field is static (class)
	 */
	 Static   {public boolean eval(){return true;}},
	 /**
	  * The method or field is instance (non-static)
	  */
	 Instance {public boolean eval(){return false;}};
	 
	 /**
	  * @return <code>true</code> if the type is static (class); <code>false</code> otherwise.
	  */
	public abstract boolean eval();

}
