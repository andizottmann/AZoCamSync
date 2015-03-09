/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsyncd.astromode_old.gui;

import de.quadrillenschule.azocamsyncd.astromode_old.PhotoProject;
import de.quadrillenschule.azocamsyncd.astromode_old.ReceivePhotoSerie;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas
 */
public class PhotoProjectTableModel extends AbstractTableModel {

    PhotoProject project;

    public enum Columns {

        STATUS, NAME, NUMBER_OF_PLANNED_PHOTOS, NUMBER_OF_RECEIVED_PHOTOS, EXPOSURE_TIME
    };

    public PhotoProjectTableModel(PhotoProject project) {
        this.project = project;
    }

    @Override
    public int getRowCount() {
        return project.getPhotoSeries().size();
    }

    @Override
    public int getColumnCount() {
        return Columns.values().length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return Columns.values()[columnIndex].name();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (Columns.values()[columnIndex]) {
            case NAME:
                return String.class;
            case EXPOSURE_TIME:
                return Long.class;
            case STATUS:
                return String.class;
        };
        return Integer.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (Columns.values()[columnIndex]) {

            case STATUS:
                return false;
            case NUMBER_OF_RECEIVED_PHOTOS:
                return false;
        };
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReceivePhotoSerie ps = project.getPhotoSeries().get(rowIndex);
        switch (Columns.values()[columnIndex]) {
            case NAME:
                return ps.getName();
            case NUMBER_OF_PLANNED_PHOTOS:
                return ps.getNumberOfPlannedPhotos();
            case NUMBER_OF_RECEIVED_PHOTOS:
                return ps.getPhotos().size();

            case EXPOSURE_TIME:
                return ps.getExposureTimeInMs();
            case STATUS:
                return ps.getStatus();
        };
        return null;

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ReceivePhotoSerie ps = project.getPhotoSeries().get(rowIndex);
        switch (Columns.values()[columnIndex]) {
            case NAME:
                ps.setName(aValue.toString());
                return;
            case NUMBER_OF_PLANNED_PHOTOS:
                ps.setNumberOfPlannedPhotos(Integer.parseInt(aValue.toString()));
                return;
            case NUMBER_OF_RECEIVED_PHOTOS:
                return;

            case EXPOSURE_TIME:
                ps.setExposureTimeInMs(Long.parseLong(aValue.toString()));
                return;
            case STATUS:
                return;
        };
    }

  

}
