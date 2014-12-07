/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.gui;

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

    public SwingBackgroundUpdater(FTPConnection ftpConnection, LocalStorage localStorage, Timer timer) {
        super();
        this.ftpConnection = ftpConnection;
        this.localStorage = localStorage;
        this.timer = timer;
    }

    @Override
    public void run() {
        timer.stop();
        LinkedList<AZoFTPFile> retval = ftpConnection.checkConnection();
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
            if (r != null) {
                ftpConnection.notify(FTPConnectionListener.FTPConnectionStatus.NUMBER_OF_SYNCHRONISABLE_FILES_DETECTED,""+ r.size(), -1);

                ftpConnection.download(r, localStorage);
            }
        }
        timer.start();
    }

}
