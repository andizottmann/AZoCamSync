/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.ftpservice;

import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author Andreas
 */
public class AZoFTPFile {
    
    public String dir;
    public String rawname;
    public FTPFile ftpFile;
    
    public AZoFTPFile(FTPFile ftpFile, String dir) {
        this.ftpFile = ftpFile;
        this.rawname = ftpFile.getRawListing();
        this.dir = dir;
        
    }
    
    public boolean equals(AZoFTPFile af) {
        return (dir.equals(af.dir) && rawname.equals(af.rawname));
    }
    
}
