/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public interface MacaqGuiApi {
    
    
    /**
     * 
     * @param type type of logic item
     * @param x x position
     * @param y int y position
     * @return id
     */
    int addItem(Class type, int x, int y);
    
    /**
     * 
     * @param id id of item
     */
    void removeItem(int id);
    
    void moveItem(int id, int x, int y);
    
    /**
     * adds a connection between an out connection and an in connection
     * @param fromId
     * @param toId 
     * @return true if connection created succesfully
     */
    boolean addConnection(int fromId, int toId);
}
