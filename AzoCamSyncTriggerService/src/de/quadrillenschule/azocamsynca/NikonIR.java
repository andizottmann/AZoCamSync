/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.quadrillenschule.azocamsynca;

import android.app.Activity;
import android.app.Application;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andreas
 */
public class NikonIR {

    String TRIGGER_CODE = "0000 0069 0005 0000 004b 0447 0014 003b 0014 008b 000f 09c4 0050 0000";
    Object irdaService;
    Method irWrite, irTest;
    private Activity activity;
    String rawTrigger;

    public NikonIR(Activity ac) {
        this.activity = ac;
        irdaService = ac.getSystemService("consumer_ir");

        Class c = irdaService.getClass();
        Class p[] = {int.class, int[].class};
        try {
            irWrite = c.getMethod("transmit", p);
            irTest = c.getMethod("hasIrEmitter", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            irTest.invoke(irdaService, null);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NikonIR.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(NikonIR.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(NikonIR.class.getName()).log(Level.SEVERE, null, ex);
        }
        rawTrigger = hex2dec(TRIGGER_CODE);
        c.getMethods();
    }

    public void trigger() {
        try {
            Class p[] = {int.class, int[].class};
            Object[] args = new Object[2];
            args[0] = (Object) Integer.parseInt(rawTrigger.split(",")[0]);
            LinkedList<Integer> code = new LinkedList();
            for (String s : rawTrigger.split(",")) {
                code.add(Integer.parseInt(s));
            }
            code.removeFirst();
            code.removeLast();
            int[] myIntArray = new int[code.size()];
            int i = 0;
            for (Integer integer : code.toArray(new Integer[code.size()])) {
                myIntArray[i] = integer.intValue();
                i++;
            }

            args[1] = string2dec(myIntArray, 1);

            irWrite.invoke(getActivity().getSystemService("consumer_ir"), args);

        } catch (IllegalAccessException ex) {
            Logger.getLogger(NikonIR.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(NikonIR.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(NikonIR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected String hex2dec(String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData
                .split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency

        list.remove(0); // seq1
        list.remove(0); // seq2

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Integer.toString(Integer.parseInt(list.get(i), 16)));
        }
        // frequency = (int) Math.ceil(frequency * 26.27272727272727);
        frequency = (int) (1000000 / (frequency * 0.241246));
        //       frequency=  1000000 / frequency; 
        list.add(0, Integer.toString(frequency));

        irData = "";
        for (String s : list) {
            irData += s + ",";
        }
        return irData;
    }

    /**
     * @return the exposureSetOnCamera
     */
    public boolean isExposureSetOnCamera(long exposureTime) {
        return false;
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

    private static int[] string2dec(int[] irData, int frequency) {
        int formula = shouldEquationRun();

        //Should we run any computations on the irData?
        if (formula != 0) {
            for (int i = 0; i < irData.length; i++) {
                if (formula == 1) {
                    irData[i] = irData[i] * 1000000 / frequency;
                } else if (formula == 2) {
                    irData[i] = (int) Math.ceil(irData[i] * 26.27272727272727); //this is the samsung formula as per http://developer.samsung.com/android/technical-docs/Workaround-to-solve-issues-with-the-ConsumerIrManager-in-Android-version-lower-than-4-4-3-KitKat
                }
            }
        }
        return irData;
    }

    /*
     * This method figures out if we should be running the equation in string2dec,
     * which is based on the type of device. Some need it to run in order to function, some need it NOT to run
     *
     * HTC needs it on (HTC One M8)
     * Samsung needs occasionally a special formula, depending on the version
     * Android 5.0+ need it on. 
     * Other devices DO NOT need anything special.
     */
    private static int shouldEquationRun() {
        return 2;
    }
    //if something else...

}
