/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.ftpservice;

import de.quadrillenschule.azocamsyncd.GlobalProperties;
import de.quadrillenschule.azocamsyncd.LocalStorage;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnectionListener.FTPConnectionStatus;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.NoRouteToHostException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.telnet.TelnetClient;

/**
 *
 * @author Andreas
 */
public class FTPConnection {

    LinkedList<FTPConnectionListener> ftpConnectionListeners = new LinkedList();

    private String[] possibleConnections = {};
    private String lastWorkingConnection = "";
    private String fileTypes[] = {"JPG", "NEF", "CR2", "TIF"};
    FTPClient ftpclient;
    LinkedList<FTPFile> remotePictureDirs = new LinkedList<>();
    public CountingOutputStream cos;
    public long downloadsize = 0;

    public FTPConnection() {

    }

    public LinkedList<AZoFTPFile> checkConnection(boolean includeDirs) {
        if (!"".equals(getLastWorkingConnection())) {
            LinkedList<String> t = new LinkedList<>();
            t.add(getLastWorkingConnection());
            for (String s : possibleConnections) {
                if (!s.equals(lastWorkingConnection)) {
                    t.add(s);
                }
            }
            possibleConnections = t.toArray(new String[0]);
        }
        for (String ip : possibleConnections) {
            notify(FTPConnectionStatus.TRYING, ip, -1);
            ftpclient = new FTPClient();

            try {
                ftpclient.setDefaultTimeout(5000);
                ftpclient.connect(ip);
                if (ftpclient.isAvailable()) {
                    LinkedList<AZoFTPFile> retval = discoverRemoteFiles("/", includeDirs);

                    lastWorkingConnection = ip;

                    close();
                    remountSD();
                    notify(FTPConnectionStatus.CONNECTED, ip, -1);

                    return retval;
                }
            } catch (IOException ex) {
              //  notify(FTPConnectionStatus.NOCONNECTION, "", -1);
                Logger.getLogger(FTPConnection.class.getName()).log(Level.INFO, null, ex);
            }

        }
 notify(FTPConnectionStatus.NOCONNECTION, "", -1);
        return null;
    }

    public void simplyConnect(int fileType) {
        boolean conn = false;
        do {
            try {
                if (ftpclient != null) {
                    close();
                }
                ftpclient = new FTPClient();
                ftpclient.setDefaultTimeout(90000);
                ftpclient.connect(getLastWorkingConnection());

                ftpclient.enterLocalPassiveMode();
                ftpclient.setFileType(fileType);
                conn = true;

            } catch (Exception ex) {
                close();
                conn = false;
                checkConnection(false);
            }
        } while (!conn);
    }

