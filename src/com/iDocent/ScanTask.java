package com.iDocent;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.TextView;

//The task to be performed by the timer
public class ScanTask extends TimerTask{
    WifiManager wifi;
    WeightedScanFactory wsFactory;
    iDocent miD;
    
    float dX = 20; 
    float dY = 20;
    
    Handler handler = new Handler();

    public ScanTask(iDocent iD, WifiManager w) {
    	miD = iD;
        wifi = w;
        dX = miD.getPosX();
        dY = miD.getPosY();
        wsFactory = new WeightedScanFactory();
    }

    public void run() 
    {
        handler.post(new Runnable() 
        {
            public void run() 
            {
        		if(wifi.isWifiEnabled())
        		{
        			int count = 0;
        			
        			//Keep track of all available access points in a list
        			List<WeightedScan> weightedScans = new LinkedList<WeightedScan>();
        			Map<String, WeightedScan> scannedAPs = new HashMap<String, WeightedScan>();
        			for(int i=0; i<8; i++)
        			{
		        		if(wifi.startScan())
		        		{			        			
			        		// List available networks
			        		List<ScanResult> scans = wifi.getScanResults();
			        		
			        		if(scans != null && !scans.isEmpty())
			        		{			        			
			        			//Go through the list of scans, give them a value from 1-20 based on strength, then
			        			//create a weighted scan and save it in the list
				        		for (ScanResult scan : scans) 
				        		{
				        	        int level = WifiManager.calculateSignalLevel(scan.level, 20);
				        	        
				        	        WeightedScan ws = scannedAPs.get(scan.BSSID);
				        	        
				        	        if(ws != null)
				        	        {
				        	        	ws.SetLevel(ws.GetLevel()+level);
			        	        		count+=level;
				        	        }
				        	        else
				        	        {
					        			WeightedScan wScan = wsFactory.Create(scan.BSSID);
					        			if(wScan != null)
					        			{
					        				wScan.SetLevel(level);
					        				count+=level;
						        			weightedScans.add(wScan);
						        			scannedAPs.put(wScan.Name(), wScan);
					        			}
				        	        }
				        		}
			        		}
		        		}
        			}
		        		
	        		Map(weightedScans, count);
	        		}
	            }
        });
        Map(null, -1);
    }
    
    /*
	 * Name : Map()
	 * Description : Calculates the position
	 * Parameters : scans - list of all the weighted scans found 
	 * 				count - the sum of all scan levels
	 * Returns : void
	*/
    private void Map(List<WeightedScan> scans, int count)
    {
    	float[] loc = {0,0};
    	if(scans.size() >= 0)
    	{			
			//Calculations of position
			for(WeightedScan scan : scans)
			{	
				loc[0] += scan.GetPos().get(0) * (float)scan.GetLevel()/(float)count;
				loc[1] += scan.GetPos().get(1) * (float)scan.GetLevel()/(float)count;
			}			
    	}
    	else
    	{

    	}
    	//dX = dX+1;
    	miD.UpdateLocation(loc[0], -loc[1]);
    	//miD.UpdateLocation(loc[0], -loc[1]);
    }
}

