/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.ftpservice;

import de.quadrillenschule.azocamsyncd.LocalStorage;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnectionListener.FTPConnectionStatus;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
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

    private String[] possibleConnections = {"192.168.178.254","192.168.178.32"};
    private String lastWorkingConnection = "";
    private String fileTypes[] = {"JPG", "NEF", "CR2", "TIF"};
    FTPClient ftpclient;
    LinkedList<FTPFile> remotePictureDirs = new LinkedList<>();

    public FTPConnection() {
    }

    public LinkedList<AZoFTPFile> checkConnection() {
        if (lastWorkingConnection != "") {
            LinkedList<String> t = new LinkedList<>();
            t.add(lastWorkingConnection);
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
                ftpclient.setDefaultTimeout(10000);
                ftpclient.connect(ip);
                if (ftpclient.isAvailable()) {
                    // ftpclient.setSoTimeout(60000);
                    notify(FTPConnectionStatus.CONNECTED, ip, -1);
                    LinkedList<AZoFTPFile> retval = discoverRemoteFiles("/");

                    lastWorkingConnection = ip;

                    remountSD();
                    notify(FTPConnectionStatus.SUCCESS, ip, -1);
                    try {
                        ftpclient.logout();
                    } catch (Exception e) {
                    }
                    return retval;
                }

            } catch (IOException ex) {

                Logger.getLogger(FTPConnection.class.getName()).log(Level.INFO, null, ex);
            }

        }
        notify(FTPConnectionStatus.NOCONNECTION, "", -1);
        return null;
    }

    void downloadConnect() {
        boolean conn = false;
        do {
            try {
                if (ftpclient != null) {
                    try {
                        ftpclient.abort();
                        ftpclient.logout();
                    } catch (Exception e) {
                    }
                }
                //   if (ftpclient == null || !ftpclient.isAvailable()) {
                ftpclient = new FTPClient();
             //   }
                //   if (!ftpclient.isConnected()) {
                ftpclient.setDefaultTimeout(90000);
                ftpclient.connect(lastWorkingConnection);

                ftpclient.enterLocalPassiveMode();
                //   }
                ftpclient.setFileTransferMode(FTPClient.COMPRESSED_TRANSFER_MODE);
                ftpclient.setFileType(FTP.BINARY_FILE_TYPE);
                conn = true;

            } catch (Exception ex) {
                try {
                    ftpclient.logout();
                } catch (Exception ex0) {
                }
                conn = false;
                checkConnection();
            }
        } while (!conn);
    }

    public LinkedList<AZoFTPFile> download(LinkedList<AZoFTPFile> afs, LocalStorage localStorage) {
        LinkedList<AZoFTPFile> retval = new LinkedList<>();
        for (AZoFTPFile a : afs) {
            retval.add(a);
        }

        downloadConnect();
        notify(FTPConnectionStatus.CONNECTED, lastWorkingConnection, -1);

        for (AZoFTPFile af : afs) {
            File localFile = null;

            try {
                localFile = localStorage.getLocalFile(af);

            } catch (IOException ex) {
                notify(FTPConnectionStatus.LOCALSTORAGEERROR, af.dir + af.ftpFile.getName(), -1);
                try {
                    ftpclient.logout();
                } catch (IOException ex2) {
                    Logger.getLogger(FTPConnection.class.getName()).log(Level.INFO, null, ex);
                };
                return retval;
            }
            if (!localStorage.prepareLocalFile(localFile)) {
                notify(FTPConnectionStatus.LOCALSTORAGEERROR, af.dir + af.ftpFile.getName(), -1);
                try {
                    ftpclient.logout();
                } catch (IOException ex) {
                    Logger.getLogger(FTPConnection.class.getName()).log(Level.INFO, null, ex);
                }
                return retval;
            }
            try {
                notify(FTPConnectionStatus.DOWNLOADING, af.dir + af.ftpFile.getName(), ((int) (90.0 * ((afs.indexOf(af) + 1.0) / (double) afs.size()))));
                FileOutputStream fos = new FileOutputStream(localFile);
                InputStream is = ftpclient.retrieveFileStream(af.dir + af.ftpFile.getName());
                IOUtils.copyLarge(is, fos);
                boolean complete = ftpclient.completePendingCommand();
                is.close();
                fos.close();
                if (complete) {
                    retval.remove(af);
                    notify(FTPConnectionStatus.SUCCESS, af.dir + af.ftpFile.getName(), -1);
                }
            } catch (IOException ex) {
                try {
                    localFile.delete();
                    downloadConnect();
                } catch (Exception ex2) {
                }

            }
        }
        try {
            ftpclient.logout();
        } catch (IOException ex) {
            Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retval;
    }

    void remountSD() {
        try {
            TelnetClient telnetclient = new TelnetClient();

            telnetclient.connect(lastWorkingConnection);
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

    public LinkedList<AZoFTPFile> discoverRemoteFiles(String root)
            throws IOException {
        LinkedList<AZoFTPFile> retval = new LinkedList();
        for (FTPFile f : ftpclient.listFiles(root)) {
            AZoFTPFile af = new AZoFTPFile(f, root);
            if (f.isFile() && isPicture(af) && !retval.contains(af)) {
                retval.add(af);
            }
            if (f.isDirectory()) {

                retval.addAll(discoverRemoteFiles(root + f.getName() + "/"));
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

}
