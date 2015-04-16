package de.quadrillenschule.azocamsynca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.NumberPicker;
import com.ikovac.timepickerwithseconds.view.TimePicker;
import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.helpers.Formats;
import de.quadrillenschule.azocamsynca.helpers.TopExceptionHandler;
import de.quadrillenschule.azocamsynca.job.JobListAdapter;
import de.quadrillenschule.azocamsynca.job.JobProcessor;
import de.quadrillenschule.azocamsynca.job.JobProcessorStatusListener;
import de.quadrillenschule.azocamsynca.job.JobProgressListener;
import de.quadrillenschule.azocamsynca.job.TriggerPhotoSerie;

public class AZoTriggerServiceActivity extends Activity implements JobProcessorStatusListener, JobProgressListener {

    /**
     * Called when the activity is first created.
     */
    AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ((AzoTriggerServiceApplication) getApplication()).onActivityCreate(this);
        ((AzoTriggerServiceApplication) getApplication()).getJobProcessor().addJobProcesssorStatusListener(this);
        ((AzoTriggerServiceApplication) getApplication()).getJobProcessor().addJobProgressListener(this);
        setContentView(R.layout.main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
       
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((AzoTriggerServiceApplication) getApplication()).onActivityResume(this);
        ((AzoTriggerServiceApplication) getApplication()).getWebService().setActivity(this);

        prepareJobConfigurationFields();
        prepareButtons();
        ListView jobListView = (ListView) findViewById(R.id.jobList);
        jobListView.setAdapter(new JobListAdapter(this, this, R.layout.job_list_item, ((AzoTriggerServiceApplication) getApplication()).getJobProcessor()));

    }

    @Override
    protected void onPause() {
        super.onPause();
        ((AzoTriggerServiceApplication) getApplication()).onActivityPause(this);

    }

    public void setValuesToEditor(PhotoSerie ps) {
        final Button numberOfExposuresButton = (Button) findViewById(R.id.numberOfExposuresButton);
        final Button exposureTimeButton = (Button) findViewById(R.id.exposureTimeButton);
        final Button initialDelayButton = (Button) findViewById(R.id.initialDelayButton);
        final Button delayAfterEachExposureButton = (Button) findViewById(R.id.delayAfterEachExposureButton);
        final EditText projectEditText = (EditText) findViewById(R.id.projectEditText);
        final EditText seriesEditText = (EditText) findViewById(R.id.seriesEditText);

        numberOfExposuresButton.setText(ps.getNumber() + "");
        exposureTimeButton.setText(Formats.toString(ps.getExposure()));
        initialDelayButton.setText(Formats.toString(ps.getInitialDelay()));
        delayAfterEachExposureButton.setText(Formats.toString(ps.getDelayAfterEachExposure()));
        projectEditText.setText(ps.getProject());
        seriesEditText.setText(ps.getSeriesName());
    }

