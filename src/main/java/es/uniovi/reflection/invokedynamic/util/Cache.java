package es.uniovi.reflection.invokedynamic.util;

/**
 * Indicates whether or not an element should be stored in the cache.
  * @author Computational Reflection Research Group. University of Oviedo
  * @version 0.3
 *
 */
public enum Cache {
	
	/**
	 * The element should be stored in the cache.
	 */
	Save {public boolean eval(){return true;}},
	/**
	 * The element should not be stored in the cache.
	 */
	NoSave {public boolean eval(){return false;}};;
	
	/**
	 *  @return Returns true if the element should be stored in the cache; false otherwise.
	 */
	public abstract boolean eval();
		 
	

}
