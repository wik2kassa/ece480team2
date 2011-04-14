package com.iDocent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.widget.TextView;

//A database that will map an ssid to an xyz location
public class AccessPointDB {
	private HashMap<String, LinkedList<Float>> accessPoints;
	
	private ArrayList<String> macs;
	
    InetRLookup irl;
	
	private static final int x=0; 
	private static final int y=1; 
	private static final int z=2; 
	
	private iDocent miD;
	
	public AccessPointDB(iDocent miD)
	{
		this.miD = miD;
		macs = new ArrayList<String>();
		accessPoints = new HashMap<String, LinkedList<Float>>();
		irl = new InetRLookup();
		FillMap();
	}
	
	/**
	 * Fill the map with ssid and xyz location sets
	                          

import java.util.LinkedList;

//A database that will map an ssid to an xyz location
public class AccessPointDB {
	private TreeMap<String, LinkedList<Float>> accessPoints;
	
	private static int x=0; 
	private static int y=1; 
	private static int z=2; 
	
	public AccessPointDB()
	{
		accessPoints = new HashMap<String, Li
	{@link FillMap}. 
	 *
	                         
	@param  none
	 *   
	 */
	private void FillMap()
	{		
		//1-07
		AddAccessPoint("00:24:6c:d0:77:00");
		AddAccessPoint("00:24:6c:d0:77:10");
		
		//1-08
		AddAccessPoint("00:24:6c:d0:76:a0");
		AddAccessPoint("00:24:6c:d0:76:b0");
		
		//1-09
		AddAccessPoint("00:24:6c:d0:79:00");
		AddAccessPoint("00:24:6c:d0:79:10");
		
		//1-10
		AddAccessPoint("00:24:6c:d0:76:c0");
		AddAccessPoint("00:24:6c:d0:76:d0");
		
		//1-11
		AddAccessPoint("00:24:6c:d0:78:60");
		AddAccessPoint("00:24:6c:d0:78:70");
		
		//1-33
		AddAccessPoint("00:24:6c:d5:c7:c0");
		AddAccessPoint("00:24:6c:d5:c7:d0");
		
		//1-34
		AddAccessPoint("00:24:6c:d0:7f:20");
		AddAccessPoint("00:24:6c:d0:7f:30");
		
		//1-35
		AddAccessPoint("00:24:6c:d0:6a:80");
		AddAccessPoint("00:24:6c:d0:6a:90");
		
		//1-36
		AddAccessPoint("00:24:6c:d0:84:20");
		AddAccessPoint("00:24:6c:d0:84:30");
		
		//AddAccessPoint("C0:C1:C0:45:BE:7D", 10, 10, 0);
	}
	
	/**
	 * Obtain the location of the user in the x direction
	                          
	{@link AddAccessPoint}. 
	 *
	                         
	@param  mac - the mac address of the access point
	 *   
	 */
	private void AddAccessPoint(String mac)
	{
		macs.add(mac.toLowerCase());
	}
	
	/**
	 * Obtain the location of an access point given the mac address
	                          
	{@link GetLocation}. 
	 *
	                         
	@param  mac - the MAC address of the desired access point
	
	@return List<Integer> - the access point's location stored in a list (x y z)
	 *   
	 */
	public List<Float> GetLocation(String mac)
	{
		return accessPoints.get(mac.toLowerCase());
	}

	public boolean StartScanLoop() {		
		//while(!irl.isConnected());
		if(!irl.isConnected())
			return false;
		for(String s : macs)
		{
			irl.setMac("\'"+s+"\'");
			irl.run();
			if(irl.WasFound())
			{
				Float[] coords = irl.getCoords();
				
				LinkedList<Float> loc = new LinkedList<Float>();				
				loc.add(x, coords[x]);
				loc.add(y, coords[y]);
				loc.add(z, coords[z]);
				accessPoints.put(s.toLowerCase(), loc);
			}
		}
		irl.Disconnect();
		return true;
	}

	public void EndScanLoop() {
		irl.Disconnect();
	}

	public boolean Connect() {
		irl.Connect();
		return irl.isConnected();
	}
}
