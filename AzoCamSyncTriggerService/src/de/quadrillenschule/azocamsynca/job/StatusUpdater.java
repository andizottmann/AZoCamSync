/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import de.quadrillenschule.azocamsynca.AZoTriggerServiceActivity;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Andreas
 */
public class StatusUpdater {

    TriggerPhotoSerie tps;
    AZoTriggerServiceActivity azoactivity;
    long startTime = 0;
    Timer timer;

    public StatusUpdater(AZoTriggerServiceActivity azoactivity) {
        this.azoactivity = azoactivity;

    }

    public void startExposure(TriggerPhotoSerie tps) {
        this.tps = tps;
        startTime = System.currentTimeMillis();

        if (timer != null) {
            try {
                timer.cancel();
            } catch (Exception e) {
            }
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updateTextView();
            }
        }, 0, 500);

    }

    public void updateTextView() {
        azoactivity.updateStatusText(printStatus());
    }

    private String printStatus() {
        if (tps == null) {
            return "No current job.";
        }
        long value = getShutterRemainingTime();

        if (tps.isToggleIsOpen()) {
            return "Shutter open for: " + value + "s";
        } else {
            return "Shutter is closed.";
        }
    }

    public long getShutterRemainingTime() {
        if ((tps == null) || (!tps.isToggleIsOpen())) {
            return 0;
        }
        return ((tps.getExposure() - (System.currentTimeMillis() - startTime)) / 1000);
    }
}
