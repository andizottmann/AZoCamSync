/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode;

import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.webservice.WebService;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author D061339
 */
public class SmartPhoneWrapper {

    public static enum SmartPhoneStatus {

        ERROR, CONNECTED
    };
    private static String LAST_WORKING_IP = "192.168.178.31";
    private static final int PORT = 8000;
    private static final String PROTOCOL = "http";

    private static LinkedList<PhotoSerie> getJobs() {
        try {
            JSONArray ja = new JSONArray(getFromSmartPhone(WebService.WebCommands.list));
            LinkedList<PhotoSerie> retval = new LinkedList<PhotoSerie>();
            for (int i = 0; i < ja.length(); i++) {
                PhotoSerie tps = new PhotoSerie();
                try {
                    tps.fromJSONObject(ja.getJSONObject(i));
                } catch (JSONException ex) {
                    Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
                }
                retval.add(tps);
            }
            return retval;
        } catch (JSONException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static String getFromSmartPhone(WebService.WebCommands command) {
        String retval = SmartPhoneStatus.ERROR.name();
        URL url;
        try {
            url = new URL(PROTOCOL + "://" + LAST_WORKING_IP + ":" + PORT + "/" + command.name());
            URLConnection uc = url.openConnection();
            StringWriter sw = new StringWriter();
            IOUtils.copy((InputStream) uc.getContent(), sw);
            retval = sw.toString();
        } catch (MalformedURLException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retval;
    }

    public static void main(String args[]) {
        String r = "";
        for (PhotoSerie ps : getJobs()) {
            r += ps.shortDescription() + "\n";
        }
        System.out.println(r);
    }
}
