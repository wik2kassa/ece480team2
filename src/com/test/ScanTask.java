package com.test;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.TextView;

public class ScanTask extends TimerTask{
    WeakReference<TextView> mRef;
    WeakReference<TextView> mapView;
    WifiManager wifi;
    int counter = 0;
    Handler handler = new Handler();

    public ScanTask(TextView text, TextView map, WifiManager w) {
        mRef = new WeakReference<TextView>(text);
        mapView = new WeakReference<TextView>(map);
        wifi = w;
    }

    public void run() {
        handler.post(new Runnable() {
            public void run() {
                mRef.get().setText("WiFi Scanner");
                
        		if(wifi.isWifiEnabled()){	                
	        		if(wifi.startScan()){
	        			int count = 0;
	        			List<WeightedScan> weightedScans = new LinkedList<WeightedScan>();
		        		// List available networks
		        		List<ScanResult> scans = wifi.getScanResults();
		        		if(scans != null && !scans.isEmpty()){
		        			int number = 1;
			        		for (ScanResult scan : scans) {
			        	        int level = WifiManager.calculateSignalLevel(scan.level, 20);
		        				
			        			mRef.get().append("\n" + number + ": " + scan.SSID);
			        			mRef.get().append(" Strength " + level);
			        			
			        			WeightedScan wScan = new WeightedScan(scan.SSID);
		        				wScan.SetCount(level);
		        				wScan.number = number;
		        				number++;
		        				count+=level;
		        				
			        			weightedScans.add(wScan);
			        		}
			        		if(!weightedScans.isEmpty()){
				        		mRef.get().append("\n\n");
				        		for (WeightedScan wscan : weightedScans)
				        		{
				        			mRef.get().append(wscan.Name() + " " + wscan.Num() + "/" + count + "\n");
				        		}
				        		
			        		}
			        		Map(weightedScans, count);
		        		}
		        		else
		        		{
		        			mRef.get().append("\nScanning...");
		        		}
	        		}
        		}
        		else
        		{
        			mRef.get().append("\nActivating Wi-Fi");
        		}
            }
        });
    }
    
    private void Map(List<WeightedScan> scans, int count)
    {
    	double[] loc = {0,0};
    	if(scans.size() >= 0)
    	{
    		//Draw the "map"
    		char[][] map = new char[12][15];		
    		map[0]="______________\n".toCharArray();
    		for(int i=1;i<=10;i++)
    		{
    			map[i]="|------------|\n".toCharArray();
    		}
			map[11]="______________ ".toCharArray(); 
			
			int pos[] = {1,1,10,12,1,12,10,1};
			
			int num = 0;
			
			for(WeightedScan scan : scans)
			{	
				if(num < 8){
					loc[0] += pos[num] * (double)scan.Num()/(double)count;
					loc[1] += pos[num+1] * (double)scan.Num()/(double)count;
					map[pos[num]][pos[num+1]] = Character.forDigit(scan.number, 10);
					num += 2;
				}
			}
			
			if((int)loc[0] != 0 && (int)loc[1] != 0 && (int)loc[0] <= 10 && (int)loc[1] <= 12)
				map[(int)loc[0]][(int)loc[1]] = 'x';
			

			String text = new String();
			for(int i=0; i<12; i++)
			{
				for(int j=0; j<15; j++)
					text += map[i][j];
			}
    		mapView.get().setText(text);
    	}
    	else
    	{
    		mapView.get().setText("Not enough access points");
    	}
    }
}
