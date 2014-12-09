/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.gui;

import de.quadrillenschule.azocamsyncd.GlobalProperties;
import de.quadrillenschule.azocamsyncd.LocalStorage;
import de.quadrillenschule.azocamsyncd.ftpservice.AZoFTPFile;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnection;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnectionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import javax.swing.Timer;

/**
 *
 * @author Andreas
 */
public class SwingBackgroundUpdater extends Thread {

    FTPConnection ftpConnection;
    LocalStorage localStorage;
    Timer timer;
    GlobalProperties gp;
    public static boolean isActive = false;

    public SwingBackgroundUpdater(GlobalProperties gp, FTPConnection ftpConnection, LocalStorage localStorage, Timer timer) {
        super();
        this.ftpConnection = ftpConnection;
        this.localStorage = localStorage;
        this.timer = timer;
        this.gp = gp;
    }

    @Override
    public void run() {
        if (isActive) {
            timer.stop();
            return;
        }
        isActive = true;
        timer.setInitialDelay(timer.getDelay());
        timer.stop();

        LinkedList<AZoFTPFile> retval = ftpConnection.checkConnection(false);
        if (retval != null) {
            ftpConnection.notify(FTPConnectionListener.FTPConnectionStatus.NUMBER_OF_FILES_DETECTED, "" + retval.size(), -1);

            LinkedList<AZoFTPFile> r = new LinkedList<>();
            for (AZoFTPFile af : retval) {
                try {
                    if (!localStorage.equalsLocal(af)) {
                        r.add(af);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(AZoCamSyncJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            };
            if ((r != null) && (r.size() > 0)) {
                ftpConnection.notify(FTPConnectionListener.FTPConnectionStatus.NUMBER_OF_SYNCHRONISABLE_FILES_DETECTED, "" + r.size(), -1);
                ftpConnection.notify(FTPConnectionListener.FTPConnectionStatus.CONNECTED, ftpConnection.getLastWorkingConnection(), -1);

                ftpConnection.download(r, localStorage);
                try {
                    ftpConnection.deleteFiles(Integer.parseInt(gp.getProperty(GlobalProperties.CamSyncProperties.SD_FILELIMIT)));
                } catch (NumberFormatException nfe) {
                }

            }
            ftpConnection.notify(FTPConnectionListener.FTPConnectionStatus.CONNECTED, ftpConnection.getLastWorkingConnection(), -1);

        }

        isActive = false;
        timer.start();

    }

}
