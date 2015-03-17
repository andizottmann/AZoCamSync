/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca.webservice;

import android.app.Activity;
import de.quadrillenschule.azocamsync.PhotoSerie;
import de.quadrillenschule.azocamsynca.AzoTriggerServiceApplication;
import de.quadrillenschule.azocamsynca.job.JobProcessor;
import de.quadrillenschule.azocamsynca.job.TriggerPhotoSerie;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONException;
import org.json.JSONObject;
// {"ID":"-8112512630831298615","EXPOSURE":4000,"INITIAL_DELAY":3000,"PROJECT":"juhupiter","DELAY_AFTER_EACH_EXPOSURE":3000,"SERIES_NAME":"darks","NUMBER_OF_EXPOSURES":10}

/**
 *
 * @author Andreas
 */
public class WebService {

    public enum WebCommands {

        list, jobprocessorstatus, startjobprocessor, pausejobprocessor, confirmdialog, addjob, addform, help, updateJob, updateTriggered, removejob
    };

    public enum WebParameters {

        jsoncontent, jobid, receivedImages
    };
    public static final String COMMAND_RECEIVED = "Command received", SYNTAX_ERROR = "Syntax error", UNKNOWN_JOB = "Unknown job";

    final JobProcessor jobProcessor;
    private Activity activity;

