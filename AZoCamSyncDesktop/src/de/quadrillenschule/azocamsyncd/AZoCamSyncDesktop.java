/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd;

import de.quadrillenschule.azocamsyncd.gui.AZoCamSyncJFrame;
import com.sun.webkit.Timer;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnection;
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
        AZoCamSyncJFrame f=new AZoCamSyncJFrame();
        f.setVisible(true);
        f.startService();
    }

}
