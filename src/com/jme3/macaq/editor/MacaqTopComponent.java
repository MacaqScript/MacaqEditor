/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor;

import com.jme3.macaq.base.LogicConnection;
import com.jme3.macaq.editor.util.ComponentUtil;
import com.jme3.macaq.base.LogicInConnection;
import com.jme3.macaq.base.LogicOutConnection;
import com.jme3.macaq.base.ScriptAPIInterface;
import com.jme3.macaq.bridge.SceneComposerBridge;
import com.jme3.macaq.editor.base.LogicPanel;
import com.jme3.macaq.editor.gui.ConnectionMainPanel;
import com.jme3.macaq.editor.base.ConnectionPanel;
import com.jme3.macaq.editor.base.MacaqGuiComponent;
import com.jme3.macaq.editor.base.ComponentSelectedListener;
import com.jme3.macaq.editor.explorer.ScriptExplorerTopComponent;
import com.jme3.macaq.editor.gui.EditorFrame;
import com.jme3.macaq.editor.palette.Utils;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import com.jme3.macaq.logic.components.Start;
import com.jme3.macaq.logic.script.ScriptConstants;
import com.jme3.util.IntMap;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JMenuItem;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.windows.WindowManager;
/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.jme3.macaq.editor//Macaq//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "MacaqTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "com.jme3.macaq.editor.MacaqTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_MacaqAction",
        preferredID = "MacaqTopComponent"
)
@Messages({
    "CTL_MacaqAction=Macaq",
    "CTL_MacaqTopComponent=Macaq Window",
    "HINT_MacaqTopComponent=This is a Macaq window"
})
public final class MacaqTopComponent extends TopComponent implements ComponentSelectedListener, LookupListener, EditorFrame.AddComponentListener{
    
    private static final String PREFERRED_ID = "MacaqTopComponent";
    private int mouseX;
    private int mouseY;
    private LogicPanel selectedComponent;
    private ConnectionPanel fromConnection;
    private ConnectionPanel toConnection;
    private Lookup.Result<MacaqGuiComponent> result = null;
    private final InstanceContent content = new InstanceContent();
    private static MacaqTopComponent instance;
    private AbstractNode topNode;
    private ScriptAPIInterface api;
    private final int connectionPanelYOffset = 20;
    
    private int pressedDownX;
    private int pressedDownY;
    private final EditorHelper editorHelper;
    private final SceneComposerBridge sceneComposerBridge;
    
    public MacaqTopComponent() {
        initComponents();
        editorHelper = new EditorHelper();
        macaqEditorFrame.add(componentsMenu);
        ((EditorFrame)macaqEditorFrame).addListener(this);
        
        ((javax.swing.plaf.basic.BasicInternalFrameUI)macaqEditorFrame.getUI()).setNorthPane(null);
        initMenu();
        setName(Bundle.CTL_MacaqTopComponent());
        setToolTipText(Bundle.HINT_MacaqTopComponent());
//        Event obj = new Event();
        result = Utilities.actionsGlobalContext().lookupResult(MacaqGuiComponent.class);
//        associateLookup(Lookups.singleton(new LogicPanel()));
        associateLookup( Lookups.fixed( new Object[] {Utils.getPalette(), content} ) );
//        associateLookup (new AbstractLookup (content)); 
        
        sceneComposerBridge = new SceneComposerBridge();
        
    }
    
