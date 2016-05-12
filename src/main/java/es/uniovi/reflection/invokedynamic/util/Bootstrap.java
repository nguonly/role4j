package es.uniovi.reflection.invokedynamic.util;

import java.util.Arrays;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


/**
 * Manages the required data to generate the Handles used to invoke the appropriate bootstrap class.
 * <p>
 *The following example shows the definition of a bootstrap. The bootstrap is cached, so that it is invoked once. 
 *The bootstrap class in placed in the <code>apiTest</code> y
 * package, and it is named <code>MiBootstrap</code>. The method to be called is <code>callMethod</code>.
 * The <code>Counter</code> class and the name of the method <code>(concat)</code>are passed as additional parameters.
 * <br>
 * <code> Bootstrap bootstrap = new Bootstrap("apiTest.MiBootstrap","callMethod", Counter.class, "concat");</code>
 * <p>
 * The signature of the method in the bootstrap to be invoked is the following one:<br>
 * <code>public static CallSite callMethod(Lookup lookup, String name,MethodType type, Class<?> nameClass, String member)</code>
 * <p>
 * The additional  <code>Counter</code> and <code>concat</code> parameters are the two last arguments passed to 
 * <code>callMethod</code> The rest of parameters are mandatory for every bootstrap. These are the mandatory parameters:
 * <ul>
 * 	<li>java.lang.invoke.MethodHandles.Lookup lookup: : Class used to search for instances of java.lang.invoke.MethodHandle</li>
 *  <li>String name: A symbolic name to designate the invocation</li>
 *  <li>java.lang.invoke.MethodType: The resolved type descriptor of the call site.</li>
 * </ul>
 * <p>
 * The signature of the <code>concat</code> method is the following one:<br>
 * <code>public static long concat(String cad1, String cad2);</code>
 * <p>
 * The first operation before calling this method is getting its MethodHandle. Since <code>concat</code> is static, we used the <code>findStatic</code> method in lookup.<br>
 * <code>MethodHandle mh = lookup.findStatic(nameClass,member,type);</code>
 * <p>
 * Once the MethodHandle is fetched, the CallSite can be created. We use a constant one in this example:<br>
 * <code>return new ConstantCallSite(mh);</code>
 * 
 * @version 0.3
 * @author Computational Reflection Research Group. University of Oviedo
  */
public class Bootstrap {

	private org.objectweb.asm.Handle handle = null;
	private final Object[] args;	
	private final Cache cache;
	private final String classBootstrap;
	private final String methodBootstrap;
	private final String desc;
		
	
	/**
	 * This constructor can be used to indicate whether or not the bootstrap must be stored in the cache.
	 * @param cache Tells whether or not the bootstrap should be stored in the cache.
	 * @param classBootstrap Descriptor of the bootstrap class to be invoked.
	 * @param methodBootstrap Name of the method in the bootstrap to be invoked.
	 * @param args Additional parameters to be passed to the bootstrap. The types allowed are: Integer, Double, Float, Long, String, Class y Handle.
	 */
	public Bootstrap(Cache cache, String classBootstrap, String methodBootstrap, Object... args) {
		this.classBootstrap = classBootstrap;
		this.methodBootstrap = methodBootstrap;		
		this.args = args;
		this.desc =getDesc();
		this.cache = cache;
	}
	
	/**
	 * This constructor stores the bootstrap in the cache.
	 * @param classBootstrap Descriptor of the Bootstrap class to be invoked.
	 * @param methodBootstrap Name of the method in the bootstrap to be invoked.
	 * @param args Additional parameters to be passed to the bootstrap. The types allowed are: Integer, Double, Float, Long, String, Class y Handle.
	 */
	public Bootstrap(String classBootstrap, String methodBootstrap, Object... args) {
		this(Cache.Save, classBootstrap,methodBootstrap,args);
	}
	
	/**
	 * Gets the additional parameters to be passed to the bootstrap.
	 * @return The additional parameters to be passed to the bootstrap.
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * Gets the handle to be used in the bootstrap invocation.	
	 * @return The handle to be used in the bootstrap invocation.
	 */
	public org.objectweb.asm.Handle getHandle() {
		if(handle == null){
			checkTypes();
			this.handle = new org.objectweb.asm.Handle(Opcodes.H_INVOKESTATIC,	classBootstrap.replace(".", "/"), methodBootstrap, desc);
		}
		
		return handle;
	}
	
	/**
	 * Gets the value indicating whether or not the bootstrap should be cached.
	 * @return the value indicating whether or not the bootstrap should be cached.
	 */
	public Cache getCache() {
		return cache;
	}
	
	/**
	 * Gets the signature of the method in the bootstrap to be invoked.
	 * @return The signature of the method in the bootstrap to be invoked.
	 */
	private String getDesc() {

		String addDes = "";

		for (int i = 0; i < args.length; i++) {
			addDes += Type.getDescriptor(args[i].getClass());
			if (args[i].getClass() == Class.class)
				args[i] = Type.getType((Class<?>) args[i]);
		}
		return "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;"
				+ addDes + ")Ljava/lang/invoke/CallSite;";
	}

	/**
	 * Checks that the types of the arguments to be passed to the bootstrap method are correct.
	 * @return True if types of the arguments to be passed to the bootstrap method are correct; false otherwise.
	 */
	private boolean checkTypes() {
		for (int i = 0; i < args.length; i++) {
			if (args[i].getClass() != Class.class
					&& args[i].getClass() != Integer.class
					&& args[i].getClass() != Float.class
					&& args[i].getClass() != Long.class
					&& args[i].getClass() != Double.class
					&& args[i].getClass() != String.class
					&& args[i].getClass() != Type.class
					&& args[i].getClass() != Handle.class)
				throw new RuntimeException("Unsupported parameter type: " + args[i]);
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(args);
		result = prime * result	+  classBootstrap.hashCode();
		result = prime * result	+  methodBootstrap.hashCode();
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
		Bootstrap other = (Bootstrap) obj;		
		 if (!classBootstrap.equals(other.classBootstrap))
			return false;
		 if (!methodBootstrap.equals(other.methodBootstrap))
			return false;
		 if (!Arrays.equals(args, other.args))
				return false;
		return true;
	}
	

}
