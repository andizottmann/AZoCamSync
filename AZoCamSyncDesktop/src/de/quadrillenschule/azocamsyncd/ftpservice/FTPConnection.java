/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.ftpservice;

import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnectionListener.FTPConnectionStatus;
import java.io.IOException;
import java.io.StringReader;
import java.net.ConnectException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.telnet.TelnetClient;

/**
 *
 * @author Andreas
 */
public class FTPConnection {

    LinkedList<FTPConnectionListener> ftpConnectionListeners = new LinkedList();

    private String[] possibleConnections = {"192.168.178.32", "192.168.178.254"};
    private String lastWorkingConnection = "";
    //    
    private String dateFormat = "yyyy_MM_dd";
    private String fileTypes[] = {"JPG", "NEF", "CR2", "TIF"};
    FTPClient ftpclient;
    LinkedList<FTPFile> remotePictureDirs = new LinkedList<>();
    private boolean useDateFolders = true;

    public FTPConnection() {
    }

    public boolean checkConnection() {
        if (lastWorkingConnection != "") {
            LinkedList<String> t = new LinkedList<>();
            t.add(lastWorkingConnection);
            for (String s : possibleConnections) {
                if (!s.equals(lastWorkingConnection)){
                t.add(s);
                }
            }
            possibleConnections=t.toArray(new String[0]);
        }
        for (String ip : possibleConnections) {
            notify(FTPConnectionStatus.TRYING, ip, 5);
            ftpclient = new FTPClient();
            try {
                ftpclient.setDefaultTimeout(3000);
                ftpclient.connect(ip);
                if (ftpclient.isAvailable()) {
                    ftpclient.setSoTimeout(60000);
                    notify(FTPConnectionStatus.CONNECTED, ip, 10);
                    for (AZoFTPFile f : discoverRemoteFiles("/")) {
                        System.out.println(f.dir + f.ftpFile.getName());
                        System.out.println(f.ftpFile.getRawListing());
                    }
                    ftpclient.logout();
                    notify(FTPConnectionStatus.SUCCESS, ip, 100);
                    lastWorkingConnection = ip;
                    return true;
                }
                remountSD();

            } catch (IOException ex) {

                Logger.getLogger(FTPConnection.class.getName()).log(Level.INFO, null, ex);
            }

        }
        notify(FTPConnectionStatus.NOCONNECTION, "", 0);
        return false;
    }

    void remountSD() throws IOException {
        TelnetClient telnetclient = new TelnetClient();

        telnetclient.connect(lastWorkingConnection);
        IOUtils.copy(new StringReader("/usr/bin/refresh_sd\r\n"), telnetclient.getOutputStream());
        telnetclient.disconnect();

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
     * @return the dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @param dateFormat the dateFormat to set
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
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
     * @return the useDateFolders
     */
    public boolean isUseDateFolders() {
        return useDateFolders;
    }

    /**
     * @param useDateFolders the useDateFolders to set
     */
    public void setUseDateFolders(boolean useDateFolders) {
        this.useDateFolders = useDateFolders;
    }
}
