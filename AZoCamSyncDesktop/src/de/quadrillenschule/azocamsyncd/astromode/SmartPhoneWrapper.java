/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode;

import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.webservice.WebService;
import de.quadrillenschule.azocamsyncd.GlobalProperties;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

    private static SmartPhoneStatus lastStatus = SmartPhoneStatus.TRYING;
    private static String LAST_WORKING_IP = "192.168.178.31";
    // private static String SMARTPHONE_IPS = "192.168.178.31,192.168.43.1";
    private static final int PORT = 8000;
    private static final String PROTOCOL = "http";

    public static SmartPhoneStatus lastStatus() {
        return lastStatus;
    }

    public static LinkedList<PhotoSerie> getJobs() throws JSONException, IOException {

        lastStatus = SmartPhoneStatus.TRYING;
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
        lastStatus = SmartPhoneStatus.CONNECTED;
        return retval;

    }

    public static SmartPhoneStatus update(PhotoSerie ps) throws IOException {
        URL url;
        String retval;
        try {
            url = new URL(PROTOCOL + "://" + LAST_WORKING_IP + ":" + PORT + "/"
                    + WebService.WebCommands.updateTriggered + "/?"
                    + WebService.WebParameters.jobid + "=" + ps.getId() + "&" + WebService.WebParameters.receivedImages + "=" + ps.getReceived()
            );
            lastStatus = SmartPhoneStatus.TRYING;
            URLConnection uc = url.openConnection();
            uc.setConnectTimeout(3000);
            StringWriter sw = new StringWriter();
            IOUtils.copy((InputStream) uc.getContent(), System.out);

        } catch (MalformedURLException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);

            return SmartPhoneStatus.ERROR;

        }

        lastStatus = SmartPhoneStatus.CONNECTED;
        return SmartPhoneStatus.CONNECTED;
    }

    public static SmartPhoneStatus remove(PhotoSerie ps) throws IOException {

        lastStatus = SmartPhoneStatus.TRYING;
        URL url;
        String retval;
        try {
            url = new URL(PROTOCOL + "://" + LAST_WORKING_IP + ":" + PORT + "/"
                    + WebService.WebCommands.removejob + "/?"
                    + WebService.WebParameters.jobid + "=" + ps.getId()
            );
            System.out.println("Trying " + url.toString());
            URLConnection uc = url.openConnection();
            uc.setConnectTimeout(3000);
            StringWriter sw = new StringWriter();
            IOUtils.copy((InputStream) uc.getContent(), System.out);

        } catch (MalformedURLException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
            return SmartPhoneStatus.ERROR;

        }
        return SmartPhoneStatus.CONNECTED;
    }

    public static void addJob(PhotoSerie ps) throws IOException {

        lastStatus = SmartPhoneStatus.TRYING;
        try {
            HttpPost httppost = new HttpPost(PROTOCOL + "://" + LAST_WORKING_IP + ":" + PORT + "/"
                    + WebService.WebCommands.addjob);
            List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
            parameters.add(new BasicNameValuePair(WebService.WebParameters.jsoncontent.name(), ps.toJSONObject().toString()));

            httppost.setEntity(new UrlEncodedFormEntity(parameters));

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(httppost);
            HttpEntity resEntity = httpResponse.getEntity();

            // Get the HTTP Status Code
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            // Get the contents of the response
            InputStream input = resEntity.getContent();
            String responseBody = IOUtils.toString(input);
            input.close();

            lastStatus = SmartPhoneStatus.CONNECTED;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void updateJob(PhotoSerie ps) {

        lastStatus = SmartPhoneStatus.TRYING;
        try {
            HttpPost httppost = new HttpPost(PROTOCOL + "://" + LAST_WORKING_IP + ":" + PORT + "/"
                    + WebService.WebCommands.updateJob);
            List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
            parameters.add(new BasicNameValuePair(WebService.WebParameters.jobid.name(), ps.getId()));

            parameters.add(new BasicNameValuePair(WebService.WebParameters.jsoncontent.name(), ps.toJSONObject().toString()));

            httppost.setEntity(new UrlEncodedFormEntity(parameters));

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(httppost);
            HttpEntity resEntity = httpResponse.getEntity();

            // Get the HTTP Status Code
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            // Get the contents of the response
            InputStream input = resEntity.getContent();
            String responseBody = IOUtils.toString(input);
            input.close();

            // Print the response code and message body
            System.out.println("HTTP Status Code: " + statusCode);
            System.out.println(responseBody);

            lastStatus = SmartPhoneStatus.CONNECTED;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getFromSmartPhone(WebService.WebCommands command) throws IOException {
        String retval;
        SmartPhoneStatus status = SmartPhoneStatus.TRYING;

        lastStatus = SmartPhoneStatus.TRYING;
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

            lastStatus = SmartPhoneStatus.CONNECTED;
        } catch (MalformedURLException ex) {
            Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
            status = SmartPhoneStatus.ERROR;
            retval = SmartPhoneStatus.ERROR.name();

            lastStatus = SmartPhoneStatus.ERROR;
        }

        return retval;
    }

    public static SmartPhoneStatus checkConnection() {
        GlobalProperties gp = new GlobalProperties();

        lastStatus = SmartPhoneStatus.TRYING;

        for (String ip : (LAST_WORKING_IP + "," + gp.getProperty(GlobalProperties.CamSyncProperties.SMARTPHONE_IPS)).split(",")) {
            LAST_WORKING_IP = ip;
            try {
                if (getFromSmartPhone(WebService.WebCommands.help) != SmartPhoneStatus.ERROR.name()) {

                    lastStatus = SmartPhoneStatus.CONNECTED;
                    return SmartPhoneStatus.CONNECTED;
                }
            } catch (IOException ex) {
                Logger.getLogger(SmartPhoneWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        lastStatus = SmartPhoneStatus.ERROR;
        return SmartPhoneStatus.ERROR;
    }

}
