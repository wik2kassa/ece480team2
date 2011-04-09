package com.iDocent;

import java.util.TimerTask;

import android.net.wifi.WifiManager;
import android.os.Handler;

//The task to be performed by the timer
public class ScanTask extends TimerTask{
    WifiManager wifi;
    
    Handler handler = new Handler();
    
    ScanResultReceiver SRR;
    
    boolean started = false;
    
    int count  = 0;

    public ScanTask(WifiManager w, ScanResultReceiver sRR) {
        wifi = w;
        SRR = sRR;
    }

    public void run() 
    {
        handler.post(new Runnable() 
        {
            public void run() 
            {
        		if(wifi.isWifiEnabled())
        		{
        			if(!started)
        			{
        				wifi.startScan();
        				started = true;
        			}
        			//wifi.startScan();
        			count++;
        			//if(count >=9)
        				UpdateLocation();
        		}
            }

			private void UpdateLocation() {
				count = 0;
				SRR.UpdateLocation();				
			}
        });
    }
}