/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd;

import de.quadrillenschule.azocamsyncd.gui.AZoCamSyncJFrame;
import com.sun.webkit.Timer;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnection;
import de.quadrillenschule.azocamsyncd.gui.ConfigurationWizardJFrame;
import java.awt.Dialog;
import java.io.File;
import javax.swing.SwingWorker;

/**
 *
 * @author Andreas
 */
public class AZoCamSyncDesktop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (!new File(System.getProperty("user.home") + System.getProperty("file.separator") + "azocamsync", "azocamsync.props").exists()){
            ConfigurationWizardJFrame cwj=new ConfigurationWizardJFrame();
            cwj.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
         //   cwj.setVisible(true);
            cwj.setModal(true);
            cwj.toFront();
            cwj.setVisible(true);
        }
        AZoCamSyncJFrame f = new AZoCamSyncJFrame();
        f.setVisible(true);
        
        f.startService();
    }

}
