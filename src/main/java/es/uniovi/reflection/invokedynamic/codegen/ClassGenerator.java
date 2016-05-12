package es.uniovi.reflection.invokedynamic.codegen;

import java.lang.invoke.MethodType;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import es.uniovi.reflection.invokedynamic.util.Bootstrap;
								
/**
 * Dynamically generates the JVM code that uses the invokedynamic opcode.
 * <br>For class generation we use ASM 4.0.
 * 
 * @version 0.3
  * @author Computational Reflection Research Group. University of Oviedo
 * @param <T> The interface that the generated class will implement.
 */
public class ClassGenerator<T> {
	private final String className;
	private final String interfaceClass;
	private final MethodType methodTypeSignature;
	private final String interfaceMethodName;
	private final MethodType interfaceSignatureBridge;
	private final MethodType methodTypeMethod;
	private final Bootstrap bootstrap;
	private final String classSignature;
	private final boolean interfaceOptimized;
	private final boolean hasBridge;
	private final int lengthAloadBridge;
	private final boolean isArray[];
	private final int aloadIndexParameter;
	
	/**
	 * Constructor of the class that receives all the arguments to dynamically create the class to perform the dynamic invocation.
	 * @param bootstrap Data that contains the Handle describing the bootstrap.
	 * @param classSignature Class signature to be defined. when the class has a generic type must indicate the signature; otherwise, must be null.
	 * <br>The next example shows the signature for a new class to implements the <code>Callable</code> interface with the gereric <code>Long</code> type.<br>
	 * <code>Ljava/lang/Object;Les/uniovi/reflection/invokedynamic/interfaces/Callable&lt;Ljava/lang/Long;&gt;;</code>
	 * @param interfaceClass Interface to be implemented by the generated class.
	 * @param interfaceMethodName Name of the method of the interface to be implemented.
	 * @param methodTypeSignature Signature of the method in the interface.
	 * @param methodTypeMethod Signature of the method, field or constructor to be invoked.
	 * @param interfaceOptimized Indicates if the optimization should be performed.
	 * @param hasBridge Indicates if generics should be used in the return type. 
	 * This will happen when the interface optimization is used and the return type is not Object.
	 * @param interfaceSignatureBridge If the bridge is used, the signature of the method should be passed.
	 * @param lengthAloadBridge If the bridge is used, the number of parameters to be passed should be passed.
	 * @param isArray Indicates when an array of Objects should be unpacked. 
	 * Each position in the array represents one parameter. 
	 * If the position is true, the parameter should be unpacked as an array; 
	 * if it is false, it should be used as Object. 
	 * The last value should always be the array. 
	 * This last value is used when the interface optimization is not performed.
	 * @param aloadIndexParameter Indicates the index from the parameters should be considered. 
	 * It is used when the interface optimization is not used.
	 */
	public ClassGenerator(Bootstrap bootstrap,
			String classSignature,
			String interfaceClass, 
			String interfaceMethodName,
			MethodType methodTypeSignature,
			MethodType methodTypeMethod,
			boolean interfaceOptimized, 
			boolean hasBridge,
			MethodType interfaceSignatureBridge,
			int lengthAloadBridge,
			boolean[] isArray,
			int aloadIndexParameter){
		
							
		this.className = GeneratorUtil.getClassName("Callable");
		this.classSignature = classSignature; 
		this.bootstrap = bootstrap;	
		this.interfaceMethodName = interfaceMethodName;
		this.methodTypeSignature = methodTypeSignature;
		this.interfaceClass = interfaceClass.replace(".", "/");
		this.interfaceSignatureBridge = interfaceSignatureBridge;
		this.methodTypeMethod = methodTypeMethod;
		this.interfaceOptimized = interfaceOptimized;
		this.hasBridge = hasBridge;
		this.lengthAloadBridge = lengthAloadBridge;
		this.isArray = isArray;
		this.aloadIndexParameter = aloadIndexParameter;
		
	}
	
	/**
	 * Returns an instance of the generated class. T is the type of the interface that the generated class implements.
	 * @return An instance of the generated class.
	 * @throws InstantiationException If there is an error instantiating the class.
	 */
	@SuppressWarnings("unchecked")
	public T getInstance() throws InstantiationException {
		byte[] code = generateClass();		
	/*	
		try{
		FileOutputStream fos = new FileOutputStream(className.replace("/", ".") +".class");
		 fos.write(code);
		 fos.close();
		}catch(Throwable th){
			
		}*/
		
		try{
			Class<T> c = (Class<T>) InvokedynamicClassLoader.getClassLoader().register(className, code);
			return c.newInstance();
		}catch(Throwable th){
			throw new InstantiationException(th.getMessage());
		}
	}
	
		
	/**
	 * Defines the class that performs the dynamic invocation.
	 * @return bytes The binary definition of the class.
	 */
	private byte[] generateClass() {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, className,classSignature, "java/lang/Object", new String[]{interfaceClass});

		visitConstructor(cw);
		if(hasBridge)
			visitBridgeConstructor(cw);
		
		methodInvoke(cw);

		cw.visitEnd();

		return cw.toByteArray();
	}
	
	/**
	 * Defines the invoke method, which performs the dynamic invocation.
	 * @param cw ClassWriter.
	 */
	private void methodInvoke(ClassWriter cw) {
		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC+ Opcodes.ACC_FINAL, interfaceMethodName,methodTypeSignature.toMethodDescriptorString() , null,null);
		mv.visitCode();

		if(interfaceOptimized)
			GeneratorUtil.putParametersType(mv,methodTypeMethod.parameterArray(),methodTypeSignature.parameterArray());	
		else
			GeneratorUtil.putParametersTypeCast( mv, methodTypeMethod.parameterArray(), isArray,aloadIndexParameter);
		
		mv.visitInvokeDynamicInsn(interfaceMethodName, methodTypeMethod.toMethodDescriptorString(), bootstrap.getHandle(),bootstrap.getArgs());
		
		if(interfaceOptimized)
			GeneratorUtil.putReturnType(mv,methodTypeMethod.returnType());
		else
			GeneratorUtil.putReturnTypeCast(mv, methodTypeMethod.returnType());

		mv.visitMaxs(3, 3);
		mv.visitEnd();

	}
	
	/**
	 * Defines the bridge method. It is only used with generics.
	 * @param cw ClassWriter
	 */
	private void visitBridgeConstructor(ClassWriter cw) {
		MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC+ Opcodes.ACC_BRIDGE + Opcodes.ACC_SYNTHETIC, interfaceMethodName,interfaceSignatureBridge.toMethodDescriptorString(),null, null);
		mw.visitCode();
		mw.visitVarInsn(Opcodes.ALOAD, 0);
		for(int i=0;i<lengthAloadBridge;i++)
			mw.visitVarInsn(Opcodes.ALOAD, i+1);		
		
		mw.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, interfaceMethodName,methodTypeSignature.toMethodDescriptorString());
		mw.visitInsn(Opcodes.ARETURN);
		mw.visitMaxs(3, 3);
		mw.visitEnd();
	}
	
	/**
	 * Defines the class constructor.
	 * @param cw ClassWriter
	 */
	private static void visitConstructor(ClassWriter cw) {
		MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", 	null, null);
		mw.visitCode();
		mw.visitVarInsn(Opcodes.ALOAD, 0); 
		mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>",	"()V");
		mw.visitInsn(Opcodes.RETURN);
		mw.visitMaxs(1, 1);
		mw.visitEnd();
	}
	
	

}
