/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes.property;

import com.jme3.macaq.logic.components.Timer;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import java.util.HashMap;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.PropertyEnv;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class TimerEventEditor implements PropertyEditor, ExPropertyEditor {
    
    private HashMap<Integer, Timer.Callback> timerEvents;

    @Override
    public void setValue(Object o) {
        timerEvents = (HashMap<Integer, Timer.Callback>) o;
    }

    @Override
    public Object getValue() {
        return timerEvents;
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
        return timerEvents.toString();
    }

    @Override
    public String getAsText() {
        String result = "";
        for(Integer i: timerEvents.keySet()){
            result += i;
            if(i < timerEvents.keySet().size()-1){
                result += ",";
            }
        }
        return result;
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        
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
