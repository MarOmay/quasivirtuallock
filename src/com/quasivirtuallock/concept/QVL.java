package com.quasivirtuallock.concept;

import com.quasivirtuallock.threads.DatabaseConnectionThread;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Hashtable;
import javax.swing.UIManager;

public class QVL {
    
    protected static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String JDBC_URL = "jdbc:derby:qvldb;user=xvfadmin;password=xvfadmin;create=true";
    public static Connection CONNECTION;
    public static Statement STATEMENT;
    public static String QUERY;
    
    public static String NAME;
    public static String ID;
    
    protected static boolean setup = true;
    
    public static float rowCount = 0;
    public static boolean dbReady = false;
    
    public static boolean physicalKey = false;
    public static boolean virtualKey = false;
    
    public static boolean DatabaseKeyThreadRunning = false;
    
    public static String[][] tableData;
    
    public static Hashtable<String, String> rowToId = new Hashtable<String, String>();
    
    
    public static void main(String[] args) throws URISyntaxException {
        
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e){
            
        }
        
        LoginScreen ls = new LoginScreen();
        
        System.out.println("Initializing db");
        new DatabaseConnectionThread(ls).startThread();

        
    }

}
