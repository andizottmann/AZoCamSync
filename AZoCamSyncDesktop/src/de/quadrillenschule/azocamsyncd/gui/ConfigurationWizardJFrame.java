/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.gui;

import de.quadrillenschule.azocamsyncd.GlobalProperties;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.html.HTMLDocument;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Andreas
 */
public class ConfigurationWizardJFrame extends javax.swing.JFrame {

    LinkedList<JPanel> stepPanels = new LinkedList();
    int selectedPanel = 0;
    GlobalProperties gp;

    /**
     * Creates new form ConfigurationWizardJFrame
     */
    public ConfigurationWizardJFrame() {
        gp = new GlobalProperties();
        initComponents();
        stepPanels.add(step1jPanel);
        stepPanels.add(step2jPanel);
        stepPanels.add(step3jPanel);
        setSize(new Dimension(800, 600));
        updateSelectedPanel(0);
    }

    public void updateSelectedPanel(int i) {
        String resource = "/de/quadrillenschule/azocamsyncd/ftpservice/res/step" + i + ".html";
        selectedPanel = i;

        for (JPanel j : stepPanels) {
            j.setEnabled(false);
            j.setBackground(Color.lightGray);

        }
        stepPanels.get(i).setEnabled(true);
        stepPanels.get(i).setBackground(Color.white);
        showDocument(resource);
    }

    void showDocument(String resource) {
        HTMLDocument h1 = new HTMLDocument();
        h1.setBase(getClass().getResource("/de/quadrillenschule/azocamsyncd/ftpservice/res/"));
        jTextPane1.setDocument(h1);

        try {
            jTextPane1.setText(FileUtils.readFileToString(new File(getClass().getResource(resource).toURI())));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ConfigurationWizardJFrame.class.getName()).log(Level.SEVERE, null, ex);
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

        jSplitPane1 = new javax.swing.JSplitPane();
        stepsjPanel = new javax.swing.JPanel();
        step1jPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        autoruninstalljButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        skipjButton = new javax.swing.JButton();
        step2jPanel = new javax.swing.JPanel();
        step3jPanel = new javax.swing.JPanel();
        infojPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout());

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(1);
        jSplitPane1.setResizeWeight(0.5);

        stepsjPanel.setLayout(new javax.swing.BoxLayout(stepsjPanel, javax.swing.BoxLayout.PAGE_AXIS));

        step1jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Step 1: Prepare WiFI SD card"));
        step1jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                step1jPanelMouseClicked(evt);
            }
        });
        step1jPanel.setLayout(new java.awt.BorderLayout());

        jPanel2.setOpaque(false);

        autoruninstalljButton1.setText("Save file to main folder of SD Card...");
        autoruninstalljButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoruninstalljButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(autoruninstalljButton1);

        step1jPanel.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        skipjButton.setText("Skip this step");
        skipjButton.setOpaque(false);
        skipjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipjButtonActionPerformed(evt);
            }
        });
        jPanel1.add(skipjButton);

        step1jPanel.add(jPanel1, java.awt.BorderLayout.SOUTH);

        stepsjPanel.add(step1jPanel);

        step2jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Step 2: Setup IP adresses of WiFi SD card"));
        step2jPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                step2jPanelMouseClicked(evt);
            }
        });
        step2jPanel.setLayout(new java.awt.BorderLayout());
        stepsjPanel.add(step2jPanel);

        step3jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Step 3: Configure local storage"));
        step3jPanel.setLayout(new java.awt.BorderLayout());
        stepsjPanel.add(step3jPanel);

        jSplitPane1.setLeftComponent(stepsjPanel);

        infojPanel.setLayout(new java.awt.GridLayout());

        jTextPane1.setEditable(false);
        jTextPane1.setContentType("text/html"); // NOI18N
        jTextPane1.setMargin(new java.awt.Insets(10, 20, 10, 10));
        jScrollPane2.setViewportView(jTextPane1);

        infojPanel.add(jScrollPane2);

        jSplitPane1.setRightComponent(infojPanel);

        getContentPane().add(jSplitPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void step1jPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_step1jPanelMouseClicked
        updateSelectedPanel(0);


    }//GEN-LAST:event_step1jPanelMouseClicked

    private void step2jPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_step2jPanelMouseClicked
        //  jTextPane1.setText("<html><body>Step2</body></html>");
        updateSelectedPanel(1);
    }//GEN-LAST:event_step2jPanelMouseClicked

    private void skipjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipjButtonActionPerformed
        updateSelectedPanel(1);

    }//GEN-LAST:event_skipjButtonActionPerformed

    private void autoruninstalljButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoruninstalljButton1ActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File startFile = new File(System.getProperty("user.dir")); //Get the current directory

        // Find System Root
        while (!FileSystemView.getFileSystemView().isFileSystemRoot(startFile)) {
            startFile = startFile.getParentFile();
        }

        jfc.setCurrentDirectory(startFile);
         int origDriveChooserRetVal = jfc.showDialog(rootPane,"Open");
            if (origDriveChooserRetVal == JFileChooser.APPROVE_OPTION)
            {
                File dir = jfc.getSelectedFile();

            }

    }//GEN-LAST:event_autoruninstalljButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(ConfigurationWizardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConfigurationWizardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConfigurationWizardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConfigurationWizardJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConfigurationWizardJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton autoruninstalljButton1;
    private javax.swing.JPanel infojPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JButton skipjButton;
    private javax.swing.JPanel step1jPanel;
    private javax.swing.JPanel step2jPanel;
    private javax.swing.JPanel step3jPanel;
    private javax.swing.JPanel stepsjPanel;
    // End of variables declaration//GEN-END:variables
}