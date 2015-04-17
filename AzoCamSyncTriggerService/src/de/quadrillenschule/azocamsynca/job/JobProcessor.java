    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.AZoTriggerServiceActivity;
import de.quadrillenschule.azocamsynca.AzoTriggerServiceApplication;
import de.quadrillenschule.azocamsynca.NikonIR;
import de.quadrillenschule.azocamsynca.R;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Andreas
 */
public class JobProcessor {

    private LinkedList<TriggerPhotoSerie> jobs = new LinkedList<TriggerPhotoSerie>();
    private LinkedList<JobProcessorStatusListener> jobProcessorStatusListeners = new LinkedList<JobProcessorStatusListener>();
    private LinkedList<JobProgressListener> jobProgressListeners = new LinkedList<JobProgressListener>();

    public enum ProcessorStatus {

        PROCESSING, PAUSED
    };

    private ProcessorStatus status = ProcessorStatus.PAUSED;
    private static ProcessorStatus requestStatus = ProcessorStatus.PAUSED;

    private Handler handler;
    private Activity activity;
    AlertDialog alertDialog;
private StatusUpdater statusUpdater;
    
    public JobProcessor(Activity ac) {
        handler = new Handler();
        this.activity = ac;
        statusUpdater=new StatusUpdater((AZoTriggerServiceActivity) ac);
        

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
            if (j.getTriggerStatus() != TriggerPhotoSerie.TriggerJobStatus.FINISHED_TRIGGERING) {
                currentJobT = j;
                if ((j.getTriggerStatus() == PhotoSerie.TriggerJobStatus.WAITFORUSER) && ((alertDialog != null) && (alertDialog.isShowing()))) {
                    return;
                }
                if ((j.getTriggerStatus() == PhotoSerie.TriggerJobStatus.WAITFORUSER)) {
                    currentJobT.setTriggerStatus(PhotoSerie.TriggerJobStatus.NEW);
                }
                break;
            }
        }
    //    statusUpdater.startExposure(currentJobT);
        if (currentJobT == null) {
            setStatus(ProcessorStatus.PAUSED);
            return;
        }

        final TriggerPhotoSerie currentJob = currentJobT;
        final NikonIR camera = ((AzoTriggerServiceApplication) getActivity().getApplication()).getCamera();

