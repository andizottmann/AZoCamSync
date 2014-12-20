/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class PhotoSerie {

    public enum Status {

        NEW, RECEIVING_FILES, COMPLETED
    };

    private Status status = Status.NEW;
    private int numberOfPlannedPhotos = 0;
    private String name = "lightframes";
    private LinkedList<File> photos;
    private PhotoProject project;

    public PhotoSerie(PhotoProject project) {
        photos = new LinkedList<>();
        this.project = project;
    }

    public void receiveFile(File file) throws IOException {
        if (!project.getFolder().exists()) {
            project.getFolder().mkdir();
        }
        if (!getFolder().exists()) {
            getFolder().mkdir();
        }
        File newFile = new File(getFolder().getAbsolutePath(), file.getName());
        Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        photos.add(file);
        status = Status.RECEIVING_FILES;
        if (isComplete()) {
            status = Status.COMPLETED;
        }

    }

    public boolean isComplete() {
        return numberOfPlannedPhotos == photos.size();
    }

    /**
     * @return the numberOfPhotos
     */
    public int getNumberOfPlannedPhotos() {
        return numberOfPlannedPhotos;
    }

    /**
     * @param numberOfPlannedPhotos the numberOfPhotos to set
     */
    public void setNumberOfPlannedPhotos(int numberOfPlannedPhotos) {
        this.numberOfPlannedPhotos = numberOfPlannedPhotos;
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
     * @return the photos
     */
    public LinkedList<File> getPhotos() {
        return photos;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the folder
     */
    public File getFolder() {
        File retval = new File(project.getFolder().getAbsolutePath(), getName());

        return retval;
    }

    /**
     * @return the project
     */
    public PhotoProject getProject() {
        return project;
    }

}