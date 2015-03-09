/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 *
 * @author D061339
 */
public class Formats {
    
    public static String DF = "H:mm:ss";
    
    public static String toString(long time) {
        return DurationFormatUtils.formatDuration(time, DF,true);
     
    }
    
    public static long toLong(String time) {
       SimpleDateFormat sdf=new SimpleDateFormat(DF);
       long retval=0; 
       try {
           retval= sdf.parse(time).getTime()-sdf.parse("0:00:00").getTime();
        } catch (ParseException ex) {
            Logger.getLogger(Formats.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return retval;
    }
   
}
