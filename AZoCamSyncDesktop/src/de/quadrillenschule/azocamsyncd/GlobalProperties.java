/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd;

import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.job.TriggerPhotoSerie;
import static de.quadrillenschule.azocamsyncd.GlobalProperties.CamSyncProperties;
import de.quadrillenschule.azocamsyncd.astromode_old.PhotoProjectProfile;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Andreas
 */
public class GlobalProperties {

    private Properties props;

    public enum CamSyncProperties {

        PULLINTERVALLSECS, SDCARD_IPS, LOCALSTORAGE_PATH, FILETYPES, SD_FILELIMIT, DATE_FORMAT, USE_DATEFOLDERS, LIST_OFSYNCED_IMAGES, LATESTIMAGEPATH,
        NOTIFY_CONNECTION, NOTIFY_DOWNLOAD, TOOLTIPS, LAST_ASTRO_FOLDER, USE_DATE_ASTRO_FOLDER, ASTRO_PROFILE, SMARTPHONE_IPS, ASTRO_JOB_LIST, ASTRO_MOVE_FILES
    };

    public static final Color COLOR_CONNECTED = new Color(100, 200, 90), COLOR_UNCONNECTED = new Color(200, 100, 90);
    public static final String USER_HOME = "System.user.home";
    public static final HashMap<CamSyncProperties, String> DEFAULTS = new HashMap();

    static {
        DEFAULTS.put(CamSyncProperties.PULLINTERVALLSECS, "30");
        DEFAULTS.put(CamSyncProperties.SDCARD_IPS, "192.168.178.254,192.168.178.32");
        DEFAULTS.put(CamSyncProperties.LOCALSTORAGE_PATH, USER_HOME);
        DEFAULTS.put(CamSyncProperties.FILETYPES, "JPG,NEF,CR2,TIF,AVI");
        DEFAULTS.put(CamSyncProperties.SD_FILELIMIT, "100");
        DEFAULTS.put(CamSyncProperties.DATE_FORMAT, "yyyy_MM_dd");
        DEFAULTS.put(CamSyncProperties.USE_DATEFOLDERS, "true");
        DEFAULTS.put(CamSyncProperties.NOTIFY_CONNECTION, "true");
        DEFAULTS.put(CamSyncProperties.NOTIFY_DOWNLOAD, "true");
        DEFAULTS.put(CamSyncProperties.TOOLTIPS, "true");
        DEFAULTS.put(CamSyncProperties.LAST_ASTRO_FOLDER, USER_HOME);
        DEFAULTS.put(CamSyncProperties.USE_DATE_ASTRO_FOLDER, "true");
        DEFAULTS.put(CamSyncProperties.SMARTPHONE_IPS, "192.168.178.31,192.168.43.1");
        DEFAULTS.put(CamSyncProperties.ASTRO_JOB_LIST, "[]");
        DEFAULTS.put(CamSyncProperties.ASTRO_MOVE_FILES, "false");

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
        if (prop.equals(CamSyncProperties.LAST_ASTRO_FOLDER)) {
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
        File dir = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "azocamsync");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return new File(System.getProperty("user.home") + System.getProperty("file.separator") + "azocamsync", "azocamsync.props");
    }

    public boolean isNewInstallation() {
        return (!getFile().exists());
    }

    public LinkedList<PhotoProjectProfile> getProfiles() {
        LinkedList<PhotoProjectProfile> retval = new LinkedList<>();
        for (Object key : props.keySet()) {
            if (key.toString().startsWith(CamSyncProperties.ASTRO_PROFILE.name())) {
                String name = key.toString().replaceAll(CamSyncProperties.ASTRO_PROFILE.name(), "");
                String profile = props.getProperty(key.toString());
                PhotoProjectProfile ppp = new PhotoProjectProfile();
                ppp.setProfileName(name);
                ppp.fromJSONProfile(profile, null);
                retval.add(ppp);
            }
        }

        return retval;
    }

    public void removeProfile(PhotoProjectProfile profile) {
        props.remove(CamSyncProperties.ASTRO_PROFILE + profile.getProfileName());
    }

    public void setProfile(PhotoProjectProfile profile) {
        props.setProperty(CamSyncProperties.ASTRO_PROFILE + profile.getProfileName(), profile.toJSONProfile().toString());
        try {
            store();
        } catch (IOException ex) {
            Logger.getLogger(GlobalProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LinkedList<PhotoSerie> readStoredAstroJobList() throws JSONException {
        LinkedList<PhotoSerie> retval = new LinkedList<>();
        JSONArray ja = new JSONArray(getProperty(CamSyncProperties.ASTRO_JOB_LIST));
        for (int i = 0; i < ja.length(); i++) {

            PhotoSerie ps = new PhotoSerie();
            ps.fromJSONObject(ja.getJSONObject(i));
            retval.add(ps);
        }
        return retval;
    }

    public void saveStoredAstroJobList(LinkedList<PhotoSerie> jobList) {
        JSONArray ja = new JSONArray();
        for (PhotoSerie ps : jobList) {
            ja.put(ps.toJSONObject());
        }
        setProperty(CamSyncProperties.ASTRO_JOB_LIST, ja.toString());
    }
}
