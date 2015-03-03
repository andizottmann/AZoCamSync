/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class History {

    public enum Fields {

        PROJECT, SERIES_NAME, INITIAL_DELAY, EXPOSURE, EXPOSURE_GAP_NUMBER, NUMBER_OF_EXPOSURES
    };
    Application application;

    public History(Application application) {
        this.application = application;
    }

    public LinkedList<String> getHistory(Fields field, String defaultString) {
        LinkedList<String> retval = new LinkedList();
        SharedPreferences prefs = application.getSharedPreferences("HISTORY_AZOTRIGGER", Context.MODE_PRIVATE);
        for (String s : prefs.getString(field.name(), defaultString).split(",")) {
            retval.add(s);
        };

        return retval;
    }

    public void addHistory(Fields field, String value) {
        SharedPreferences prefs = application.getSharedPreferences("HISTORY_AZOTRIGGER", Context.MODE_PRIVATE);
        LinkedList<String> history = getHistory(field, "");
        if (history.contains(value)) {
            history.remove(value);

        }
        history.addFirst(value);
        String retval = "", sep = "";
        for (String s : history) {
            retval += sep + s;
            sep=",";
        }
        prefs.edit().putString(field.name(), retval).commit();
    }
}
