/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import de.quadrillenschule.azocamsynca.AzoTriggerServiceApplication;
import de.quadrillenschule.azocamsynca.NikonIR;
import java.util.UUID;

/**
 *
 * @author Andreas
 */
public class TriggerJob implements Job {

    private JobStatus status = JobStatus.NEW;
    String id;
    private String project = "New Project";
    private String seriesName = "flats";
    private long initialDelay = 0;
    private long exposure = 0;
    private long exposureGapTime = 0;
    private int number = 0;

    private int triggered = 0;
    private long firstTriggerTime = 0, lastTriggerTime = 0;
    private boolean toggleIsOpen = false;

    Activity ac;
    private JobProcessor jobProcessor;

    public TriggerJob(Activity ac) {
        id = UUID.randomUUID().toString();
        this.ac = ac;
    }
    
   

   

    /**
     * @return the initialDelay
     */
    public long getInitialDelay() {
        return initialDelay;
    }

    /**
     * @param initialDelay the initialDelay to set
     */
    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    /**
     * @return the exposure
     */
    public long getExposure() {
        return exposure;
    }

    /**
     * @param exposure the exposure to set
     */
    public void setExposure(long exposure) {
        this.exposure = exposure;
    }

    /**
     * @return the triggered
     */
    public int getTriggered() {
        return triggered;
    }

    /**
     * @param triggered the triggered to set
     */
    public void setTriggered(int triggered) {
        this.triggered = triggered;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the status
     */
    public JobStatus getStatus() {
        return status;
    }

    /**
     * @return the seriesName
     */
    public String getSeriesName() {
        return seriesName;
    }

    /**
     * @param seriesName the seriesName to set
     */
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    /**
     * @return the firstTriggerTime
     */
    public long getFirstTriggerTime() {
        return firstTriggerTime;
    }

    /**
     * @return the lastTriggerTime
     */
    public long getLastTriggerTime() {
        return lastTriggerTime;
    }

    /**
     * @return the exposureGapTime
     */
    public long getExposureGapTime() {
        return exposureGapTime;
    }

    /**
     * @param exposureGapTime the exposureGapTime to set
     */
    public void setExposureGapTime(long exposureGapTime) {
        this.exposureGapTime = exposureGapTime;
    }

    /**
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(String project) {
        this.project = project;
    }

    /**
     * @return the jobProcessor
     */
    public JobProcessor getJobProcessor() {
        return jobProcessor;
    }

    /**
     * @param jobProcessor the jobProcessor to set
     */
    public void setJobProcessor(JobProcessor jobProcessor) {
        this.jobProcessor = jobProcessor;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(JobStatus status) {
        this.status = status;
    }

    /**
     * @param firstTriggerTime the firstTriggerTime to set
     */
    public void setFirstTriggerTime(long firstTriggerTime) {
        this.firstTriggerTime = firstTriggerTime;
    }

    /**
     * @param lastTriggerTime the lastTriggerTime to set
     */
    public void setLastTriggerTime(long lastTriggerTime) {
        this.lastTriggerTime = lastTriggerTime;
    }

    /**
     * @return the toggleIsOpen
     */
    public boolean isToggleIsOpen() {
        return toggleIsOpen;
    }

    /**
     * @param toggleIsOpen the toggleIsOpen to set
     */
    public void setToggleIsOpen(boolean toggleIsOpen) {
        this.toggleIsOpen = toggleIsOpen;
    }

}
