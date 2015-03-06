package de.quadrillenschule.azocamsynca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.NumberPicker;
import com.ikovac.timepickerwithseconds.view.TimePicker;
import de.quadrillenschule.azocamsynca.job.JobProcessor;
import de.quadrillenschule.azocamsynca.job.JobProcessorStatusListener;
import de.quadrillenschule.azocamsynca.job.TriggerPhotoSerie;

public class AZoTriggerServiceActivity extends Activity implements JobProcessorStatusListener {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AzoTriggerServiceApplication) getApplication()).onActivityCreate(this);
        ((AzoTriggerServiceApplication) getApplication()).getJobProcessor().addJobProcesssorStatusListener(this);
        setContentView(R.layout.main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((AzoTriggerServiceApplication) getApplication()).onActivityResume(this);

        prepareJobConfigurationFields();
        prepareButtons();
    }

    public void prepareJobConfigurationFields() {
        final History history = new History(getApplication());
        final Activity myActivity = this;

        final NumberPicker numberOfexposuresPicker = (NumberPicker) findViewById(R.id.numberOfExposuresPicker);
        final Button numberOfExposuresHistory = (Button) findViewById(R.id.numberOfExposuresHistoryButton);
        final TimePicker exposureTimePicker = (TimePicker) findViewById(R.id.exposureTimePicker);
        final Button exposureHistoryButton = (Button) findViewById(R.id.exposureHistoryButton);
        final TimePicker initialDelayTimePicker = (TimePicker) findViewById(R.id.initialDelayPicker);
        final Button initialDelayHistoryButton = (Button) findViewById(R.id.initialDelayHistoryButton);
        final TimePicker exposureGapTimePicker = (TimePicker) findViewById(R.id.delayAfterEachExposurePicker);
        final Button exposureGapHistoryButton = (Button) findViewById(R.id.delayAfterEachExposureHistoryButton);
        final EditText projectEditText = (EditText) findViewById(R.id.projectEditText);
        final Button projectHistoryButton = (Button) findViewById(R.id.projectHistoryButton);
        final EditText seriesEditText = (EditText) findViewById(R.id.seriesEditText);
        final Button seriesHistoryButton = (Button) findViewById(R.id.seriesHistoryButton);

        numberOfexposuresPicker.setMinValue(1);
        numberOfexposuresPicker.setMaxValue(999);
        numberOfexposuresPicker.setValue(Integer.parseInt(history.getHistory(History.Fields.NUMBER_OF_EXPOSURES, "10").getFirst()));

        numberOfExposuresHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                ListPopupWindow lpw = new ListPopupWindow(myActivity);
                lpw.setAdapter(new ArrayAdapter(myActivity, R.layout.history_list_item, history.getHistory(History.Fields.NUMBER_OF_EXPOSURES, "10")));
                lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        numberOfexposuresPicker.setValue(Integer.parseInt(history.getHistory(History.Fields.NUMBER_OF_EXPOSURES, "10").get(arg2)));
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                lpw.setAnchorView(findViewById(R.id.numberOfExposuresHistoryButton));
                lpw.setModal(true);
                lpw.show();
            }
        });
        exposureTimePicker.setIs24HourView(true);
        exposureTimePicker.setTimeInMs(4000);

        exposureHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                showHistoryPopup(myActivity, exposureHistoryButton, History.Fields.EXPOSURE, exposureTimePicker);
            }
        });

        initialDelayTimePicker.setIs24HourView(true);
        initialDelayTimePicker.setTimeInMs(3000);

        initialDelayHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                showHistoryPopup(myActivity, initialDelayHistoryButton, History.Fields.INITIAL_DELAY, initialDelayTimePicker);
            }
        });

        exposureGapTimePicker.setIs24HourView(true);
        exposureGapTimePicker.setTimeInMs(0000);

        exposureGapHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                showHistoryPopup(myActivity, exposureGapHistoryButton, History.Fields.DELAY_AFTER_EACH_EXPOSURE, exposureGapTimePicker);
            }
        });

        projectEditText.setText(history.getHistory(History.Fields.PROJECT, "Noname Project").getFirst());

        projectHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                final History history = new History(myActivity.getApplication());
                ListPopupWindow lpw = new ListPopupWindow(myActivity);
                lpw.setAdapter(new ArrayAdapter(myActivity, R.layout.history_list_item, history.getHistory(History.Fields.PROJECT, "Noname Project")));
                lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        projectEditText.setText(history.getHistory(History.Fields.PROJECT, "Noname Project").get(arg2));
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                lpw.setAnchorView(projectHistoryButton);
                lpw.setModal(true);
                lpw.show();
            }
        });

        seriesEditText.setText(history.getHistory(History.Fields.SERIES_NAME, "flats").getFirst());

        seriesHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                final History history = new History(myActivity.getApplication());
                ListPopupWindow lpw = new ListPopupWindow(myActivity);
                lpw.setAdapter(new ArrayAdapter(myActivity, R.layout.history_list_item, history.getHistory(History.Fields.SERIES_NAME, "flats")));
                lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        projectEditText.setText(history.getHistory(History.Fields.SERIES_NAME, "flats").get(arg2));
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                lpw.setAnchorView(seriesHistoryButton);
                lpw.setModal(true);
                lpw.show();
            }
        });
    }

    public void prepareButtons() {
        final History history = new History(getApplication());
        final Activity myActivity = this;

        final NumberPicker numberOfexposuresPicker = (NumberPicker) findViewById(R.id.numberOfExposuresPicker);
        final TimePicker exposureTimePicker = (TimePicker) findViewById(R.id.exposureTimePicker);
        final TimePicker initialDelayTimePicker = (TimePicker) findViewById(R.id.initialDelayPicker);
        final TimePicker exposureGapTimePicker = (TimePicker) findViewById(R.id.delayAfterEachExposurePicker);
        final EditText projectEditText = (EditText) findViewById(R.id.projectEditText);
        final EditText seriesEditText = (EditText) findViewById(R.id.seriesEditText);

        Button viewJobListButton = (Button) findViewById(R.id.viewjoblist);
        viewJobListButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent intent = new Intent(myActivity, AZoJobListActivity.class);
                startActivity(intent);
            }
        });

        Button triggerButton = (Button) findViewById(R.id.trigger);
        triggerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                ((AzoTriggerServiceApplication) getApplication()).getCamera().trigger();
            }
        });

        final Button addJobButton = (Button) findViewById(R.id.addJobButton);

        addJobButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                TriggerPhotoSerie tj = new TriggerPhotoSerie(myActivity);

                tj.setNumber(numberOfexposuresPicker.getValue());
                history.addHistory(History.Fields.NUMBER_OF_EXPOSURES, numberOfexposuresPicker.getValue() + "");

                tj.setExposure(exposureTimePicker.getTimeInMs());
                history.addHistory(History.Fields.EXPOSURE, exposureTimePicker.getTimeInMs() + "");

                tj.setInitialDelay(initialDelayTimePicker.getTimeInMs());
                history.addHistory(History.Fields.INITIAL_DELAY, initialDelayTimePicker.getTimeInMs() + "");

                tj.setDelayAfterEachExposure(exposureGapTimePicker.getTimeInMs());
                history.addHistory(History.Fields.DELAY_AFTER_EACH_EXPOSURE, exposureGapTimePicker.getTimeInMs() + "");

                tj.setProject(projectEditText.getText().toString());
                history.addHistory(History.Fields.PROJECT, projectEditText.getText().toString() + "");

                tj.setSeriesName(seriesEditText.getText().toString());
                history.addHistory(History.Fields.SERIES_NAME, seriesEditText.getText().toString() + "");

                JobProcessor jobProcessor = ((AzoTriggerServiceApplication) getApplication()).getJobProcessor();
                jobProcessor.getJobs().add(tj);
                jobProcessor.processingLoop();
            }
        });

        final Button startstopQueueButton = (Button) findViewById(R.id.startstopQueueButton);

        startstopQueueButton.setText(((AzoTriggerServiceApplication) getApplication()).getJobProcessor().getStatus().name());
        startstopQueueButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                JobProcessor jobProcessor = ((AzoTriggerServiceApplication) getApplication()).getJobProcessor();
                if (jobProcessor.getStatus() != JobProcessor.ProcessorStatus.PROCESSING) {
                    jobProcessor.start();

                } else {
                    jobProcessor.pause();

                }
                startstopQueueButton.setText(jobProcessor.getStatus().name());
            }

        });

    }

    public static void showHistoryPopup(Activity myActivity, View anchorView, final History.Fields field, final TimePicker timepicker) {
        final History history = new History(myActivity.getApplication());
        ListPopupWindow lpw = new ListPopupWindow(myActivity);
        lpw.setAdapter(new ArrayAdapter(myActivity, R.layout.history_list_item, history.getHistory(field, "10")));
        lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                timepicker.setTimeInMs(Integer.parseInt(history.getHistory(field, "4000").get(arg2)));
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        lpw.setAnchorView(anchorView);
        lpw.setModal(true);
        lpw.show();
    }

    public void jobProcessStatusChanged(JobProcessor.ProcessorStatus oldStatus, JobProcessor.ProcessorStatus newStatus) {
        final Button startstopQueueButton = (Button) findViewById(R.id.startstopQueueButton);
        startstopQueueButton.setText(newStatus.name());

    }
}
