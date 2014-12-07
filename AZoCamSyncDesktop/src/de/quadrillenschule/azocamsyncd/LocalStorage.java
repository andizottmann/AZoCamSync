/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd;

import de.quadrillenschule.azocamsyncd.ftpservice.AZoFTPFile;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Andreas
 */
public class LocalStorage {

    File directory;
    private boolean useDateFolders = true;
    private boolean overwriteLocalFiles = true;
    //
    private String dateFormat = "yyyy_MM_dd";

    public LocalStorage(File directory) {
        this.directory = directory;
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public File getLocalFile(AZoFTPFile af) throws IOException {
        File retval;
        File dir;
        if (isUseDateFolders()) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            dir = new File(directory, sdf.format(af.ftpFile.getTimestamp().getTime()));
            try {
                dir.mkdir();
            } catch (Exception ex) {
            }
            if (!dir.isDirectory()) {
                return null;
            }

        } else {
            dir = directory;
        }
        retval = new File(dir, af.ftpFile.getName());

        return retval;
    }

    public boolean prepareLocalFile(File file) {
        File retval = file;
        if (file.exists() && !overwriteLocalFiles) {
            return false;
        }
        try {
            retval.createNewFile(); {
               //return false;
            }
        } catch (IOException ex) {
         //   return false;
        }

        if (!retval.canWrite()) {
            return false;
        }
        return true;
    }

    public boolean equalsLocal(AZoFTPFile remote) throws IOException {
        File local = getLocalFile(remote);
        if (!local.exists()) {
            return false;
        }
        if (local.length() != remote.ftpFile.getSize()) {
            return false;
        }
        if (!local.getName().equals(remote.ftpFile.getName())) {
            return false;
        }

        return true;
    }

    /**
     * @return the useDateFolders
     */
    public boolean isUseDateFolders() {
        return useDateFolders;
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
     * @param useDateFolders the useDateFolders to set
     */
    public void setUseDateFolders(boolean useDateFolders) {
        this.useDateFolders = useDateFolders;
    }

}
