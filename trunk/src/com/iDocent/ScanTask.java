package com.iDocent;

import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;

//The task to be performed by the timer
public class ScanTask extends TimerTask{
    WifiManager wifi;
    WeightedScanFactory wsFactory;
    iDocent miD;
    
	private static int x=0; 
	private static int y=1; 
	private static int z=2; 
    
    float[] oldX = new float[2]; 
    float[] oldY = new float[2];
    
    Handler handler = new Handler();

    public ScanTask(iDocent iD, WifiManager w) {
    	miD = iD;
        wifi = w;
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
        			float sumX = 0;
        			float sumY = 0;
        			
        			//Keep track of all available access points in a list
        			List<WeightedScan> weightedScans = new LinkedList<WeightedScan>();
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
				        			if(scan.BSSID.equals("00:24:6c:d0:78:60")||scan.BSSID.equals("00:24:6c:d0:78:70"))
				        			{
				        				int x =5;
				        				System.out.println(x);
				        			}
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
		        		
	        		Map(sumX, sumY, count);
	        	}
        	}
        });
    }
    
    /*
	 * Name : Map()
	 * Description : Calculates the position
	 * Parameters : scans - list of all the weighted scans found 
	 * 				count - the sum of all scan levels
	 * Returns : void
	*/
    private void Map(float sX, float sY, int count)
    {
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
}

