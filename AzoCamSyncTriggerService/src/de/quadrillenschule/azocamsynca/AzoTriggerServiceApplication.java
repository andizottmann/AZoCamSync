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
        if (camera == null) {
            setCamera(new NikonIR(a));
        }

        //  camera.setActivity(a);
        if (jobProcessor == null) {
            jobProcessor = new JobProcessor(a);
        }
        //   jobProcessor.setActivity(a);

        if (getWebService() == null) {
            setWebService(new WebService(jobProcessor));
        }
    }

    public void onActivityResume(Activity a) {
        jobProcessor.load();
    }

 public void onActivityPause(Activity a) {
        jobProcessor.store();
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

    /**
     * @return the webService
     */
    public WebService getWebService() {
        return webService;
    }

    /**
     * @param webService the webService to set
     */
    public void setWebService(WebService webService) {
        this.webService = webService;
    }
}
