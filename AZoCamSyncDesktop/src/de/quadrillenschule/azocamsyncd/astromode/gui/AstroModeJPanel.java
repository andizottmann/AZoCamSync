/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode.gui;

import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsyncd.GlobalProperties;
import de.quadrillenschule.azocamsyncd.GlobalProperties.CamSyncProperties;
import de.quadrillenschule.azocamsyncd.astromode.SmartPhoneWrapper;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnection;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnectionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;

/**
 *
 * @author D061339
 */
public class AstroModeJPanel extends javax.swing.JPanel implements FTPConnectionListener {

    /**
     * Creates new form AstroModeJPanel
     */
    public enum Status {

        PAUSED, RUNNING
    }

    GlobalProperties gp;
    LinkedList<PhotoSerie> jobList = new LinkedList<>();
    private FTPConnection ftpConnection;
    private Status status = Status.PAUSED;

    public AstroModeJPanel() {
        gp = new GlobalProperties();
        initComponents();
        jobjTable.setModel(new PhotoSeriesTableModel(jobList));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        smartPhoneIPjTextField = new javax.swing.JTextField();
        startAstroModejButton = new javax.swing.JButton();
        updateTablejButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jobjTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        astroFolderjTextField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Smartphone IPs:");
        jPanel1.add(jLabel1);

        smartPhoneIPjTextField.setText(gp.getProperty(CamSyncProperties.SMARTPHONE_IPS)
        );
        smartPhoneIPjTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smartPhoneIPjTextFieldActionPerformed(evt);
            }
        });
        jPanel1.add(smartPhoneIPjTextField);

        startAstroModejButton.setText("Start AstroMode");
        startAstroModejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startAstroModejButtonActionPerformed(evt);
            }
        });
        jPanel1.add(startAstroModejButton);

        updateTablejButton.setText("Update Table");
        updateTablejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTablejButtonActionPerformed(evt);
            }
        });
        jPanel1.add(updateTablejButton);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jobjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jobjTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jobjTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jScrollPane1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel2.add(jPanel4, gridBagConstraints);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        astroFolderjTextField.setColumns(30);
        astroFolderjTextField.setText(gp.getProperty(CamSyncProperties.LAST_ASTRO_FOLDER)
        );
        astroFolderjTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                astroFolderjTextFieldActionPerformed(evt);
            }
        });
        astroFolderjTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                astroFolderjTextFieldKeyReleased(evt);
            }
        });
        jPanel3.add(astroFolderjTextField);

        add(jPanel3, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void smartPhoneIPjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smartPhoneIPjTextFieldActionPerformed

        gp.setProperty(GlobalProperties.CamSyncProperties.SMARTPHONE_IPS, smartPhoneIPjTextField.getText());
    }//GEN-LAST:event_smartPhoneIPjTextFieldActionPerformed

    private void startAstroModejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startAstroModejButtonActionPerformed
        if (status == Status.PAUSED) {
            SmartPhoneWrapper.checkConnection();
            updateTablejButtonActionPerformed(evt);
            ftpConnection.addFTPConnectionListenerOnce(this);
            status = Status.RUNNING;

        } else {
            ftpConnection.removeFTPConnectionListener(this);
            status = Status.PAUSED;

        }
        startAstroModejButton.setText(status.name());
    }//GEN-LAST:event_startAstroModejButtonActionPerformed

    private void updateTablejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTablejButtonActionPerformed
        syncJobLists();
        if (jobList != null) {
            ((PhotoSeriesTableModel) jobjTable.getModel()).setPhotoSeries(jobList);
        }
        ((PhotoSeriesTableModel) jobjTable.getModel()).fireTableDataChanged();
    }//GEN-LAST:event_updateTablejButtonActionPerformed

    private void astroFolderjTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_astroFolderjTextFieldKeyReleased

    }//GEN-LAST:event_astroFolderjTextFieldKeyReleased

    private void astroFolderjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_astroFolderjTextFieldActionPerformed
        gp.setProperty(CamSyncProperties.LAST_ASTRO_FOLDER, astroFolderjTextField.getText());
    }//GEN-LAST:event_astroFolderjTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField astroFolderjTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jobjTable;
    private javax.swing.JTextField smartPhoneIPjTextField;
    private javax.swing.JButton startAstroModejButton;
    private javax.swing.JButton updateTablejButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void receiveNotification(FTPConnectionStatus status, String message, int progress) {
        if (status == FTPConnectionStatus.NEW_LOCAL_FILE) {
            updateTablejButtonActionPerformed(null);
            PhotoSerie myPS = null;
            for (PhotoSerie p : jobList) {
                if (p.getTriggered() > p.getReceived()) {
                    myPS = p;
                }
            }
            if (myPS == null) {
                return;
            }
            File receivedFile = new File(message);
            File targetFolder = new File(
                    gp.getProperty(CamSyncProperties.LAST_ASTRO_FOLDER)
                    + System.getProperty("file.separator")
                    + myPS.getProject() + System.getProperty("file.separator")
                    + myPS.getSeriesName());
            try {
                FileUtils.moveFileToDirectory(receivedFile, targetFolder, true);
            } catch (IOException ex) {
                Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (message.toUpperCase().endsWith(".NEF")) {
                myPS.setReceived(myPS.getReceived() + 1);
            }
            SmartPhoneWrapper.update(myPS);
            updateTablejButtonActionPerformed(null);
        }
    }

    public void syncJobLists() {
        try {
            LinkedList<PhotoSerie> remoteJobList = SmartPhoneWrapper.getJobs();
            jobList = gp.readStoredAstroJobList();
//Updating the number of received files from local;
            for (PhotoSerie remote : remoteJobList) {
                for (PhotoSerie local : jobList) {
                    if (remote.getId() == local.getId()) {
                        if (local.getReceived() != remote.getReceived()) {
                            SmartPhoneWrapper.update(local);
                            remote.setReceived(local.getReceived());
                        }
                    }
                }
            }
            jobList = remoteJobList;
            gp.saveStoredAstroJobList(jobList);
        } catch (JSONException | IOException ex) {
            try {
                jobList = gp.readStoredAstroJobList();
            } catch (JSONException ex1) {
                Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    /**
     * @return the ftpConnection
     */
    public FTPConnection getFtpConnection() {
        return ftpConnection;
    }

    /**
     * @param ftpConnection the ftpConnection to set
     */
    public void setFtpConnection(FTPConnection ftpConnection) {
        this.ftpConnection = ftpConnection;
    }
}
