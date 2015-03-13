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

        ERROR, TRYING, CONNECTED
    };
    private static String LAST_WORKING_IP = "192.168.178.31";
    private static String SMARTPHONE_IPS = "192.168.178.31,192.168.43.1";
    private static final int PORT = 8000;
    private static final String PROTOCOL = "http";

    public static LinkedList<PhotoSerie> getJobs() throws JSONException, IOException {
            JSONArray ja = new JSONArray(getFromSmartPhone(WebService.WebCommands.list, true));
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
      
    }

    public static void update(PhotoSerie ps) {
        URL url;
        String retval;
        try {
            url = new URL(PROTOCOL + "://" + LAST_WORKING_IP + ":" + PORT + "/"
                    + WebService.WebCommands.updateTriggered + "/?"
                    + WebService.WebParameters.jobid + "=" + ps.getId() + "&" + WebService.WebParameters.receivedImages +"="+ ps.getReceived()
            );
            System.out.println("Trying " + url.toString());
            URLConnection uc = url.openConnection();
            uc.setConnectTimeout(3000);
            StringWriter sw = new StringWriter();
            IOUtils.copy((InputStream) uc.getContent(), sw);
            retval = sw.toString();
        } catch (MalformedURLException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
            retval = SmartPhoneStatus.ERROR.name();

        } catch (IOException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getFromSmartPhone(WebService.WebCommands command, boolean doCheck) throws IOException {
        String retval;
        SmartPhoneStatus status = SmartPhoneStatus.TRYING;

        URL url;
        try {
            url = new URL(PROTOCOL + "://" + LAST_WORKING_IP + ":" + PORT + "/" + command.name());
            System.out.println("Trying " + url.toString());
            URLConnection uc = url.openConnection();
            uc.setConnectTimeout(3000);
            StringWriter sw = new StringWriter();
            IOUtils.copy((InputStream) uc.getContent(), sw);
            retval = sw.toString();
            status = SmartPhoneStatus.CONNECTED;
        } catch (MalformedURLException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
            status = SmartPhoneStatus.ERROR;
            retval = SmartPhoneStatus.ERROR.name();

        }

        return retval;
    }

    public static SmartPhoneStatus checkConnection() {
        for (String ip : (LAST_WORKING_IP + "," + SMARTPHONE_IPS).split(",")) {
            LAST_WORKING_IP = ip;
            try {
                if (getFromSmartPhone(WebService.WebCommands.help, false) != SmartPhoneStatus.ERROR.name()) {
                    return SmartPhoneStatus.CONNECTED;
                }
            } catch (IOException ex) {
                Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return SmartPhoneStatus.ERROR;
    }

}
