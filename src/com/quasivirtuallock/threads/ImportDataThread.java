package com.quasivirtuallock.threads;

import com.quasivirtuallock.concept.DashboardAdd;
import com.quasivirtuallock.concept.ImportScreen;
import com.quasivirtuallock.concept.QVL;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

public class ImportDataThread {
    
    private List<File> files;
    private DashboardAdd dashboard;
    private ImportScreen is;
    
    public ImportDataThread(List<File> files, DashboardAdd dashboard, ImportScreen is){
        this.files = files;
        this.dashboard = dashboard;
        this.is = is;
    }
    
    
    public void startThread(){
        
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                
                is.setAlwaysOnTop(true);
                
                dashboard.setEnabled(false);
                
                dashboard.mouseListenerEnabled = false;
                dashboard.addButton.setEnabled(false);
                dashboard.vaultButton.setEnabled(false);
                dashboard.settingsButton.setEnabled(false);
                
                float i = 1;
                
                for (File file : files){
                    
                    is.jl.setText(String.format("Processing %.0f of " + files.size(), i));
                    is.jpb.setValue(Integer.parseInt(String.format("%.0f", i / files.size() * 100)));
                    is.revalidate();
                    is.repaint();
                    
                    sleep();
                    
                    //compute file size
                    String filesize;
                    long q, r;
                    long b = 1024;
                    long len = file.length();
                    if (len > (b * b * b)){
                        filesize = len / (b *b * b) + "." + ("" + (len % (b *b * b))).substring(0, 1) + " Gb";
                    }
                    else if (len > (b * b)){
                        filesize = len / (b * b) + "." + ("" + (len % (b * b))).substring(0, 1) + " Mb";
                    }
                    else if (len > b){
                        filesize = len / b + "." + ("" + (len % b)).substring(0, 1) + " Kb";
                    }
                    else {
                        filesize = file.length() + " b ";
                    }

                    String filename = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1);
                    String filetype = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1);
                    String filedate = LocalDate.now().toString();

                    try{
                        PreparedStatement ps = QVL.CONNECTION.prepareStatement("INSERT INTO files VALUES (DEFAULT, ?, ?, ?, ?, ?)");
                        ps.setString(1, filename);
                        ps.setString(2, filetype);
                        ps.setString(3, filesize);
                        ps.setString(4, filedate);
                        ps.setBinaryStream(5, new FileInputStream(file.getAbsoluteFile()));
                        ps.execute();
                        //System.out.println("Successful query");
                    }
                    catch (Exception e){
                        //System.out.println("Unsuccessful query");
                        //e.printStackTrace();
                    }
                    
                    i++;
                    QVL.rowCount += 1;
                }
                is.setVisible(false);
                
                JOptionPane.showMessageDialog(dashboard, "Operation successful!\nItems: " + (int)(i - 1), "QVL Import files", JOptionPane.INFORMATION_MESSAGE);
                
                is.dispose();
                
                dashboard.setEnabled(true);
                dashboard.requestFocus();
                dashboard.revalidate();
                dashboard.repaint();
                
                dashboard.jPanel2.setEnabled(true);
                dashboard.addButton.setEnabled(true);
                dashboard.vaultButton.setEnabled(true);
                dashboard.settingsButton.setEnabled(true);
                dashboard.jProgressBar1.setValue(0);
                dashboard.jProgressBar1.setVisible(false);
                dashboard.mouseListenerEnabled = true;      
            }
        });
        
        t.start();
        t.setPriority(Thread.NORM_PRIORITY);
    
    }
    
    private void sleep(){
        
        try{
            Thread.sleep(10);
        }
        catch (Exception e){
        
        }
    }
    
}
