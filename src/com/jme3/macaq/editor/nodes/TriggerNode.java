/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes;

import com.jme3.macaq.editor.nodes.property.ActorNamesEditor;
import com.jme3.macaq.editor.nodes.property.SpatialNameEditor;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.components.Trigger;
import java.beans.IntrospectionException;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 *
 * @author Rickard
 */
public class TriggerNode extends LogicNode{

    public TriggerNode(AbstractLogicalComponent key) throws IntrospectionException {
        super(key);
        setShortDescription("The Trigger is connected to a spatial which acts as its bounds in the scene, specified by triggerSpatialName. actorNames is a comma separated list of spatials that may act with and trigger the Trigger. Once they are inside its bounds, it will call onTrigger, passing the name of the spatial.");
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = sheet.get("Default");
        Trigger obj = getLookup().lookup(Trigger.class);

        try {
            PropertySupport.Reflection<String> triggerSpatialName = new PropertySupport.Reflection(obj, String.class, "triggerSpatialName");
            triggerSpatialName.setName("triggerSpatialName");
            triggerSpatialName.setPropertyEditorClass(SpatialNameEditor.class);
            set.put(triggerSpatialName);
            
            PropertySupport.Reflection actorNamesList = new PropertySupport.Reflection(obj, String[].class, "actorNames");
            actorNamesList.setName("actorNames");
            actorNamesList.setPropertyEditorClass(ActorNamesEditor.class);
            set.put(actorNamesList);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);
        return sheet;
    }
    
    
    
}
