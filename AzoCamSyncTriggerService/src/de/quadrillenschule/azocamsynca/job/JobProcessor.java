/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import de.quadrillenschule.azocamsynca.AzoTriggerServiceApplication;
import de.quadrillenschule.azocamsynca.NikonIR;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class JobProcessor {

    private LinkedList<TriggerJob> jobs = new LinkedList<TriggerJob>();

    public enum ProcessorStatus {

        IDLE, PROCESSING, PAUSED
    };

    private ProcessorStatus status = ProcessorStatus.IDLE;
    private static ProcessorStatus requestStatus = ProcessorStatus.IDLE;

    private Handler handler;
    Activity activity;

    public JobProcessor(Activity ac) {
        handler = new Handler();
        this.activity = ac;

    }

    public void start() {
        setRequestStatus(ProcessorStatus.PROCESSING);
    }

    public void pause() {
        setRequestStatus(ProcessorStatus.PAUSED);
    }

    public void executeNext() {
        TriggerJob currentJobT = null;
        for (TriggerJob j : jobs) {
            if (j.getStatus() != TriggerJob.TriggerJobStatus.FINISHED) {
                currentJobT = j;
                break;
            }
        }
        if (currentJobT == null) {
            setStatus(ProcessorStatus.IDLE);
            return;
        }
        
        final TriggerJob currentJob = currentJobT;
        final NikonIR camera = ((AzoTriggerServiceApplication) activity.getApplication()).getCamera();

        if (currentJob.getStatus() == TriggerJob.TriggerJobStatus.NEW) {

            AlertDialog.Builder ad = new AlertDialog.Builder(activity);
            ad.setMessage("Confirm that everything is prepared for:\n "
                    + currentJob.getProject() + ": " + currentJob.getSeriesName() + "\n"
                    + currentJob.getNumber() + " x " + (int) (currentJob.getExposure() / 1000) + "s\n"
                    + "Additional Gap:" + currentJob.getExposureGapTime() / 1000 + "s\n"
                    + "Camera controls time: " + camera.isExposureSetOnCamera(currentJob.getExposure())
                    + "Camera delays between trigger:" + camera.getDelayBetweenTrigger() / 1000 + "s\n"
                    + "Total time: " + (currentJob.getExposure() + currentJob.getExposureGapTime() + camera.getDelayBetweenTrigger()) / 1000
            );
            ad.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    currentJob.setStatus(TriggerJob.TriggerJobStatus.PREPARED);
                    executeNext();
                }
            });
            ad.setNegativeButton("Abort", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    currentJob.setStatus(TriggerJob.TriggerJobStatus.FINISHED);
                }
            });
            ad.create().show();
        }

        final Handler handler = new Handler();

        if (currentJob.getStatus() == TriggerJob.TriggerJobStatus.PREPARED) {
            handler.postDelayed(new Runnable() {

                public void run() {

                    camera.trigger();
                    if (currentJob.getFirstTriggerTime() == 0) {
                        currentJob.setFirstTriggerTime(System.currentTimeMillis());
                    }
                    currentJob.setStatus(TriggerJob.TriggerJobStatus.RUNNING);
                    currentJob.setToggleIsOpen(!currentJob.isToggleIsOpen());

                    if (!camera.isExposureSetOnCamera(currentJob.getExposure())) {
                        if (!currentJob.isToggleIsOpen()) {
                            currentJob.setTriggered(currentJob.getTriggered() + 1);

                            currentJob.setLastTriggerTime(System.currentTimeMillis());
                        }
                    } else {
                        currentJob.setTriggered(currentJob.getTriggered() + 1);

                    }
                    if (currentJob.getTriggered() < currentJob.getNumber()) {
                        long time;
                        if (camera.isExposureSetOnCamera(currentJob.getExposure())) {
                            time = currentJob.getExposure() + currentJob.getExposureGapTime() + camera.getDelayBetweenTrigger();
                        } else {
                            if (currentJob.isToggleIsOpen()) {
                                time = currentJob.getExposure();
                            } else {
                                time = camera.getDelayBetweenTrigger();
                            }
                        }
                        handler.postDelayed(this, time);
                    } else {
                        currentJob.setStatus(TriggerJob.TriggerJobStatus.FINISHED);
                        executeNext();
                    }
                }
            }, currentJob.getInitialDelay());
        }

    }

    /**
     * @return the jobs
     */
    public LinkedList<TriggerJob> getJobs() {
        return jobs;
    }

    /**
     * @return the requestStatus
     */
    public static ProcessorStatus getRequestStatus() {
        return requestStatus;
    }

    /**
     * @param aRequestStatus the requestStatus to set
     */
    public static void setRequestStatus(ProcessorStatus aRequestStatus) {
        requestStatus = aRequestStatus;
    }

    /**
     * @return the status
     */
    public ProcessorStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ProcessorStatus status) {
        this.status = status;
    }

}
