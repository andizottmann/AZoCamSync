package de.quadrillenschule.azocamsynca;

import de.quadrillenschule.azocamsynca.job.TriggerJob;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
        ((AzoTriggerServiceApplication) getApplication()).setCamera(new NikonIR(this));
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
        numberOfexposuresPicker.setValue(10);
        
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
                
                JobProcessor jobProcessor = ((AzoTriggerServiceApplication) getApplication()).getJobProcessor();
                jobProcessor.getJobs().add(tj);
                jobProcessor.executeNext();
            }
        });
    }
    
}
