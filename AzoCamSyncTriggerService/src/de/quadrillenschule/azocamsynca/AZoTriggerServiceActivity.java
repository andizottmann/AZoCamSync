package de.quadrillenschule.azocamsynca;

import de.quadrillenschule.azocamsynca.job.TriggerJob;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.NumberPicker;
import com.ikovac.timepickerwithseconds.view.TimePicker;
import de.quadrillenschule.azocamsynca.job.JobProcessor;

public class AZoTriggerServiceActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AzoTriggerServiceApplication) getApplication()).onActivityCreate(this);

        setContentView(R.layout.main);

    }

    @Override
    protected void onResume() {
        super.onResume(); //To change body of generated methods, choose Tools | Templates.
        final History history = new History(getApplication());
        final Activity myActivity = this;

        final NumberPicker numberOfexposuresPicker = (NumberPicker) findViewById(R.id.numberOfExposuresPicker);
        numberOfexposuresPicker.setMinValue(1);
        numberOfexposuresPicker.setMaxValue(999);
        numberOfexposuresPicker.setValue(Integer.parseInt(history.getHistory(History.Fields.NUMBER_OF_EXPOSURES, "10").getFirst()));

        Button numberOfExposuresHistory = (Button) findViewById(R.id.numberOfExposuresHistoryButton);
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
        final TimePicker exposureTimePicker = (TimePicker) findViewById(R.id.exposureTimePicker);
        exposureTimePicker.setIs24HourView(true);
        exposureTimePicker.setTimeInMs(4000);

        final Button exposureHistoryButton = (Button) findViewById(R.id.exposureHistoryButton);
        exposureHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                showHistoryPopup(myActivity, exposureHistoryButton, History.Fields.EXPOSURE, exposureTimePicker);
            }
        });

        final TimePicker initialDelayTimePicker = (TimePicker) findViewById(R.id.initialDelayPicker);
        initialDelayTimePicker.setIs24HourView(true);
        initialDelayTimePicker.setTimeInMs(3000);

        final Button initialDelayHistoryButton = (Button) findViewById(R.id.initialDelayHistoryButton);
        initialDelayHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                showHistoryPopup(myActivity, initialDelayHistoryButton, History.Fields.INITIAL_DELAY, initialDelayTimePicker);
            }
        });

        final TimePicker exposureGapTimePicker = (TimePicker) findViewById(R.id.exposureGapPicker);
        exposureGapTimePicker.setIs24HourView(true);
        exposureGapTimePicker.setTimeInMs(0000);

        final Button exposureGapHistoryButton = (Button) findViewById(R.id.exposureGapHistoryButton);
        exposureGapHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                showHistoryPopup(myActivity, exposureGapHistoryButton, History.Fields.EXPOSURE_GAP_NUMBER, exposureGapTimePicker);
            }
        });

        final EditText projectEditText = (EditText) findViewById(R.id.projectEditText);
        projectEditText.setText(history.getHistory(History.Fields.PROJECT, "Noname Project").getFirst());

        final Button projectHistoryButton = (Button) findViewById(R.id.projectHistoryButton);
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

        final EditText seriesEditText = (EditText) findViewById(R.id.seriesEditText);
        seriesEditText.setText(history.getHistory(History.Fields.SERIES_NAME, "flats").getFirst());

        final Button seriesHistoryButton = (Button) findViewById(R.id.seriesHistoryButton);
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

        Button triggerJobTest = (Button) findViewById(R.id.triggerJobButton);

        triggerJobTest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                TriggerJob tj = new TriggerJob(myActivity);

                tj.setNumber(numberOfexposuresPicker.getValue());
                history.addHistory(History.Fields.NUMBER_OF_EXPOSURES, numberOfexposuresPicker.getValue() + "");

                tj.setExposure(exposureTimePicker.getTimeInMs());
                history.addHistory(History.Fields.EXPOSURE, exposureTimePicker.getTimeInMs() + "");

                tj.setInitialDelay(initialDelayTimePicker.getTimeInMs());
                history.addHistory(History.Fields.INITIAL_DELAY, initialDelayTimePicker.getTimeInMs() + "");

                tj.setExposureGapTime(exposureGapTimePicker.getTimeInMs());
                history.addHistory(History.Fields.EXPOSURE_GAP_NUMBER, exposureGapTimePicker.getTimeInMs() + "");

                tj.setProject(projectEditText.getText().toString());
                history.addHistory(History.Fields.PROJECT, projectEditText.getText().toString() + "");

                tj.setSeriesName(seriesEditText.getText().toString());
                history.addHistory(History.Fields.SERIES_NAME, seriesEditText.getText().toString() + "");

                JobProcessor jobProcessor = ((AzoTriggerServiceApplication) getApplication()).getJobProcessor();
                jobProcessor.getJobs().add(tj);
                if (jobProcessor.getStatus() != JobProcessor.ProcessorStatus.PROCESSING) {
                    jobProcessor.executeNext();
                }
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
}
