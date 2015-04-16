/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode.gui;

import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.helpers.Formats;
import de.quadrillenschule.azocamsynca.job.JobProcessor;
import de.quadrillenschule.azocamsynca.webservice.WebService;
import de.quadrillenschule.azocamsyncd.GlobalProperties;
import de.quadrillenschule.azocamsyncd.GlobalProperties.CamSyncProperties;
import de.quadrillenschule.azocamsyncd.astromode.SmartPhoneWrapper;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnection;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnectionListener;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
    Timer updateTimer;
    public static boolean PROGRAMMATIC_SELECTION = false;
    public Frame parentFrame = null;

    public AstroModeJPanel() {
        gp = new GlobalProperties();
        initComponents();
        jobjTable.setModel(new PhotoSeriesTableModel(jobList));
        jobjTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (PROGRAMMATIC_SELECTION || (jobjTable.getSelectedRow() < 0)) {
                    return;
                }

                PhotoSerie ps = jobList.get(jobjTable.getSelectedRow());
                projectTextField.setText(ps.getProject());
                seriesTextField.setText(ps.getSeriesName());
                numberTextField.setText("" + ps.getNumber());
                exposureTextField.setText(Formats.toString(ps.getExposure()));
                initialDelayTextField.setText(Formats.toString(ps.getInitialDelay()));
                delayAfterEachTextField.setText(Formats.toString(ps.getDelayAfterEachExposure()));
            }
        });

    }

    public void update() {
        int selectedRow = jobjTable.getSelectedRow();
        syncJobLists();

        if (jobList != null) {

            ((PhotoSeriesTableModel) jobjTable.getModel()).setPhotoSeries(jobList);
        }
        boolean somethingToConfirm = false;
        for (PhotoSerie ps : jobList) {
            if (ps.getTriggerStatus().equals(PhotoSerie.TriggerJobStatus.WAITFORUSER)) {
                confirmSPjButton.setBackground(Color.red);
                confirmSPjButton.setEnabled(true);
                somethingToConfirm = true;
            }
        }
        if (!somethingToConfirm) {
            confirmSPjButton.setBackground(addJobjButton.getBackground());
            confirmSPjButton.setEnabled(false);

        }
        ((PhotoSeriesTableModel) jobjTable.getModel()).fireTableDataChanged();
        smartPhoneStatus.setText(SmartPhoneWrapper.lastStatus().name());
        Color statusColor = Color.BLACK;
        switch (SmartPhoneWrapper.lastStatus()) {
            case CONNECTED:
                statusColor = Color.GREEN;
                break;
            case ERROR:
                statusColor = Color.RED;
                break;
        }
        smartPhoneStatus.setForeground(statusColor);
        try {
            if (SmartPhoneWrapper.getFromSmartPhone(WebService.WebCommands.jobprocessorstatus, true).contains(JobProcessor.ProcessorStatus.PAUSED.name())) {
                pauseQueuejButton.setEnabled(false);
                startProcessorjButton.setEnabled(true);
            } else {
                pauseQueuejButton.setEnabled(true);
                startProcessorjButton.setEnabled(false);

            }
        } catch (IOException ex) {
            Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        PROGRAMMATIC_SELECTION = true;
        jobjTable.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);

        PROGRAMMATIC_SELECTION = false;
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
        startAstroModejButton = new javax.swing.JButton();
        updateTablejButton = new javax.swing.JButton();
        smartPhoneStatus = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jobjTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        projectTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        seriesTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        numberTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        exposureTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        initialDelayTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        delayAfterEachTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        chooseDirjButton = new javax.swing.JButton();
        astroFolderjTextField = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        addDatejButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        smartPhoneIPjTextField = new javax.swing.JTextField();
        moveFilesjCheckBox = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        confirmSPjButton = new javax.swing.JButton();
        startProcessorjButton = new javax.swing.JButton();
        pauseQueuejButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        addJobjButton = new javax.swing.JButton();
        modifyJobjButton = new javax.swing.JButton();
        removeJobjButton = new javax.swing.JButton();
        removeallJobsjButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        addDefaultjButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        startAstroModejButton.setFont(startAstroModejButton.getFont().deriveFont(startAstroModejButton.getFont().getStyle() | java.awt.Font.BOLD));
        startAstroModejButton.setText("Start AstroMode");
        startAstroModejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startAstroModejButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(startAstroModejButton, gridBagConstraints);

        updateTablejButton.setText("Update Table");
        updateTablejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTablejButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel1.add(updateTablejButton, gridBagConstraints);

        smartPhoneStatus.setFont(smartPhoneStatus.getFont().deriveFont(smartPhoneStatus.getFont().getStyle() | java.awt.Font.BOLD, smartPhoneStatus.getFont().getSize()+1));
        smartPhoneStatus.setText(SmartPhoneWrapper.lastStatus().name());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        jPanel1.add(smartPhoneStatus, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.SOUTH);

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

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Project");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel4.add(jLabel2, gridBagConstraints);

        projectTextField.setText("default");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(projectTextField, gridBagConstraints);

        jLabel3.setText("Series");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel4.add(jLabel3, gridBagConstraints);

        seriesTextField.setText("lights");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(seriesTextField, gridBagConstraints);

        jLabel4.setText("Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel4.add(jLabel4, gridBagConstraints);

        numberTextField.setText("20");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(numberTextField, gridBagConstraints);

        jLabel5.setText("Exposure");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel4.add(jLabel5, gridBagConstraints);

        exposureTextField.setText("0:01:30");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(exposureTextField, gridBagConstraints);

        jLabel6.setText("Initial Delay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel4.add(jLabel6, gridBagConstraints);

        initialDelayTextField.setText("0:00:02");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(initialDelayTextField, gridBagConstraints);

        jLabel7.setText("Delay after each");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel4.add(jLabel7, gridBagConstraints);

        delayAfterEachTextField.setText("0:00:04");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(delayAfterEachTextField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel4, gridBagConstraints);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Configure"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        chooseDirjButton.setText("Choose Dir...");
        chooseDirjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseDirjButtonActionPerformed(evt);
            }
        });
        jPanel3.add(chooseDirjButton, new java.awt.GridBagConstraints());

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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(astroFolderjTextField, gridBagConstraints);
        jPanel3.add(jSeparator1, new java.awt.GridBagConstraints());

        addDatejButton.setText("Add date");
        addDatejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDatejButtonActionPerformed(evt);
            }
        });
        jPanel3.add(addDatejButton, new java.awt.GridBagConstraints());

        jLabel1.setText("Smartphone IPs:");
        jPanel3.add(jLabel1, new java.awt.GridBagConstraints());

        smartPhoneIPjTextField.setText(gp.getProperty(CamSyncProperties.SMARTPHONE_IPS)
        );
        smartPhoneIPjTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smartPhoneIPjTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(smartPhoneIPjTextField, gridBagConstraints);

        moveFilesjCheckBox.setSelected(Boolean.parseBoolean(gp.getProperty(CamSyncProperties.ASTRO_MOVE_FILES))
        );
        moveFilesjCheckBox.setText("Move files");
        moveFilesjCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveFilesjCheckBoxActionPerformed(evt);
            }
        });
        jPanel3.add(moveFilesjCheckBox, new java.awt.GridBagConstraints());

        add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Smartphone Actions"));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        confirmSPjButton.setText("Confirm Dialog");
        confirmSPjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmSPjButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(confirmSPjButton, gridBagConstraints);

        startProcessorjButton.setText("Start Queue");
        startProcessorjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startProcessorjButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(startProcessorjButton, gridBagConstraints);

        pauseQueuejButton.setText("Pause Queue");
        pauseQueuejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseQueuejButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel5.add(pauseQueuejButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jPanel5, gridBagConstraints);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Current Job"));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        addJobjButton.setText("Add Job");
        addJobjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJobjButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(addJobjButton, gridBagConstraints);

        modifyJobjButton.setText("Modify Job");
        modifyJobjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyJobjButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(modifyJobjButton, gridBagConstraints);

        removeJobjButton.setText("Remove Job");
        removeJobjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeJobjButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(removeJobjButton, gridBagConstraints);

        removeallJobsjButton.setText("Remove all Jobs");
        removeallJobsjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeallJobsjButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel8.add(removeallJobsjButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel6.add(jPanel8, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Series"));

        addDefaultjButton.setText("Add default");
        addDefaultjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDefaultjButtonActionPerformed(evt);
            }
        });
        jPanel7.add(addDefaultjButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel6.add(jPanel7, gridBagConstraints);

        add(jPanel6, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void smartPhoneIPjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smartPhoneIPjTextFieldActionPerformed

        gp.setProperty(GlobalProperties.CamSyncProperties.SMARTPHONE_IPS, smartPhoneIPjTextField.getText());
    }//GEN-LAST:event_smartPhoneIPjTextFieldActionPerformed

    private void startAstroModejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startAstroModejButtonActionPerformed

        if (status == Status.PAUSED) {
            SmartPhoneWrapper.checkConnection();
            //   updateTablejButtonActionPerformed(evt);
            ftpConnection.addFTPConnectionListenerOnce(this);
            final AstroModeJPanel acf = this;
            if (updateTimer != null) {
                updateTimer.stop();
            }
            updateTimer = new Timer(0, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    AstroModeBackgroundUpdater ambu = new AstroModeBackgroundUpdater(updateTimer, acf);
                    ambu.start();
                }
            });
            updateTimer.start();
            status = Status.RUNNING;

        } else {
            ftpConnection.removeFTPConnectionListener(this);
            status = Status.PAUSED;
            updateTimer.stop();
        }
        startAstroModejButton.setText(status.name());
    }//GEN-LAST:event_startAstroModejButtonActionPerformed

    private void updateTablejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTablejButtonActionPerformed
        update();
    }//GEN-LAST:event_updateTablejButtonActionPerformed

    private void astroFolderjTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_astroFolderjTextFieldKeyReleased
        astroFolderjTextFieldActionPerformed(null);
    }//GEN-LAST:event_astroFolderjTextFieldKeyReleased

    private void astroFolderjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_astroFolderjTextFieldActionPerformed
        gp.setProperty(CamSyncProperties.LAST_ASTRO_FOLDER, astroFolderjTextField.getText());
    }//GEN-LAST:event_astroFolderjTextFieldActionPerformed

    private void chooseDirjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseDirjButtonActionPerformed
        JFileChooser jfc = new JFileChooser(astroFolderjTextField.getText());
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            astroFolderjTextField.setText(jfc.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_chooseDirjButtonActionPerformed

    PhotoSerie fromTextFields(PhotoSerie myPs) {

        myPs.setProject(projectTextField.getText());
        myPs.setSeriesName(seriesTextField.getText());
        myPs.setNumber(Integer.parseInt(numberTextField.getText()));
        myPs.setExposure(Formats.toLong(exposureTextField.getText()));
        myPs.setInitialDelay(Formats.toLong(initialDelayTextField.getText()));
        myPs.setDelayAfterEachExposure(Formats.toLong(delayAfterEachTextField.getText()));
        return myPs;
    }
    private void addJobjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJobjButtonActionPerformed
        PhotoSerie myPs = new PhotoSerie();
        try {
            SmartPhoneWrapper.addJob(fromTextFields(myPs));
        } catch (IOException ex) {
            Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //   updateTablejButtonActionPerformed(evt);
    }//GEN-LAST:event_addJobjButtonActionPerformed

    private void modifyJobjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyJobjButtonActionPerformed
        if (jobjTable.getSelectedRow() >= 0) {
            PhotoSerie ps = jobList.get(jobjTable.getSelectedRow());
            SmartPhoneWrapper.updateJob(fromTextFields(ps));
            //   updateTablejButtonActionPerformed(evt);
        }
    }//GEN-LAST:event_modifyJobjButtonActionPerformed

    private void confirmSPjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmSPjButtonActionPerformed
        try {
            SmartPhoneWrapper.getFromSmartPhone(WebService.WebCommands.confirmdialog, true);
        } catch (IOException ex) {
            Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //  updateTablejButtonActionPerformed(evt);
    }//GEN-LAST:event_confirmSPjButtonActionPerformed

    private void pauseQueuejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseQueuejButtonActionPerformed
        try {
            SmartPhoneWrapper.getFromSmartPhone(WebService.WebCommands.pausejobprocessor, true);
        } catch (IOException ex) {
            Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //   updateTablejButtonActionPerformed(evt);
    }//GEN-LAST:event_pauseQueuejButtonActionPerformed

    private void startProcessorjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startProcessorjButtonActionPerformed
        try {
            SmartPhoneWrapper.getFromSmartPhone(WebService.WebCommands.startjobprocessor, true);
        } catch (IOException ex) {
            Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //   updateTablejButtonActionPerformed(evt);
    }//GEN-LAST:event_startProcessorjButtonActionPerformed

    private void removeJobjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeJobjButtonActionPerformed
        if (jobjTable.getSelectedRow() >= 0) {
            PhotoSerie ps = jobList.get(jobjTable.getSelectedRow());
            try {
                SmartPhoneWrapper.remove(fromTextFields(ps));
                //   updateTablejButtonActionPerformed(evt);
            } catch (IOException ex) {
                Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_removeJobjButtonActionPerformed

    private void addDefaultjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDefaultjButtonActionPerformed
        AddDefaultSeriesJDialog asjd = new AddDefaultSeriesJDialog(parentFrame, true);
        asjd.setLocationRelativeTo(this);
        asjd.setVisible(true);
        if (!asjd.approved) {
            return;
        };
        PhotoSerie testShots = new PhotoSerie();
        testShots.setSeriesName(PhotoSerie.TESTSHOTS);
        testShots.setProject(asjd.project);
        testShots.setExposure(asjd.exposure);
        testShots.setNumber(1);

        PhotoSerie lights = new PhotoSerie();
        lights.setSeriesName(PhotoSerie.LIGHTS);
        lights.setProject(asjd.project);
        lights.setExposure(asjd.exposure);
        lights.setNumber(20);
        lights.setInitialDelay(2000);
        lights.setDelayAfterEachExposure(5000);

        PhotoSerie darks = new PhotoSerie();
        darks.setSeriesName(PhotoSerie.DARKS);
        darks.setProject(asjd.project);
        darks.setExposure(asjd.exposure);
        darks.setNumber(10);
        darks.setDelayAfterEachExposure(5000);

        try {
            SmartPhoneWrapper.addJob(testShots);
            SmartPhoneWrapper.addJob(lights);
            SmartPhoneWrapper.addJob(darks);
        } catch (IOException ex) {
            Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addDefaultjButtonActionPerformed

    private void addDatejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDatejButtonActionPerformed
        GlobalProperties gp = new GlobalProperties();
        SimpleDateFormat sdf = new SimpleDateFormat(
                gp.getProperty(CamSyncProperties.DATE_FORMAT));

        String text = astroFolderjTextField.getText();
        if (text.contains("20") && text.contains("_")) {//Most likely already contains a date
            text = text.substring(0, text.length() - 11);
        }
        text += System.getProperty("file.separator") + sdf.format(new Date(System.currentTimeMillis()));
        astroFolderjTextField.setText(text);
    }//GEN-LAST:event_addDatejButtonActionPerformed

    private void removeallJobsjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeallJobsjButtonActionPerformed
        if (JOptionPane.showConfirmDialog(parentFrame, "Really remove all jobs?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            for (PhotoSerie ps : jobList) {
                try {
                    SmartPhoneWrapper.remove(ps);
                } catch (IOException ex) {
                    Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_removeallJobsjButtonActionPerformed

    private void moveFilesjCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveFilesjCheckBoxActionPerformed
        gp.setProperty(CamSyncProperties.ASTRO_MOVE_FILES, "" + moveFilesjCheckBox.isSelected());
    }//GEN-LAST:event_moveFilesjCheckBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDatejButton;
    private javax.swing.JButton addDefaultjButton;
    private javax.swing.JButton addJobjButton;
    private javax.swing.JTextField astroFolderjTextField;
    private javax.swing.JButton chooseDirjButton;
    private javax.swing.JButton confirmSPjButton;
    private javax.swing.JTextField delayAfterEachTextField;
    private javax.swing.JTextField exposureTextField;
    private javax.swing.JTextField initialDelayTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jobjTable;
    private javax.swing.JButton modifyJobjButton;
    private javax.swing.JCheckBox moveFilesjCheckBox;
    private javax.swing.JTextField numberTextField;
    private javax.swing.JButton pauseQueuejButton;
    private javax.swing.JTextField projectTextField;
    private javax.swing.JButton removeJobjButton;
    private javax.swing.JButton removeallJobsjButton;
    private javax.swing.JTextField seriesTextField;
    private javax.swing.JTextField smartPhoneIPjTextField;
    private javax.swing.JLabel smartPhoneStatus;
    private javax.swing.JButton startAstroModejButton;
    private javax.swing.JButton startProcessorjButton;
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
                    break;
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
            createFolderAndParents(targetFolder);
            boolean moved = false;
            int fileexistscounter = 0;
            while (!moved) {
                try {
                    try {
                        if (Boolean.parseBoolean(gp.getProperty(CamSyncProperties.ASTRO_MOVE_FILES))) {
                            FileUtils.moveFileToDirectory(receivedFile, targetFolder, true);
                        }
                        moved = true;
                    } catch (FileExistsException fee) {
                        fileexistscounter++;
                        int extInedx = receivedFile.getAbsolutePath().lastIndexOf(".");
                        String newFileName = receivedFile.getAbsolutePath().substring(0, extInedx) + "_" + fileexistscounter + receivedFile.getAbsolutePath().substring(extInedx);
                        receivedFile.renameTo(new File(newFileName));
                        receivedFile = new File(newFileName);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (message.toUpperCase().endsWith(".NEF")) {
                myPS.setReceived(myPS.getReceived() + 1);

                SmartPhoneWrapper.SmartPhoneStatus smartPhoneStatus;
                do {
                    try {
                        smartPhoneStatus = SmartPhoneWrapper.update(myPS);
                    } catch (IOException ex) {
                        Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
                        smartPhoneStatus = SmartPhoneWrapper.SmartPhoneStatus.ERROR;
                        SmartPhoneWrapper.checkConnection();
                    }
                } while (smartPhoneStatus != SmartPhoneWrapper.SmartPhoneStatus.CONNECTED);
            }
            finalizeCompletelyFinishedJobs();
            gp.saveStoredAstroJobList(jobList);
            updateTablejButtonActionPerformed(null);
        }
    }

    void finalizeCompletelyFinishedJobs() {
        PhotoSerie myJob = null;
        for (PhotoSerie job : jobList) {
            if (job.getTriggerStatus() == PhotoSerie.TriggerJobStatus.FINISHED_TRIGGERING) {
                if (job.getTriggered() == job.getReceived()) {
                    myJob = job;
                    break;
                }
            }
        }
        if (myJob != null) {
            File targetFolder = new File(
                    gp.getProperty(CamSyncProperties.LAST_ASTRO_FOLDER)
                    + System.getProperty("file.separator")
                    + myJob.getProject() + System.getProperty("file.separator")
                    + myJob.getSeriesName());
            File azoinfoFile = new File(targetFolder, "azojob.txt");
            File azojsonFile = new File(targetFolder, "azojob.json");
            FileOutputStream fos1, fos2;
            try {
                fos1 = new FileOutputStream(azoinfoFile);
                IOUtils.copy(new StringReader(myJob.shortDescription()), fos1);
                fos1.close();

                fos2 = new FileOutputStream(azojsonFile);
                IOUtils.copy(new StringReader(myJob.toJSONObject().toString()), fos2);
                fos2.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

            /*
             jobList.remove(myJob);
             try {
      
             SmartPhoneWrapper.remove(myJob);
             } catch (IOException ex) {
             Logger.getLogger(AstroModeJPanel.class.getName()).log(Level.SEVERE, null, ex);
             }
             */
        }

    }

    void createFolderAndParents(File folder) {
        if (!folder.getParentFile().exists()) {
            createFolderAndParents(folder.getParentFile());
        }
        if (!folder.exists()) {
            folder.mkdir();
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

        } catch (IOException | JSONException ex) {
            try {
                SmartPhoneWrapper.checkConnection();
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
