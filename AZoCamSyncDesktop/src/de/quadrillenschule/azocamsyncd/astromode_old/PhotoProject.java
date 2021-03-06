/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode_old;

import de.quadrillenschule.azocamsyncd.astromode_old.ReceivePhotoSerie.ReceiveStatus;
import java.io.File;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class PhotoProject extends PhotoProjectProfile {

    private String name = "Unnamed";
    private File folder;

    public PhotoProject(File folder) {
        super();
        setFolder(folder);

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

    public ReceivePhotoSerie getActivePhotoSerie() {
        for (ReceivePhotoSerie ps : photoSeries) {
            if (ps.getStatus().equals(ReceiveStatus.RECEIVING_FILES)) {
                return ps;
            }
            if (ps.getStatus().equals(ReceiveStatus.NEW)) {
                return ps;
            }
            
        }
        return null;
    }
}
