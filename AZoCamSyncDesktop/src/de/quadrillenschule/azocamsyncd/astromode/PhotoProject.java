/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode;

import java.io.File;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class PhotoProject {
    private LinkedList<PhotoSerie> photoSeries;
    private String name="Unnamed";
    private File folder;

    public PhotoProject(File folder) {
        setFolder(folder);
        photoSeries=new LinkedList<>();
    }

    
    
    

    /**
     * @return the photoSeries
     */
    public LinkedList<PhotoSerie> getPhotoSeries() {
        return photoSeries;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the folder
     */
    public File getFolder() {
        return folder;
    }

    /**
     * @param folder the folder to set
     */
    public void setFolder(File folder) {
        this.folder = folder;
    }
}
