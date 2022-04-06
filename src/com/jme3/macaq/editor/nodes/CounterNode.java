/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes;

import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.components.Counter;
import java.beans.IntrospectionException;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

/**
 *
 * @author Rickard
 */
public class CounterNode extends LogicNode{

    public CounterNode(AbstractLogicalComponent key) throws IntrospectionException {
        super(key);
        setShortDescription("The Counter counts upwards, once for each trigger input. When it reaches triggerOnCount it will send an outsignal.");
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = sheet.get("Default");
        Counter obj = getLookup().lookup(Counter.class);

        try {
            PropertySupport.Reflection<Integer> count = new PropertySupport.Reflection(obj, int.class, "count");
//            Property count = new PropertySupport.Reflection<>(obj, Integer.class, "getCount", "setCount");
            count.setName("count");
            PropertySupport.Reflection<Integer> trigger = new PropertySupport.Reflection(obj, int.class, "triggerOnCount");
            trigger.setName("triggerOnCount");
                
            set.put(count);
            set.put(trigger);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }

        sheet.put(set);
        return sheet;
    }
    
    
    
}
