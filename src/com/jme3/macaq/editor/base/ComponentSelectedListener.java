/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.base;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public interface ComponentSelectedListener {
    
    void onSelected(MacaqGuiComponent c);
    
    void onDisconnect(MacaqGuiComponent c);
    
    void onMove(MacaqGuiComponent c, int x, int y);
}
