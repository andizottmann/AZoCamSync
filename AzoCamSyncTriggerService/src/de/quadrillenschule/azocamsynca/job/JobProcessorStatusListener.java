/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

/**
 *
 * @author D061339
 */
public interface JobProcessorStatusListener {
    public void jobProcessStatusChanged(JobProcessor.ProcessorStatus oldStatus,JobProcessor.ProcessorStatus newStatus);
}
