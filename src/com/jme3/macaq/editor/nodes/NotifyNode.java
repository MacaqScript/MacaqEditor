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
public class NotifyNode extends LogicNode{

    public NotifyNode(AbstractLogicalComponent key) throws IntrospectionException {
        super(key);
        setShortDescription("Notify is a customizable component that can be used to interact with controls in the application. It has a controlClassName, which is the qualified name of a control on spatialName. When message is called, it will send the message string through onMessage. This can then be parsed by the control. The control can then use onPerformed to say when it is done.");
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = sheet.get("Default");
        AbstractNotify obj = getLookup().lookup(AbstractNotify.class);

        try {
            PropertySupport.Reflection<String> spatialName = new PropertySupport.Reflection(obj, String.class, "spatialName");
            spatialName.setName("spatialName");
            spatialName.setPropertyEditorClass(SpatialNameEditor.class);
            set.put(spatialName);
            
            PropertySupport.Reflection controlClassName = new PropertySupport.Reflection(obj, String.class, "controlClassName");
            controlClassName.setName("controlClassName");
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
