/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.ftpservice;

import de.quadrillenschule.azocamsyncd.LocalStorage;
import de.quadrillenschule.azocamsyncd.ftpservice.FTPConnectionListener.FTPConnectionStatus;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
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
            if (ftpclient != null) {
                close();
            }
            ftpclient = new FTPClient();

            try {
                ftpclient.setDefaultTimeout(3000);
                ftpclient.connect(ip);
                if (ftpclient.isAvailable()) {
                    LinkedList<AZoFTPFile> retval = discoverRemoteFiles("/", includeDirs);

                    lastWorkingConnection = ip;

                    close();
                    remountSD(null);
                    notify(FTPConnectionStatus.CONNECTED, ip, -1);

                    return retval;
                } else {
                    close();
                }
            } catch (IOException ex) {
                close();
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
                // if (ftpclient != null) {
                close();
                // }
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
                close();
                return retval;
            }
            if (!localStorage.prepareLocalFile(localFile)) {
                notify(FTPConnectionStatus.LOCALSTORAGEERROR, af.dir + af.ftpFile.getName(), -1);
                close();
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
                    close();
                }

            }
        }
        close();

        return retval;
    }

    public void close() {
        if (ftpclient == null) {
            return;
        };
        /*      try {
         ftpclient.noop();
         } catch (IOException ex) {
         //        Logger.getLogger(FTPConnection.class.getName()).log(Level.WARNING, null, ex);
         }
         try {
         ftpclient.logout();

         } catch (IOException ex) {
         //      Logger.getLogger(FTPConnection.class.getName()).log(Level.WARNING, null, ex);
         }
         */
        try {
            ftpclient.disconnect();
        } catch (IOException ex) {
            //    Logger.getLogger(FTPConnection.class.getName()).log(Level.WARNING, null, ex);
        }

        //  ftpclient=null;
    }

    public void deleteFiles(int remainingNumber) {
        if (remainingNumber < 0) {
            return;
        }
        if (ftpclient != null) {
            close();
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
                close();
                LinkedList<String> deleteables = new LinkedList();
                for (AZoFTPFile af : afs) {
                    i++;
                    //   deleteSingleFile(lastWorkingConnection)
                    deleteables.add((af.dir + af.ftpFile.getName()).replaceAll(" ", "*"));
                    //    System.out.println("Would delete"+af.ftpFile.getName());
                    if (i >= todelete) {
                        break;
                    }
                }

                remountSD(deleteables);
                notify(FTPConnectionStatus.SUCCESS, "", -1);

            } else {
                close();
            }

        } catch (IOException ex) {
            close();
        }
        close();
    }

    public String telnetCommand(String command) {
        close();
        final TelnetClient telnetclient = new TelnetClient();

        try {
            telnetclient.connect(getLastWorkingConnection());

            final StringWriter sw = new StringWriter();

            /*   Thread tins = new Thread(new Runnable() {

             @Override
             public void run() {
             try {
             IOUtils.copy(telnetclient.getInputStream(), sw);
             } catch (IOException ex) {
             //     Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
             }

             }
             });
             tins.start();*/
            //(telnetclient.getInputStream());
            IOUtils.copy(new ByteArrayInputStream((command + "\r\n").getBytes(telnetclient.getCharset())), telnetclient.getOutputStream());
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
            }

            // tins.join();
            telnetclient.disconnect();
              //  System.out.println(sw.toString());

            return null;//sw.toString();//sw.toString();

        } catch (IOException ex) {
            try {
                telnetclient.disconnect();
            } catch (IOException ex1) {
                Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(FTPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void remountSD(LinkedList<String> deleteables) {
        if (deleteables != null) {
            for (String delme : deleteables) {
                telnetCommand("rm /mnt/sd" + delme + ";sync");
            }
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
