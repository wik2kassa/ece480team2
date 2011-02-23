package com.iDocent;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

//A database that will map an ssid to an xyz location
public class AccessPointDB {
	private TreeMap<String, LinkedList<Integer>> accessPoints;
	
	private static int x=0; 
	private static int y=1; 
	private static int z=2; 
	
	public AccessPointDB()
	{
		accessPoints = new TreeMap<String, LinkedList<Integer>>();
		FillMap();
	}
	
	/*
	* Name : FillMap()
	* Description : Fill the map with ssid and xyz location sets
	* Parameters :  
	* Returns : void
	*/
	private void FillMap()
	{
		//"00:14:d1:e9:f5:64"
		AddAccessPoint("00:14:d1:e9:f5:63", 0, 0, 0);//BLUE

		//"00:14:d1:ea:06:c2"
		AddAccessPoint("00:14:d1:ea:06:c1", 46, 0, 0);//GREEN

		//"00:14:d1:ea:06:96"
		AddAccessPoint("00:14:d1:ea:06:95", 66, 0, 0);//ORANGE

		//"00:14:d1:e9:f5:6a"
		AddAccessPoint("00:14:d1:e9:f5:69", 30, 0, 0);//YELLOW
		
		//AddAccessPoint("C0:C1:C0:45:BE:7D", 10, 10, 0);
	}
	
	private void AddAccessPoint(String mac, int X, int Y, int Z)
	{
		LinkedList<Integer> loc = new LinkedList<Integer>();
		loc.add(x, X);
		loc.add(y, Y);
		loc.add(z, Z);
		accessPoints.put(mac.toLowerCase(), loc);
	}
	
	/*
	 * Name : GetLocation()
	 * Description : 
	 * Parameters : String ssid 
	 * Returns : List<Integer>
	 */
	public List<Integer> GetLocation(String mac)
	{
		List<Integer> pos = accessPoints.get(mac.toLowerCase());
		if(pos != null)
		{
			return pos;
		}
		
		return null;
	}
}
