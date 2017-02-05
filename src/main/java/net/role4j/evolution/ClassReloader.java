package net.role4j.evolution;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by nguonly on 1/14/17.
 */
public class ClassReloader extends ClassLoader {
    String classPath = "target" + File.separator + "test-classes"; //default target/classes

    String reloadedClass;

    public ClassReloader(){

    }

    public ClassReloader(String classPath){
        this.classPath = classPath;
    }

    public Class<?> reload(){
        return loadClass(reloadedClass);
    }

    public Class<?> reload(String clazz){
        reloadedClass = clazz;
        return reload();
    }

    @Override
    public Class<?> loadClass(String s) {
        return findClass(s);
    }

    @Override
    public Class<?> findClass(String s) {
        try {
//            if(!s.equals(reloadedClass)) throw new IOException();
            byte[] bytes = loadClassData(s);
            return defineClass(s, bytes, 0, bytes.length);
        } catch (IOException ioe) {
            try {
                return super.loadClass(s);
            } catch (ClassNotFoundException ignore) { }
            ioe.printStackTrace(System.out);
            return null;
        }
    }

    public Class<?> loadClass(Class<?> clazz){
        return loadClass(clazz.getName());
    }

    private byte[] loadClassData(String className) throws IOException {
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
}
