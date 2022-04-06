/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.macaq.base.ScriptAPIInterface;
import com.jme3.macaq.filetype.mqs.FileTypeUtil;
import com.jme3.macaq.filetype.mqs.MqsDataObject;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.script.MacaqScript;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.spi.actions.AbstractSavable;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class MacaqEditorApiImplementation implements ScriptAPIInterface{

    
    private String name;
    private ScriptContainer scriptContainer;
    private AssetManager assetManager;
    
    public MacaqEditorApiImplementation(){
        
    }
    
    public MacaqEditorApiImplementation(AssetManager assetManager, MqsDataObject context){
        this();
        this.assetManager = assetManager;
        name = FileTypeUtil.getFileNameFromContext(context);
        System.out.println("MacaqEditorApiImplementation " + name);
        load();
    }
    
    public void setContext(MqsDataObject context){
        name = FileTypeUtil.getFileNameFromContext(context);
        System.out.println("setContext " + name);
    }
    
    @Override
    public boolean addComponent(AbstractLogicalComponent component) {
        scriptContainer.script.addComponent(component);
        scriptContainer.modify();
        return true;
    }

    @Override
    public boolean removeComponent(int id) {
        scriptContainer.script.removeComponent(id);
        scriptContainer.modify();
        return true;
    }

    @Override
    public void save() {
    }

    @Override
    public void load() {
        scriptContainer = new ScriptContainer(new MacaqScript());
        try {
            scriptContainer.script = (MacaqScript) assetManager.loadAsset(new AssetKey(name));
            Logger.getLogger(MacaqEditorApiImplementation.class.getSimpleName()).log(Level.INFO, "Macaq script loaded " + name);
        }catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(MacaqEditorApiImplementation.class.getSimpleName()).log(Level.INFO, "Failed to load script" + name);
        }
        if(scriptContainer.script == null){
            scriptContainer.script = new MacaqScript("Macaq Script");
        }
    }
    
    public MacaqScript getScript(){
        return scriptContainer.script;
    }
    
    public ScriptContainer getScriptContainer(){
        return scriptContainer;
    }

    @Override
    public boolean moveComponent(int id, int x, int y) {
        AbstractLogicalComponent component = scriptContainer.script.getScriptMap().get(id);
        if(component != null){
            component.setX(x);
            component.setY(y);
            scriptContainer.modify();
            return true;
        }
        Logger.getLogger(MacaqEditorApiImplementation.class.getSimpleName()).log(Level.INFO, "Failed to find component with id " + id);
        return false;
    }

    @Override
    public AbstractLogicalComponent getComponent(int i) {
        return scriptContainer.script.getScriptMap().get(i);
    }

    @Override
    public boolean removeConnection(int id) {
        scriptContainer.script.removeConnection(id);
        scriptContainer.modify();
        return true;
    }

    @Override
    public boolean addConnection(int from, int to) {
        scriptContainer.script.addConnection(from, to);
        scriptContainer.modify();
        return true;
    }
    
    @Override
    public boolean equals(Object o){
        return scriptContainer.equals(o);
    }

    private class ScriptContainer extends AbstractSavable {
        
        MacaqScript script;
        
        public ScriptContainer(){
            register();
        }

        private ScriptContainer(MacaqScript macaqScript) {
            this.script = macaqScript;
            register();
        }
        
        @Override
        protected String findDisplayName() {
            return script.getName();
        }

        @Override
        protected void handleSave() throws IOException {
            BinaryExporter exporter = BinaryExporter.getInstance();
            exporter.save(script, new File(name));
            Logger.getLogger(MacaqEditorApiImplementation.class.getSimpleName()).log(Level.INFO, "Saved script " + name);
            unregister();
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof ScriptContainer){
                if(((ScriptContainer)o).script.equals(this.script)){
                    return true;
                }
            }
            return false;
        }
        
        public void modify(){
            register();
        }

        @Override
        public int hashCode() {
            return script.hashCode();
        }
  }
}
