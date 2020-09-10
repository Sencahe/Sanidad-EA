
package main;

import java.awt.Color;
import mytools.Icons;
import mytools.Utilities;


public class Login extends javax.swing.JFrame {

    MainFrame mainFrame;
    
    
    
    public Login(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        Icons icons = mainFrame.getIcons();
        Utilities utility = mainFrame.getUtility();
        
        initComponents();
        this.setTitle("Ingresar al sistema de Sanidad R1-\"Patricios\"");
        this.setIconImage(icons.getIconHealthService().getImage());
        this.setLocationRelativeTo(null);
        
        
        getContentPane().setBackground(Color.white);
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        buttonLogin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(680, 420));
        setMinimumSize(new java.awt.Dimension(680, 420));
        setPreferredSize(new java.awt.Dimension(680, 420));
        setResizable(false);
        setSize(new java.awt.Dimension(680, 420));
        getContentPane().setLayout(null);
        getContentPane().add(jPasswordField1);
        jPasswordField1.setBounds(470, 190, 172, 20);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Contraseña");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(510, 160, 80, 17);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/myimages/logo_sanidad.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 10, 389, 388);

        buttonLogin.setText("Ingresar");
        buttonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoginActionPerformed(evt);
            }
        });
        getContentPane().add(buttonLogin);
        buttonLogin.setBounds(510, 220, 80, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoginActionPerformed
            
    }//GEN-LAST:event_buttonLoginActionPerformed

 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField jPasswordField1;
    // End of variables declaration//GEN-END:variables
}
