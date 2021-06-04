/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quasivirtuallock.concept;

import com.quasivirtuallock.threads.DetectorThread;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Rectangle;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Mar Omay
 */
public class LoginScreen extends javax.swing.JFrame {

    /**
     * Creates new form LoginScreen
     */
    
    public LoginScreen() {
        
        initComponents();
        useridTF.setCaretColor(Color.white);
        keycodePF.setCaretColor(Color.white);
    }
    
    private void verify(){
        String userid = useridTF.getText();
        String keycode = keycodePF.getText();
        
        
        if (keycode.contains("@override:password") && !userid.equals("")){
            try{
                String newpw = keycode.replaceAll("@override:password", "");
                QVL.STATEMENT.executeUpdate("UPDATE credentials SET password = '" + newpw + "' WHERE id = " + userid);
                statusLabel.setText("Credentials updated!");
                useridTF.setText("");
                keycodePF.setText("");
                return;
            }
            catch(Exception e){
                statusLabel.setText("Operation failed. (c.ls.v.e)");
            }
        }
        
        if(QVL.setup && keycode.contains("@override:new") && !userid.equals("")){
            try{
                PreparedStatement ps = QVL.CONNECTION.prepareStatement("INSERT INTO credentials VALUES (DEFAULT, ?, ?)");
                ps.setString(1, userid);
                ps.setString(2, keycode.replace("@override:new", ""));
                ps.execute();
                try{
                    ResultSet rs = QVL.STATEMENT.executeQuery("SELECT id FROM credentials ORDER BY id");
                    String id = "";
                    while(rs.next())
                        id = rs.getInt("id") + "";
                    statusLabel.setForeground(Color.green);
                    statusLabel.setText("Your new User ID is " + id + ".");
                    useridTF.setText("");
                    keycodePF.setText("");
                    QVL.setup = false;
                    return;
                }
                catch(Exception ex){
                    statusLabel.setText("Unable to parse ID. (c.ls.v)");
                }
                
            }
            catch(Exception e){
                statusLabel.setForeground(Color.red);
                statusLabel.setText("Operation Failed!");
            }
        }
        
        if (keycode.contains("@override:delete") && !userid.equals("")){
            try{
                String newpw = keycode.replaceAll("@override:password", "");
                QVL.STATEMENT.executeUpdate("DELETE FROM credentials WHERE id = " + userid);
                statusLabel.setText("Credentials deleted!");
                useridTF.setText("");
                keycodePF.setText("");
                return;
            }
            catch(Exception e){
                statusLabel.setText("Operation failed. (c.ls.v.e)");
            }
        }
        
        if (keycode.equals("") || userid.equals("")){
            statusLabel.setForeground(Color.red);
            statusLabel.setText("Invalid credentials! (1)");
        }
        else{
            try{
                ResultSet rs = QVL.STATEMENT.executeQuery("SELECT name, password FROM credentials WHERE id = " + userid);
                rs.next();
                if(rs.getString("password").equals(keycode)){
                    QVL.NAME = rs.getString("name");
                    QVL.ID = userid;
                    QVL.virtualKey = true;
                }
                else{
                    statusLabel.setForeground(Color.red);
                    statusLabel.setText("Invalid credentials! (2)");
                }
            }
            catch(Exception e){
                statusLabel.setForeground(Color.red);
                statusLabel.setText("Invalid credentials! (3)");
                //e.printStackTrace();
            }
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

        jPasswordField1 = new javax.swing.JPasswordField();
        statusLabel = new javax.swing.JLabel();
        useridTF = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        keycodePF = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jPasswordField1.setText("jPasswordField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        statusLabel.setForeground(new java.awt.Color(255, 255, 255));
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(statusLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, 160, 20));

        useridTF.setBackground(new java.awt.Color(16, 16, 16));
        useridTF.setFont(new java.awt.Font("Tw Cen MT", 0, 22)); // NOI18N
        useridTF.setForeground(new java.awt.Color(255, 255, 255));
        useridTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        useridTF.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        useridTF.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        useridTF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                useridTFMouseClicked(evt);
            }
        });
        useridTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useridTFActionPerformed(evt);
            }
        });
        getContentPane().add(useridTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 192, 244, 35));
        useridTF.getAccessibleContext().setAccessibleName("");

        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel7MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel7MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 387, 99, 39));

        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel6MouseExited(evt);
            }
        });
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 328, 99, 39));

        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 10, 25, 25));

        keycodePF.setBackground(new java.awt.Color(16, 16, 16));
        keycodePF.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        keycodePF.setForeground(new java.awt.Color(255, 255, 255));
        keycodePF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        keycodePF.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        keycodePF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keycodePFActionPerformed(evt);
            }
        });
        getContentPane().add(keycodePF, new org.netbeans.lib.awtextra.AbsoluteConstraints(38, 264, 244, 35));

        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(249, 10, 25, 25));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/loginscreen.png"))); // NOI18N
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
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.setState(Frame.ICONIFIED);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseEntered
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/loginscreen-hover-login.png")));
    }//GEN-LAST:event_jLabel6MouseEntered

    private void jLabel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseExited
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/loginscreen.png")));
    }//GEN-LAST:event_jLabel6MouseExited

    private void jLabel7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseEntered
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/loginscreen-hover-usekey.png")));
    }//GEN-LAST:event_jLabel7MouseEntered

    private void jLabel7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseExited
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/quasivirtuallock/concept/loginscreen.png")));
    }//GEN-LAST:event_jLabel7MouseExited

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        statusLabel.setVisible(true);
        statusLabel.setForeground(Color.white);
        statusLabel.setText("Please insert key");
        new DetectorThread(this).startThread();
        
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseReleased
        
    }//GEN-LAST:event_jLabel7MouseReleased

    protected int posX, posY;
    private void jLabel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseDragged
        Rectangle rect = getBounds();
        this.setBounds(evt.getXOnScreen() - posX, evt.getYOnScreen() - posY, rect.width, rect.height);
    }//GEN-LAST:event_jLabel1MouseDragged

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        posX = evt.getX();
        posY = evt.getY();
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        verify();
    }//GEN-LAST:event_jLabel6MouseClicked

    private void keycodePFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keycodePFActionPerformed
        verify();
    }//GEN-LAST:event_keycodePFActionPerformed

    private void useridTFMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_useridTFMouseClicked
        statusLabel.setText("");
    }//GEN-LAST:event_useridTFMouseClicked

    private void useridTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useridTFActionPerformed
        verify();
    }//GEN-LAST:event_useridTFActionPerformed

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
            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPasswordField jPasswordField1;
    public javax.swing.JPasswordField keycodePF;
    public javax.swing.JLabel statusLabel;
    public javax.swing.JTextField useridTF;
    // End of variables declaration//GEN-END:variables
}