    public WebService(final JobProcessor jobProcessor) {
        this.jobProcessor = jobProcessor;
        Server server = new Server(8000);
        server.setHandler(new AbstractHandler() {

            public void handle(String string, Request baseRequest, HttpServletRequest hsr, HttpServletResponse response) throws IOException, ServletException {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                final HttpServletResponse finalresponse = response;
                baseRequest.setHandled(true);
                if (baseRequest.getPathInfo().contains(WebCommands.list.name())) {
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().println(jobProcessor.toJSONArray().toString());
                    return;
                }
                if (baseRequest.getPathInfo().contains(WebCommands.jobprocessorstatus.name())) {
                    response.getWriter().println(jobProcessor.getStatus().name());
                    return;
                }
                if (baseRequest.getPathInfo().contains(WebCommands.startjobprocessor.name())) {
                    startJobProcessor(finalresponse);
                    return;
                }
                if (baseRequest.getPathInfo().contains(WebCommands.pausejobprocessor.name())) {
                    pauseJobProcessor(finalresponse);
                    return;
                }
                if (baseRequest.getPathInfo().contains(WebCommands.addjob.name())) {
                    addJob(finalresponse, baseRequest);
                    return;
                }
                if (baseRequest.getPathInfo().contains(WebCommands.confirmdialog.name())) {
                    ((AzoTriggerServiceApplication) activity.getApplication()).getJobProcessor().confirmPreparedDialog();
                    response.getWriter().println(COMMAND_RECEIVED);

                    return;
                }
                if (baseRequest.getPathInfo().contains(WebCommands.addform.name())) {
                    response.getWriter().println(printForm());
                    return;
                }
                if (baseRequest.getPathInfo().contains(WebCommands.help.name())) {
                    response.getWriter().println(ArrayUtils.toString(WebCommands.values()));
                    return;
                }
                if (baseRequest.getPathInfo().contains(WebCommands.updateTriggered.name())) {
                    updateTriggered(finalresponse, baseRequest);
                    return;
                }
                if (baseRequest.getPathInfo().contains(WebCommands.updateJob.name())) {
                    updateJob(finalresponse, baseRequest);
                    return;
                }
                if (baseRequest.getPathInfo().contains(WebCommands.removejob.name())) {
                    removeJob(finalresponse, baseRequest);
                    return;
                }
                response.getWriter().println("<h1>AZoCamsyncTrigger Webservice is alive!</h1>");

            }
        });
        try {
            server.start();
        } catch (Exception ex) {
            Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String printForm() {
        return "<html><head><title>AZoCamSync Triggering Form</title></head>\n"
                + "<body>\n"
                + "<form method=\"POST\" action=\"" + WebCommands.addjob.name() + "\"/>\n"
                + "<p>JSON for a job to add</p>\n"
                + "<input name=\"" + WebParameters.jsoncontent + "\" type=\"text\" />\n"
                + "<input type=\"submit\" value=\"Add job!\" />\n"
                + "</form>"
                + "</body>\n"
                + "</html>";
    }

    private void addJob(final HttpServletResponse finalresponse, final Request request) {
        final TriggerPhotoSerie tps = new TriggerPhotoSerie(activity);
        JSONObject json;
        try {
            json = new JSONObject(request.getParameter(WebParameters.jsoncontent.name()));
            tps.fromJSONObject(json);
            jobProcessor.getJobs().add(tps);
        } catch (JSONException ex) {
            Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
            try {
                finalresponse.getWriter().println(SYNTAX_ERROR);
            } catch (IOException ex1) {
                Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return;
        }

        getActivity().runOnUiThread(new Runnable() {

            public void run() {
                jobProcessor.fireJobProgressEvent(tps);
            }
        });
        try {
            finalresponse.getWriter().println(COMMAND_RECEIVED);
        } catch (IOException ex) {
            Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateTriggered(final HttpServletResponse finalresponse, final Request request) {
        PhotoSerie myPs = null;

        String jobId = request.getParameter(WebParameters.jobid.name());
        long receivedImages = Integer.parseInt(request.getParameter(WebParameters.receivedImages.name()));
        for (PhotoSerie ps : jobProcessor.getJobs()) {
            if (ps.getId().equals(jobId)) {
                ps.setReceived(receivedImages);
                myPs = ps;
                break;
            }
        }

        if (myPs == null) {
            try {
                finalresponse.getWriter().println(UNKNOWN_JOB);
            } catch (IOException ex) {
                Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        };
        final PhotoSerie finalPs = myPs;
        getActivity().runOnUiThread(new Runnable() {

            public void run() {
                jobProcessor.fireJobProgressEvent(finalPs);
            }
        });
        try {
            finalresponse.getWriter().println(COMMAND_RECEIVED);
        } catch (IOException ex) {
            Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateJob(final HttpServletResponse finalresponse, final Request request) {
        PhotoSerie myPs = null;

        String jobId = request.getParameter(WebParameters.jobid.name());
        for (PhotoSerie ps : jobProcessor.getJobs()) {
            if (ps.getId().equals(jobId)) {

                myPs = ps;
                break;
            }
        }
        if (myPs == null) {
            try {
                finalresponse.getWriter().println(UNKNOWN_JOB);
            } catch (IOException ex) {
                Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        };
        try {
            JSONObject jso = new JSONObject(request.getParameter(WebParameters.jsoncontent.name()));
            TriggerPhotoSerie updated = new TriggerPhotoSerie(activity);
            updated.fromJSONObject(jso);
            int index = jobProcessor.getJobs().indexOf(myPs);
            jobProcessor.getJobs().add(index, updated);
            jobProcessor.getJobs().remove(myPs);
            myPs = updated;

        } catch (JSONException ex) {
            Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
            try {
                finalresponse.getWriter().println(SYNTAX_ERROR);
            } catch (IOException ex2) {
                Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex2);
            }
            return;
        }

        final PhotoSerie finalPs = myPs;
        getActivity().runOnUiThread(new Runnable() {

            public void run() {
                jobProcessor.fireJobProgressEvent(finalPs);
            }
        });
        try {
            finalresponse.getWriter().println(COMMAND_RECEIVED);
        } catch (IOException ex) {
            Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void removeJob(final HttpServletResponse finalresponse, final Request request) {
        PhotoSerie myPs = null;

        String jobId = request.getParameter(WebParameters.jobid.name());
        for (PhotoSerie ps : jobProcessor.getJobs()) {
            if (ps.getId().equals(jobId)) {
                myPs = ps;
                break;
            }
        }

        if (myPs == null) {
            try {
                finalresponse.getWriter().println(UNKNOWN_JOB);
            } catch (IOException ex) {
                Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        };
        jobProcessor.getJobs().remove(myPs);
        getActivity().runOnUiThread(new Runnable() {

            public void run() {
                jobProcessor.fireJobProgressEvent(null);
            }
        });
        try {
            finalresponse.getWriter().println(COMMAND_RECEIVED);
        } catch (IOException ex) {
            Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startJobProcessor(final HttpServletResponse finalresponse) {
        getActivity().runOnUiThread(new Runnable() {

            public void run() {
                jobProcessor.start();
            }
        });
        try {
            finalresponse.getWriter().println(COMMAND_RECEIVED);
        } catch (IOException ex) {
            Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;
    }

    private void pauseJobProcessor(final HttpServletResponse finalresponse) {
        getActivity().runOnUiThread(new Runnable() {

            public void run() {
                jobProcessor.pause();
            }
        });
        try {
            finalresponse.getWriter().println(COMMAND_RECEIVED);
        } catch (IOException ex) {
            Logger.getLogger(WebService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
