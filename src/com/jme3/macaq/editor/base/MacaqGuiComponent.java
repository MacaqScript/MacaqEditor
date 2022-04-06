/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.base;

import org.openide.nodes.Node;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public interface MacaqGuiComponent {
    
    int getId();
    
    void setId(int id);
    
    void setSelectedListener(ComponentSelectedListener listener);
    
    Node getNode();
    
    void setNode(Node node);
}
