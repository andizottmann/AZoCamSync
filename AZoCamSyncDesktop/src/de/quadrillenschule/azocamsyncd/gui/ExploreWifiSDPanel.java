/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.gui;

import de.quadrillenschule.azocamsyncd.LocalStorage;
import de.quadrillenschule.azocamsyncd.ftpservice.AZoFTPFile;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnection;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;

/**
 *
 * @author Andreas
 */
public class ExploreWifiSDPanel extends javax.swing.JPanel {

    DefaultMutableTreeNode rootNode;
    private FTPConnection ftpConnection;
    private LocalStorage localStorage;
    LinkedList<AZoFTPFile> afs;

    /**
     * Creates new form ExploreWifiSDPanel
     */
    public ExploreWifiSDPanel() {
        initComponents();
        rootNode = new DefaultMutableTreeNode("/");
        DefaultTreeModel dtm = new DefaultTreeModel(rootNode);
        remotejTree.setModel(dtm);
        remotejTree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                updateSingleView();
            }
        });
        remotejTree.setCellRenderer(new TreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                JLabel retval = new JLabel(value.toString());
                AZoFTPFile myaffile = null;
                if (afs == null) {
                    return retval;
                }
                for (AZoFTPFile af : afs) {
                    if (new String(af.dir + af.ftpFile.getName()).equals(value.toString())) {
                        myaffile = af;
                        break;
                    }
                }
                try {
                    if (!localStorage.getLocalFile(myaffile).exists()) {
                        if (!myaffile.ftpFile.isDirectory()) {
                            retval.setForeground(Color.red);
                        }
                    }
                } catch (Exception ex) {
                    //           Logger.getLogger(ExploreWifiSDPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (selected) {
                    retval.setOpaque(true);
                    retval.setBackground(Color.darkGray);
                }
                return retval;
            }
        });
    }

    private void updateSingleView() {
        try {
            if (remotejTree.getSelectionPaths().length > 0) {
                TreePath tp = remotejTree.getSelectionPaths()[0];
                String mynode = tp.getLastPathComponent().toString();
                AZoFTPFile myaffilea = null;
                for (AZoFTPFile af : afs) {
                    if (new String(af.dir + af.ftpFile.getName()).equals(mynode)) {
                        myaffilea = af;
                        break;
                    }
                }
                final AZoFTPFile myaffile = myaffilea;
                Thread imageUpdater = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            File localFile;

                            localFile = localStorage.getLocalFile(myaffile);

                            localFileNamejTextField1.setText(localFile.getAbsolutePath());
                            ImageIcon ii = new ImageIcon(localFile.toURI().toURL());

                            int mywidth = imagejLabel.getWidth();
                            int width = ii.getIconWidth();
                            int height = ii.getIconHeight();
                            if (width <= 0) {
                                imagejLabel.setText("No image to view.");
                            } else {
                                imagejLabel.setText("");
                            }
                            double factor = (double) height / (double) width;
                            Image image = ii.getImage().getScaledInstance(mywidth, (int) ((double) mywidth * factor), Image.SCALE_FAST);
                            imagejLabel.setIcon(new ImageIcon(image));
                        } catch (IOException ex) {
                            imagejLabel.setText("No image to view.");
                        }
                    }
                });
                imageUpdater.start();

            }
        } catch (Exception e) {

        }
    }

    private void createNodes(DefaultMutableTreeNode top) {
        afs = ftpConnection.checkConnection(true);
        if (afs != null) {
            createSubNodes(top, afs);
        }
    }

    private void createSubNodes(DefaultMutableTreeNode parent, LinkedList<AZoFTPFile> afs) {
        String parentNodeName = parent.toString();
        for (AZoFTPFile af : afs) {
            String nodeName = af.dir + af.ftpFile.getName();
            if (af.ftpFile.isDirectory()) {
                if (!parentNodeName.equals(nodeName)) {

                    if (nodeName.contains(parentNodeName)) {
                        if (StringUtils.countMatches(nodeName, "/") - 1 == StringUtils.countMatches(parentNodeName, "/")) {
                            DefaultMutableTreeNode tn = new DefaultMutableTreeNode(nodeName);
                            parent.add(tn);
                            createSubNodes(tn, afs);
                        }
                    };
                }
            }
//isFile
            if (af.ftpFile.isFile()) {
                if (nodeName.contains(parentNodeName)) {
                    if (StringUtils.countMatches(nodeName, "/") == 1 + StringUtils.countMatches(parentNodeName, "/")) {
                        DefaultMutableTreeNode tn = new DefaultMutableTreeNode(nodeName);

                        parent.add(tn);

                    }
                };
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
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        remotejTree = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        localFileNamejTextField1 = new javax.swing.JTextField();
        imagejLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        deletejButton1 = new javax.swing.JButton();
        openLocalFilejButton = new javax.swing.JButton();
        openfolderjButton = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Manage Files on Remote WiFI SD"));
        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setPreferredSize(new java.awt.Dimension(250, 350));
        jScrollPane1.setViewportView(remotejTree);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);
        add(jPanel1, new java.awt.GridBagConstraints());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected File"));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        localFileNamejTextField1.setEditable(false);
        localFileNamejTextField1.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(localFileNamejTextField1, gridBagConstraints);

        imagejLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagejLabel.setText("No viewable image selected.");
        imagejLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        imagejLabel.setMaximumSize(new java.awt.Dimension(225, 150));
        imagejLabel.setMinimumSize(new java.awt.Dimension(225, 150));
        imagejLabel.setPreferredSize(new java.awt.Dimension(225, 150));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(imagejLabel, gridBagConstraints);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        deletejButton1.setText("Delete remote files...");
        deletejButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletejButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(deletejButton1);

        openLocalFilejButton.setText("Open (local) File...");
        openLocalFilejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openLocalFilejButtonActionPerformed(evt);
            }
        });
        jPanel3.add(openLocalFilejButton);

        openfolderjButton.setText("Open (local) Folder...");
        openfolderjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openfolderjButtonActionPerformed(evt);
            }
        });
        jPanel3.add(openfolderjButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jPanel2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
 DefaultTreeModel dtm;

    public void updateTree() {
        Enumeration<TreePath> storeExpand = remotejTree.getExpandedDescendants(new TreePath(remotejTree.getModel().getRoot()));
        rootNode.removeAllChildren();
        createNodes(rootNode);
        if (dtm == null) {
            dtm = new DefaultTreeModel(rootNode);
            remotejTree.setModel(dtm);

        }
        dtm.nodeStructureChanged(rootNode);
        if (storeExpand != null) {
            while (storeExpand.hasMoreElements()) {
                TreePath t = storeExpand.nextElement();
                remotejTree.expandPath(t);
                remotejTree.setSelectionPath(t);
            }
        }
    }
    private void deletejButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletejButton1ActionPerformed
        LinkedList<String> deleteables = new LinkedList<>();
        for (TreePath tp : remotejTree.getSelectionPaths()) {
            //  System.out.println("Woudl delete:"+tp.getLastPathComponent().toString());
            deleteables.add(tp.getLastPathComponent().toString());
        }
        if (JOptionPane.showConfirmDialog(deletejButton1, "About to delete " + deleteables.size() + " files.", "Delete Files on Remote?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
            ftpConnection.close();
            ftpConnection.remountSD(deleteables);
            updateTree();
        }
    }//GEN-LAST:event_deletejButton1ActionPerformed

    private void openLocalFilejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openLocalFilejButtonActionPerformed
        try {
            Desktop.getDesktop().open(new File(localFileNamejTextField1.getText()));
        } catch (Exception ex) {
            Logger.getLogger(AZoCamSyncJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_openLocalFilejButtonActionPerformed

    private void openfolderjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openfolderjButtonActionPerformed
        try {
            Desktop.getDesktop().open(new File(localFileNamejTextField1.getText()).getParentFile());
        } catch (Exception ex) {
            Logger.getLogger(AZoCamSyncJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_openfolderjButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deletejButton1;
    private javax.swing.JLabel imagejLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField localFileNamejTextField1;
    private javax.swing.JButton openLocalFilejButton;
    private javax.swing.JButton openfolderjButton;
    private javax.swing.JTree remotejTree;
    // End of variables declaration//GEN-END:variables

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

    /**
     * @return the localStorage
     */
    public LocalStorage getLocalStorage() {
        return localStorage;
    }

    /**
     * @param localStorage the localStorage to set
     */
    public void setLocalStorage(LocalStorage localStorage) {
        this.localStorage = localStorage;
    }
}
