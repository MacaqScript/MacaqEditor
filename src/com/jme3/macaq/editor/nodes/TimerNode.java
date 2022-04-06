/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes;

import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.components.Timer;
import java.beans.IntrospectionException;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 * 
 * @author Rickard <neph1 @ github>
 */
public class TimerNode extends LogicNode{
    
    public TimerNode(AbstractLogicalComponent key) throws IntrospectionException {
        super(key);
        setShortDescription("Timer begins off and when start is called, it starts measuring time until triggerTime is reached. When it is, it will call onTrigger, stop and reset its time.");
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = sheet.get("Default");
        Timer obj = getLookup().lookup(Timer.class);
        try {
            PropertySupport.Reflection<Integer> triggerTime = new PropertySupport.Reflection(obj, int.class, "triggerTime");
            triggerTime.setName("triggerTime");
            set.put(triggerTime);
        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }
        sheet.put(set);
        return sheet;
    }
}
