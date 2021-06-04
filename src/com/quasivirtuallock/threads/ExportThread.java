package com.quasivirtuallock.threads;

import com.quasivirtuallock.concept.FileMenu;
import com.quasivirtuallock.concept.QVL;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class ExportThread {
    
    private FileMenu fm;
    private String name;
    private String address;
    
    public ExportThread(FileMenu fm, String name, String address){
        this.fm = fm;
        this.name = name;
        this.address = address;
    }
    
    public void startThread(){
        
        Thread t = new Thread(new Runnable(){
        
            @Override
            public void run(){
                
                fm.closeLabel.setEnabled(false);
                fm.exportLabel.setEnabled(false);
                fm.deleteLabel.setEnabled(false);
                fm.hoverable = false;
                
                fm.exportingLabel.setVisible(true);
                fm.percentLabel.setVisible(true);
                
                try {
                    
                    ResultSet rs = QVL.STATEMENT.executeQuery("SELECT file FROM files WHERE name = '" + name + "'");
                    byte buff[] = new byte[1024];
                    float i = 0;
                    while (rs.next()){
                        
                        Blob blob = rs.getBlob(1);
                        
                        File f = new File(address + "\\" + name);
                        
                        InputStream is = blob.getBinaryStream();
                        
                        FileOutputStream fos = new FileOutputStream(f);
                        
                        for (int b = is.read(buff); b != -1; b = is.read(buff)){
                            fos.write(buff, 0, b);
                            progressUpdate(Float.parseFloat(String.format("%.0f", i / (float) blob.length() * 100)));
                            i += 1024;
                        }
                        //System.out.println("Exiting inner loop" + blob.length());
                        
                        is.close();
                        fos.close();
                        rs.close();
                        JOptionPane.showMessageDialog(fm, "File exported successffuly!", "QVL", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                    //System.out.println("Exiting outer loop");
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(null, "Export failed.", "QVL", JOptionPane.ERROR_MESSAGE);
                }
                
                //System.out.println("Exiting try");
                
                fm.exportingLabel.setVisible(false);
                fm.percentLabel.setVisible(false);

                fm.closeLabel.setEnabled(true);
                fm.exportLabel.setEnabled(true);
                fm.deleteLabel.setEnabled(true);
                fm.hoverable = true;

            }
        
        });
        
        t.start();
        t.setPriority(Thread.MAX_PRIORITY);
        
    }
    
    private void progressUpdate(float i){
            fm.percentLabel.setText(String.format("%.0f" + "%%", i));
            fm.revalidate();
            fm.repaint();
            try{
                //Thread.sleep(25);
            }
            catch (Exception e){

            }
        
    }
    
}
