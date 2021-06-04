/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quasivirtuallock.concept;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Mar Omay
 */
public class SettingsScreen extends javax.swing.JFrame {

    /**
     * Creates new form SettingsScreen
     */
    
    private static DashboardAdd dashboard;
    private boolean nameEditMode = false;
    private boolean passEditMode = false;
    
    private boolean[] flipped = {false, false, false};
    private boolean[] clickable = {false, false, false};
    
    private String [] keys = new String[3];
    private int[] ids = new int[3];
    
    public SettingsScreen(DashboardAdd dashboard) {
        initComponents();
        this.dashboard = dashboard;
        this.dashboard.mouseListenerEnabled = false;
        dashboard.setEnabled(false);
        this.nameTF.setText(QVL.NAME);
        displayKeys();
    }
    
    private void restartWindow(String message, Color color){
        SettingsScreen ss = new SettingsScreen(this.dashboard);
        ss.statusLabel.setForeground(color);
        ss.statusLabel.setText(message);
        this.dispose();
        ss.setVisible(true);
    }
    
    private void edit(JTextField tf, JLabel icon){
        statusLabel.setText("");
        
        if (nameEditMode){
            icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/edit.png")));
            try{
                QVL.STATEMENT.executeUpdate("UPDATE credentials SET name = '" + tf.getText() + "' WHERE id = " + QVL.ID);
                QVL.NAME = tf.getText();
                statusLabel.setForeground(Color.green);
                statusLabel.setText("Saved!");
            }
            catch(Exception e){
                statusLabel.setForeground(Color.red);
                statusLabel.setText("Unable to saved changes.");
            }
            tf.setEditable(false);
            tf.getCaret().setVisible(false);
            nameEditMode = false;
        }   
        else{
            icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/save.png")));
            tf.setEditable(true);
            tf.getCaret().setVisible(true);
            tf.requestFocus();
            nameEditMode = true;
        }
    }
    
    private void edit(JPasswordField pf, JLabel icon){
        statusLabel.setText("");
        
        if (passEditMode){
            icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/edit.png")));
            try{
                QVL.STATEMENT.executeUpdate("UPDATE credentials SET password = '" + pf.getText() + "' WHERE id = " + QVL.ID);
                statusLabel.setForeground(Color.green);
                statusLabel.setText("Saved!");
                pf.setText("password");
            }
            catch(Exception e){
                statusLabel.setForeground(Color.red);
                statusLabel.setText("Unable to saved changes.");
            }
            pf.setEditable(false);
            pf.getCaret().setVisible(false);
            passEditMode = false;
        }   
        else{
            icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/save.png")));
            pf.setEditable(true);
            pf.setText("");
            pf.getCaret().setVisible(true);
            pf.requestFocus();
            passEditMode = true;
        }
    }

    private void displayKeys(){
        try{
            ResultSet rs = QVL.STATEMENT.executeQuery("SELECT id, filename FROM keys WHERE userid = " + QVL.ID);
            int ctr = 0;
            while(rs.next()){
                ids[ctr] = rs.getInt("id");
                keys[ctr] = rs.getString("filename");
                ctr++;
            }
            flipped[0] = false;
            key1Label.setText("");
            key1Icon.setEnabled(true);
            key1Icon.setVisible(true);
            key1Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/add.png")));
            
            flipped[1] = false;
            key2Label.setText("");
            key2Icon.setEnabled(false);
            key2Icon.setVisible(false);
            key2Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/add.png")));
            
            flipped[2] = false;
            key3Label.setText("");
            key3Icon.setEnabled(false);
            key3Icon.setVisible(false);
            key3Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/add.png")));
            
            if (ctr == 0){
                key1Icon.setEnabled(true);
                key1Icon.setVisible(true);
                clickable[0] = true;
            }
            if (ctr >= 1){
                key1Label.setText(keys[0]);
                key1Label.setToolTipText(keys[0]);
                key1Icon.setEnabled(true);
                key1Icon.setVisible(true);
                key1Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/delete.png")));
                if(ctr == 1){
                    key2Icon.setEnabled(true);
                    key2Icon.setVisible(true);
                    clickable[1] = true;
                }
                clickable[0] = true;
                flipped[0] = true;
            }
            if (ctr >= 2){
                key2Label.setText(keys[1]);
                key2Label.setToolTipText(keys[1]);
                key2Icon.setEnabled(true);
                key2Icon.setVisible(true);
                key2Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/delete.png")));
                if(ctr == 2){
                    key3Icon.setEnabled(true);
                    key3Icon.setVisible(true);
                    clickable[2] = true;
                }
                clickable[1] = true;
                flipped[1] = true;
            }
            if (ctr == 3){
                key3Label.setText(keys[2]);
                key3Label.setToolTipText(keys[2]);
                key3Icon.setEnabled(true);
                key3Icon.setVisible(true);
                key3Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/delete.png")));
                clickable[2] = true;
                flipped[2] = true;
            }
        }
        catch(Exception e){
            statusLabel.setForeground(Color.red);
            statusLabel.setText("Error displaying keys.");
            //e.printStackTrace();
        }
    }
    
