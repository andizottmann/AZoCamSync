/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode.gui;

import de.quadrillenschule.azocamsync.PhotoSerie;
import java.util.LinkedList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas
 */
public class PhotoSeriesTableModel extends AbstractTableModel {

    private LinkedList<PhotoSerie> photoSeries;

    public PhotoSeriesTableModel(LinkedList<PhotoSerie> photoSeries) {
        this.photoSeries = photoSeries;
    }

    @Override
    public int getRowCount() {
        return getPhotoSeries().size();
    }

    @Override
    public int getColumnCount() {
        return PhotoSerie.Fields.values().length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return PhotoSerie.Fields.values()[columnIndex].name();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (PhotoSerie.Fields.values()[columnIndex]) {
            case PROJECT:
                return String.class;
            case SERIES_NAME:
                return String.class;
            case ID:
                return String.class;
            case TRIGGER_JOB_STATUS:
                return String.class;
            
        };
        return Long.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
      
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
       PhotoSerie ps=getPhotoSeries().get(rowIndex);
       return ps.getValueForField(PhotoSerie.Fields.values()[columnIndex]);

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
       
    }

    /**
     * @return the photoSeries
     */
    public LinkedList<PhotoSerie> getPhotoSeries() {
        return photoSeries;
    }

    /**
     * @param photoSeries the photoSeries to set
     */
    public void setPhotoSeries(LinkedList<PhotoSerie> photoSeries) {
        this.photoSeries = photoSeries;
    }

}
