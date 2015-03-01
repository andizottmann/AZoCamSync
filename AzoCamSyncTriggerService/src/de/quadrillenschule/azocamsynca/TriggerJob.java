/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca;

import android.app.Activity;
import android.os.Handler;
import java.util.UUID;

/**
 *
 * @author Andreas
 */
public class TriggerJob {

    /**
     * @return the status
     */
    public TriggerJobStatus getStatus() {
        return status;
    }

    public enum TriggerJobStatus {

        NEW, RUNNING, FINISHED
    };
    private TriggerJobStatus status = TriggerJobStatus.NEW;
    String id;
    private long initialDelay = 0;
    //   private long intervall = 0;
    private long exposure = 0;
    private boolean exposureSetOnCamera = true;
    private int triggered = 0;
    private int number = 0;
    private boolean toggleIsOpen = false;
    Activity ac;

    public TriggerJob(Activity ac) {
        id = UUID.randomUUID().toString();
        this.ac = ac;
    }

    public void execute() {
        final NikonIR nikonIr = new NikonIR(ac);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                nikonIr.trigger();
                status = TriggerJobStatus.RUNNING;

                toggleIsOpen = !toggleIsOpen;

                if (!exposureSetOnCamera) {
                    if (!toggleIsOpen) {
                        triggered++;
                    }
                } else {
                    triggered++;
                }
                if (triggered < number) {
                    long time;
                    if (exposureSetOnCamera) {
                        time = exposure + 3000;
                    } else {
                        if (toggleIsOpen) {
                            time = exposure;
                        } else {
                            time = 3000;
                        }
                    }
                    handler.postDelayed(this, time);
                } else {
                    status = TriggerJobStatus.FINISHED;
                }
            }
        }, initialDelay);

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
     * @return the exposureSetOnCamera
     */
    public boolean isExposureSetOnCamera() {
        return exposureSetOnCamera;
    }

    /**
     * @param exposureSetOnCamera the exposureSetOnCamera to set
     */
    public void setExposureSetOnCamera(boolean exposureSetOnCamera) {
        this.exposureSetOnCamera = exposureSetOnCamera;
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

}
