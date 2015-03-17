/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode.gui;

import javax.swing.Timer;

/**
 *
 * @author D061339
 */
public class AstroModeBackgroundUpdater extends Thread {

    final Timer timer;
    final AstroModeJPanel astroModeJPanel;
    public static final int TIMER_DELAY=1000;

    public AstroModeBackgroundUpdater(Timer timer,AstroModeJPanel astroModeJPanel) {
        super();
        this.timer = timer;
        this.astroModeJPanel=astroModeJPanel;
    }

    @Override
    public void run() {
        timer.stop();
        astroModeJPanel.update();
        
        timer.setInitialDelay(TIMER_DELAY);
        timer.setDelay(TIMER_DELAY);
        timer.start();
    }

}
