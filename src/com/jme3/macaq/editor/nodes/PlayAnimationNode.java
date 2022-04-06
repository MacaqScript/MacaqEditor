/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes;

import com.jme3.macaq.editor.nodes.property.LoopModeEditor;
import com.jme3.macaq.editor.nodes.property.SpatialNameEditor;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.components.PlayAnimation;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class PlayAnimationNode extends LogicNode{
    
    public PlayAnimationNode(AbstractLogicalComponent key){
        super(key);
        setShortDescription("Connects to an controls an AnimControl on a Spatial");
    }
    
    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = sheet.get("Default");
        PlayAnimation animation = getLookup().lookup(PlayAnimation.class);
        try {
            PropertySupport.Reflection<String> spatialName = new PropertySupport.Reflection(animation, String.class, "spatialName");
            spatialName.setName("spatialName");
            spatialName.setPropertyEditorClass(SpatialNameEditor.class);
            set.put(spatialName);
            
            PropertySupport.Reflection<String> animationName = new PropertySupport.Reflection(animation, String.class, "animation");
            animationName.setName("animation");
            set.put(animationName);
            
            PropertySupport.Reflection<Integer> channel = new PropertySupport.Reflection(animation, int.class, "channel");
            channel.setName("channel");
            set.put(channel);
            
            PropertySupport.Reflection<String> loopMode = new PropertySupport.Reflection(animation, String.class, "loopMode");
            loopMode.setName("loopMode");
            loopMode.setPropertyEditorClass(LoopModeEditor.class);
            set.put(loopMode);
            
            PropertySupport.Reflection<Float> animationSpeed = new PropertySupport.Reflection(animation, float.class, "animationSpeed");
            animationSpeed.setName("animationSpeed");
            set.put(animationSpeed);
            
            PropertySupport.Reflection<Float> blendTime = new PropertySupport.Reflection(animation, float.class, "blendTime");
            blendTime.setName("blendTime");
            set.put(blendTime);
            
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        }
        return sheet;
    }
}
