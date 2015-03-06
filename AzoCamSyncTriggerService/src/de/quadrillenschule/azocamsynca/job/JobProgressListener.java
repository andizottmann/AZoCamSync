/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import de.quadrillenschule.azocamsync.PhotoSerie;

/**
 *
 * @author D061339
 */
public interface JobProgressListener {
    public void jobProgressed(PhotoSerie ps);
}
