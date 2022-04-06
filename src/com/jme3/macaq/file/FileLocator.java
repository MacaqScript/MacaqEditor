/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.file;

import com.google.common.reflect.ClassPath;
import com.jme3.macaq.logic.components.Counter;
import java.io.IOException;
import java.util.Set;
import org.openide.util.Exceptions;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class FileLocator {
    
    public Set<ClassPath.ClassInfo> findClasses(String pack, String type) throws IOException{
        ClassLoader cl = com.jme3.macaq.logic.components.Counter.class.getClassLoader();
        ClassPath classPath = ClassPath.from(cl);
        
        try {
            Class c = cl.loadClass("com.jme3.macaq.logic.components.Counter");
            System.out.println("class " + c);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        System.out.println("Classpath " + classPath.getTopLevelClassesRecursive(Counter.class.getName()).size() +" " );
        Set<ClassPath.ClassInfo> classesInPackage = ClassPath.from(cl).getTopLevelClassesRecursive(pack);
        return classesInPackage;
    }
    
//    public Set<Class<? extends MyInterface>> findClassesExtending(String pack, String className){
//        ClassLoader.getSystemClassLoader().
//    }
}
