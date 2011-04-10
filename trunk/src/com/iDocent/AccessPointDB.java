package com.iDocent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//A database that will map an ssid to an xyz location
public class AccessPointDB {
	private HashMap<String, LinkedList<Float>> accessPoints;
	
	private ArrayList<String> macs;
	
    InetRLookup irl;
	
	private static final int x=0; 
	private static final int y=1; 
	private static final int z=2; 
	
	public AccessPointDB()
	{
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
		AddAccessPoint("00:24:6c:d0:77:00", 184.5f,16.4f,0f);
		AddAccessPoint("00:24:6c:d0:77:10", 184.5f,16.4f,0f);
		
		//1-08
		AddAccessPoint("00:24:6c:d0:76:a0", 131.5f,28.4f,0f);
		AddAccessPoint("00:24:6c:d0:76:b0", 131.5f,28.4f,0f);
		
		//1-09
		AddAccessPoint("00:24:6c:d0:79:00", 82.5f,28.4f,0f);
		AddAccessPoint("00:24:6c:d0:79:10", 82.5f,28.4f,0f);
		
		//1-10
		AddAccessPoint("00:24:6c:d0:76:c0", 23f,28.4f,0f);
		AddAccessPoint("00:24:6c:d0:76:d0", 23f,28.4f,0f);
		
		//1-11
		AddAccessPoint("00:24:6c:d0:78:60", -20f,28.4f,0f);
		AddAccessPoint("00:24:6c:d0:78:70", -20f,28.4f,0f);
		
		//1-33
		AddAccessPoint("00:24:6c:d5:c7:c0", 131.5f,72.4f,0f);
		AddAccessPoint("00:24:6c:d5:c7:d0", 131.5f,72.4f,0f);
		
		//1-34
		AddAccessPoint("00:24:6c:d0:7f:20", 131.5f,124.4f,0f);
		AddAccessPoint("00:24:6c:d0:7f:30", 131.5f,124.4f,0f);
		
		//1-35
		AddAccessPoint("00:24:6c:d0:6a:80", 131.5f,172.4f,0f);
		AddAccessPoint("00:24:6c:d0:6a:90", 131.5f,172.4f,0f);
		
		//1-36
		AddAccessPoint("00:24:6c:d0:84:20", 131.5f,220.4f,0f);
		AddAccessPoint("00:24:6c:d0:84:30", 131.5f,220.4f,0f);
		
		//AddAccessPoint("C0:C1:C0:45:BE:7D", 10, 10, 0);
	}
	
	/**
	 * Obtain the location of the user in the x direction
	                          
	{@link AddAccessPoint}. 
	 *
	                         
	@param  mac - the mac address of the access point
			X - the location of the access point in the x direction
			Y - the location of the access point in the y direction
			Z - the location of the access point in the z direction
	 *   
	 */
	private void AddAccessPoint(String mac, Float X, Float Y, Float Z)
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

	public void StartScanLoop() {
		irl.Connect();
		while(!irl.isConnected());
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
	}

	public void EndScanLoop() {
		irl.Disconnect();
	}
}
