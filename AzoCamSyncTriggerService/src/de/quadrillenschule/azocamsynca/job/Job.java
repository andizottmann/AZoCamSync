/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

/**
 *
 * @author Andreas
 */
public interface Job {
     public enum JobStatus {

        NEW, PREPARED, RUNNING, FINISHED
    };
    
    public JobStatus getStatus();
    public void setJobProcessor(JobProcessor jp);
}