    public static synchronized MacaqTopComponent getDefault() {
        if (instance == null) {
            instance = new MacaqTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the SceneExplorerTopComponent instance. Never call
     * {@link #getDefault} directly!
     * @return 
     */
    public static synchronized MacaqTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            return getDefault();
        }
        if (win instanceof MacaqTopComponent) {
            return (MacaqTopComponent) win;
        }
        return getDefault();
    }
    
    public void cleanup(){
        for(Component c : macaqEditorFrame.getContentPane().getComponents()){
            if(c instanceof LogicPanel){
                macaqEditorFrame.getContentPane().remove(c);
                content.remove(c);
            }
            
        }
        ((ConnectionMainPanel) connectionPanel).clear();
        macaqEditorFrame.repaint();
        ScriptConstants.reset();
    }
    
    public void load(){
        cleanup();
        topNode = new AbstractNode(Children.create(new ChildFactory<AbstractNode>() {
            @Override
            protected boolean createKeys(List<AbstractNode> list) {
                return true;
            }
        }, true));
        topNode.setDisplayName("Script");
        topNode.setName("Script");
        
        ScriptExplorerTopComponent.findInstance().setTopNode(topNode);
        IntMap<AbstractLogicalComponent> scriptMap = ((MacaqEditorApiImplementation)api).getScript().getScriptMap();
        if(scriptMap.size() == 0){// FIXME: temp solution until file instance works
            Start start = new Start();
            start.setX(300);
            start.setY(200);
            api.addComponent(start);
        }
        for(IntMap.Entry<AbstractLogicalComponent> comp : scriptMap){
            addVisualComponent(comp.getValue(), comp.getValue().getX(), comp.getValue().getY());
        }
        initMenu();
    }
    
    
    @Override
    public void addLogicGuiComponent(AbstractLogicalComponent component, int x, int y){
        if(api.addComponent(component)){
            addVisualComponent(component, x, y);
        }
    }
    
    private void addVisualComponent(AbstractLogicalComponent component, int x, int y){
        LogicPanel p = ComponentUtil.setupLogicPanel(MacaqTopComponent.this, component, x, y, (ConnectionMainPanel) connectionPanel);
        macaqEditorFrame.add(p);
        macaqEditorFrame.requestFocus();
        content.add(p);
        p.repaint();
        topNode.getChildren().add(new Node[]{p.getNode()});
    }
    
    private void initMenu(){
        
        componentsMenu.removeAll();
        ActionListener menuListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent ae) {
                AbstractLogicalComponent component = null;
                try {
//                    Class c = Class.forName(ae.getActionCommand());
                    Class c = editorHelper.loadComponent(ae.getActionCommand());
                    component = (AbstractLogicalComponent) c.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
                if(component != null){
                    component.setX(mouseX);
                    component.setY(mouseY);
                    addLogicGuiComponent(component, mouseX, mouseY);
                }
                macaqEditorFrame.repaint();
            }
          };
        List<Class<? extends AbstractLogicalComponent>> components = editorHelper.getComponents();
        for(Class c: components){
            System.out.println("Component: " + c.getName());
            JMenuItem item = new JMenuItem(c.getSimpleName());
            item.setActionCommand(c.getName());
            item.addActionListener(menuListener);
            componentsMenu.add(item);
            
        }  
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        componentsMenu = new javax.swing.JPopupMenu();
        jScrollPane1 = new javax.swing.JScrollPane();
        macaqEditorFrame = new EditorFrame();
        connectionPanel = new ConnectionMainPanel();

        macaqEditorFrame.setComponentPopupMenu(componentsMenu);
        macaqEditorFrame.setPreferredSize(new java.awt.Dimension(4000, 4000));
        macaqEditorFrame.setVisible(true);
        macaqEditorFrame.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                macaqEditorFrameMouseMoved(evt);
            }
        });
        macaqEditorFrame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                macaqEditorFrameMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout connectionPanelLayout = new javax.swing.GroupLayout(connectionPanel);
        connectionPanel.setLayout(connectionPanelLayout);
        connectionPanelLayout.setHorizontalGroup(
            connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3989, Short.MAX_VALUE)
        );
        connectionPanelLayout.setVerticalGroup(
            connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 3970, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout macaqEditorFrameLayout = new javax.swing.GroupLayout(macaqEditorFrame.getContentPane());
        macaqEditorFrame.getContentPane().setLayout(macaqEditorFrameLayout);
        macaqEditorFrameLayout.setHorizontalGroup(
            macaqEditorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(connectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        macaqEditorFrameLayout.setVerticalGroup(
            macaqEditorFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(connectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(macaqEditorFrame);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1191, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void macaqEditorFrameMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_macaqEditorFrameMouseMoved
        mouseX = evt.getX();
        mouseY = evt.getY();
        if(fromConnection != null || toConnection != null){
            ((ConnectionMainPanel)connectionPanel).shouldDrawLine(true);
            ((ConnectionMainPanel)connectionPanel).setLineCoords(pressedDownX, pressedDownY, mouseX - connectionPanel.getX(), mouseY - connectionPanelYOffset); // FIXME: magic number
            ((ConnectionMainPanel)connectionPanel).repaint();
        }
    }//GEN-LAST:event_macaqEditorFrameMouseMoved

    private void macaqEditorFrameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_macaqEditorFrameMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3){
            fromConnection = null;
            toConnection = null;
            ((ConnectionMainPanel)connectionPanel).shouldDrawLine(false);
            ((ConnectionMainPanel)connectionPanel).repaint();
            sceneComposerBridge.getSelectedSpatialName();
        }
    }//GEN-LAST:event_macaqEditorFrameMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu componentsMenu;
    private javax.swing.JPanel connectionPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JInternalFrame macaqEditorFrame;
    // End of variables declaration//GEN-END:variables
    
    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void onSelected(MacaqGuiComponent c) {
        if(c instanceof LogicPanel){
            if(selectedComponent != null){
                selectedComponent.setSelected(false);
            }
            
            selectedComponent = (LogicPanel) c;
            setActivatedNodes(new Node[]{selectedComponent.getNode()});
            content.set(Collections.singleton (selectedComponent), null);
            ScriptExplorerTopComponent.findInstance().setSelectedNode((AbstractNode) c.getNode());
            connectionPanel.repaint();
        } else if(c instanceof ConnectionPanel){
            if(fromConnection == null && !((ConnectionPanel) c).isInConnection()){
                fromConnection = (ConnectionPanel) c;
            } else if(fromConnection != null && ((ConnectionPanel) c).isInConnection()){
                toConnection = (ConnectionPanel) c;
            }
            ((ConnectionMainPanel)connectionPanel).shouldDrawLine(true);
            pressedDownX = ((ConnectionPanel) c).getAbsX();
            pressedDownY = ((ConnectionPanel) c).getAbsY();
        }
        if(fromConnection != null && toConnection != null){
            if(api.addConnection(fromConnection.getConnection().getId(), toConnection.getConnection().getId())){
                ((LogicOutConnection)fromConnection.getConnection()).attachChild((LogicInConnection) toConnection.getConnection());
                fromConnection = null;
                toConnection = null;
                 ((ConnectionMainPanel)connectionPanel).shouldDrawLine(false);
                connectionPanel.repaint();
            }
        }
    }

    @Override
    public void onDisconnect(MacaqGuiComponent c) {
        if(c instanceof ConnectionPanel){
            LogicConnection connection = ((ConnectionPanel) c).getConnection();
            if(api.removeConnection(connection.getId())){
                if(connection instanceof LogicInConnection){
                    System.out.println("Please select the out connection!");
                } else{
                    ((LogicOutConnection)connection).getConnections().clear();
                }
                connectionPanel.repaint();
            }
            
        }
    }

    @Override
    public void componentOpened() {
        result.addLookupListener (this);
    }
    
    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
    }
    
    @Override
    public void resultChanged(LookupEvent lookupEvent) {
        Collection<? extends MacaqGuiComponent> allEvents = result.allInstances();
        if (!allEvents.isEmpty()) {
        }
    }

    public ScriptAPIInterface getApi() {
        return api;
    }

    public void setApi(ScriptAPIInterface api) {
        System.out.println("setting api " + api);
        this.api = api;
    }

    @Override
    public void onMove(MacaqGuiComponent c, int x, int y) {
        api.moveComponent(((LogicPanel)c).getId(), x, y);
        connectionPanel.repaint();
        
    }
    
    public void removeLogicComponent(MacaqGuiComponent c){
        if(c instanceof LogicPanel && api.removeComponent(c.getId())){
            LogicPanel p = (LogicPanel) c;
            macaqEditorFrame.remove(p);
            content.remove(p);
            p.repaint();
            topNode.getChildren().remove(new Node[]{p.getNode()});
        }
    }
}
