/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes;

import com.jme3.macaq.editor.nodes.property.SpatialNameEditor;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.components.PlayCinematic;
import java.beans.IntrospectionException;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 *
 * @author Rickard
 */
public class PlayCinematicNode extends LogicNode{
    
    public PlayCinematicNode(){
        
    }

    public PlayCinematicNode(AbstractLogicalComponent key) throws IntrospectionException {
        super(key);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = sheet.get("Default");
        PlayCinematic obj = getLookup().lookup(PlayCinematic.class);

        try {
            PropertySupport.Reflection<String> nodeName = new PropertySupport.Reflection(obj, String.class, "nodeName");
            nodeName.setName("nodeName");
            nodeName.setPropertyEditorClass(SpatialNameEditor.class);
            set.put(nodeName);
            
            PropertySupport.Reflection cinematicName = new PropertySupport.Reflection(obj, String.class, "cinematicName");
            cinematicName.setName("cinematicName");
            set.put(cinematicName);
        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }
        sheet.put(set);
        return sheet;
    }
    
    
    
}
