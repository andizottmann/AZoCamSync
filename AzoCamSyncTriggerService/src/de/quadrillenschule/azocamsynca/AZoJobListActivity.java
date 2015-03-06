package de.quadrillenschule.azocamsynca;

import de.quadrillenschule.azocamsynca.job.JobListAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.job.JobProgressListener;

public class AZoJobListActivity extends Activity implements JobProgressListener {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AzoTriggerServiceApplication) getApplication()).onActivityCreate(this);
        setContentView(R.layout.joblist);
        
    }
    
    @Override
    protected void onResume() {
        super.onResume(); //To change body of generated methods, choose Tools | Templates.
        ((AzoTriggerServiceApplication) getApplication()).onActivityResume(this);
        ((AzoTriggerServiceApplication) getApplication()).getJobProcessor().addJobProgressListener(this);
        ListView jobListView = (ListView) findViewById(R.id.jobList);
        jobListView.setAdapter(new JobListAdapter(this, R.layout.job_list_item, ((AzoTriggerServiceApplication) getApplication()).getJobProcessor().getJobs()));
    }
    
    public void jobProgressed(PhotoSerie ps) {
        ((JobListAdapter) ((ListView) findViewById(R.id.jobList)).getAdapter()).notifyDataSetChanged();
    }
    
}
