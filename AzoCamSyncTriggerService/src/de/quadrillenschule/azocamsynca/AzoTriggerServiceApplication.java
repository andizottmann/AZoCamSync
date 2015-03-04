/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca;

import android.app.Activity;
import android.app.Application;
import de.quadrillenschule.azocamsynca.job.JobProcessor;
import de.quadrillenschule.azocamsynca.webservice.WebService;

/**
 *
 * @author Andreas
 */
public class AzoTriggerServiceApplication extends Application {

    private JobProcessor jobProcessor;
    private WebService webService;
    private NikonIR camera;

    public AzoTriggerServiceApplication() {
        super();

    }

    public void onActivityCreate(Activity a) {
        setCamera(new NikonIR(a));

        if (jobProcessor == null) {
            jobProcessor = new JobProcessor(a);
        }
        if (webService == null) {
            webService = new WebService(jobProcessor);
        }
    }

    /**
     * @return the jobProcessor
     */
    public JobProcessor getJobProcessor() {
        return jobProcessor;
    }

    /**
     * @return the camera
     */
    public NikonIR getCamera() {
        return camera;
    }

    /**
     * @param camera the camera to set
     */
    public void setCamera(NikonIR camera) {
        this.camera = camera;
    }

    /**
     * @param jobProcessor the jobProcessor to set
     */
    public void setJobProcessor(JobProcessor jobProcessor) {
        this.jobProcessor = jobProcessor;
    }
}
