/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd;

import static de.quadrillenschule.azocamsyncd.GlobalProperties.CamSyncProperties;
import java.awt.Color;
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

        SDCARD_IPS, LOCALSTORAGE_PATH, FILETYPES, SD_FILELIMIT, DATE_FORMAT, USE_DATEFOLDERS, LATESTIMAGEPATH,
        NOTIFY_CONNECTION, NOTIFY_DOWNLOAD,TOOLTIPS
    };

    public static final Color COLOR_CONNECTED = new Color(100, 200, 90), COLOR_UNCONNECTED = new Color(200, 100, 90);
    public static final String USER_HOME = "System.user.home";
    public static final HashMap<CamSyncProperties, String> DEFAULTS = new HashMap();

    static {
        DEFAULTS.put(CamSyncProperties.SDCARD_IPS, "192.168.178.254,192.168.178.32");
        DEFAULTS.put(CamSyncProperties.LOCALSTORAGE_PATH, USER_HOME);
        DEFAULTS.put(CamSyncProperties.FILETYPES, "JPG,NEF,CR2,TIF,AVI");
        DEFAULTS.put(CamSyncProperties.SD_FILELIMIT, "100");
        DEFAULTS.put(CamSyncProperties.DATE_FORMAT, "yyyy_MM_dd");
        DEFAULTS.put(CamSyncProperties.USE_DATEFOLDERS, "true");
        DEFAULTS.put(CamSyncProperties.NOTIFY_CONNECTION, "true");
        DEFAULTS.put(CamSyncProperties.NOTIFY_DOWNLOAD, "true");
        DEFAULTS.put(CamSyncProperties.TOOLTIPS, "true");
    }

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
            if (props.getProperty(prop.name(), DEFAULTS.get(prop)).equals(USER_HOME)) {
                return System.getProperty("user.home") + System.getProperty("file.separator") + "azocamsync";
            }
        }
        if (DEFAULTS.get(prop) == null) {
              return props.getProperty(prop.name(), "");
        }
        return props.getProperty(prop.name(), DEFAULTS.get(prop));
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
