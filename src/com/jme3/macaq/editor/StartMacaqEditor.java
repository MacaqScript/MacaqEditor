/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.MacaqLoader;
import com.jme3.asset.plugins.ClasspathLocator;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.macaq.filetype.mqs.FileTypeUtil;
import com.jme3.macaq.filetype.mqs.MqsDataObject;
import com.jme3.macaq.logic.script.MacaqScript;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "com.jme3.macaq.filetype.StartMacaqEditor"
)
@ActionRegistration(
        displayName = "#CTL_StartMacaqEditor"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 0),
    @ActionReference(path = "Loaders/application/macaq/Actions", position = 0)
})
@Messages("CTL_StartMacaqEditor=Edit Script")
public final class StartMacaqEditor implements ActionListener {

    private final MqsDataObject context;

    public StartMacaqEditor(MqsDataObject context) {
        this.context = context;
        MacaqScript obj = Lookup.getDefault().lookup(MacaqScript.class);

    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        DesktopAssetManager assetManager = new DesktopAssetManager(true);
        
//        for(Project p : OpenProjects.getDefault().getOpenProjects()){
//            assetManager.registerLocator(p.getProjectDirectory().getPath(), FileLocator.class);
//        }
        assetManager.addClassLoader(MacaqScript.class.getClassLoader());

        assetManager.registerLocator(null, FileLocator.class);
        assetManager.registerLocator(".", FileLocator.class);
        assetManager.registerLocator("./", FileLocator.class);
        assetManager.registerLocator(null, ClasspathLocator.class);
        assetManager.registerLoader(MacaqLoader.class, "mqs");
        BinaryImporter.getInstance().setAssetManager(assetManager);
        AssetInfo info = assetManager.locateAsset(new AssetKey(FileTypeUtil.getFileNameFromContext(context)));
        System.out.println("Info " + info);
//        MacaqScript macaq = (MacaqScript) assetManager.loadAsset(new AssetKey(FileTypeUtil.getFileNameFromContext(context)));
//        System.out.println("MacaqScript " + macaq);
//        
//        Logger.getLogger(getClass().getSimpleName()).log(Level.INFO, "macaq " + macaq);
//        final ProjectAssetManager manager = context.getLookup().lookup(ProjectAssetManager.class);
//        if (manager == null) {
//            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, "Failed to look up ProjectAssetManager");
//            return;
//        }
//        manager.clearCache();
        MacaqTopComponent gui = MacaqTopComponent.findInstance();
        if (gui.getApi() == null) {
            gui.setApi(new MacaqEditorApiImplementation(assetManager, context));
        } else {
            ((MacaqEditorApiImplementation) gui.getApi()).setContext(context);
            gui.getApi().load();
        }
        gui.load();
    }
}
