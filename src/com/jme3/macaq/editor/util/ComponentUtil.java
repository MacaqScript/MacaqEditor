/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.util;

import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.editor.base.LogicPanel;
import com.jme3.macaq.editor.base.ConnectionPanel;
import com.jme3.macaq.editor.base.ComponentSelectedListener;
import com.jme3.macaq.editor.nodes.LogicNode;
import com.jme3.macaq.editor.gui.ConnectionMainPanel;
import com.jme3.macaq.editor.nodes.CounterNode;
import com.jme3.macaq.editor.nodes.PlayEffectNode;
import com.jme3.macaq.editor.nodes.TimerNode;
import com.jme3.macaq.editor.nodes.TriggerNode;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.components.Counter;
import com.jme3.macaq.logic.components.PlayEffect;
import com.jme3.macaq.logic.components.Timer;
import com.jme3.macaq.logic.components.Trigger;
import java.awt.Component;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class ComponentUtil {
    
    public static LogicPanel setupLogicPanel(ComponentSelectedListener listener, AbstractLogicalComponent type, int mouseX, int mouseY, ConnectionMainPanel connectionPanel){
        LogicPanel p = new LogicPanel(type);
        p.setSelectedListener(listener);
        type.forceAddConnections();
        type.setX(mouseX);
        type.setY(mouseY);
        p.setLocation(mouseX, mouseY);
        JPanel inPanel = null;
        JPanel outPanel = null;
        for(Component c : p.getComponents()){
            if(c.getName().equals("inConnectionsPanel")){
                inPanel = (JPanel) c;
            } else if(c.getName().equals("outConnectionsPanel")){
                outPanel = (JPanel) c;
            }
        }
        
        int inPanelSize = 0;
        int outPanelSize = 0;
        if(inPanel != null){
            for(LogicInConnection connection: type.getInConnections().values()){
                ConnectionPanel conPan = new ConnectionPanel(connection);
                conPan.setSelectedListener(listener);
                connectionPanel.addLogic(conPan);
                ((JLabel)conPan.getComponent(1)).setText(connection.getName());
                conPan.setInConnection(true);
                inPanel.add(conPan);
            }
            
            inPanelSize = 50 + Math.max(0, type.getInConnections().size() - 3) * 15;
        }
        if(outPanel != null){
            for(LogicOutConnection connection: type.getOutConnections().values()){
                ConnectionPanel conPan = new ConnectionPanel(connection);
                conPan.setSelectedListener(listener);
                connectionPanel.addLogic(conPan);
                ((JLabel)conPan.getComponent(1)).setText(connection.getName());
                outPanel.add(conPan);
            }
            outPanelSize = 50 + Math.max(0, type.getOutConnections().size() - 3) * 15;
        }
        inPanel.setSize(inPanel.getWidth(), Math.max(inPanelSize, outPanelSize));
        outPanel.setSize(inPanel.getSize());
        p.setSize(120, inPanel.getHeight() + 25);
        p.setNode(createNodeForKey(type));
        p.getNode().addPropertyChangeListener(p);
        return p;
    }
    
    private static LogicNode createNodeForKey(AbstractLogicalComponent key) {
        LogicNode result = null;
        if(key instanceof Counter){
            try {
                result = new CounterNode(key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if(key instanceof PlayEffect){
            try {
                result = new PlayEffectNode(key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if(key instanceof Trigger){
            try {
                result = new TriggerNode(key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else if(key instanceof Timer){
            try {
                result = new TimerNode(key);
            } catch (IntrospectionException ex) {
                Exceptions.printStackTrace(ex);
            }
        }else {
            try {
                // fall back and convencience code. Will try to look for a suitable Node by name of component
                result = (LogicNode)Class.forName("com.jme3.macaq.editor.nodes." + key.getClass().getSimpleName() + "Node").getConstructor(AbstractLogicalComponent.class).newInstance(key);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(ComponentUtil.class.getSimpleName()).log(Level.SEVERE, "Couldn''t find Node for component {0}", key.getClass().getSimpleName());
            } finally {
                if(result == null){
                    result = new LogicNode(key);
                }
            }
            
            
        }
//        result.setDisplayName(""+key.getName());
        return result;
    }
    
}
