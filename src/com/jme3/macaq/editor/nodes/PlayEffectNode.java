/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes;

import com.jme3.macaq.editor.nodes.property.SpatialNameEditor;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.components.PlayEffect;
import java.beans.IntrospectionException;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 *
 * @author Rickard
 */
public class PlayEffectNode extends LogicNode{

    public PlayEffectNode(AbstractLogicalComponent key) throws IntrospectionException {
        super(key);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = sheet.get("Default");
        PlayEffect obj = getLookup().lookup(PlayEffect.class);

        try {
            PropertySupport.Reflection<String> emitterName = new PropertySupport.Reflection(obj, String.class, "emitterName");
            emitterName.setName("emitterName");
            
            emitterName.setPropertyEditorClass(SpatialNameEditor.class);
            set.put(emitterName);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);
        return sheet;
    }
    
    
    
}
