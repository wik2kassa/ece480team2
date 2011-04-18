package com.iDocent;

import java.util.TimerTask;

import android.net.wifi.WifiManager;
import android.os.Handler;

//The task to be performed by the timer
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

    public void run() 
    {
        handler.post(new Runnable() 
        {
            public void run() 
            {
            	wifi = iD.getWifi();
        		if(wifi != null)
        		{
        			//if(!started)
        			{
        				wifi.startScan();
        				//started = true;
        				//iD.EndTimer();        				
        			}
        		}
            }
        });
    }
}