/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.gui;

import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class PollingIntervallListEntry {

    public static LinkedList<PollingIntervallListEntry> allEntries() {
        LinkedList<PollingIntervallListEntry> retval = new LinkedList<>();
        retval.add(new PollingIntervallListEntry("5 Secs", 5));
        retval.add(new PollingIntervallListEntry("10 Secs", 10));
        retval.add(new PollingIntervallListEntry("20 Secs", 20));
        retval.add(new PollingIntervallListEntry("30 Secs", 30));
        retval.add(new PollingIntervallListEntry("1 Minute", 60));
        retval.add(new PollingIntervallListEntry("2 Minutes", 120));
        retval.add(new PollingIntervallListEntry("3 Minutes", 180));
        retval.add(new PollingIntervallListEntry("5 Minutes", 300));
        retval.add(new PollingIntervallListEntry("10 Minutes", 600));

        return retval;
    }

    public static int indexOf(int seconds) {
        LinkedList<PollingIntervallListEntry> ae = allEntries();
        for (PollingIntervallListEntry ple : ae) {
            if (ple.seconds == seconds) {
                return ae.indexOf(ple);
            }
        }
        return 0;
    }
    public String humanReadable;
    public int seconds;

    public PollingIntervallListEntry(String humanReadable, int seconds) {
        this.humanReadable = humanReadable;
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return humanReadable;
    }
}
