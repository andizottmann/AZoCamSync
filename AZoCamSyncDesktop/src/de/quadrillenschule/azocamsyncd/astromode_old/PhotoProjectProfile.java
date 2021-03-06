/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode_old;

import de.quadrillenschule.azocamsyncd.astromode_old.gui.PhotoProjectTableModel;
import de.quadrillenschule.azocamsyncd.astromode_old.gui.PhotoProjectTableModel.Columns;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Andreas
 */
public class PhotoProjectProfile {

    protected LinkedList<ReceivePhotoSerie> photoSeries;
    protected String profileName = "Unnamed Profile";

    public PhotoProjectProfile() {
        photoSeries = new LinkedList<>();
    }

    public void fromProfile(String profile, PhotoProject project) {
        photoSeries = new LinkedList<>();
        String name = "";

        for (int i = 0; i + 2 < profile.split(",").length; i = i + 3) {
            /* if ((i % 2) == 0) {
             name = s;
             } else {*/
            ReceivePhotoSerie ps = new ReceivePhotoSerie(project);
            ps.setName(profile.split(",")[i]);

            ps.setNumberOfPlannedPhotos(Integer.parseInt(profile.split(",")[i + 1]));
            ps.setExposureTimeInMs(Long.parseLong(profile.split(",")[i + 2]));
            photoSeries.add(ps);

        }
    }

    public void fromJSONProfile(String jsonProfile, PhotoProject project) {
        photoSeries = new LinkedList<>();
        try {
            JSONObject jsa = new JSONObject(jsonProfile);

            ReceivePhotoSerie[] psArray = new ReceivePhotoSerie[jsa.names().length()];
            for (int i = 0; i < jsa.names().length(); i++) {
                JSONObject seriesArray = (JSONObject) jsa.get(jsa.names().getString(i));
                ReceivePhotoSerie ps = new ReceivePhotoSerie(project);
                ps.setName(jsa.names().get(i).toString());
                ps.setNumberOfPlannedPhotos(seriesArray.getInt(Columns.NUMBER_OF_PLANNED_PHOTOS.name()));
                ps.setExposureTimeInMs(seriesArray.getLong(Columns.EXPOSURE_TIME.name()));
                psArray[seriesArray.getInt("ORDER_NUMBER")] = ps;
            }
            for (ReceivePhotoSerie ps : psArray) {
                photoSeries.add(ps);
            }
        } catch (JSONException ex) {
            Logger.getLogger(PhotoProjectProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the photoSeries
     */
    public LinkedList<ReceivePhotoSerie> getPhotoSeries() {
        return photoSeries;
    }

    /**
     * @return the profileName
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * @param profileName the profileName to set
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String toProfile() {
        String retval = "";
        String sep = "";
        for (ReceivePhotoSerie ps : photoSeries) {
            retval += sep + ps.getName() + "," + ps.getNumberOfPlannedPhotos() + "," + ps.getExposureTimeInMs();
            sep = ",";
        }
        return retval;
    }

    public JSONObject toJSONProfile() {
        //     JSONObject retval = new JSONObject();
        JSONObject seriesArray = new JSONObject();
        try {
            for (int j = 0; j < photoSeries.size(); j++) {
                ReceivePhotoSerie ps = photoSeries.get(j);
                JSONObject serieObject = new JSONObject();
                serieObject.put(PhotoProjectTableModel.Columns.NUMBER_OF_PLANNED_PHOTOS.name(), ps.getNumberOfPlannedPhotos());
                serieObject.put(PhotoProjectTableModel.Columns.EXPOSURE_TIME.name(), ps.getExposureTimeInMs());

                serieObject.put("ORDER_NUMBER", j);
                seriesArray.put(ps.getName(), serieObject);

            }

            //  retval.append(profileName, seriesArray);
        } catch (JSONException ex) {
            Logger.getLogger(PhotoProjectProfile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return seriesArray;
    }

    @Override
    public String toString() {
        return getProfileName();
    }
}
