/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes;

import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.components.MoveToTarget;
import java.beans.IntrospectionException;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 *
 * @author Rickard
 */
public class MoveToTargetNode extends LogicNode{

    public MoveToTargetNode(AbstractLogicalComponent key) throws IntrospectionException {
        super(key);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = sheet.get("Default");
        MoveToTarget obj = getLookup().lookup(MoveToTarget.class);

        try {
            PropertySupport.Reflection spatialName = new PropertySupport.Reflection(obj, String.class, "spatialName");
            spatialName.setName("spatialName");
            set.put(spatialName);
            
            PropertySupport.Reflection targetSpatialName = new PropertySupport.Reflection(obj, String.class, "targetSpatialName");
            targetSpatialName.setName("targetSpatialName");
            set.put(targetSpatialName);
            
            PropertySupport.Reflection<Float> speed = new PropertySupport.Reflection(obj, float.class, "speed");
            speed.setName("speed");
            set.put(speed);
            
            PropertySupport.Reflection<Boolean> lookAt = new PropertySupport.Reflection(obj, boolean.class, "lookAt");
            lookAt.setName("lookAt");
            set.put(lookAt);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);
        return sheet;
    }
    
    
    
}
