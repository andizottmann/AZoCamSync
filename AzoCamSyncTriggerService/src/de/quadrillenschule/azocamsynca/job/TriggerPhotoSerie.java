/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import android.app.Activity;
import de.quadrillenschule.azocamsync.PhotoSerie;
import java.util.UUID;

/**
 *
 * @author Andreas
 */
public class TriggerPhotoSerie extends PhotoSerie{

   
    String id;
    private boolean toggleIsOpen = false;

    Activity ac;
    private JobProcessor jobProcessor;

    public TriggerPhotoSerie(Activity ac) {
        id = UUID.randomUUID().toString();
        this.ac = ac;
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
