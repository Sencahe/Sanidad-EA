package main;

import database.Configurations;
import dialogs.Configurator;
import java.awt.Color;
import java.awt.event.KeyEvent;
import mytools.Icons;
import mytools.Utilities;

public class Login extends javax.swing.JFrame {

    MainFrame mainFrame;
    Configurator configurator;

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

    private void logIn() {
        
        String input = String.valueOf(jPasswordField1.getPassword());
        
        Configurations config = new Configurations();
        boolean result = config.getPassword(input);
        
        if(result){
            mainFrame.setVisible(true);
            configurator.setFlagPassword(input);           
            dispose();
        } 
        jPasswordField1.setText("");
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        buttonLogin = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(680, 420));
        setResizable(false);
        setSize(new java.awt.Dimension(680, 420));
        getContentPane().setLayout(null);

        jPasswordField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField1KeyPressed(evt);
            }
        });
        getContentPane().add(jPasswordField1);
        jPasswordField1.setBounds(410, 170, 240, 20);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Contraseña");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(480, 140, 80, 17);

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
        buttonLogin.setBounds(480, 210, 80, 30);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void buttonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoginActionPerformed
        logIn();
    }//GEN-LAST:event_buttonLoginActionPerformed

    private void jPasswordField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField1KeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            logIn();
        }
    }//GEN-LAST:event_jPasswordField1KeyPressed

    //-----------------------SETTERS Y GETTERS----------------------------------
    
    public void setConfigurator(Configurator configurator) {
        this.configurator = configurator;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPopupMenu jPopupMenu1;
    // End of variables declaration//GEN-END:variables
}
