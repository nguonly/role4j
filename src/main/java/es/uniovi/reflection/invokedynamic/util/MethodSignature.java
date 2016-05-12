package es.uniovi.reflection.invokedynamic.util;

import java.util.Arrays;

/**
 * Represents the signature of a method, field or constructor, giving a specific class.
 * 
   * @author Computational Reflection Research Group. University of Oviedo
  * @version 0.3
 *
 */

public class MethodSignature {
	
	private static Class<?>[] ptypesnull ={};
	private final Class<?> clazz;
	private final Class<?> rtype;
	private Class<?>[] parametersType;
	
	
	/**
	 * Class constructor that receives the signature to define instance members.
	 * <ul>
	 * <li>
	 *  To define the signature of a method in the <code>Counter</code> class that receives a long parameter and returns a long:<br>
	 * <code>MethodSignature methodSignature = new MethodSignature( long.class,Counter.class,	new Class[] { long.class });</code>
	 * </li>
	 * <li>
	 * To define the signature of a field in the <code>Counter</code> class to be modified whose type is long:<br>
	 * <code>MethodSignature methodSignature = new MethodSignature(void.class, Counter.class, new Class<?>[]{long.class});</code>
	 *  </li>
	 * @param rtype The return type of the member.
	 * @param clazz Class the member belongs to.
	 * @param parametersType Type of the parameters of the member.
	 */
	public MethodSignature( Class<?> rtype,Class<?> clazz, Class<?>[]parametersType){
		this.clazz = clazz;
		this.rtype = rtype;
		this.parametersType = parametersType;
		
	}
	
	/**
	 * Class constructor that receives the signature to define static members or constructors.
	 * <ul>
	 * <li>
	 * To define the signature of a static method receives a long parameter and returns a long.<br>
	 * <code>MethodSignature methodSignature = new MethodSignature( long.class,new Class[] { long.class });</code>
	 * </li>
	 * <li>
	 * To define the signature of a static field to be modified whose type is long:<br>
	 * <code>MethodSignature methodSignature = new MethodSignature(void.class, new Class<?>[]{long.class});</code>
	 *  </li>
	 *  <li>
	 * To define a constructor of the Counter class that receives a long<br>
	 * <code>MethodSignature methodSignature = new MethodSignature(Counter.class, new Class<?>[]{long.class});</code>
	 *  </li>
	 * @param rtype The return type of the member.
	 * @param parametersType Type of the parameters of the member.
	 */
	public MethodSignature( Class<?> rtype, Class<?>[]parametersType){
		this.clazz = null;
		this.rtype = rtype;
		this.parametersType = parametersType;
		
	}
	
	/**
	 * Class constructor that receives the signature to define instance members or constructor without arguments.
	 * <ul>
	 * <li>
	 * 	To define the signature of a method in the <code>Counter</code> class that returns a long:<br>
	 * <code>MethodSignature methodSignature = new MethodSignature( long.class,Counter.class);</code>
	 * </li>
	 * <li>
	 * 	To define the signature of a field in the <code>Counter</code> class to be get its value whose type is long:<br>
	 * <code>MethodSignature methodSignature = new MethodSignature( long.class, Counter.class);</code>
	 *</li>
	 * @param rtype The return type of the member.
	 * @param clazz Object of the class the member belongs to.
	 */
	public MethodSignature( Class<?> rtype, Class<?> clazz){
		this.clazz = clazz;
		this.rtype = rtype;
		this.parametersType = ptypesnull;
		
	}
	
	
	/**
	 * Class constructor that receives the signature to define static members.
	 * <ul>
	 * <li>
	 * To define the signature of a static method that returns a long:<br>
	 * <code>MethodSignature methodSignature = new MethodSignature( long.class);</code>
	 * </li>
	 * <li>
	 * To define the signature of a static field to get its value and whose type is long:<br>
	 * <code>MethodSignature methodSignature = new MethodSignature( long.class);</code>
	 *</li>
	 * <li>
	 * To define the signature of a constructor of <code>Counter</code> with no parameters:<br>
	 * <code>MethodSignature methodSignature = new MethodSignature(Counter.class);</code>
	 *  </li>
	 * @param rtype The return type of the member.
	 */
	public MethodSignature( Class<?> rtype){
		this.clazz = null;
		this.rtype = rtype;
		this.parametersType =ptypesnull;		
	}

	/**
	 * Gets the class the defined method belongs to.
	 * @return Gets the class the defined method belongs to. If it is static, its value is <code>null</code>
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * Gets the return type of the member.
	 * @return The return type of the member.
	 */
	public Class<?> getRtype() {
		return rtype;
	}

	/**
	 * Gets the type of the parameters of the member
	 * @return Type of the parameters of the member
	 */
	public Class<?>[] getParametersType() {
		return parametersType;
	}

	/**
	 * Tells whether the member is static.
	 * @return <code>true</code> if the member is static; <code>false</code> otherwise.
	 */
	public boolean isStatic(){
		if(clazz == null)
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( clazz == null ?0: clazz.hashCode());
		result = prime * result + Arrays.hashCode(parametersType);
		result = prime * result + rtype.hashCode();
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
		MethodSignature other = (MethodSignature) obj;
		if (rtype != other.rtype)
			return false;
		if (clazz != other.clazz)
			return false;
		if (!Arrays.equals(parametersType, other.parametersType))
			return false;
		
		return true;
	}
	
	


}
