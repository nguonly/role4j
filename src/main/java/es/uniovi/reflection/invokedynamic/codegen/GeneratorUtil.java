package es.uniovi.reflection.invokedynamic.codegen;

import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Provides a collection of utility methods to facilitate the dynamic generation of classes. These methods are used to build classes at runtime.
  * @version 0.3
 * @author Computational Reflection Research Group. University of Oviedo
 */
public class GeneratorUtil {
	
	
	public static final String CallableClassName = "es/uniovi/reflection/invokedynamic/interfaces/Callable";
	public static final String CallableMehtodName = "invoke";
	public static final MethodType CallableSignature = MethodType.methodType(Object.class, Object.class, Object[].class);	
	public static final int CallablelengthAloadBridge = 2;
	
	
	public static final String getCallableClassSignatureBridge(String rtype){
		return  "Ljava/lang/Object;Les/uniovi/reflection/invokedynamic/interfaces/Callable<" + rtype+ ">;";
	}	
	public static final MethodType getCallableMethodSignatureBridge(Class<?> rtype){
		if(rtype.isPrimitive())
			rtype = primitiveOrVoidObjectClass.get(rtype);
		return MethodType.methodType(rtype, Object.class, Object[].class);	
	}
	
	public static final String ConstructorClassName = "es/uniovi/reflection/invokedynamic/interfaces/Constructor";
	public static final String ConstructorMehtodName = "newInstance";
	public static final MethodType ConstructorSignature =  MethodType.methodType(Object.class, Object[].class);	
	public static final int ConstructorlengthAloadBridge = 1;
	
	
	public static final String getConstructorClassSignatureBridge(String rtype){
		return  "Ljava/lang/Object;Les/uniovi/reflection/invokedynamic/interfaces/Constructor<" + rtype+ ">;";
	}	
	public static final MethodType getConstructorMethodSignatureBridge(Class<?> rtype){
		if(rtype.isPrimitive())
			rtype = primitiveOrVoidObjectClass.get(rtype);
		return MethodType.methodType(rtype, Object[].class);	
	}
	
	
	
	public static final String GetPropertyClassName = "es/uniovi/reflection/invokedynamic/interfaces/GetProperty";
	public static final String GetPropertyMehtodName = "get";
	public static final MethodType GetPropertySignature =  MethodType.methodType(Object.class, Object.class);
	public static final int GetPropertylengthAloadBridge = 1;
	
	
	public static final String getGetPropertyClassSignatureBridge(String rtype){
		return "Ljava/lang/Object;Les/uniovi/reflection/invokedynamic/interfaces/GetProperty<"+rtype+">;";
	}	
	public static final MethodType getGetPropertyMethodSignatureBridge(Class<?> rtype){
		if(rtype.isPrimitive())
			rtype = primitiveOrVoidObjectClass.get(rtype);
		return MethodType.methodType(rtype, Object.class);
	}
	
	public static final String SetPropertyClassName = "es/uniovi/reflection/invokedynamic/interfaces/SetProperty";
	public static final String SetPropertyMethodName = "set";
	public static final MethodType SetPropertySignature = MethodType.methodType(Void.class,Object.class, Object.class);	
	

	public static String getClassName(String invokeType) {
		return "invokedynamic/" + invokeType + nextClass();
	}

	public static String getReturnType(Class<?> rtype) {
		if ( rtype.isPrimitive() || rtype == void.class)
			return Type.getDescriptor(primitiveOrVoidObjectClass.get(rtype));
		else
			return Type.getDescriptor(rtype);
	}

	public static void putParametersTypeCast( MethodVisitor mv, Class<?>[] parameters, boolean useArray[],int aloadIndexParameter) {
		int parameterIndex=0;
		int arrayIndex=aloadIndexParameter;
		int size = parameters.length;
		int constantIndex =0;
		
		for( ;arrayIndex<=useArray.length;arrayIndex++){
			if(!useArray[arrayIndex-1] && parameterIndex < size){				
				mv.visitVarInsn(Opcodes.ALOAD, arrayIndex);	
				doCast(mv, parameters[parameterIndex++]);
			}
		}
		
		for (int i = parameterIndex; i < size; i++) {				
				mv.visitVarInsn(Opcodes.ALOAD, arrayIndex-1);
				pushConstant(mv, constantIndex++);
				mv.visitInsn(Opcodes.AALOAD);	
				doCast(mv, parameters[i]);
			}
	}

