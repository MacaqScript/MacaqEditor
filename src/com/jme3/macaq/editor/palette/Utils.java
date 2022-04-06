/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.macaq.editor.palette;

import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.util.Lookup;

/**
 *
 * @author Rickard <neph1 @ github>
 */
public class Utils {

    public static final DataFlavor MY_DATA_FLAVOR = DataFlavor.allHtmlFlavor;//new DataFlavor(MyItemData.class, "My Item Data");
    private static PaletteController thePalette;

    public static PaletteController getPalette() {
        //create the palette
        if (null == thePalette) {
            try {
                //DND start
                //use custom DragAndDropHandler when creating the palette so that our custom
                //dataflavor gets added when an item is being dragged from the palette:
                thePalette = PaletteFactory.createPalette("MacaqPalette", new MyActions());//, null, new MyDnDHandler() );
                //DND end
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return thePalette;
    }

    private static class MyActions extends PaletteActions {

        @Override
        public Action[] getImportActions() {
            return null;
        }

        @Override
        public Action[] getCustomPaletteActions() {
            return null;
        }

        @Override
        public Action[] getCustomCategoryActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action[] getCustomItemActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action getPreferredAction(Lookup lookup) {
            return null;
        }

    }
}
