package net.role4j.evolution;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by nguonly on 1/14/17.
 */
public class JarClassReloader extends ClassLoader {
    private static String jarFile = ""; //Path to the jar file

    public JarClassReloader(){
        super(JarClassReloader.class.getClassLoader()); //calls the parent class loader's constructor
    }

    public JarClassReloader(String path){
        this();

        addJarFile(path);
    }

    public void addJarFile(String path){
        jarFile = path;

        updateCachedClasses();
    }

    /**
     *
     * @param className is full-qualified domain name: e.g. kh.com.lycog.MyClass
     * @param classPath
     */
    public void addClassFile(String className, String classPath) throws IOException {

//        String userDir = System.getProperty("user.dir");
//        String fullDir = userDir + File.separator + classPath + File.separator;
//        File f = new File(fullDir + className.replaceAll("\\.", File.separator) + ".class");

        File f = new File(classPath);

        if(!f.exists()) throw new IOException(); //force to use super (ClassLoader) to load class

        int size = (int) f.length();
        byte buff[] = new byte[size];
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        dis.readFully(buff);
        dis.close();

        Class result = defineClass(className, buff, 0, buff.length);
        CachedClassLoader.classes.put(className, result);
    }

    public Class loadClass(String className) throws ClassNotFoundException {
        return findClass(className);
    }

    public Class<?> loadClass(Class<?> clazz) throws ClassNotFoundException {
        return loadClass(clazz.getName());
    }

    public Class findClass(String className) {
        Class result;

        result = CachedClassLoader.classes.get(className); //checks in cached classes
        if (result != null) {
            return result;
        }

        try {
            return findSystemClass(className);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateCachedClasses(){
        //TODO: check the if the jar files have been modified
        byte classByte[];
        Class result;
        try {
            JarFile jar = new JarFile(jarFile);
            Enumeration<JarEntry> elements = jar.entries();
            while(elements.hasMoreElements()) {
//                JarEntry entry = jar.getJarEntry(className + ".class");
                JarEntry entry = elements.nextElement();

                if(entry.isDirectory() || !entry.getName().endsWith(".class")){
                    continue;
                }

                InputStream is = jar.getInputStream(entry);
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                int nextValue = is.read();
                while (-1 != nextValue) {
                    byteStream.write(nextValue);
                    nextValue = is.read();
                }
                // -6 because of .class
                String className = entry.getName().substring(0,entry.getName().length()-6);
                classByte = byteStream.toByteArray();
                className = className.replace('/', '.');
                result = defineClass(className, classByte, 0, classByte.length, null);
                CachedClassLoader.classes.put(className, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] loadClassData(String className) throws IOException {
        String classPath = "target" + File.separator + "test-classes"; //default target/classes
        String userDir = System.getProperty("user.dir");
        String fullDir = userDir + File.separator + classPath + File.separator;
        File f = new File(fullDir + className.replaceAll("\\.", File.separator) + ".class");

        if(!f.exists()) throw new IOException(); //force to use super (ClassLoader) to load class

        int size = (int) f.length();
        byte buff[] = new byte[size];
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        dis.readFully(buff);
        dis.close();
        return buff;
    }

//    public URL findResource(String name) {
//        try {
//            File file = new File(jarFile);
//            String url = file.toURL().toString();
//            return new URL("jar:"+url+"!/"+name);
//        } catch (Exception e) {
//            return null;
//        }
//
//    }
}
