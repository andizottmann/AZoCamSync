/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.AZoTriggerServiceActivity;
import de.quadrillenschule.azocamsynca.R;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class JobListAdapter extends ArrayAdapter {
    
    LinkedList<TriggerPhotoSerie> jobs;
    JobProcessor jobProcessor;
    private TriggerPhotoSerie selectedJob = null;
    AZoTriggerServiceActivity activity;
    
    public static enum ContextMenu {
        
        Remove, Skip, MoveUp, MoveDown, Duplicate
    }
    
    public JobListAdapter(AZoTriggerServiceActivity activity, Context context, int textViewResourceId, JobProcessor jobProcessor) {
        super(context, textViewResourceId, jobProcessor.getJobs());
        this.jobs = jobProcessor.getJobs();
        this.jobProcessor = jobProcessor;
        this.activity = activity;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        final TriggerPhotoSerie job = jobs.get(position);
        LinearLayout retval = new LinearLayout(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        
        TextView tv = new TextView(getContext());
        
        tv.setTextColor(getContext().getResources().getColor(R.color.red));
        
        if (job.equals(selectedJob)) {
            if (!jobProcessor.getStatus().equals(JobProcessor.ProcessorStatus.PROCESSING)) {
                
                retval.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
            }
            tv.setTextColor(getContext().getResources().getColor(android.R.color.black));
            
        }
        
        switch (job.getTriggerStatus()) {
            case RUNNING:
                tv.setTextColor(getContext().getResources().getColor(R.color.lightred));
                if (jobProcessor.getStatus().equals(JobProcessor.ProcessorStatus.PROCESSING)) {
                    retval.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
                    
                }
                break;
            case FINISHED_TRIGGERING:
                tv.setTextColor(getContext().getResources().getColor(R.color.red));
            
        }
        tv.setText(job.shortDescription());
        tv.setTextSize(16);
        retval.addView(tv);
        prepareDontextMenu(retval, job, parent);
        prepareSelectionListener(activity, retval, job, parent);
        return retval;
    }
    
    private void prepareSelectionListener(final AZoTriggerServiceActivity activity, final LinearLayout retval, final TriggerPhotoSerie job, final ViewGroup parent) {
        retval.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
                if (selectedJob == null) {
                    selectedJob = job;
                    activity.setValuesToEditor(job);
                } else {
                    if (selectedJob == job) {
                        selectedJob = null;
                    } else {
                        selectedJob = job;
                        
                        activity.setValuesToEditor(job);
                    }
                }
                ((JobListAdapter) ((ListView) parent.findViewById(R.id.jobList)).getAdapter()).notifyDataSetChanged();
                
            }
        });
    }
    
    private void prepareDontextMenu(final LinearLayout retval, final TriggerPhotoSerie job, final ViewGroup parent) {
        retval.setOnLongClickListener(new View.OnLongClickListener() {
            
            public boolean onLongClick(View v) {
                if (selectedJob == null) {
                    selectedJob = job;
                    activity.setValuesToEditor(job);
                } else {
                    if (selectedJob == job) {
                        selectedJob = null;
                    } else {
                        selectedJob = job;
                        
                        activity.setValuesToEditor(job);
                    }
                }
                //((JobListAdapter) ((ListView) parent.findViewById(R.id.jobList)).getAdapter()).notifyDataSetChanged();

                final ListPopupWindow lpw = new ListPopupWindow(retval.getContext());
                /*      if (!((job.getTriggerStatus() == TriggerPhotoSerie.TriggerJobStatus.NEW)
                 || job.getTriggerStatus() == TriggerPhotoSerie.TriggerJobStatus.FINISHED_TRIGGERING)) {
                 lpw.setAdapter(new ArrayAdapter(retval.getContext(), R.layout.history_list_item, new ContextMenu[]{ContextMenu.Skip}));

                 } else {
             
                 }*/
                lpw.setAdapter(new ArrayAdapter(retval.getContext(), R.layout.history_list_item, ContextMenu.values()));
                
                lpw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ContextMenu cme = ContextMenu.values()[position];
                        switch (cme) {
                            case Remove:
                                jobs.remove(job);
                                jobProcessor.fireJobProgressEvent(job);
                                lpw.dismiss();
                                break;
                            case Skip:
                                job.setNumber(job.getTriggered());
                                job.setTriggerStatus(PhotoSerie.TriggerJobStatus.FINISHED_TRIGGERING);
                                jobProcessor.fireJobProgressEvent(job);
                                lpw.dismiss();
                                break;
                            case MoveUp: {
                                int currentIndex = jobs.indexOf(job);
                                if (currentIndex > 0) {
                                    jobs.remove(job);
                                    jobs.add(currentIndex - 1, job);
                                    jobProcessor.fireJobProgressEvent(job);
                                    lpw.dismiss();
                                }
                                break;
                            }
                            case MoveDown: {
                                int currentIndex = jobs.indexOf(job);
                                if (currentIndex < jobs.size() - 1) {
                                    jobs.remove(job);
                                    jobs.add(currentIndex + 1, job);
                                    jobProcessor.fireJobProgressEvent(job);
                                    lpw.dismiss();
                                }
                                break;
                            }
                            case Duplicate: {
                                TriggerPhotoSerie newJob = new TriggerPhotoSerie(activity);
                                
                                newJob.fromJSONObject(job.toJSONObject());
                                newJob.setTriggered(0);
                                newJob.setReceived(0);
                                newJob.setTriggerStatus(PhotoSerie.TriggerJobStatus.NEW);
                                jobs.add(newJob);
                                jobProcessor.fireJobProgressEvent(newJob);
                                lpw.dismiss();
                            }
                        }
                    }
                });
                lpw.setAnchorView(retval);
                lpw.setModal(true);
                lpw.show();
                
                return true;
            }
        });
    }

    /**
     * @return the selectedJob
     */
    public TriggerPhotoSerie getSelectedJob() {
        return selectedJob;
    }

    /**
     * @param selectedJob the selectedJob to set
     */
    public void setSelectedJob(TriggerPhotoSerie selectedJob) {
        this.selectedJob = selectedJob;
    }
    
}
