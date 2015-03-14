/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsync;

import com.google.android.maps.Projection;
import de.quadrillenschule.azocamsynca.helpers.Formats;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author D061339
 */
public class PhotoSerie {

    public enum TriggerJobStatus {

        NEW, WAITFORUSER, PREPARED, RUNNING, FINISHED_TRIGGERING
    };

    public enum Fields {

        PROJECT, SERIES_NAME, INITIAL_DELAY, EXPOSURE, DELAY_AFTER_EACH_EXPOSURE, NUMBER_OF_EXPOSURES, RECEIVED, ID, TRIGGER_JOB_STATUS, TRIGGERED
    };

    public static String TESTSHOTS = "testShots";
    private long firstTriggerTime = 0;
    private int number = 0;
    private String seriesName = TESTSHOTS;
    private TriggerJobStatus status = TriggerJobStatus.NEW;
    private long initialDelay = 0;
    private String project = "New Project";
    private long lastTriggerTime = 0;
    private int triggered = 0;
    private long exposure = 0;
    private long delayAfterEachExposure = 0;
    private long received = 0;
    private String id;

    public PhotoSerie() {
        setId(UUID.randomUUID().toString() + "");
    }

    public void setFieldFromHR(Fields field, String value) {
        switch (field) {
            case PROJECT:
                setProject(value);
                break;
            case SERIES_NAME:
                setSeriesName(value);
                break;
            case INITIAL_DELAY:
                setInitialDelay(Formats.toLong(value));
                break;
            case EXPOSURE:
                setExposure(Formats.toLong(value));
                break;
            case DELAY_AFTER_EACH_EXPOSURE:
                setDelayAfterEachExposure(Formats.toLong(value));
                break;
            case NUMBER_OF_EXPOSURES:
                setNumber(Integer.parseInt(value));
                break;
            case RECEIVED:
                setReceived(Integer.parseInt(value));
                break;
            case ID:
                setId(value);
                break;
            case TRIGGER_JOB_STATUS:
                setTriggerStatus(TriggerJobStatus.valueOf(value));
                break;
            case TRIGGERED:
                setTriggered(Integer.parseInt(value));
                break;

        }
    }

