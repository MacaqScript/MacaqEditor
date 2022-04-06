/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor;

import com.jme3.macaq.logic.AbstractLogicalComponent;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class EditorHelperTest {
    
    public EditorHelperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getComponents method, of class EditorHelper.
     */
    @Test
    public void testGetComponents() {
        System.out.println("getComponents");
        EditorHelper instance = new EditorHelper();
        List<Class<? extends AbstractLogicalComponent>> expResult = null;
//        List<Class<? extends AbstractLogicalComponent>> result = instance.getComponents();
//        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
