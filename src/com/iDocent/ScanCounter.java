/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

/**An object to add up and average the AP locations*/
public class ScanCounter implements Runnable {
	private List<ScanResult> scans;
	private WeightedScanFactory wsFactory;
	private ScanResultReceiver SRR;
	
	private float sumX = 0;
	private float sumY = 0;
	private float sumZ = 0;
	private int count = 0;
	
	private static int x=0; 
	private static int y=1; 
	private static int z=2;
	
	public ScanCounter(ScanResultReceiver scanResultReceiver, List<ScanResult> s, WeightedScanFactory wsF) {
		scans = s;
		wsFactory = wsF;
		SRR = scanResultReceiver;
	}
	
	/**
	 * This is run when the thread is started.  It runs through the list of APs in range
	 * and determines the location of the user.
	 */
	@Override
	public void run() {
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
					sumZ += level*wScan.GetPos().get(z);
					count+=level;
				}
			}
			
			SRR.UpdateSums(sumX, sumY, sumZ, count);
		}
	}
}
