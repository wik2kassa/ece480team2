package com.iDocent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

//A database that will map an ssid to an xyz location
public class AccessPointDB {
	private HashMap<String, LinkedList<Float>> accessPoints;
	private HashSet<String> notInDB;
	
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
		notInDB = new HashSet<String>();
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
//		//"00:14:d1:e9:f5:64"
//		AddAccessPoint("00:14:d1:e9:f5:63", 0f, 0f, 0f);//BLUE
//
//		//"00:14:d1:ea:06:c2"
//		AddAccessPoint("00:14:d1:ea:06:c1", 46f, 0f, 0f);//GREEN
//
//		//"00:14:d1:ea:06:96"
//		AddAccessPoint("00:14:d1:ea:06:95", 66f, 0f, 0f);//ORANGE
//
//		//"00:14:d1:e9:f5:6a"
//		AddAccessPoint("00:14:d1:e9:f5:69", 30f, 0f, 0f);//YELLOW
		
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
//		LinkedList<Float> loc = new LinkedList<Float>();
//		if(Y > 28.4)
//			X = (132f+120f)/2.0f;
//		else if(X < 120)
//			Y = (28.4f+16.4f)/2.0f;
//		else if(Y < 28.4 && X > 120)
//		{
//			X = (132f+120f)/2.0f;
//			Y = (28.4f+16.4f)/2.0f;
//		}
//		
//		loc.add(x, X);
//		loc.add(y, Y);
//		loc.add(z, Z);
//		accessPoints.put(mac.toLowerCase(), loc);
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
//		List<Float> pos = accessPoints.get(mac.toLowerCase());
//		if(pos != null)
//		{
//			return pos;
//		}
//		if(!notInDB.contains(mac))
//		{
//			irl.setMac("\'"+mac.toLowerCase()+"\'");
//			irl.run();
//			if(irl.WasFound())
//			{
//				Float[] coords = irl.getCoords();
//				
//				AddAccessPoint(mac.toLowerCase(), coords[x], coords[y], coords[z]);
//				return accessPoints.get(mac.toLowerCase());
//			}
//			else
//			{
//				notInDB.add(mac);
//			}
//		}
		
		//return null;
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
