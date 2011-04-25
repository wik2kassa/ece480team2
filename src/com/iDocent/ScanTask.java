/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import java.util.TimerTask;

import android.net.wifi.WifiManager;
import android.os.Handler;

/**The task to be performed by the timer.  Start the first scan.*/
public class ScanTask extends TimerTask{
    WifiManager wifi;
    iDocent iD;
    
    Handler handler = new Handler();
    
    boolean started = false;
    
    int count  = 0;

    public ScanTask(WifiManager w, iDocent iD) {
        wifi = w;
        this.iD = iD;
    }

    //This timer will initialize the scanning.
    public void run() 
    {
        handler.post(new Runnable() 
        {
            public void run() 
            {
            	wifi = iD.getWifi();
        		if(wifi != null)
        		{
        			if(!started)
        			{
        				wifi.startScan();
        				started = true;
        				iD.EndTimer();        				
        			}
        		}
            }
        });
    }
}