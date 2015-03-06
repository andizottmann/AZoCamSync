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

    static int SIZE=7;
    public enum Fields {

        PROJECT, SERIES_NAME, INITIAL_DELAY, EXPOSURE, DELAY_AFTER_EACH_EXPOSURE, NUMBER_OF_EXPOSURES
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
        int size = SIZE;
        if (history.size() < SIZE) {
            size = history.size();
        }
        for (String s : history.subList(0, size)) {
            retval += sep + s;
            sep = ",";
        }
        prefs.edit().putString(field.name(), retval).commit();
    }
}
