/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.quadrillenschule.azocamsynca.job.TriggerPhotoSerie;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class JobListAdapter extends ArrayAdapter {

    LinkedList<TriggerPhotoSerie> jobs;

    public JobListAdapter(Context context, int textViewResourceId, LinkedList<TriggerPhotoSerie> jobs) {
        super(context, textViewResourceId, jobs);
        this.jobs = jobs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final TriggerPhotoSerie job = jobs.get(position);
        LinearLayout retval = new LinearLayout(getContext());

        TextView tv = new TextView(getContext());
        tv.setText(
                job.getTriggerStatus().name() + " "
                        + job.getTriggered() + "/" + job.getNumber()+"\n"
                + job.getProject() + "-" + job.getSeriesName() + "\n"
                + job.getNumber() + "x (" + job.getExposure() / 1000 + " + "+job.getDelayAfterEachExposure()/1000+")s\n"
                
        );
        retval.addView(tv);

        if (job.getTriggerStatus() == TriggerPhotoSerie.TriggerJobStatus.NEW) {
            Button removeButton = new Button(getContext());
            removeButton.setText("Remove");
            removeButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View arg0) {
                    jobs.remove(job);
                }
            });
            retval.addView(removeButton);
        }

        return retval;
        //To change body of generated methods, choose Tools | Templates.
    }

}
