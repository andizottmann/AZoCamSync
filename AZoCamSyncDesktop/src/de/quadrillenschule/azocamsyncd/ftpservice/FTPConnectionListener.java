/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.ftpservice;

/**
 *
 * @author Andreas
 */
public interface FTPConnectionListener {

    public static enum FTPConnectionStatus {

        TRYING, DOWNLOADING, CONNECTED, SUCCESS, NOCONNECTION, DOWNLOADERROR, LOCALSTORAGEERROR, NUMBER_OF_FILES_DETECTED, NUMBER_OF_SYNCHRONISABLE_FILES_DETECTED, DELETING_FILES, NEW_LOCAL_FILE,FULLY_SYNCED
    };

    public void receiveNotification(FTPConnectionStatus status, String message, int progress);
}
