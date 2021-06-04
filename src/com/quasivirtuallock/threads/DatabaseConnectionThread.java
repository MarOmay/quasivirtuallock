package com.quasivirtuallock.threads;

import com.quasivirtuallock.concept.LoadingScreen;
import com.quasivirtuallock.concept.LoginScreen;
import com.quasivirtuallock.concept.Operations;
import com.quasivirtuallock.concept.QVL;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class DatabaseConnectionThread {
    
    private LoginScreen ls;
    
    public DatabaseConnectionThread(LoginScreen ls){
        this.ls = ls;
        
    }

    public void startThread() {
        
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                
                LoadingScreen loading = new LoadingScreen();
                loading.setAlwaysOnTop(true);
                loading.setVisible(true);
                
                try{
                    QVL.CONNECTION = Operations.getConnection();
                    try {
                        QVL.STATEMENT = QVL.CONNECTION.createStatement();
                        try {
                            ResultSet rs = QVL.STATEMENT.executeQuery("SELECT name FROM files");
                            while (rs.next())
                                QVL.rowCount += 1;
                            QVL.dbReady = true;
                            if (!QVL.DatabaseKeyThreadRunning){
                                loading.dispose();
                                ls.setVisible(true);
                                new DatabaseKeyThread(ls).startThread();
                            }
                        }
                        catch (Exception e){
                            JOptionPane.showMessageDialog(null, "Database connection error.", "QVL", JOptionPane.ERROR_MESSAGE);
                            System.exit(0);
                        }
                    }
                    catch (Exception e){
                        //System.out.println("Can't create statement.");
                    }
                }
                catch (Exception e){
                    //System.out.println("Failed to initialize connection.");
                }
                
            }
        });
        
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }
    
    
}
