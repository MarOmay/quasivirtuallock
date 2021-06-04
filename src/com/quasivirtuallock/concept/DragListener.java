
package com.quasivirtuallock.concept;

import com.quasivirtuallock.threads.ImportDataThread;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;
import javax.swing.JOptionPane;

public class DragListener implements DropTargetListener{
    
    private DashboardAdd dashboard;
    
    public DragListener(DashboardAdd dashboard){
        this.dashboard = dashboard;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        
        
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        
        dtde.acceptDrop(DnDConstants.ACTION_COPY);
        
        Transferable t = dtde.getTransferable();
        
        DataFlavor[] df = t.getTransferDataFlavors();
        
        for (DataFlavor flavor : df){
        
            try{
                
                if (flavor.isFlavorJavaFileListType()){
                    
                    List<File> files = (List<File>) t.getTransferData(flavor);
                    
                    ImportScreen is = new ImportScreen();
                    
                    is.setLocation(dashboard.getX() + dashboard.getWidth() / 2 - is.getWidth() / 2,
                                   dashboard.getY() + dashboard.getHeight() / 2 - is.getHeight() / 2);
                
                    is.jpb.setValue(0);
                    is.jl.setText("Processing...");
                
                    is.setVisible(true);
                    
                    new ImportDataThread(files, dashboard, is).startThread();
                }
                
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(null, "Unsupported operation!");
            }
            
        }
        
        
    }
    
}
