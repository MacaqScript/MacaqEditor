/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.gui;

import com.jme3.macaq.editor.MacaqTopComponent;
import com.jme3.macaq.editor.base.LogicPanel;
import com.jme3.macaq.editor.palette.Utils;
import com.jme3.macaq.logic.AbstractLogicalComponent;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JInternalFrame;
import org.openide.loaders.DataNode;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class EditorFrame extends JInternalFrame {

    private Map displayers = new HashMap();
    private DragGestureRecognizer recognizer;
    private AddComponentListener listener;

    public EditorFrame() {
        //DND start
        //start listening for for d'n'd events in the dropPanel
        setDropTarget(new DropTarget(this, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                doDragOver(dtde);
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                doDragOver(dtde);
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                doDrop(dtde);
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
                //ignore
                //we don't really care whether it's a COPY or MOVE operation, let's treat both cases as the same
            }
        }));
        //DND end
        setLayout(null);
    }
    
    private void doDragOver(DropTargetDragEvent dtde) {
//    if( dtde.isDataFlavorSupported( Utils.MY_DATA_FLAVOR ) ) {
        //only accept object of our type
        dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
//    } else {
//        System.out.println("Rejecting!");
//        //reject everything else
//        dtde.rejectDrag();
//    }
    }

    private void doDrop(DropTargetDropEvent dtde) {
        //first check if we support this type of data
//    if( !dtde.isDataFlavorSupported( Utils.MY_DATA_FLAVOR ) ) {
//        dtde.rejectDrop();
//    }
        //accept the drop so that we can access the Transferable
        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        DataNode data = null;
        try {
            DataFlavor[] flavors = dtde.getTransferable().getTransferDataFlavors();
            data = (DataNode) dtde.getTransferable().getTransferData(flavors[0]);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (UnsupportedFlavorException ex) {
            ex.printStackTrace();
        }

        dtde.dropComplete(null != data);
        if (null != data) {
            String className = data.getName();

            try {
                Class c = Class.forName(className);
                if (c.getSuperclass().equals(Class.forName(AbstractLogicalComponent.class.getName())) || c.getSuperclass().getSuperclass().equals(Class.forName(AbstractLogicalComponent.class.getName()))) {
                    AbstractLogicalComponent component = (AbstractLogicalComponent) c.newInstance();
                    listener.addLogicGuiComponent(component, dtde.getLocation().x, dtde.getLocation().y);
                } else {
                    System.out.println("Drop target is not a component");
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }

        }
    }
    
    public interface AddComponentListener{
        
        void addLogicGuiComponent(AbstractLogicalComponent component, int x, int y);
    }
    
    public void addListener(AddComponentListener listener){
        this.listener = listener;
    }
}