    public void prepareJobConfigurationFields() {
        final History history = new History(getApplication());
        final Activity myActivity = this;

        final Button numberOfExposuresButton = (Button) findViewById(R.id.numberOfExposuresButton);
        final Button numberOfExposuresHistory = (Button) findViewById(R.id.numberOfExposuresHistoryButton);
        final Button exposureTimeButton = (Button) findViewById(R.id.exposureTimeButton);
        final Button exposureHistoryButton = (Button) findViewById(R.id.exposureHistoryButton);
        final Button initialDelayButton = (Button) findViewById(R.id.initialDelayButton);
        final Button initialDelayHistoryButton = (Button) findViewById(R.id.initialDelayHistoryButton);
        final Button delayAfterEachExposureButton = (Button) findViewById(R.id.delayAfterEachExposureButton);
        final Button exposureGapHistoryButton = (Button) findViewById(R.id.delayAfterEachExposureHistoryButton);
        final EditText projectEditText = (EditText) findViewById(R.id.projectEditText);
        final Button projectHistoryButton = (Button) findViewById(R.id.projectHistoryButton);
        final EditText seriesEditText = (EditText) findViewById(R.id.seriesEditText);
        final Button seriesHistoryButton = (Button) findViewById(R.id.seriesHistoryButton);

        numberOfExposuresButton.setText("" + Integer.parseInt(history.getHistory(PhotoSerie.Fields.NUMBER_OF_EXPOSURES, "10").getFirst()));
        numberOfExposuresButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final AlertDialog.Builder ab = new AlertDialog.Builder(myActivity);
                final NumberPicker numberOfexposuresPicker = new NumberPicker(myActivity);
                numberOfexposuresPicker.setMinValue(1);
                numberOfexposuresPicker.setMaxValue(999);
                numberOfexposuresPicker.setValue(Integer.parseInt(history.getHistory(PhotoSerie.Fields.NUMBER_OF_EXPOSURES, "10").getFirst()));

                ab.setView(numberOfexposuresPicker);
                ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        numberOfExposuresButton.setText("" + numberOfexposuresPicker.getValue());

                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                ab.show();
            }
        });
        numberOfExposuresHistory.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                final ListPopupWindow lpw = new ListPopupWindow(myActivity);
                lpw.setAdapter(new ArrayAdapter(myActivity, R.layout.history_list_item, history.getHistory(PhotoSerie.Fields.NUMBER_OF_EXPOSURES, "10")));
                lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        numberOfExposuresButton.setText("" + Integer.parseInt(history.getHistory(PhotoSerie.Fields.NUMBER_OF_EXPOSURES, "10").get(arg2)));

                        lpw.dismiss();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        lpw.dismiss();
                    }
                });
                lpw.setAnchorView(findViewById(R.id.numberOfExposuresHistoryButton));
                lpw.setModal(true);
                lpw.show();
            }
        });

        exposureTimeButton.setText("" + history.getHistory(PhotoSerie.Fields.EXPOSURE, "0:00:04").getFirst());
        exposureTimeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(myActivity);
                final TimePicker exposurePicker = new TimePicker(myActivity);
                exposurePicker.setIs24HourView(true);
                exposurePicker.setTimeInMs(Formats.toLong(history.getHistory(PhotoSerie.Fields.EXPOSURE, "0:00:04").getFirst()));

                ab.setView(exposurePicker);
                ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        exposureTimeButton.setText("" + Formats.toString(exposurePicker.getTimeInMs()));

                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

               ab.show();
            }
        });

        exposureHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                showHistoryPopup(myActivity, exposureHistoryButton, PhotoSerie.Fields.EXPOSURE, exposureTimeButton, "0:00:04");
            }
        });

        initialDelayButton.setText("" + history.getHistory(PhotoSerie.Fields.INITIAL_DELAY, "0:00:03").getFirst());
        initialDelayButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(myActivity);
                final TimePicker initialDelayPicker = new TimePicker(myActivity);
                initialDelayPicker.setIs24HourView(true);
                initialDelayPicker.setTimeInMs(Formats.toLong(history.getHistory(PhotoSerie.Fields.INITIAL_DELAY, "0:00:03").getFirst()));

                ab.setView(initialDelayPicker);
                ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        initialDelayButton.setText("" + Formats.toString(initialDelayPicker.getTimeInMs()));

                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
               ab.show();
            }
        });

        initialDelayHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                showHistoryPopup(myActivity, initialDelayHistoryButton, PhotoSerie.Fields.INITIAL_DELAY, initialDelayButton, "0:00:03");
            }
        });

        delayAfterEachExposureButton.setText("" + history.getHistory(PhotoSerie.Fields.DELAY_AFTER_EACH_EXPOSURE, "0:00:03").getFirst());
        delayAfterEachExposureButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(myActivity);
                final TimePicker delayAfterEachExposurePicker = new TimePicker(myActivity);
                delayAfterEachExposurePicker.setIs24HourView(true);
                delayAfterEachExposurePicker.setTimeInMs(Formats.toLong(history.getHistory(PhotoSerie.Fields.DELAY_AFTER_EACH_EXPOSURE, "0:00:03").getFirst()));

                ab.setView(delayAfterEachExposurePicker);
                ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        delayAfterEachExposureButton.setText("" + Formats.toString(delayAfterEachExposurePicker.getTimeInMs()));

                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                ab.show();
            }
        });

        exposureGapHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                showHistoryPopup(myActivity, exposureGapHistoryButton, PhotoSerie.Fields.DELAY_AFTER_EACH_EXPOSURE, delayAfterEachExposureButton, "0:00:03");
            }
        });

        projectEditText.setText(history.getHistory(PhotoSerie.Fields.PROJECT, "Noname Project").getFirst());
        projectEditText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                return false;
            }
        });
        projectHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                final History history = new History(myActivity.getApplication());
                final ListPopupWindow lpw = new ListPopupWindow(myActivity);
                lpw.setAdapter(new ArrayAdapter(myActivity, R.layout.history_list_item, history.getHistory(PhotoSerie.Fields.PROJECT, "Noname Project")));
                lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        projectEditText.setText(history.getHistory(PhotoSerie.Fields.PROJECT, "Noname Project").get(arg2));
                        lpw.dismiss();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        lpw.dismiss();
                    }
                });
                lpw.setAnchorView(projectHistoryButton);
                lpw.setModal(true);
                lpw.show();
            }
        });

        seriesEditText.setText(history.getHistory(PhotoSerie.Fields.SERIES_NAME, PhotoSerie.TESTSHOTS).getFirst());

        seriesHistoryButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                final History history = new History(myActivity.getApplication());
                final ListPopupWindow lpw = new ListPopupWindow(myActivity);
                lpw.setAdapter(new ArrayAdapter(myActivity, R.layout.history_list_item, history.getHistory(PhotoSerie.Fields.SERIES_NAME, PhotoSerie.TESTSHOTS)));
                lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        seriesEditText.setText(history.getHistory(PhotoSerie.Fields.SERIES_NAME, PhotoSerie.TESTSHOTS).get(arg2));
                        lpw.dismiss();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        lpw.dismiss();
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
        final Button numberOfExposuresButton = (Button) findViewById(R.id.numberOfExposuresButton);
        final Button exposureTimeButton = (Button) findViewById(R.id.exposureTimeButton);
        final Button initialDelayButton = (Button) findViewById(R.id.initialDelayButton);
        final Button delayAfterEachExposureButton = (Button) findViewById(R.id.delayAfterEachExposureButton);
        final EditText projectEditText = (EditText) findViewById(R.id.projectEditText);
        final EditText seriesEditText = (EditText) findViewById(R.id.seriesEditText);

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

                tj.setNumber(Integer.parseInt(numberOfExposuresButton.getText().toString()));
                history.addHistory(PhotoSerie.Fields.NUMBER_OF_EXPOSURES, tj.getNumber() + "");

                tj.setExposure(Formats.toLong(exposureTimeButton.getText().toString()));
                history.addHistory(PhotoSerie.Fields.EXPOSURE, exposureTimeButton.getText().toString() + "");

                tj.setInitialDelay(Formats.toLong(initialDelayButton.getText().toString()));
                history.addHistory(PhotoSerie.Fields.INITIAL_DELAY, initialDelayButton.getText().toString() + "");

                tj.setDelayAfterEachExposure(Formats.toLong(delayAfterEachExposureButton.getText().toString()));
                history.addHistory(PhotoSerie.Fields.DELAY_AFTER_EACH_EXPOSURE, delayAfterEachExposureButton.getText().toString() + "");

                tj.setProject(projectEditText.getText().toString());
                history.addHistory(PhotoSerie.Fields.PROJECT, projectEditText.getText().toString() + "");

                tj.setSeriesName(seriesEditText.getText().toString());
                history.addHistory(PhotoSerie.Fields.SERIES_NAME, seriesEditText.getText().toString() + "");

                JobProcessor jobProcessor = ((AzoTriggerServiceApplication) getApplication()).getJobProcessor();
                jobProcessor.getJobs().add(tj);
                jobProgressed(tj);
                jobProcessor.processingLoop();
            }
        });

        final Button modifyJobButton = (Button) findViewById(R.id.modifyJobButton);
        modifyJobButton.setEnabled(false);
        modifyJobButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                TriggerPhotoSerie tj = ((JobListAdapter) ((ListView) findViewById(R.id.jobList)).getAdapter()).getSelectedJob();

                tj.setNumber(Integer.parseInt(numberOfExposuresButton.getText().toString()));
                history.addHistory(PhotoSerie.Fields.NUMBER_OF_EXPOSURES, tj.getNumber() + "");

                tj.setExposure(Formats.toLong(exposureTimeButton.getText().toString()));
                history.addHistory(PhotoSerie.Fields.EXPOSURE, exposureTimeButton.getText().toString() + "");

                tj.setInitialDelay(Formats.toLong(initialDelayButton.getText().toString()));
                history.addHistory(PhotoSerie.Fields.INITIAL_DELAY, initialDelayButton.getText().toString() + "");

                tj.setDelayAfterEachExposure(Formats.toLong(delayAfterEachExposureButton.getText().toString()));
                history.addHistory(PhotoSerie.Fields.DELAY_AFTER_EACH_EXPOSURE, delayAfterEachExposureButton.getText().toString() + "");

                tj.setProject(projectEditText.getText().toString());
                history.addHistory(PhotoSerie.Fields.PROJECT, projectEditText.getText().toString() + "");

                tj.setSeriesName(seriesEditText.getText().toString());
                history.addHistory(PhotoSerie.Fields.SERIES_NAME, seriesEditText.getText().toString() + "");

                ((JobListAdapter) ((ListView) findViewById(R.id.jobList)).getAdapter()).notifyDataSetChanged();

                JobProcessor jobProcessor = ((AzoTriggerServiceApplication) getApplication()).getJobProcessor();
                jobProgressed(tj);
                jobProcessor.processingLoop();

            }
        });

        final Button startstopQueueButton = (Button) findViewById(R.id.startstopQueueButton);

        startstopQueueButton.setText(((AzoTriggerServiceApplication) getApplication()).getJobProcessor().getStatus().name());
        startstopQueueButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                JobProcessor jobProcessor = ((AzoTriggerServiceApplication) getApplication()).getJobProcessor();
                if (jobProcessor.getStatus() == JobProcessor.ProcessorStatus.PAUSED) {

                    jobProcessor.start();

                } else {

                    jobProcessor.pause();

                }
                startstopQueueButton.setText(jobProcessor.getStatus().name());
            }

        });

    }

    public static void showHistoryPopup(final Activity myActivity, View anchorView, final PhotoSerie.Fields field, final Button timebutton, String def) {
        final History history = new History(myActivity.getApplication());
        final ListPopupWindow lpw = new ListPopupWindow(myActivity);
        lpw.setAdapter(new ArrayAdapter(myActivity, R.layout.history_list_item, history.getHistory(field, def)));
        lpw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                timebutton.setText(history.getHistory(field, "0:00:04").get(arg2));
                lpw.dismiss();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                lpw.dismiss();
            }
        });
        lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                timebutton.setText(history.getHistory(field, "0:00:04").get(arg2));
                TriggerPhotoSerie tps = ((JobListAdapter) ((ListView) myActivity.findViewById(R.id.jobList)).getAdapter()).getSelectedJob();
                if (tps != null) {
                    tps.setFieldFromHR(field, timebutton.getText().toString());

                    ((AzoTriggerServiceApplication) myActivity.getApplication()).getJobProcessor().fireJobProgressEvent(tps);
                }
                lpw.dismiss();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                lpw.dismiss();
            }
        });
        lpw.setAnchorView(anchorView);
        lpw.setModal(true);
        lpw.show();
    }

    public void jobProcessStatusChanged(JobProcessor.ProcessorStatus oldStatus, JobProcessor.ProcessorStatus newStatus) {
        final Button startstopQueueButton = (Button) findViewById(R.id.startstopQueueButton);
        startstopQueueButton.setText(newStatus.name());
        if (newStatus.equals(JobProcessor.ProcessorStatus.PROCESSING)) {
            LinearLayout editorLL = ((LinearLayout) findViewById(R.id.editorLinearLayout));
            //editorLL.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams lp = editorLL.getLayoutParams();
            lp.width = 0;
            lp.height = 0;
            editorLL.setLayoutParams(lp);
        } else {
            LinearLayout editorLL = ((LinearLayout) findViewById(R.id.editorLinearLayout));
            //editorLL.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams lp = editorLL.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            editorLL.setLayoutParams(lp);
        }
    }

    public void jobProgressed(PhotoSerie ps) {
        ((JobListAdapter) ((ListView) findViewById(R.id.jobList)).getAdapter()).notifyDataSetChanged();

    }

    public void setModifyButtonEnabled(boolean enabled) {
        final Button modifyJobButton = (Button) findViewById(R.id.modifyJobButton);
        modifyJobButton.setEnabled(enabled);
    }
    
    public void confirmAlertDialog(){
    if (alertDialog.isShowing()){
    
    }
    }
    
  
}