	public static void putReturnTypeCast(MethodVisitor mv, Class<?> rtype) {
		box(mv, rtype);		
		if(rtype == void.class)
			mv.visitInsn(Opcodes.ACONST_NULL);
		mv.visitInsn(Opcodes.ARETURN);		
	}

	public static void putReturnType(MethodVisitor mv, Class<?> rtype) {
		if (!rtype.isPrimitive())
			mv.visitInsn(Opcodes.ARETURN);
		else if (rtype == void.class)
			mv.visitInsn(Opcodes.RETURN);
		else if (rtype == long.class)
			mv.visitInsn(Opcodes.LRETURN);
		else if (rtype == double.class)
			mv.visitInsn(Opcodes.DRETURN);
		else if (rtype == float.class)
			mv.visitInsn(Opcodes.FRETURN);
		else
			mv.visitInsn(Opcodes.IRETURN);

	}

	public static void putParametersType(MethodVisitor mv, Class<?>[] ptypes,Class<?>[] ptypesInterfaceSignature) {
		int increment=1;
		int index =0;
		
		if(ptypes.length >= 1)
			if(ptypes[index] != ptypesInterfaceSignature[index] && !ptypes[index].isPrimitive()){
				mv.visitVarInsn(Opcodes.ALOAD, index  + increment);
				mv.visitTypeInsn(Opcodes.CHECKCAST,	 ptypes[index].getName().replace('.', '/'));
				index++;
			}
			
		for (int i = index; i < ptypes.length; i++) {
			if (!ptypes[i].isPrimitive()){
				mv.visitVarInsn(Opcodes.ALOAD, i + increment);
				increment = 1;
			}else if (ptypes[i] == long.class){
				mv.visitVarInsn(Opcodes.LLOAD, i + increment);
				increment = 2;
			}else if (ptypes[i] == double.class){
				mv.visitVarInsn(Opcodes.DLOAD, i + increment);
				increment = 2;
			}else if (ptypes[i] == float.class){
				mv.visitVarInsn(Opcodes.FLOAD, i + increment);
				increment = 1;
			}else{
				mv.visitVarInsn(Opcodes.ILOAD, i + increment);
				increment = 1;
			}
		}

	}

