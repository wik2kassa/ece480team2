package com.iDocent;

import java.util.LinkedList;
import java.util.List;

public class WeightedScanFactory {
	//A database that will map an ssid to an xyz location
	AccessPointDB locations;
	
	public WeightedScanFactory(){
		locations = new AccessPointDB();
	}
	
	public WeightedScan Create(String ssid)
	{
		List<Integer> loc = locations.GetLocation(ssid);
		
		if(loc != null)
			return new WeightedScan(ssid, locations.GetLocation(ssid));
		else 
			return null;
	}

	//Get a demo weighted scan that bypasses the map for location
	public WeightedScan Create(String ssid, boolean demo) {
		List<Integer> loc = new LinkedList<Integer>();
		loc.add(0);
		loc.add(0);		
		loc.add(0);
		return new WeightedScan(ssid, loc);
	}
}
