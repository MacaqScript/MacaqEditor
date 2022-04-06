/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes;

import com.jme3.macaq.logic.AbstractLogicalComponent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.openide.actions.DeleteAction;
import org.openide.actions.PropertiesAction;
/**
 *
 * @author Rickard <neph1 @ github>
 */
public class LogicNode extends AbstractNode implements PropertyChangeListener{
    
    public LogicNode(AbstractLogicalComponent obj) {
        super (Children.LEAF, Lookups.singleton(obj));
        setDisplayName (obj.getName() != null ? obj.getName(): obj.getClass().getSimpleName());
        setShortDescription("This is a Macaq component");
    }
    
    public LogicNode() {
        super (Children.LEAF);
        setDisplayName ("Root");
    }

    @Override
    public SystemAction[] getActions() {
         SystemAction[] result = new SystemAction[]{
//            SystemAction.get(DeleteAction.class),
            SystemAction.get(PropertiesAction.class),};
        return result;
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        AbstractLogicalComponent obj = getLookup().lookup(AbstractLogicalComponent.class);
        set.setName("Default");
        try {

            Property indexProp = new PropertySupport.Reflection(obj, Integer.class, "getId", null);
            indexProp.setName("id");
            
            PropertySupport.Reflection nameProp = new PropertySupport.Reflection(obj, String.class, "name");
            nameProp.setName("name");
            nameProp.getPropertyEditor().addPropertyChangeListener(this);
            PropertySupport.Reflection enabledProp = new PropertySupport.Reflection(obj, boolean.class, "enabled");
            set.put(indexProp);
            set.put(nameProp);
            set.put(enabledProp);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        }
        sheet.put(set);
        return sheet;

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Logger.getLogger(LogicNode.class.getSimpleName()).log(Level.INFO, "Event {0} ", evt);
        firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }
    
}
