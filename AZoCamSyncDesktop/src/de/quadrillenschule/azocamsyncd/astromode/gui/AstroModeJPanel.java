/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode.gui;

import de.quadrillenschule.azocamsyncd.GlobalProperties;

/**
 *
 * @author D061339
 */
public class AstroModeJPanel extends javax.swing.JPanel {

    /**
     * Creates new form AstroModeJPanel
     */
    GlobalProperties gp;

    public AstroModeJPanel() {
        GlobalProperties gp = new GlobalProperties();
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        smartPhoneIPjTextField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Smartphone IP:");
        jPanel1.add(jLabel1);

        smartPhoneIPjTextField.setText("192.168.178.31");
        smartPhoneIPjTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smartPhoneIPjTextFieldActionPerformed(evt);
            }
        });
        jPanel1.add(smartPhoneIPjTextField);

        add(jPanel1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void smartPhoneIPjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smartPhoneIPjTextFieldActionPerformed

        gp.setProperty(GlobalProperties.CamSyncProperties.SMARTPHONE_IPS, smartPhoneIPjTextField.getText());
    }//GEN-LAST:event_smartPhoneIPjTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField smartPhoneIPjTextField;
    // End of variables declaration//GEN-END:variables
}