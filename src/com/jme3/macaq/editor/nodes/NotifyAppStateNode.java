/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes;

import com.jme3.macaq.editor.nodes.property.SpatialNameEditor;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.components.AbstractNotify;
import java.beans.IntrospectionException;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 *
 * @author Rickard
 */
public class NotifyAppStateNode extends LogicNode{

    public NotifyAppStateNode(AbstractLogicalComponent key) throws IntrospectionException {
        super(key);
        setShortDescription("Notify Appstate is a customizable component that can be used to interact with app states in the application.");
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = sheet.get("Default");
        AbstractNotify obj = getLookup().lookup(AbstractNotify.class);

        try {
            PropertySupport.Reflection controlClassName = new PropertySupport.Reflection(obj, String.class, "appStateName");
            controlClassName.setName("appStateName");
            set.put(controlClassName);
            
            PropertySupport.Reflection message = new PropertySupport.Reflection(obj, String.class, "message");
            message.setName("message");
            set.put(message);
            
        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);
        return sheet;
    }
    
    
    
}
