/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes.property;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class ActorNamesEditor implements PropertyEditor, ExPropertyEditor {

    private String[] value = new String[0];
    
    @Override
    public void setValue(Object o) {
        value = (String[]) o;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isPaintable() {
        return false;
    }

    @Override
    public void paintValue(Graphics grphcs, Rectangle rctngl) {
    }

    @Override
    public String getJavaInitializationString() {
        return value.toString();
    }

    @Override
    public String getAsText() {
        String result = "";
        for(int i = 0; i < value.length; i++){
            result += value[i];
            if(i < value.length-1){
                result += ",";
            }
        }
        return result;
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        value = string.split(",");
    }

    @Override
    public String[] getTags() {
        return null;
    }

    @Override
    public Component getCustomEditor() {
        return null;
    }

    @Override
    public boolean supportsCustomEditor() {
        return false;
    }

    
    private final PropertyChangeSupport supp = new PropertyChangeSupport(this);
    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
        supp.addPropertyChangeListener(pl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
        supp.removePropertyChangeListener(pl);
    }

    private PropertyEnv env;
    @Override
    public void attachEnv(PropertyEnv pe) {
        env = pe;
    }
    
}
