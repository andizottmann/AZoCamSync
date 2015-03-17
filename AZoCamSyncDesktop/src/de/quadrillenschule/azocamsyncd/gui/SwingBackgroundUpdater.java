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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author Andreas
 */
public class SwingBackgroundUpdater extends Thread {

    FTPConnection ftpConnection;
    LocalStorage localStorage;
    private Timer timer;
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
            getTimer().stop();
            return;
        }
        ftpConnection.close();
        isActive = true;
        getTimer().setInitialDelay(getTimer().getDelay());
        getTimer().stop();
        LinkedList<AZoFTPFile> retval = ftpConnection.checkConnection(false);
        if (retval != null) {
            ftpConnection.notify(FTPConnectionListener.FTPConnectionStatus.NUMBER_OF_FILES_DETECTED, "" + retval.size(), -1);
            localStorage.removeSyncedFileEntriesNotOnList(retval);
            LinkedList<AZoFTPFile> r = new LinkedList<>();
            for (AZoFTPFile af : retval) {
                try {
                    if (!localStorage.equalsLocal(af)) {
                        if (!localStorage.isFileSynced(af)) {
                            r.add(af);
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(AZoCamSyncJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            };

            if ((r != null) && (r.size() == 0)) {
                ftpConnection.setLooksFullySynced(true);
                ftpConnection.notify(FTPConnectionListener.FTPConnectionStatus.FULLY_SYNCED, "" + r.size(), -1);

            }
            if ((r != null) && (r.size() > 0)) {
                ftpConnection.setLooksFullySynced(false);
                Collections.sort(r, new Comparator<AZoFTPFile>() {

                    @Override
                    public int compare(AZoFTPFile o1, AZoFTPFile o2) {
                        return o1.ftpFile.getName().compareTo(o2.ftpFile.getName());
                    }
                });

                ftpConnection.notify(FTPConnectionListener.FTPConnectionStatus.NUMBER_OF_SYNCHRONISABLE_FILES_DETECTED, "" + r.size(), -1);
                ftpConnection.notify(FTPConnectionListener.FTPConnectionStatus.CONNECTED, ftpConnection.getLastWorkingConnection(), -1);

                LinkedList<AZoFTPFile> downloaded = ftpConnection.download(r, localStorage);
                if (downloaded.size() == r.size()) {
                    ftpConnection.setLooksFullySynced(true);
                } else {
                    ftpConnection.setLooksFullySynced(false);
                };
            }

            try {
                //     ftpConnection.deleteFiles(Integer.parseInt(gp.getProperty(GlobalProperties.CamSyncProperties.SD_FILELIMIT)),localStorage);
            } catch (NumberFormatException nfe) {
            }
            ftpConnection.remountSD();

            ftpConnection.notify(FTPConnectionListener.FTPConnectionStatus.CONNECTED, ftpConnection.getLastWorkingConnection(), -1);

        }

        ftpConnection.close();

        isActive = false;
        if (ftpConnection.isLooksFullySynced()) {
            getTimer().setDelay(1000 * Integer.parseInt(gp.getProperty(GlobalProperties.CamSyncProperties.PULLINTERVALLSECS)));
            getTimer().setInitialDelay(1000 * Integer.parseInt(gp.getProperty(GlobalProperties.CamSyncProperties.PULLINTERVALLSECS)));
        } else {
            getTimer().setDelay(500);

            getTimer().setDelay(500);
        }
        getTimer().start();

    }

    String[] removeSyncedButNonExistingOnSD(LinkedList<AZoFTPFile> afs, String[] syncedFiles) {
        LinkedList<String> retval = new LinkedList<>();

        for (String s : syncedFiles) {

            if (containsFTPName(afs, s)) {
                retval.add(s);
            } else {
                System.out.println("not on sd");
            }

        }
        return retval.toArray(new String[retval.size()]);
    }

    boolean containsFTPName(LinkedList<AZoFTPFile> afs, String ftpString) {
        int i = 0;
        for (AZoFTPFile a : afs) {
            if (a.equalsFTPName(ftpString)) {
                return true;
            }
            i++;
        }
        return false;
    }

    /**
     * @return the timer
     */
    public Timer getTimer() {
        return timer;
    }

    @Override
    public void interrupt() {
        super.interrupt();

    }
}
