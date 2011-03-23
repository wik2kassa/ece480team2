package com.iDocent;

import java.util.LinkedList;
import java.util.List;

public class WeightedScanFactory {
	//A database that will map an ssid to an xyz location
	AccessPointDB locations;
	
	public WeightedScanFactory(){
		locations = new AccessPointDB();
	}
	
	public WeightedScan Create(String mac)
	{
		List<Float> loc = locations.GetLocation(mac);
		
		if(loc != null)
			return new WeightedScan(mac, loc);
		else 
			return null;
	}

	//Get a demo weighted scan that bypasses the map for location
	public WeightedScan Create(String mac, boolean demo) {
		List<Float> loc = new LinkedList<Float>();
		loc.add(0f);
		loc.add(0f);		
		loc.add(0f);
		return new WeightedScan(mac, loc);
	}
}
