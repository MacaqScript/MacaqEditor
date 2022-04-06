/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.util;

import com.jme3.macaq.logic.AbstractLogicalComponent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import org.openide.util.Exceptions;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class ExtensionsComponentsLoader {
    private HashMap<String, Class<? extends AbstractLogicalComponent>> classes = new HashMap(); //used to cache already defined classes
    private URLClassLoader classLoader;

    public ExtensionsComponentsLoader(ClassLoader parent, File file) {
        try {
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            classLoader = new URLClassLoader(urls, parent);
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public Class<? extends AbstractLogicalComponent> loadComponentClass(String name) throws ClassNotFoundException{
        Class c = classes.get(name);
        if(c == null){
            c = classLoader.loadClass(name);
            if(c != null){
                classes.put(name, c);
            }
        }
        return c;
    }
    
    public URLClassLoader getClassLoader(){
        return classLoader;
    }
}
