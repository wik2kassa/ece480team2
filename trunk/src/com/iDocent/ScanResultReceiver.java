package com.iDocent;

import java.util.LinkedList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class ScanResultReceiver extends BroadcastReceiver {
	WifiManager wifi = null;
    WeightedScanFactory wsFactory;
    iDocent miD;
    
	private static int x=0; 
	private static int y=1; 
	private static int z=2; 
	
	float sumX = 0;
	float sumY = 0;
	int count = 0;
    
    float[] oldX = new float[2]; 
    float[] oldY = new float[2];
    
	int iterations = 0;

	public ScanResultReceiver(iDocent iD) {
		miD = iD;
        wsFactory = new WeightedScanFactory();
	}

	@Override
	public void onReceive(Context c, Intent i){
		// Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event occurs
		wifi = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		
		if(wifi != null && wifi.isWifiEnabled())
		{		    		
			wifi.startScan();
			iterations++;
			List<ScanResult> scans = wifi.getScanResults(); // Returns a <list> of scanResults
			if(scans != null && !scans.isEmpty())
			{			        			
				//Go through the list of scans, give them a value from 1-20 based on strength, then
				//create a weighted scan and save it in the list
				for (ScanResult scan : scans) 
				{
					int level = WifiManager.calculateSignalLevel(scan.level, 20);
			        
					WeightedScan wScan = wsFactory.Create(scan.BSSID);
					if(wScan != null)
					{
						wScan.SetLevel(level);
						sumX += level*wScan.GetPos().get(x);
						sumY += level*wScan.GetPos().get(y);
						count+=level;
					}
				}
			}
		}	
	}

	private void Map(float sX, float sY, int count) {
    	iterations = 0;
		float[] loc = {0,0};
    	if(count > 0)
    	{			
			//Calculations of position
			loc[0] += sX/(float)count;
			loc[1] += sY/(float)count;		
    	}
    	else
    	{

    	}
    	//dX = dX+1;
    	float alpha = 0.1f;
    	float filteredX = oldX[0]*alpha+loc[x]*(1-alpha);
    	float filteredY = oldY[0]*alpha+loc[y]*(1-alpha);
    	if(loc[x] != 0 && loc[y] != 0 && oldY[0] != 0 && oldX[0] != 0)
    		miD.UpdateLocation(filteredX, -filteredY);
    	else if(loc[x] != 0 && loc[y] != 0)
    		miD.UpdateLocation(loc[x], -loc[y]);
    	
    	oldX[1] = oldX[0];
    	oldY[1] = oldY[0];
    	oldX[0] = loc[0];
    	oldY[0] = loc[1];
	}

	public void UpdateLocation() {	
		if(iterations >= 2)
		{
			Map(sumX, sumY, count);
			
			count = 0;
			sumX = 0;
			sumY = 0;
		}
	}

}