    private void chooseKey(){
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        fc.setDialogTitle("Choose file");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION){
            File f = fc.getSelectedFile();
            String key = new Operations(f.getAbsolutePath()).getKey();
            try{
                
                String file = f.getAbsolutePath().substring(3, f.getAbsolutePath().length());
                
                PreparedStatement ps = QVL.CONNECTION.prepareStatement("INSERT INTO keys VALUES (DEFAULT, ?, ?, ?)");
                ps.setInt(1, Integer.parseInt(QVL.ID));
                ps.setString(2, file);
                ps.setString(3, key);
                ps.execute();
                statusLabel.setForeground(Color.green);
                statusLabel.setText("Saved!");
                
                restartWindow("New key saved!", Color.GREEN);
                
            }
            catch(Exception e){
                statusLabel.setForeground(Color.red);
                statusLabel.setText("Not saved!");
                //e.printStackTrace();
            }
            
            
        }
    }

    private void keyIconClicked(JLabel l, int index){
        if (flipped[index] && clickable[index]){
            statusLabel.setText("trash");
            try{
                QVL.STATEMENT.executeUpdate("DELETE FROM keys WHERE id = " + ids[index]);
                restartWindow("Key daleted.", Color.GREEN);
            }
            catch(Exception e){
                restartWindow("Failed to delete key.", Color.RED);
            }
        }
        else if (clickable[index]){
            chooseKey();
        }
        else{
            statusLabel.setText("Operation failed! (c.ss.kic)");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JLabel();
        nameTF = new javax.swing.JTextField();
        passwordPF = new javax.swing.JPasswordField();
        key1Label = new javax.swing.JLabel();
        key2Label = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        key3Label = new javax.swing.JLabel();
        passwordIcon = new javax.swing.JLabel();
        nameIcon = new javax.swing.JLabel();
        key2Icon = new javax.swing.JLabel();
        key1Icon = new javax.swing.JLabel();
        key3Icon = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        closeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeButtonMouseClicked(evt);
            }
        });
        getContentPane().add(closeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(463, 11, 25, 25));

        nameTF.setEditable(false);
        nameTF.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        nameTF.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().add(nameTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 100, 180, 30));

        passwordPF.setEditable(false);
        passwordPF.setFont(new java.awt.Font("Calibri", 0, 16)); // NOI18N
        passwordPF.setText("password");
        getContentPane().add(passwordPF, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 168, 180, 30));

        key1Label.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        key1Label.setForeground(new java.awt.Color(255, 255, 255));
        key1Label.setText("jLabel3");
        getContentPane().add(key1Label, new org.netbeans.lib.awtextra.AbsoluteConstraints(289, 100, 150, 30));

        key2Label.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        key2Label.setForeground(new java.awt.Color(255, 255, 255));
        key2Label.setText("jLabel3");
        getContentPane().add(key2Label, new org.netbeans.lib.awtextra.AbsoluteConstraints(289, 140, 150, 30));

        statusLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        statusLabel.setForeground(new java.awt.Color(255, 0, 0));
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(statusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 240, 470, 40));

        key3Label.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        key3Label.setForeground(new java.awt.Color(255, 255, 255));
        key3Label.setText("jLabel3");
        getContentPane().add(key3Label, new org.netbeans.lib.awtextra.AbsoluteConstraints(289, 180, 150, 30));

        passwordIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/edit.png"))); // NOI18N
        passwordIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        passwordIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                passwordIconMouseClicked(evt);
            }
        });
        getContentPane().add(passwordIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 168, 30, 30));

        nameIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/edit.png"))); // NOI18N
        nameIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nameIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nameIconMouseClicked(evt);
            }
        });
        getContentPane().add(nameIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 30, 30));

        key2Icon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        key2Icon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                key2IconMouseClicked(evt);
            }
        });
        getContentPane().add(key2Icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(444, 140, 30, 30));

        key1Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/add.png"))); // NOI18N
        key1Icon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        key1Icon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                key1IconMouseClicked(evt);
            }
        });
        getContentPane().add(key1Icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(444, 100, 30, 30));

        key3Icon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        key3Icon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                key3IconMouseClicked(evt);
            }
        });
        getContentPane().add(key3Icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(444, 180, 30, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/settings.png"))); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jLabel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabel1MouseDragged(evt);
            }
        });
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
        });
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 320));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMouseClicked
        this.dispose();
        this.dashboard.mouseListenerEnabled = true;
        this.dashboard.setEnabled(true);
        this.dashboard.requestFocus();
    }//GEN-LAST:event_closeButtonMouseClicked

    private int posX, posY; 
    private void jLabel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseDragged
        Rectangle rect = getBounds();
        this.setBounds(evt.getXOnScreen() - posX, evt.getYOnScreen() - posY, rect.width, rect.height);
    }//GEN-LAST:event_jLabel1MouseDragged

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        posX = evt.getX();
        posY = evt.getY();
    }//GEN-LAST:event_jLabel1MousePressed

    
    private void nameIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nameIconMouseClicked
        edit(nameTF, nameIcon);
    }//GEN-LAST:event_nameIconMouseClicked

    private void passwordIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passwordIconMouseClicked
        edit(passwordPF, passwordIcon);
    }//GEN-LAST:event_passwordIconMouseClicked

    private void key1IconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_key1IconMouseClicked
        keyIconClicked(key1Icon, 0);
    }//GEN-LAST:event_key1IconMouseClicked

    private void key2IconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_key2IconMouseClicked
        keyIconClicked(key2Icon, 1);
    }//GEN-LAST:event_key2IconMouseClicked

    private void key3IconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_key3IconMouseClicked
        keyIconClicked(key3Icon, 2);
    }//GEN-LAST:event_key3IconMouseClicked

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SettingsScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SettingsScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SettingsScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SettingsScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SettingsScreen(dashboard).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel closeButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel key1Icon;
    private javax.swing.JLabel key1Label;
    private javax.swing.JLabel key2Icon;
    private javax.swing.JLabel key2Label;
    private javax.swing.JLabel key3Icon;
    private javax.swing.JLabel key3Label;
    private javax.swing.JLabel nameIcon;
    private javax.swing.JTextField nameTF;
    private javax.swing.JLabel passwordIcon;
    private javax.swing.JPasswordField passwordPF;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}
