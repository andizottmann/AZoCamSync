/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import android.content.Context;
import android.hardware.Camera;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.quadrillenschule.azocamsynca.R;
import de.quadrillenschule.azocamsynca.job.TriggerPhotoSerie;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class JobListAdapter extends ArrayAdapter {

    LinkedList<TriggerPhotoSerie> jobs;
    JobProcessor jobProcessor;

    public JobListAdapter(Context context, int textViewResourceId, JobProcessor jobProcessor) {
        super(context, textViewResourceId, jobProcessor.getJobs());
        this.jobs = jobProcessor.getJobs();
        this.jobProcessor=jobProcessor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final TriggerPhotoSerie job = jobs.get(position);
        LinearLayout retval = new LinearLayout(getContext());

        TextView tv = new TextView(getContext());
        tv.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));

        
        tv.setText(
                job.getTriggerStatus().name() + " "
                + job.getProject() + "-" + job.getSeriesName() + "\n"
                + " Triggered: " + job.getTriggered() + "/" + job.getNumber()
                + " Received: " + job.getReceived() + "/" + job.getNumber() + "\n"
                + job.getNumber() + "x (" + job.getExposure() / 1000 + " + " + job.getDelayAfterEachExposure() / 1000 + ")s\n"
        );
        tv.setTextSize(16);
        retval.addView(tv);

        if (job.getTriggerStatus() == TriggerPhotoSerie.TriggerJobStatus.NEW) {
            Button removeButton = new Button(getContext());
            removeButton.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_light));
            
            removeButton.setText("Remove");

            removeButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    jobs.remove(job);
                    jobProcessor.fireJobProgressEvent(job);
                }
            });
            retval.addView(removeButton);
        }

        return retval;
        //To change body of generated methods, choose Tools | Templates.
    }

}
