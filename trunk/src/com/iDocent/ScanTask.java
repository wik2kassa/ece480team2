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
    WeakReference<TextView> textView;
    WifiManager wifi;
    WeightedScanFactory wsFactory;
    
    Handler handler = new Handler();

    public ScanTask(TextView text, WifiManager w) {
        textView = new WeakReference<TextView>(text);
        wifi = w;
        wsFactory = new WeightedScanFactory();
    }
    
    public void SetTextView(TextView text)
    {
    	textView = new WeakReference<TextView>(text);
    }

    public void run() 
    {
        handler.post(new Runnable() 
        {
            public void run() 
            {
            	if(textView != null && textView.get() != null)
            	{
	                textView.get().setText("WiFi Scanner");
	                
	                //Makes sure that wifi is enabled and it can scan for access points
	        		if(wifi.isWifiEnabled())
	        		{
	        			int count = 0;
	        			
	        			//Keep track of all available access points in a list
	        			List<WeightedScan> weightedScans = new LinkedList<WeightedScan>();
	        			Map<String, WeightedScan> scannedAPs = new HashMap<String, WeightedScan>();
	        			for(int i=0; i<16; i++)
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
					        	        
					        	        //An alternate way that saves some memory but takes longer
//					        	        boolean addNew = true;
//					        	        for(WeightedScan s : weightedScans)
//					        	        {
//					        	        	if(s.Name().equals(scan.BSSID))
//					        	        	{
//					        	        		s.SetLevel(s.GetLevel()+level);
//					        	        		count+=level;
//					        	        		addNew = false;
//					        	        	}
//					        	        }
//					        			
//					        	        if(addNew)
//					        	        {
//						        			WeightedScan wScan = wsFactory.Create(scan.BSSID);
//						        			if(wScan != null)
//						        			{
//						        				wScan.SetLevel(level);
//						        				count+=level;
//							        			weightedScans.add(wScan);
//						        			}
//					        	        }
					        		}
				        		}
				        		else
				        		{
				        			textView.get().setText("WiFi Scanner\nScanning...");
				        		}
			        		}
	        			}
	        			
		        		if(!weightedScans.isEmpty())//DEMO CODE - for display
		        		{
			        		textView.get().append("\n\n");
			        		for (WeightedScan wscan : weightedScans)
			        		{
			        			textView.get().append(wscan.Name() + " " + wscan.GetLevel() + "/" + count + 
			        					"\n" + wscan.GetPos().get(0) + " " + wscan.GetPos().get(1) + "\n");
			        		}
			        		
		        		}
		        		
	        			Map(weightedScans, count);
	        		}
	        		else
	        		{
	        			textView.get().append("\nActivating Wi-Fi");
	        		}
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
    private void Map(List<WeightedScan> scans, int count)
    {
    	double[] loc = {0,0};
    	if(scans.size() >= 0)
    	{			
			//Calculations of position
			for(WeightedScan scan : scans)
			{	
				loc[0] += scan.GetPos().get(0) * (double)scan.GetLevel()/(double)count;
				loc[1] += scan.GetPos().get(1) * (double)scan.GetLevel()/(double)count;
			}
						

			if(textView != null && textView.get() != null)
			{
				textView.get().append("\n\n( " + loc[0] + ",\n " + loc[1] + ")\n"); 
			}
    	}
    	else
    	{

    	}
    }
}

