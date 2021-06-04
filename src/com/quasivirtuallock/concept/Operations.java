package com.quasivirtuallock.concept;

import com.quasivirtuallock.threads.ImportDataThread;
import static com.quasivirtuallock.concept.QVL.DRIVER;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static com.quasivirtuallock.concept.QVL.JDBC_URL;

public class Operations {

    private static String ADDRESS;
    private static File FILE;
    public static File DRIVE;
    
    public Operations (String address){
        this.ADDRESS = address;
        this.FILE = new File(this.ADDRESS);
        
    }

    private DashboardAdd dashboard;
    
    public Operations (DashboardAdd dashboard){
        this.dashboard = dashboard;
    }
    
    private LoadingScreen loading;
    
    public Operations (LoadingScreen loading, DashboardAdd dashboard){
        this.loading = loading;
        this.dashboard = dashboard;
    }
    
    public String getKey(){
        String base = toBase64(FILE);
        String attr = getAttribute(FILE);
        if (base.length() > 32000)
            base = base.substring(0, (31999 - attr.length()));
        return base + attr; 
    }
    
    private static String toBase64(File file){
        
        String content = null;
        
        try{
            FileInputStream fiu = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fiu.read(bytes);
            content = Base64.getEncoder().encodeToString(bytes);
        }
        catch (Exception e){
            //System.out.println("Error: Can't convert to Base64. (Operations)");
        }
        
        
        
        return content;
    }
    
    private static String getAttribute(File file){
        
        String dateCreated = null;
        
        try{
            Path path = Paths.get(file.getAbsolutePath());
            BasicFileAttributes attrib = Files.getFileAttributeView(path, BasicFileAttributeView.class).readAttributes();
            dateCreated = attrib.creationTime().toString();
        }
        catch(Exception e){
            //System.out.println("Error: Can't fetch Attribute. (Operations)");
        }
        
        return dateCreated;
    }
    
    public static boolean detectPhysicalKey(){
        
        long t = System.currentTimeMillis();
        long end = t + 20000;
        
        File [] drives = File.listRoots();
        
        while(System.currentTimeMillis() < end){
            if(File.listRoots().length > drives.length){
                
                drives = File.listRoots();
                
                //System.out.println("Drive " + drives[drives.length-1] + " detected.");
                
                DRIVE = drives[drives.length-1];
                
                return true;
            }
        }
        return false;
    }
    
    public static void exportKey(String key) throws URISyntaxException{
        String dir = Operations.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        //System.out.println("Dir: " + dir);
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(dir + "xvf.java"));
            bw.write(key);
            bw.close();
            //System.out.print("Successful export!");
        }
        catch(Exception e){
            //System.out.print("Error: Can't export key.");
        }
    }
    
    public static Connection getConnection(){
        
        /*
            Also creates the primary table
        */
        
        String userTable = "CREATE TABLE credentials (id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 10001, INCREMENT BY 1), name VARCHAR(255), password VARCHAR(255))";
        String vaultTable = "CREATE TABLE files (id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), name VARCHAR(255), type VARCHAR(15), size VARCHAR(255), date VARCHAR(15),  file BLOB)";
        String hashTable = "CREATE TABLE keys (id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), userid INT, filename VARCHAR(255), utf VARCHAR(32000))";
        
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(QVL.JDBC_URL);
            
            try{
                conn.createStatement().execute(userTable);
                //System.out.println("Table created (USER).");
            }
            catch(Exception e){
                //System.out.println("Table NOT created (USER).");
            }
            
            try{
                conn.createStatement().execute(vaultTable);
                //System.out.println("Table created (VAULT).");
            }
            catch(Exception e){
                //System.out.println("Table NOT created (VAULT).");
            }
            QVL.setup = true;
            
            try{
                conn.createStatement().execute(hashTable);
                //System.out.println("Table created (HASH).");
            }
            catch(Exception e){
                //System.out.println("Table NOT created (HASH).");
            }
        }
        catch (ClassNotFoundException | SQLException e){
            try{
                Class.forName(DRIVER);
                conn = DriverManager.getConnection(JDBC_URL);
                //System.out.println("Database accessed.");
                
            }
            catch (ClassNotFoundException | SQLException ex){
                
                //System.out.println("Connection: NONE");
                JOptionPane.showMessageDialog(null, "Can't connect to database.", "QVL", JOptionPane.ERROR_MESSAGE);
            }
        }
        return conn;
    }
    
    public void processFile(List<File> files){ 
        
        ImportScreen is = new ImportScreen();
                    
        is.setLocation(dashboard.getX() - dashboard.getX() / 2 - is.getWidth(),
                       dashboard.getY() - dashboard.getY() / 2 - is.getHeight());
                
        is.jpb.setValue(0);
        is.jl.setText("Processing...");
                
        is.setVisible(true);
        
        new ImportDataThread(files, dashboard, is).startThread();      
    }
    
    public ArrayList fetchData(){
        
        JPanel jp = dashboard.jPanel2;
        loading.setVisible(true);
        
        ArrayList<ArrayList> arl = new ArrayList<ArrayList>();
        dashboard.addButton.setEnabled(false);
        dashboard.settingsButton.setEnabled(false);
        try {
            ResultSet rs = QVL.STATEMENT.executeQuery("SELECT id, name, type, size, date FROM files ORDER BY id");
            float i = 0;
            while (rs.next()){
                dashboard.jProgressBar1.setValue(Integer.parseInt(String.format("%.0f",i / QVL.rowCount * 100)));
                dashboard.revalidate();
                dashboard.repaint();
                Thread.sleep(10);
                QVL.rowToId.put("" + (int) (i + 1), rs.getString("id"));
                ArrayList<String> arr = new ArrayList<String>();
                arr.add("" + (int)(i + 1));
                arr.add("  " + rs.getString("name"));
                arr.add(rs.getString("type"));
                arr.add(rs.getString("size") + "  ");
                arr.add(rs.getString("date"));
                
                arl.add(arr);
                i += 1;
            }
            dashboard.jProgressBar1.setValue(0);
            dashboard.jProgressBar1.setVisible(false);
            //System.out.println("All data retrieved from database");
        }
        catch (Exception e){
            //System.out.println("Failed to retrieve from database");
        }
        
        loading.dispose();
        
        dashboard.addButton.setEnabled(true);
        dashboard.settingsButton.setEnabled(true);
        return arl; 
    }
    
    public String[][] processData(ArrayList<ArrayList<String>> al){
        
        JPanel jp = dashboard.jPanel2;
        loading.setVisible(true);
        
        String[][] s = new String[al.size()][5];
        for (int i = 0; i < al.size(); i++){
            for (int j = 0; j < 5; j++){
                s[i][j] = al.get(i).get(j);
            }
        }
        
        loading.dispose();
        
        return s;
    }
    
    public void fileOperation(String[] s){
        FileMenu fm = new FileMenu(new String[]{s[1].substring(2), s[3], s[4], s[0]}, dashboard);
        fm.setLocation(dashboard.getX() + dashboard.getWidth() / 2 - fm.getWidth() / 2,
                                   dashboard.getY() + dashboard.getHeight() / 2 - fm.getHeight() / 2);
        fm.setAlwaysOnTop(true);
        fm.setVisible(true);
        
        
    }
    
    public static void pause(int ms){
        try{
            Thread.sleep(ms);
        }
        catch(Exception e){
            
        }
    }

}
