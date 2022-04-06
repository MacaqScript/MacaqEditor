/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.jme3.macaq.base.LogicalComponentInterface;
import com.jme3.macaq.editor.util.ExtensionsComponentsLoader;
import com.jme3.macaq.file.FileLocator;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.DummyComponent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class EditorHelper {

    private List<Class<? extends AbstractLogicalComponent>> availableComponents;
    private final FileLocator locator = new FileLocator();
    private static final String COMPONENTS_PACKAGE_PATH = "com.jme3.macaq.logic.components";
    private static final String COMPONENTS_PACKAGE_PATH_2 = "com/jme3/macaq/logic/components/";
    private static final String NODES_PACKAGE_PATH = "com.jme3.macaq.editor.nodes";
    private ExtensionsComponentsLoader loader;

    private static final String[] preloadedClasses = new String[]{
        "com.jme3.macaq.logic.components.Counter",
        "com.jme3.macaq.logic.components.Notify",
        "com.jme3.macaq.logic.components.NotifyAppState",
        "com.jme3.macaq.logic.components.PlayAnimation",
        "com.jme3.macaq.logic.components.PlayCinematic",
        "com.jme3.macaq.logic.components.PlayEffect",
        "com.jme3.macaq.logic.components.Start",
        "com.jme3.macaq.logic.components.Timer",
        "com.jme3.macaq.logic.components.Trigger"
    };

    public List<Class<? extends AbstractLogicalComponent>> getComponents() {
//        if(availableComponents == null){
        availableComponents = new ArrayList<>();
        try {
            ClassLoader cl = LogicalComponentInterface.class.getClassLoader();
            Set<ClassPath.ClassInfo> classesInPackage = ClassPath.from(cl).getTopLevelClassesRecursive(COMPONENTS_PACKAGE_PATH);
//                Set<ClassPath.ClassInfo> classesInPackage = locator.findClasses(COMPONENTS_PACKAGE_PATH, "");
            convertToClasses(classesInPackage, availableComponents);
            loadAdditionalComponents(availableComponents);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
//        }
        return availableComponents;
    }

    private List<Class<? extends AbstractLogicalComponent>> convertToClasses(Set<ClassPath.ClassInfo> classesInPackage, List<Class<? extends AbstractLogicalComponent>> availableComponents) {
        Iterator<ClassInfo> it = classesInPackage.iterator();
        System.out.println("classesInPackage " + classesInPackage.size());
        if (classesInPackage.isEmpty()) {
//            loadHardCodedClasses(availableComponents);
            loadBaseClasses(availableComponents);
        } else {
            while (it.hasNext()) {
                ClassInfo c = it.next();
                if (c.getPackageName().startsWith(COMPONENTS_PACKAGE_PATH)) {
                    System.out.println("added " + c.getSimpleName());
                    availableComponents.add((Class<? extends AbstractLogicalComponent>) c.load());
                }
            }
        }
        return availableComponents;
    }

    private List<Class<? extends AbstractLogicalComponent>> loadHardCodedClasses(List<Class<? extends AbstractLogicalComponent>> availableComponents) {
        ClassLoader cl = getClass().getClassLoader();
        Class c;

        for (String className : preloadedClasses) {
            try {
                c = cl.loadClass(className);
                availableComponents.add(c);
            } catch (ClassNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return availableComponents;
    }

    private List<Class<? extends AbstractLogicalComponent>> loadBaseClasses(List<Class<? extends AbstractLogicalComponent>> availableComponents) {
        ClassLoader cl = LogicalComponentInterface.class.getClassLoader();
        Class c;
        InputStream input = cl.getResourceAsStream("base-components.txt");//cl.getResourceAsStream("/components.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String s;
        try {
            while ((s = reader.readLine()) != null) {
                c = cl.loadClass(s);
                availableComponents.add(c);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return availableComponents;
    }

    private List<Class<? extends AbstractLogicalComponent>> loadAdditionalComponents(List<Class<? extends AbstractLogicalComponent>> availableComponents) throws IOException {
        if(loader == null){
            File file = new File("G:\\Documents\\NetBeansProjects\\BasicGame\\ComponentsLib.jar");
            loader = new ExtensionsComponentsLoader(Thread.currentThread().getContextClassLoader(), file);
        }
        Set<ClassPath.ClassInfo> classesInPackage = ClassPath.from(loader.getClassLoader()).getTopLevelClassesRecursive(COMPONENTS_PACKAGE_PATH);
//                Set<ClassPath.ClassInfo> classesInPackage = locator.findClasses(COMPONENTS_PACKAGE_PATH, "");
        convertToClasses(classesInPackage, availableComponents);
        return availableComponents;
    }

    public Class<? extends AbstractLogicalComponent> loadComponent(String name){
        try {
            return loader.loadComponentClass(name);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
}
