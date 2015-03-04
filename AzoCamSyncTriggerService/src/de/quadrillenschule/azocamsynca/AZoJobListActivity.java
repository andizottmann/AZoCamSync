package de.quadrillenschule.azocamsynca;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class AZoJobListActivity extends Activity {

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

        ListView jobListView = (ListView) findViewById(R.id.jobList);
        jobListView.setAdapter(new JobListAdapter(this, R.layout.job_list_item, ((AzoTriggerServiceApplication) getApplication()).getJobProcessor().getJobs()));
    }

}
