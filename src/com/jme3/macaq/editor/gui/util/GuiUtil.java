/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.gui.util;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class GuiUtil {
    
    public static int[][] generatePolyLine(int x1, int y1, int x2, int y2){
        int[][] coords = null;
        if(x2 > x1){ // simple
            int split = x1 + (x2 - x1) / 2;// + (Math.max(y1, y2) - Math.min(y1, y2)) / 2;
            coords = new int[][]{
                {x1, split, split, x2},
                {y1, y1, y2, y2}
            };
        } else {
            int splitX1 = x1 + 30;
            int splitX2 = x2 - 30;
            int splitY = Math.min(y1, y2) + (Math.max(y1, y2) - Math.min(y1, y2)) / 2;
            coords = new int[][]{
                {x1, splitX1, splitX1, splitX2, splitX2, x2},
                {y1, y1, splitY, splitY, y2, y2}
            };
        }
        return coords;
    }
}
