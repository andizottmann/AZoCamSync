package de.quadrillenschule.azocamsynca;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AZoTriggerServiceActivity extends Activity {

    NikonIR nir=null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.main);
         if (nir == null) {
            nir = new NikonIR(this);
        }
        Button triggerButton = (Button) findViewById(R.id.trigger);
        triggerButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                  
                nir.trigger();
            }
        });
        
          Button triggerJobTest = (Button) findViewById(R.id.triggerJobTest);
          final Activity myActivity=this;
          triggerJobTest.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                TriggerJob tj=new TriggerJob(myActivity);
                tj.setNumber(3);
                tj.setExposure(5000);
                tj.setExposureSetOnCamera(false);
                tj.execute();
             }
        });
      
    }
}
