package com.iDocent;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class ScanResultReceiver extends BroadcastReceiver{
	WifiManager wifi = null;
    WeightedScanFactory wsFactory;
    iDocent miD;
    
	private float sumX = 0;
	private float sumY = 0;
	private int count = 0;
    
	private static int x=0; 
	private static int y=1; 
	private static int z=2; 
    
    private float[] oldX = new float[2]; 
    private float[] oldY = new float[2];
    
	private int iterations = 0;
	
	private List<ScanResult> scans;
	
	private Thread t;
	boolean loaded = false;
	
	private float tempx = 20, tempy = 20;

	public ScanResultReceiver(iDocent iD) {
		miD = iD;
        wsFactory = new WeightedScanFactory(miD);
//		wsFactory.StartScanLoop();
	}

	@Override
	public void onReceive(Context c, Intent i){
		// Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event occurs
		wifi = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		
    	miD.UpdateLocation(tempx, tempy, 0);
    	if(tempx < 125)
    		tempx+=2;
    	else
    		tempy+=1;
		
		if(wifi != null && wifi.isWifiEnabled() && wifi.getConnectionInfo().getBSSID()!=null)
		{
			if(!loaded)
			{
				loaded = wsFactory.StartScanLoop();
				if(loaded)
					miD.DownloadRooms();
			}
			
			wifi.startScan();
			
			if(loaded)
			{
				iterations++;
				scans = wifi.getScanResults(); // Returns a <list> of scanResults
				if(scans != null)
				{
					if((t==null || !t.isAlive()))
					{
						ScanCounter sc = new ScanCounter(this, scans, wsFactory);
						t = new Thread(sc);
						t.setName("Scan Counter");
						t.start();
					}
				}
			}
		}	
	}
	
	public void UpdateSums(float sX, float sY, int count)
	{
		sumX += sX;
		sumY += sY;
		this.count += count;
		UpdateLocation();
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
    		miD.UpdateLocation(filteredX, -filteredY, 0);
    	else if(loc[x] != 0 && loc[y] != 0)
    		miD.UpdateLocation(loc[x], -loc[y], 0);
    	
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
	
	public void End()
	{
		wsFactory.EndScanLoop();
	}
}
