/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andreas
 */
public class GlobalProperties {

    private Properties props;

    public enum CamSyncProperties {

        SDCARD_IPS, LOCALSTORAGE_PATH, FILETYPES, SD_FILELIMIT, DATE_FORMAT,USE_DATEFOLDERS
    };

    public static final String USER_HOME = "System.user.home";
    public static final String[] DEFAULTS = {
        "192.168.178.254,192.168.178.32", USER_HOME, "JPG,NEF,CR2,TIF,AVI", "25","yyyy_MM_dd","true"};

    public GlobalProperties() {
        props = new Properties();
        try {
            load();
        } catch (IOException ex) {
            Logger.getLogger(GlobalProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setProperty(CamSyncProperties prop, String value) {
        props.setProperty(prop.name(), value);
        try {
            store();
        } catch (IOException ex) {
            Logger.getLogger(GlobalProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getProperty(CamSyncProperties prop) {
        if (prop.equals(CamSyncProperties.LOCALSTORAGE_PATH)) {
            if (props.getProperty(prop.name(), DEFAULTS[prop.ordinal()]).equals(USER_HOME)) {
                return System.getProperty("user.home") + System.getProperty("file.separator") + "azocamsync";
            }
        }
        return props.getProperty(prop.name(), DEFAULTS[prop.ordinal()]);
    }

    public void load() throws FileNotFoundException, IOException {
        if (!getFile().exists()) {
            getFile().createNewFile();
        }
        FileInputStream fis = new FileInputStream(getFile());
        props.load(fis);
        fis.close();
    }

    public void store() throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(getFile());
        props.store(fos, "Created by AZoCamSync");
        fos.close();
    }

    public File getFile() {
        return new File(System.getProperty("user.home"), "azocamsync.props");
    }
}
