package com.quasivirtuallock.threads;

import com.quasivirtuallock.concept.DashboardAdd;
import com.quasivirtuallock.concept.LoginScreen;
import com.quasivirtuallock.concept.QVL;

public class DatabaseKeyThread {
    
    private LoginScreen ls;
    
    public DatabaseKeyThread(LoginScreen ls){
        this.ls = ls;
    }
    
    public void startThread(){
        
        Thread t = new Thread(new Runnable() {
        
            @Override
            public void run() {
                
                QVL.DatabaseKeyThreadRunning = true;
                //System.out.println("Validating requirements");
                
                while (true){
                    if (QVL.dbReady && (QVL.physicalKey || QVL.virtualKey)){
                        //System.out.println("Launching Dashboard");
                        try {
                            Thread.sleep(1000);
                        }
                            catch (Exception e){

                        }
                        ls.dispose();
                        new DashboardAdd().setVisible(true);
                        break;
                    }
                    try {
                        Thread.sleep(300);
                    }
                    catch (Exception e){
                        
                    }
                }
            
            }
        
        });
        
        t.start();
        t.setPriority(Thread.MIN_PRIORITY);
        
    }
    
}
