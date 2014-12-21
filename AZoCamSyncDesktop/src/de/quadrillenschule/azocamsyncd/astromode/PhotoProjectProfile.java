/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode;

import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class PhotoProjectProfile {

    protected LinkedList<PhotoSerie> photoSeries;
    protected String profileName = "Unnamed Profile";

    public PhotoProjectProfile() {
        photoSeries = new LinkedList<>();
    }

    public void fromProfile(String profile, PhotoProject project) {
        photoSeries = new LinkedList<>();
        String name = "";
        
        for (int i=0;i+2<profile.split(",").length;i=i+3) {
           /* if ((i % 2) == 0) {
                name = s;
            } else {*/
                PhotoSerie ps = new PhotoSerie(project);
                ps.setName(profile.split(",")[i]);
                
                ps.setNumberOfPlannedPhotos(Integer.parseInt(profile.split(",")[i+1]));
                ps.setExposureTimeInMs(Long.parseLong(profile.split(",")[i+2]));
                photoSeries.add(ps);
            
        }
    }

    /**
     * @return the photoSeries
     */
    public LinkedList<PhotoSerie> getPhotoSeries() {
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
        for (PhotoSerie ps : photoSeries) {
            retval += sep + ps.getName() + "," + ps.getNumberOfPlannedPhotos()+","+ps.getExposureTimeInMs();
            sep = ",";
        }
        return retval;
    }

    @Override
    public String toString() {
        return getProfileName();
    }
}
