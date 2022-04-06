/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.file;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import junit.framework.TestCase;
import org.openide.util.Exceptions;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class FileLocatorTest extends TestCase{
    
    public void testLocateComponents(){
        FileLocator locator = new FileLocator();
        
        try {
            Set<ClassPath.ClassInfo> classesInPackage = locator.findClasses("com.jme3.macaq.logic.components", "");
            Iterator<ClassInfo> it = classesInPackage.iterator();
            while(it.hasNext()){
                System.out.println("Class " + it.next().getName());
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
