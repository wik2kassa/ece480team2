package com.iDocent;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
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
					        	        int level = WifiManager.calculateSignalLevel(scan.level, 30);
					        			
					        			WeightedScan wScan = wsFactory.Create(scan.BSSID);
					        			if(wScan != null)
					        			{
					        				wScan.SetLevel(level);
					        				
					        				count+=level;
					        				
						        			weightedScans.add(wScan);
					        			}
					        		}
					        		if(!weightedScans.isEmpty())//DEMO CODE - for display
					        		{
						        		textView.get().append("\n\n");
						        		for (WeightedScan wscan : weightedScans)
						        		{
						        			textView.get().append(wscan.Name() + " " + wscan.Num() + "/" + count + 
						        					"\n" + wscan.GetPos().get(0) + " " + wscan.GetPos().get(1) + "\n");
						        		}
						        		
					        		}
				        		}
				        		else
				        		{
				        			textView.get().append("\nScanning...");
				        		}
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
				loc[0] += scan.GetPos().get(0) * (double)scan.Num()/(double)count;
				loc[1] += scan.GetPos().get(1) * (double)scan.Num()/(double)count;
			}
			
			textView.get().append("\n\n( " + loc[0] + ",\n " + loc[1] + ")\n"); 			

			if(textView != null && textView.get() != null)
			{
				textView.get().setText(/*text + */"\n\n(" + loc[0] +", "+loc[1]+")\n");
			}
    	}
    	else
    	{

    	}
    }
}

