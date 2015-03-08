/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.job;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.R;
import java.util.LinkedList;

/**
 *
 * @author Andreas
 */
public class JobListAdapter extends ArrayAdapter {

    LinkedList<TriggerPhotoSerie> jobs;
    JobProcessor jobProcessor;

    public static enum ContextMenu {

        Remove, Skip, MoveUp, MoveDown
    }

    public JobListAdapter(Context context, int textViewResourceId, JobProcessor jobProcessor) {
        super(context, textViewResourceId, jobProcessor.getJobs());
        this.jobs = jobProcessor.getJobs();
        this.jobProcessor = jobProcessor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final TriggerPhotoSerie job = jobs.get(position);
        LinearLayout retval = new LinearLayout(getContext());

        TextView tv = new TextView(getContext());
        tv.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));

        tv.setText(job.shortDescription());
        tv.setTextSize(16);
        retval.addView(tv);
        prepareDontextMenu(retval, job);
        return retval;
    }

    private void prepareDontextMenu(final LinearLayout retval, final TriggerPhotoSerie job) {
        retval.setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {
                final ListPopupWindow lpw = new ListPopupWindow(retval.getContext());
                if (!((job.getTriggerStatus() == TriggerPhotoSerie.TriggerJobStatus.NEW)
                        || job.getTriggerStatus() == TriggerPhotoSerie.TriggerJobStatus.FINISHED_TRIGGERING)) {
                    lpw.setAdapter(new ArrayAdapter(retval.getContext(), R.layout.history_list_item, new ContextMenu[]{ContextMenu.Skip}));

                } else {
                    lpw.setAdapter(new ArrayAdapter(retval.getContext(), R.layout.history_list_item, ContextMenu.values()));

                }
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
}
