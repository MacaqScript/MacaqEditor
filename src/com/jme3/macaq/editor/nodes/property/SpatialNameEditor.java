/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.nodes.property;

import com.jme3.macaq.bridge.SceneComposerBridge;
import com.jme3.macaq.editor.util.AutoCompletion;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class SpatialNameEditor implements PropertyEditor, ExPropertyEditor, InplaceEditor.Factory {

    private String value = "";
    private static SceneComposerBridge bridge;

    public SpatialNameEditor() {
        bridge = new SceneComposerBridge();
    }

    @Override
    public void setValue(Object o) {
        value = (String) o;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isPaintable() {
        return false;
    }

    @Override
    public void paintValue(Graphics grphcs, Rectangle rctngl) {

    }

    @Override
    public String getJavaInitializationString() {
        return value;
    }

    @Override
    public String getAsText() {
        return value;
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException {
        value = string;
    }

    @Override
    public String[] getTags() {
        return null;
    }

    @Override
    public Component getCustomEditor() {
        return null;
    }

    @Override
    public boolean supportsCustomEditor() {
        return false;
    }

    private final PropertyChangeSupport supp = new PropertyChangeSupport(this);

    @Override
    public void addPropertyChangeListener(PropertyChangeListener pl) {
        supp.addPropertyChangeListener(pl);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener pl) {
        supp.removePropertyChangeListener(pl);
    }

    private PropertyEnv env;
    
    private InplaceEditor ed = null;

@Override
public InplaceEditor getInplaceEditor() {
    if (ed == null) {
        ed = new Inplace();
    }
    return ed;
}

    @Override
    public void attachEnv(PropertyEnv pe) {
        pe.registerInplaceEditorFactory(this);
    }

    private static class Inplace implements InplaceEditor {

        private PropertyEditor editor = null;
        private JComboBox comboBox;
        private AutoCompletion autoComplete;
//        private Button button;
        
        public Inplace(){
            String[] names = null;
            if(bridge.isActive()){
                names = bridge.getAllSpatialNames();
                comboBox = new JComboBox(names);
            } else {
//                bridge.init();
                comboBox = new JComboBox();
            }
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Bridge is active " + bridge.isActive() + " " + names ) ;
            comboBox.setEditable(true);
            autoComplete = new AutoCompletion(comboBox);
        }

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            reset();
        }

        @Override
        public JComponent getComponent() {
            return comboBox;
        }

        @Override
        public void clear() {
            //avoid memory leaks:
            editor = null;
            model = null;
        }

        @Override
        public Object getValue() {
            return autoComplete.getText();
        }

        @Override
        public void setValue(Object object) {
            comboBox.addItem(object);
            comboBox.setSelectedItem(object);
            
        }

        @Override
        public boolean supportsTextEntry() {
            return true;
        }

        @Override
        public void reset() {
            String text = (String) editor.getValue();
            if (text != null) {
                comboBox.setSelectedItem(text);
            }
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            return new KeyStroke[0];
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            return editor;
        }

        @Override
        public PropertyModel getPropertyModel() {
            return model;
        }

        private PropertyModel model;

        @Override
        public void setPropertyModel(PropertyModel propertyModel) {
            this.model = propertyModel;
        }

        @Override
        public boolean isKnownComponent(Component component) {
            return component == comboBox || comboBox.isAncestorOf(component);
        }

        @Override
        public void addActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }

        @Override
        public void removeActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }
    }
}
