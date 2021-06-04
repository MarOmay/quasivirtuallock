package com.quasivirtuallock.threads;

import com.quasivirtuallock.concept.LoginScreen;
import com.quasivirtuallock.concept.Operations;
import static com.quasivirtuallock.concept.Operations.DRIVE;
import com.quasivirtuallock.concept.QVL;
import java.awt.Color;
import java.io.File;
import java.sql.ResultSet;

public class DetectorThread {
    
    private LoginScreen ls;
    
    public DetectorThread(LoginScreen ls){
        this.ls = ls;
    }
    
    public void startThread(){
        
        Thread t = new Thread(new Runnable (){
            
            @Override
            public void run() {
                
                //System.out.println("Detection started");
        
                while(true){
                    
                    File [] drives = File.listRoots();
                    for (File f: drives)
                            System.out.print("");
                    System.out.print("");
                    
                    if(File.listRoots().length > drives.length){
                
                        drives = File.listRoots();
                        
                        ls.statusLabel.setText("Drive " + drives[drives.length-1] + " detected.");
                        Operations.pause(300);
                        
                        DRIVE = drives[drives.length-1];
                        
                        try{
                            ls.statusLabel.setText("Scanning...");
                            Operations.pause(300);
                            ResultSet rs = QVL.STATEMENT.executeQuery("SELECT id, userid, filename FROM keys ORDER BY id");
                            while(rs.next()){
                                try{
                                    String fname = rs.getString("filename");
                                    int userid = rs.getInt("userid");
                                    int id = rs.getInt("id");
                                    
                                    File f = new File(DRIVE + fname);
                                    if (f.exists()){
                                        ls.statusLabel.setText("Key found!");
                                        Operations.pause(300);
                                        String tempKey = new Operations(f.getAbsolutePath()).getKey();
                                        ResultSet rsdb = QVL.STATEMENT.executeQuery("SELECT utf FROM keys WHERE filename = '" + fname + "' AND id = " + id);
                                        if (rsdb.next()){
                                            if(tempKey.equals(rsdb.getString("utf"))){
                                                ls.statusLabel.setText("Analyzing key...");
                                                Operations.pause(300);
                                            
                                                QVL.ID = userid + "";
                                                try{
                                                    rs = QVL.STATEMENT.executeQuery("SELECT name FROM credentials WHERE id = " + QVL.ID);
                                                    if(rs.next()){
                                                        QVL.NAME = rs.getString("name");
                                                    }
                                                }
                                                catch(Exception e){
                                                    //System.out.println("Cannot fetch QVL.NAME");
                                                }

                                                ls.useridTF.setText(QVL.ID);
                                                ls.keycodePF.setText("password");
                                                ls.statusLabel.setText("Access granted");
                                                ls.statusLabel.setForeground(Color.green);
                                                Operations.pause(500);

                                                QVL.physicalKey = true;

                                                //System.out.println("Detection ended");

                                                if (!QVL.DatabaseKeyThreadRunning){
                                                    new DatabaseKeyThread(ls).startThread();
                                                }

                                                break;
                                            }else{
                                                //System.out.println("Doesn't match");
                                                ls.statusLabel.setText("Key mismatch!");
                                            }
                                        }
                                        rsdb.close();
                                    }
                                    else{
                                        //System.out.println("Doesn't exist");
                                        ls.statusLabel.setText("Searching...");
                                        Operations.pause(300);
                                    }
                                }
                                catch(Exception ex){
                                    ls.statusLabel.setForeground(Color.red);
                                    ls.statusLabel.setText("Operation failed! (t.dt,ex)");
                                    //ex.printStackTrace();
                                }
                            }
                            rs.close();
                        }
                        catch(Exception e){
                            //e.printStackTrace();
                        }
                        
                        
                    }
                    else if (QVL.virtualKey){
                        //System.out.println("Detection ended");
                        break;
                    }
                }
            }
        });
        
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
        
    }
    
}