        if (currentJob.getTriggerStatus() == PhotoSerie.TriggerJobStatus.NEW) {
            currentJob.setTriggerStatus(PhotoSerie.TriggerJobStatus.WAITFORUSER);
            if (currentJob.getSeriesName().equals(PhotoSerie.TESTSHOTS)) {
                doTestShots(currentJob);
                return;
            }
            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity(), R.style.dialog);
            ad.setTitle(currentJob.getProject() + ": " + currentJob.getSeriesName());
            ad.setMessage(
                    currentJob.getNumber() + " x " + (int) (currentJob.getExposure() / 1000) + "s\n\n"
                    + "Delay after each exposure:" + currentJob.getDelayAfterEachExposure() / 1000 + "s\n"
                    + "Camera controls time: " + camera.isExposureSetOnCamera(currentJob.getExposure()) + "\n"
                    + "Total time: " + ((currentJob.getNumber() * (currentJob.getExposure() + currentJob.getDelayAfterEachExposure())) / 1000) + "s"
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

            MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.oida_peda);
            mediaPlayer.start();
            ad.create();
            alertDialog = ad.show();

        }

        final Handler handler = new Handler();

        if ((currentJob.getTriggerStatus() == PhotoSerie.TriggerJobStatus.PREPARED || currentJob.getTriggerStatus() == PhotoSerie.TriggerJobStatus.RUNNING)) {
            handler.postDelayed(new Runnable() {

                public void run() {
                    if ((((AzoTriggerServiceApplication) getActivity().getApplication()).getJobProcessor().getStatus() == ProcessorStatus.PAUSED)
                            && (!currentJob.isToggleIsOpen())) {

                        return;
                    }
                    camera.trigger();
                     statusUpdater.startExposure(currentJob);
       
                    for (JobProgressListener j : jobProgressListeners) {
                        j.jobProgressed(currentJob);
                    }
                    if (currentJob.getFirstTriggerTime() == 0) {
                        currentJob.setFirstTriggerTime(System.currentTimeMillis());
                    }
                    currentJob.setTriggerStatus(PhotoSerie.TriggerJobStatus.RUNNING);

                    if (!camera.isExposureSetOnCamera(currentJob.getExposure())) {
                        if (!currentJob.isToggleIsOpen()) {
                            currentJob.setToggleIsOpen(!currentJob.isToggleIsOpen());

                            currentJob.setLastTriggerTime(System.currentTimeMillis());
                        } else {
                            currentJob.setToggleIsOpen(!currentJob.isToggleIsOpen());
                            currentJob.setTriggered(currentJob.getTriggered() + 1);

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
                        currentJob.setTriggerStatus(PhotoSerie.TriggerJobStatus.FINISHED_TRIGGERING);
                        processingLoop();
                    }
                }
            }, currentJob.getInitialDelay());
        } else {
        }

    }

    public void doTestShots(final TriggerPhotoSerie job) {
        final NikonIR camera = ((AzoTriggerServiceApplication) getActivity().getApplication()).getCamera();
        final JobProcessor jobProcessor = ((AzoTriggerServiceApplication) getActivity().getApplication()).getJobProcessor();

        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(), R.style.dialog);
        job.setTriggerStatus(PhotoSerie.TriggerJobStatus.WAITFORUSER);
        adb.setTitle("Test Shots");
        adb.setMessage("This series collects all images during preparation of the project\n"
                + job.getProject() + "\n"
                + "Camera controls time: " + camera.isExposureSetOnCamera(job.getExposure())
        );
        if (!job.isToggleIsOpen()) {
            adb.setPositiveButton("Finish", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    job.setTriggerStatus(PhotoSerie.TriggerJobStatus.FINISHED_TRIGGERING);
                    jobProcessor.fireJobProgressEvent(job);
                    processingLoop();
                }
            });
        }

        adb.setNegativeButton("Trigger", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                camera.trigger();
                if (!camera.isExposureSetOnCamera(job.getExposure())) {
                    job.setToggleIsOpen(!job.isToggleIsOpen());

                    if (!job.isToggleIsOpen()) {
                        job.setNumber(job.getNumber() + 1);
                        job.setTriggered(job.getTriggered() + 1);

                    }

                } else {
                    job.setNumber(job.getNumber() + 1);
                    job.setTriggered(job.getTriggered() + 1);
                }
                doTestShots(job);
            }
        });
        MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.oida_peda);
        mediaPlayer.start();
        // adb.create().show();
        adb.create();

        alertDialog = adb.show();
    }

    public void confirmPreparedDialog() {
        if ((alertDialog != null)) {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick();
            alertDialog.dismiss();
        }
    }
    public static final String JOBQUEUE = "AZOJOBQUEUE";

    public void store() {
        SharedPreferences prefs = activity.getApplication().getSharedPreferences(JOBQUEUE, Context.MODE_PRIVATE);
        prefs.edit().putString(JOBQUEUE, toJSONArray().toString()).commit();

    }

    public void load() {
        SharedPreferences prefs = activity.getApplication().getSharedPreferences(JOBQUEUE, Context.MODE_PRIVATE);
        String string;
        string = prefs.getString(JOBQUEUE, "[]");
        try {
            jobs = fromJSONArray(new JSONArray(string));
        } catch (JSONException ex) {
            Logger.getLogger(JobProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JSONArray toJSONArray() {
        JSONArray ja = new JSONArray();
        for (PhotoSerie ps : jobs) {
            ja.put(ps.toJSONObject());
        }
        return ja;
    }

    public LinkedList<TriggerPhotoSerie> fromJSONArray(JSONArray ja) {
        LinkedList<TriggerPhotoSerie> retval = new LinkedList<TriggerPhotoSerie>();
        for (int i = 0; i < ja.length(); i++) {
            TriggerPhotoSerie tps = new TriggerPhotoSerie(activity);
            try {
                tps.fromJSONObject(ja.getJSONObject(i));
            } catch (JSONException ex) {
                Logger.getLogger(JobProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
            retval.add(tps);
        }
        return retval;
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

    public void fireJobProgressEvent(PhotoSerie ps) {
        for (JobProgressListener j : jobProgressListeners) {
            j.jobProgressed(ps);
        }
    }

    public void fireJobProcesserStatusEvent(ProcessorStatus oldStatus, ProcessorStatus newStatus) {
        for (JobProcessorStatusListener j : jobProcessorStatusListeners) {
            j.jobProcessStatusChanged(oldStatus, newStatus);
        }
    }
}