    public LinkedList<AZoFTPFile> download(LinkedList<AZoFTPFile> afs, LocalStorage localStorage) {
        if (afs.size() <= 0) {
            return afs;
        }
        LinkedList<AZoFTPFile> retval = new LinkedList<>();
        for (AZoFTPFile a : afs) {
            retval.add(a);
        }

        simplyConnect(FTP.BINARY_FILE_TYPE);
        notify(FTPConnectionStatus.CONNECTED, getLastWorkingConnection(), -1);

        for (AZoFTPFile af : afs) {
            File localFile = null;

            try {
                localFile = localStorage.getLocalFile(af);

            } catch (IOException ex) {
                notify(FTPConnectionStatus.LOCALSTORAGEERROR, af.dir + af.ftpFile.getName(), -1);
                try {
                    ftpclient.logout();

                    ftpclient.disconnect();
                } catch (IOException ex2) {
                    Logger.getLogger(FTPConnection.class.getName()).log(Level.INFO, null, ex);
                };
                return retval;
            }
            if (!localStorage.prepareLocalFile(localFile)) {
                notify(FTPConnectionStatus.LOCALSTORAGEERROR, af.dir + af.ftpFile.getName(), -1);
                try {
                    ftpclient.logout();
                    ftpclient.disconnect();
                } catch (IOException ex) {
                    Logger.getLogger(FTPConnection.class.getName()).log(Level.INFO, null, ex);
                }
                return retval;
            }
            FileOutputStream fos = null;
            InputStream is = null;
            try {

                fos = new FileOutputStream(localFile);
                is = ftpclient.retrieveFileStream(af.dir + af.ftpFile.getName());
                cos = new CountingOutputStream(fos);
                downloadsize = af.ftpFile.getSize();
                notify(FTPConnectionStatus.DOWNLOADING, af.dir + af.ftpFile.getName(), ((int) (100.0 * ((afs.indexOf(af) + 1.0) / (double) afs.size()))));

                IOUtils.copyLarge(is, cos);
                while (!ftpclient.completePendingCommand()) {
                    try {
                        Thread.currentThread().wait(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                };

                if (true) {
                    is.close();
                    fos.close();
                    localStorage.setLatestIncoming(localFile);
                    notify(FTPConnectionStatus.NEW_LOCAL_FILE, localFile.getAbsolutePath(), -1);
                    retval.remove(af);
                    notify(FTPConnectionStatus.SUCCESS, af.dir + af.ftpFile.getName(), ((int) (100.0 * ((afs.indexOf(af) + 2.0) / (double) afs.size()))));
                }
            } catch (Exception ex) {
                try {
                    is.close();
                    fos.close();
                    close();
                    localFile.delete();
                    simplyConnect(FTP.BINARY_FILE_TYPE);
                } catch (Exception ex2) {
                }

            }
        }
        close();

        return retval;
    }

    public boolean deleteSingleFile(String fullname) throws IOException {
        return ftpclient.deleteFile(fullname);
    }

    public void close() {
        try {
            ftpclient.noop();
        } catch (IOException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.WARNING, null, ex);
        }
        try {
            ftpclient.logout();

        } catch (IOException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.WARNING, null, ex);
        }
        try {
            ftpclient.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    public void deleteFiles(int remainingNumber) {
        if (remainingNumber < 0) {
            return;
        }
        ftpclient = new FTPClient();

        ftpclient.setDefaultTimeout(3000);
        try {

            ftpclient.connect(getLastWorkingConnection());
            LinkedList<AZoFTPFile> afs = discoverRemoteFiles("/", false);
            int todelete = afs.size() - remainingNumber;
            if (todelete > 0) {
                notify(FTPConnectionStatus.DELETING_FILES, "", -1);
                int i = 0;
                Collections.sort(afs, new Comparator<AZoFTPFile>() {

                    @Override
                    public int compare(AZoFTPFile o1, AZoFTPFile o2) {
                        return o1.ftpFile.getTimestamp().compareTo(o2.ftpFile.getTimestamp());
                    }
                });
                for (AZoFTPFile af : afs) {
                    i++;
                    ftpclient.deleteFile(af.dir + af.ftpFile.getName());
                    //    System.out.println("Would delete"+af.ftpFile.getName());
                    if (i >= todelete) {
                        break;
                    }
                }
                notify(FTPConnectionStatus.SUCCESS, "", -1);

                remountSD();
            }

        } catch (IOException ex) {
            close();
        }
        close();
    }

    public void remountSD() {
        try {

            if (ftpclient.isConnected()) {
                close();
            }
            TelnetClient telnetclient = new TelnetClient();

            telnetclient.connect(getLastWorkingConnection());
            IOUtils.copy(new StringReader("/usr/bin/refresh_sd\r\n"), telnetclient.getOutputStream());
            telnetclient.disconnect();
        } catch (IOException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.WARNING, null, ex);
        }

    }

    public boolean isPicture(AZoFTPFile ftpfile) {
        for (String f : fileTypes) {
            if (ftpfile.ftpFile.getName().toLowerCase().endsWith(f.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public LinkedList<AZoFTPFile> discoverRemoteFiles(String root, boolean includeDirs)
            throws IOException {
        LinkedList<AZoFTPFile> retval = new LinkedList();
        for (FTPFile f : ftpclient.listFiles(root)) {

            AZoFTPFile af = new AZoFTPFile(f, root);
            if (f.isFile() && isPicture(af) && !retval.contains(af)) {
                retval.add(af);
            }
            if (f.isDirectory()) {
                if (includeDirs) {
                    retval.add(af);
                }

                retval.addAll(discoverRemoteFiles(root + f.getName() + "/", includeDirs));
            }
        }
        return retval;

    }

    public void notify(FTPConnectionStatus status, String message, int progress) {
        for (FTPConnectionListener f : ftpConnectionListeners) {
            f.receiveNotification(status, message, progress);
        }
    }

    public void addFTPConnectionListenerOnce(FTPConnectionListener f) {
        if (!ftpConnectionListeners.contains(f)) {
            ftpConnectionListeners.add(f);
        }
    }

    /**
     * @return the possibleConnections
     */
    public String[] getPossibleConnections() {
        return possibleConnections;
    }

    /**
     * @param possibleConnections the possibleConnections to set
     */
    public void setPossibleConnections(String[] possibleConnections) {
        this.possibleConnections = possibleConnections;
    }

    /**
     * @return the fileTypes
     */
    public String[] getFileTypes() {
        return fileTypes;
    }

    /**
     * @param fileTypes the fileTypes to set
     */
    public void setFileTypes(String[] fileTypes) {
        this.fileTypes = fileTypes;
    }

    /**
     * @return the lastWorkingConnection
     */
    public String getLastWorkingConnection() {
        return lastWorkingConnection;
    }

}