	public static void pushConstant(MethodVisitor mv, int value) {
		switch (value) {
		case 0:
			mv.visitInsn(Opcodes.ICONST_0);
			break;
		case 1:
			mv.visitInsn(Opcodes.ICONST_1);
			break;
		case 2:
			mv.visitInsn(Opcodes.ICONST_2);
			break;
		case 3:
			mv.visitInsn(Opcodes.ICONST_3);
			break;
		case 4:
			mv.visitInsn(Opcodes.ICONST_4);
			break;
		case 5:
			mv.visitInsn(Opcodes.ICONST_5);
			break;
		default:
			if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
				mv.visitIntInsn(Opcodes.BIPUSH, value);
			} else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
				mv.visitIntInsn(Opcodes.SIPUSH, value);
			} else {
				mv.visitLdcInsn(Integer.valueOf(value));
			}
		}
	}

	public static void doCast(MethodVisitor mv, Class<?> type) {
		if (type == Object.class)
			return;
		if (type.isPrimitive() && type != Void.TYPE) {
			unbox(mv, type);
		} else {
			mv.visitTypeInsn(Opcodes.CHECKCAST,
					type.isArray() ? Type.getDescriptor(type) : type.getName()
							.replace('.', '/'));
		}
	}

	public static void unbox(MethodVisitor mv, Class<?> type) {
		if (type.isPrimitive() && type != Void.TYPE) {
			String returnString = "(Ljava/lang/Object;)"
					+ Type.getDescriptor(type);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC,
					"es/uniovi/reflection/invokedynamic/codegen/GeneratorUtil",
					type.getName() + "Unbox", returnString);
		}
	}

	public static boolean box(MethodVisitor mv, Class<?> type) {
		if (type.isPrimitive() && type != void.class) {
			String returnString = "(" + Type.getDescriptor(type) + ")" + GeneratorUtil.getReturnType(type);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getInternalName(primitiveOrVoidObjectClass.get(type)), 
					"valueOf", returnString);
			return true;
		}
		return false;
		
	}

	// --------------------------------------------------------
	// unboxing methods
	// --------------------------------------------------------

	public static byte byteUnbox(Object value) {
		Number n = castToNumber(value, byte.class);
		return n.byteValue();
	}

	public static char charUnbox(Object value) {
		return castToChar(value);
	}

	public static short shortUnbox(Object value) {
		Number n = castToNumber(value, short.class);
		return n.shortValue();
	}

	public static int intUnbox(Object value) {
		Number n = castToNumber(value, int.class);
		return n.intValue();
	}

	public static boolean booleanUnbox(Object value) {
		return castToBoolean(value);
	}

	public static long longUnbox(Object value) {
		Number n = castToNumber(value, long.class);
		return n.longValue();
	}

	public static float floatUnbox(Object value) {
		Number n = castToNumber(value, float.class);
		return n.floatValue();
	}

	public static double doubleUnbox(Object value) {
		Number n = castToNumber(value, double.class);
		return n.doubleValue();
	}

	
	public static Number castToNumber(Object object) {
		// default to Number class in exception details, else use the specified
		// Number subtype.
		return castToNumber(object, Number.class);
	}

	public static Number castToNumber(Object object, Class<?> type) {
		if (object instanceof Number)
			return  (Number) object;
		if (object instanceof Character) {
			return Integer.valueOf(((Character) object).charValue());
		}

		if (object instanceof String) {
			String c = (String) object;
			if (c.length() == 1) {
				return Integer.valueOf(c.charAt(0));
			} else {
				throw new ClassCastException("Error cast to Number " + object);
			}
		}
		throw new ClassCastException("Error cast to Number " + object + " type: "+  type);
	}

	public static char castToChar(Object object) {
		if (object instanceof Character) {
			return ((Character) object).charValue();
		} else if (object instanceof Number) {
			Number value = (Number) object;
			return (char) value.intValue();
		} else {
			String text = object.toString();
			if (text.length() == 1) {
				return text.charAt(0);
			} else {
				throw new ClassCastException("Error cast to char " + object);
			}
		}
	}

	public static boolean castToBoolean(Object object) {
		// null is always false
		if (object == null) {
			return false;
		}

		// equality check is enough and faster than instanceof check, no need to
		// check superclasses since Boolean is final
		if (object.getClass() == Boolean.class) {
			return ((Boolean) object).booleanValue();
		} else {
			throw new ClassCastException("Error cast to boolean ");
		}
	}
	
	public static Class<?> getDescriptorPrimitive(String descriptor){
		Class<?> clazz =  descriptorPrimitive.get(Type.getType(descriptor).getClassName());
		if(clazz == null)
			clazz =  descriptorPrimitive.get(descriptor);
		return clazz;
	}

	private static int number = 1;

	private static int nextClass() {
		return number++;
	}

	private static Map<Class<?>,Class<?>> primitiveOrVoidObjectClass = new HashMap<Class<?>, Class<?>>();
	public static Map<String,Class<?>> descriptorPrimitive = new HashMap<String, Class<?>>();
	

	static {
			
		primitiveOrVoidObjectClass.put(int.class, Integer.class);
		primitiveOrVoidObjectClass.put(short.class, Short.class);
		primitiveOrVoidObjectClass.put(double.class, Double.class);
		primitiveOrVoidObjectClass.put(float.class, Float.class);
		primitiveOrVoidObjectClass.put(long.class, Long.class);
		primitiveOrVoidObjectClass.put(boolean.class, Boolean.class);
		primitiveOrVoidObjectClass.put(char.class, Character.class);
		primitiveOrVoidObjectClass.put(byte.class, Byte.class);
		primitiveOrVoidObjectClass.put(void.class, Void.class);
		
		primitiveOrVoidObjectClass.put(int[].class, Integer[].class);
		primitiveOrVoidObjectClass.put(short[].class, Short[].class);
		primitiveOrVoidObjectClass.put(double[].class, Double[].class);
		primitiveOrVoidObjectClass.put(float[].class, Float[].class);
		primitiveOrVoidObjectClass.put(long[].class, Long[].class);
		primitiveOrVoidObjectClass.put(boolean[].class, Boolean[].class);
		primitiveOrVoidObjectClass.put(char[].class, Character[].class);
		primitiveOrVoidObjectClass.put(byte[].class, Byte[].class);
		
		descriptorPrimitive.put("int", int.class);
		descriptorPrimitive.put("short", short.class);
		descriptorPrimitive.put("double", double.class);
		descriptorPrimitive.put("float", float.class);
		descriptorPrimitive.put("long", long.class);
		descriptorPrimitive.put("boolean", boolean.class);
		descriptorPrimitive.put("char", char.class);
		descriptorPrimitive.put("byte", byte.class);
		descriptorPrimitive.put("void", void.class);
		
		
		
		
	}

}
