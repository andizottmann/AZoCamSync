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
import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.AzoTriggerServiceApplication;
import de.quadrillenschule.azocamsynca.NikonIR;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class JobProcessor {

    private LinkedList<TriggerPhotoSerie> jobs = new LinkedList<TriggerPhotoSerie>();
    private LinkedList<JobProcessorStatusListener> jobProcessorStatusListeners = new LinkedList<JobProcessorStatusListener>();
    private LinkedList<JobProgressListener> jobProgressListeners = new LinkedList<JobProgressListener>();

    public enum ProcessorStatus {

        IDLE, PROCESSING, PAUSED
    };

    private ProcessorStatus status = ProcessorStatus.PAUSED;
    private static ProcessorStatus requestStatus = ProcessorStatus.PAUSED;

    private Handler handler;
    private Activity activity;

    public JobProcessor(Activity ac) {
        handler = new Handler();
        this.activity = ac;

    }

    public void start() {
        setRequestStatus(ProcessorStatus.PROCESSING);
        processingLoop();
    }

    public void pause() {
        setRequestStatus(ProcessorStatus.PAUSED);
        processingLoop();
    }

    public void processingLoop() {

        if (getRequestStatus() == ProcessorStatus.PAUSED) {
            setStatus(ProcessorStatus.PAUSED);
            return;
        }
        setStatus(ProcessorStatus.PROCESSING);
        TriggerPhotoSerie currentJobT = null;
        for (TriggerPhotoSerie j : jobs) {
            if (j.getTriggerStatus() != TriggerPhotoSerie.TriggerJobStatus.FINISHED) {
                currentJobT = j;
                if (j.getTriggerStatus() == PhotoSerie.TriggerJobStatus.WAITFORUSER) {
                    return;
                }
                break;
            }
        }
        if (currentJobT == null) {
            setStatus(ProcessorStatus.IDLE);
            return;
        }

        final TriggerPhotoSerie currentJob = currentJobT;
        final NikonIR camera = ((AzoTriggerServiceApplication) getActivity().getApplication()).getCamera();

        if (currentJob.getTriggerStatus() == PhotoSerie.TriggerJobStatus.NEW) {
            currentJob.setTriggerStatus(PhotoSerie.TriggerJobStatus.WAITFORUSER);
            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
            ad.setMessage("Let's start\n "
                    + currentJob.getProject() + ": " + currentJob.getSeriesName() + "\n"
                    + currentJob.getNumber() + " x " + (int) (currentJob.getExposure() / 1000) + "s\n"
                    + "Delay after each exposure:" + currentJob.getDelayAfterEachExposure() / 1000 + "s\n"
                    + "Camera controls time: " + camera.isExposureSetOnCamera(currentJob.getExposure()) + "\n"
                    + "Total time: " + ((currentJob.getNumber() * (currentJob.getExposure() + currentJob.getDelayAfterEachExposure())) / 1000)+"s"
            );
           
            ad.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    currentJob.setTriggerStatus(PhotoSerie.TriggerJobStatus.PREPARED);
                    processingLoop();
                }
            });
            ad.setNegativeButton("Pause", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    currentJob.setTriggerStatus(PhotoSerie.TriggerJobStatus.NEW);
                    pause();
                }
            });
            ad.create().show();
        }

        final Handler handler = new Handler();

        if ((currentJob.getTriggerStatus() == PhotoSerie.TriggerJobStatus.PREPARED||currentJob.getTriggerStatus() == PhotoSerie.TriggerJobStatus.RUNNING)) {
            handler.postDelayed(new Runnable() {

                public void run() {
                    if ((((AzoTriggerServiceApplication) getActivity().getApplication()).getJobProcessor().getStatus() == ProcessorStatus.PAUSED)
                            && (!currentJob.isToggleIsOpen())) {

                        return;
                    }
                    camera.trigger();
                    for (JobProgressListener j : jobProgressListeners) {
                        j.jobProgressed(currentJob);
                    }
                    if (currentJob.getFirstTriggerTime() == 0) {
                        currentJob.setFirstTriggerTime(System.currentTimeMillis());
                    }
                    currentJob.setTriggerStatus(PhotoSerie.TriggerJobStatus.RUNNING);
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
                            time = currentJob.getExposure() + currentJob.getDelayAfterEachExposure();
                        } else {
                            if (currentJob.isToggleIsOpen()) {
                                time = currentJob.getExposure();
                            } else {
                                time = currentJob.getDelayAfterEachExposure();
                            }
                        }
                        handler.postDelayed(this, time);
                    } else {
                        currentJob.setTriggerStatus(PhotoSerie.TriggerJobStatus.FINISHED);
                        processingLoop();
                    }
                }
            }, currentJob.getInitialDelay());
        } else {
        }

    }

    /**
     * @return the jobs
     */
    public LinkedList<TriggerPhotoSerie> getJobs() {
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
     * @param pstatus the status to set
     */
    public void setStatus(ProcessorStatus pstatus) {
        if (this.status == pstatus) {
            return;
        }
        for (JobProcessorStatusListener j : jobProcessorStatusListeners) {
            j.jobProcessStatusChanged(this.status, pstatus);
        }
        this.status = pstatus;
    }

    public void addJobProcesssorStatusListener(JobProcessorStatusListener j) {
        if (!jobProcessorStatusListeners.contains(j)) {
            jobProcessorStatusListeners.add(j);
        }
    }

    public void addJobProgressListener(JobProgressListener j) {
        if (!jobProgressListeners.contains(j)) {
            jobProgressListeners.add(j);
        }
    }

    /**
     * @return the activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
