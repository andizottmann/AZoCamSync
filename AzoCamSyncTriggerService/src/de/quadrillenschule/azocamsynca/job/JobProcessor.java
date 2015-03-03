/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import android.app.Activity;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class JobProcessor {

    private LinkedList<Job> jobs = new LinkedList<Job>();

    public JobProcessor() {

    }

    public void executeNext() {
        for (Job j : jobs) {
            if (j.getStatus() == Job.JobStatus.NEW) {
                j.execute();
            }
        }

    }

    /**
     * @return the jobs
     */
    public LinkedList<Job> getJobs() {
        return jobs;
    }

}
