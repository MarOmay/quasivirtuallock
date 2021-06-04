package com.quasivirtuallock.threads;

import com.quasivirtuallock.concept.DashboardAdd;
import com.quasivirtuallock.concept.LoadingScreen;
import com.quasivirtuallock.concept.Operations;
import com.quasivirtuallock.concept.QVL;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;



public class LoadDatabaseThread {

    private DashboardAdd dashboard;
    
    public LoadDatabaseThread(DashboardAdd dashboard){
        this.dashboard = dashboard;
    }
    
    public void startThread(){
        
        Thread t = new Thread(new Runnable(){
            
            @Override
            public void run() {
                
                dashboard.mouseListenerEnabled = false;
                
                dashboard.jTable1 = new JTable();
        
                dashboard.jTable1.setFont(new Font("Helvetica", Font.PLAIN, 12));

                String[][] rec = generateData();
                
                QVL.tableData = rec;

                dashboard.jTable1.setModel(new javax.swing.table.DefaultTableModel(
                    rec,
                    new String [] {
                        "No.","Filename", "Type", "Size", "Date"
                    }
                ) {
                    Class[] types = new Class [] {
                        java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types [columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit [columnIndex];
                    }
                });

                dashboard.jTable1.setColumnSelectionAllowed(false);
                dashboard.jTable1.getTableHeader().setReorderingAllowed(false);
                JScrollPane jScrollPane1 = new JScrollPane();
                jScrollPane1.setViewportView(dashboard.jTable1);

                dashboard.jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                
                if (dashboard.jTable1.getColumnModel().getColumnCount() > 0) {
                    
                    DefaultTableCellRenderer rendererRight = new DefaultTableCellRenderer();
                    rendererRight.setHorizontalAlignment(JLabel.RIGHT);
                    
                    DefaultTableCellRenderer rendererCenter = new DefaultTableCellRenderer();
                    rendererCenter.setHorizontalAlignment(JLabel.CENTER);
                    
                    dashboard.jTable1.getColumnModel().getColumn(0).setResizable(true);
                    dashboard.jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
                    dashboard.jTable1.getColumnModel().getColumn(0).setMaxWidth(30);
                    dashboard.jTable1.getColumnModel().getColumn(0).setCellRenderer(rendererRight);
                    dashboard.jTable1.getColumnModel().getColumn(1).setResizable(true);
                    dashboard.jTable1.getColumnModel().getColumn(1).setPreferredWidth(230);
                    dashboard.jTable1.getColumnModel().getColumn(2).setResizable(true);
                    dashboard.jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);
                    dashboard.jTable1.getColumnModel().getColumn(2).setCellRenderer(rendererCenter);
                    dashboard.jTable1.getColumnModel().getColumn(2).setMaxWidth(50);
                    dashboard.jTable1.getColumnModel().getColumn(3).setResizable(true);
                    dashboard.jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);
                    dashboard.jTable1.getColumnModel().getColumn(3).setCellRenderer(rendererRight);
                    dashboard.jTable1.getColumnModel().getColumn(4).setResizable(false);
                    dashboard.jTable1.getColumnModel().getColumn(4).setPreferredWidth(30);
                    dashboard.jTable1.getColumnModel().getColumn(4).setCellRenderer(rendererCenter);
                }
                
                dashboard.jTable1.addMouseListener(new MouseAdapter(){
            
                    public void mousePressed(MouseEvent me){
                        JTable t = (JTable) me.getSource();
                        Point p = me.getPoint();
                        int row = t.rowAtPoint(p);
                        if (me.getClickCount() == 2 && t.getSelectedRow() != -1){
                            new Operations(dashboard).fileOperation(QVL.tableData[t.getSelectedRow()]);
                        }
                    }
                 });
                dashboard.jTable1.setAutoCreateRowSorter(true);
                dashboard.jsp = new JScrollPane(dashboard.jTable1);
                dashboard.jPanel2.add(dashboard.jsp, BorderLayout.CENTER);
                dashboard.revalidate();
                dashboard.revalidate();
                dashboard.revalidate();
                dashboard.repaint();
                dashboard.mouseListenerEnabled = true;
            }
        });
        
        t.start();
        t.setPriority(Thread.MAX_PRIORITY);
        
    }
    
    private String[][] generateData(){
        LoadingScreen loading = new LoadingScreen();
        loading.setLocation(dashboard.getX() + 60 + dashboard.getWidth() / 2 - loading.getWidth() / 2,
                            dashboard.getY() + 60 + dashboard.getHeight() / 2 - loading.getHeight() / 2);
        loading.setVisible(true);
        
        return new Operations(loading, dashboard).processData(new Operations(loading, dashboard).fetchData());
    }
    
}