    public JSONObject toJSONObject() {
        JSONObject retval = new JSONObject();
        try {
            retval.put(Fields.PROJECT.name(), getProject());
            retval.put(Fields.SERIES_NAME.name(), getSeriesName());
            retval.put(Fields.INITIAL_DELAY.name(), getInitialDelay());
            retval.put(Fields.EXPOSURE.name(), getExposure());
            retval.put(Fields.DELAY_AFTER_EACH_EXPOSURE.name(), getDelayAfterEachExposure());
            retval.put(Fields.NUMBER_OF_EXPOSURES.name(), getNumber());
            retval.put(Fields.ID.name(), getId());
            retval.put(Fields.RECEIVED.name(), received);
            retval.put(Fields.TRIGGER_JOB_STATUS.name(), getTriggerStatus().name());
            retval.put(Fields.TRIGGERED.name(), getTriggered());
        } catch (JSONException ex) {
            Logger.getLogger(PhotoSerie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retval;
    }

    public void fromJSONObject(JSONObject jso) {
        try {
            setProject(jso.getString(Fields.PROJECT.name()));
            setSeriesName(jso.getString(Fields.SERIES_NAME.name()));
            setInitialDelay(jso.getLong(Fields.INITIAL_DELAY.name()));
            setExposure(jso.getLong(Fields.EXPOSURE.name()));
            setDelayAfterEachExposure(jso.getLong(Fields.DELAY_AFTER_EACH_EXPOSURE.name()));
            setNumber(jso.getInt(Fields.NUMBER_OF_EXPOSURES.name()));
            setId(jso.getString(Fields.ID.name()));
            setReceived(jso.getInt(Fields.RECEIVED.name()));
            setTriggerStatus(TriggerJobStatus.valueOf(jso.getString(Fields.TRIGGER_JOB_STATUS.name())));
            setTriggered(jso.getInt(Fields.TRIGGERED.name()));

        } catch (JSONException ex) {
            Logger.getLogger(PhotoSerie.class.getName()).log(Level.SEVERE, null, ex);
        }

        return;
    }

    public Object getValueForField(PhotoSerie.Fields field) {
        switch (field) {
            case PROJECT:
                return getProject();
            case SERIES_NAME:
                return getSeriesName();
            case INITIAL_DELAY:
                return getInitialDelay();
            case EXPOSURE:
                return getExposure();
            case DELAY_AFTER_EACH_EXPOSURE:
                return getDelayAfterEachExposure();
            case NUMBER_OF_EXPOSURES:
                return getNumber();
            case RECEIVED:
                return getReceived();
            case ID:
                return getId();
            case TRIGGERED:
                return getTriggered();
            case TRIGGER_JOB_STATUS:
                return getTriggerStatus();

        }
        return null;
    }

    /**
     * @return the initialDelay
     */
    public long getInitialDelay() {
        return initialDelay;
    }

    /**
     * @return the delayAfterEachExposure
     */
    public long getDelayAfterEachExposure() {
        return delayAfterEachExposure;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the status
     */
    public TriggerJobStatus getTriggerStatus() {
        return status;
    }

    /**
     * @return the lastTriggerTime
     */
    public long getLastTriggerTime() {
        return lastTriggerTime;
    }

    /**
     * @param status the status to set
     */
    public void setTriggerStatus(TriggerJobStatus status) {
        this.status = status;
    }

    /**
     * @param delayAfterEachExposure the delayAfterEachExposure to set
     */
    public void setDelayAfterEachExposure(long delayAfterEachExposure) {
        this.delayAfterEachExposure = delayAfterEachExposure;
    }

    /**
     * @param seriesName the seriesName to set
     */
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return the exposure
     */
    public long getExposure() {
        return exposure;
    }

    /**
     * @return the firstTriggerTime
     */
    public long getFirstTriggerTime() {
        return firstTriggerTime;
    }

    /**
     * @param exposure the exposure to set
     */
    public void setExposure(long exposure) {
        this.exposure = exposure;
    }

    /**
     * @param triggered the triggered to set
     */
    public void setTriggered(int triggered) {
        this.triggered = triggered;
    }

    /**
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * @param lastTriggerTime the lastTriggerTime to set
     */
    public void setLastTriggerTime(long lastTriggerTime) {
        this.lastTriggerTime = lastTriggerTime;
    }

    /**
     * @return the seriesName
     */
    public String getSeriesName() {
        return seriesName;
    }

    /**
     * @param project the project to set
     */
    public void setProject(String project) {
        this.project = project;
    }

    /**
     * @return the triggered
     */
    public int getTriggered() {
        return triggered;
    }

    /**
     * @param firstTriggerTime the firstTriggerTime to set
     */
    public void setFirstTriggerTime(long firstTriggerTime) {
        this.firstTriggerTime = firstTriggerTime;
    }

    /**
     * @param initialDelay the initialDelay to set
     */
    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the received
     */
    public long getReceived() {
        return received;
    }

    /**
     * @param received the received to set
     */
    public void setReceived(long received) {
        this.received = received;
    }

    public String shortDescription() {
        return getTriggerStatus().name() + " "
                + getProject() + "  " + getSeriesName() + "\n"
                + " Triggered: " + getTriggered() + "/" + getNumber() + "  "
                + " Received: " + getReceived() + "/" + getNumber() + "\n"
                + " Planned: " + getNumber() + "x (" + getExposure() / 1000 + " + " + getDelayAfterEachExposure() / 1000 + ")s ="
                + " Total time: " + ((getNumber() * (getExposure() + getDelayAfterEachExposure())) / 1000) + "s";

    }
}
