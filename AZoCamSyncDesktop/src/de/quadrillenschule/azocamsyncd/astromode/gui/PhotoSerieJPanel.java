/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode.gui;

import de.quadrillenschule.azocamsyncd.GlobalProperties;
import de.quadrillenschule.azocamsyncd.astromode.PhotoSerie;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnectionListener;
import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;

/**
 *
 * @author Andreas
 */
public class PhotoSerieJPanel extends javax.swing.JPanel {

    private PhotoSerie photoSerie;
    private PhotoProjectJPanel projectPanel;

    /**
     * Creates new form PhotSerieJPanel
     */
    public PhotoSerieJPanel(PhotoSerie photoSerie, PhotoProjectJPanel projectPanel) {
        this.photoSerie = photoSerie;
        this.projectPanel = projectPanel;
        initComponents();
        update();
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
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        currentFilesjLabel = new javax.swing.JLabel();
        statusjLabel = new javax.swing.JLabel();
        folderjLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        namejTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        numberOfPlannedFilesjTextField = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 51), 4));
        setEnabled(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(new java.awt.GridBagLayout());

        currentFilesjLabel.setText("Current number of files: "+photoSerie.getPhotos().size());
        currentFilesjLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(currentFilesjLabel, gridBagConstraints);

        statusjLabel.setText(photoSerie.getStatus().name());
        statusjLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(statusjLabel, gridBagConstraints);

        folderjLabel.setText(photoSerie.getFolder().getAbsolutePath());
        folderjLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        add(folderjLabel, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Series", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N
        jPanel1.setLayout(new java.awt.GridLayout());

        namejTextField.setText(photoSerie.getName());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), namejTextField, org.jdesktop.beansbinding.BeanProperty.create("editable"));
        bindingGroup.addBinding(binding);

        namejTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        jPanel1.add(namejTextField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 0.5;
        add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Number of Files", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 10))); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout());

        numberOfPlannedFilesjTextField.setText("jTextField1");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), numberOfPlannedFilesjTextField, org.jdesktop.beansbinding.BeanProperty.create("editable"));
        bindingGroup.addBinding(binding);

        numberOfPlannedFilesjTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        numberOfPlannedFilesjTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                numberOfPlannedFilesjTextFieldKeyReleased(evt);
            }
        });
        jPanel2.add(numberOfPlannedFilesjTextField);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        add(jPanel2, gridBagConstraints);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void numberOfPlannedFilesjTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numberOfPlannedFilesjTextFieldKeyReleased
        photoSerie.setNumberOfPlannedPhotos(Integer.parseInt(numberOfPlannedFilesjTextField.getText()));
    }//GEN-LAST:event_numberOfPlannedFilesjTextFieldKeyReleased

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        projectPanel.clickedOnPanel(this);
    }//GEN-LAST:event_formMouseClicked

    public void update() {
        numberOfPlannedFilesjTextField.setText("" + getPhotoSerie().getNumberOfPlannedPhotos());
        currentFilesjLabel.setText("Current number of files: " + getPhotoSerie().getPhotos().size());
      
        statusjLabel.setText(photoSerie.getStatus().name());
        folderjLabel.setText(photoSerie.getFolder().getAbsolutePath());
        setBorderForState(false);

    }

    public void setBorderForState(boolean selected) {
        int width = 2;
        if (selected) {
            width = 6;
        }
        LineBorder border = (LineBorder) getBorder();
        switch (getPhotoSerie().getStatus()) {
            case RECEIVING_FILES:
                border = new LineBorder(Color.blue, width);
                break;
            case NEW:
                border = new LineBorder(Color.gray, width);
                break;
            case COMPLETED:
                border = new LineBorder(Color.green, width);
                break;
        }
        setBorder(border);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel currentFilesjLabel;
    private javax.swing.JLabel folderjLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField namejTextField;
    private javax.swing.JTextField numberOfPlannedFilesjTextField;
    private javax.swing.JLabel statusjLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the photoSerie
     */
    public PhotoSerie getPhotoSerie() {
        return photoSerie;
    }

    /**
     * @param photoSerie the photoSerie to set
     */
    public void setPhotoSerie(PhotoSerie photoSerie) {
        this.photoSerie = photoSerie;
    }
}
