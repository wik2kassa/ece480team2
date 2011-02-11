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
		LinkedList<Integer> loc = new LinkedList<Integer>();
		loc.add(x, 1);
		loc.add(y, 2);
		loc.add(z, 0);
		//"00:14:d1:e9:f5:64"
		accessPoints.put("00:14:d1:e9:f5:63", loc);//BLUE
		loc = new LinkedList<Integer>();
		loc.add(x, 3);
		loc.add(y, 4);
		loc.add(z, 0);
		//"00:14:d1:ea:06:c2"
		accessPoints.put("00:14:d1:ea:06:c1", loc);//GREEN
		loc = new LinkedList<Integer>();
		loc.add(x, 5);
		loc.add(y, 6);
		loc.add(z, 0);
		//"00:14:d1:ea:06:96"
		accessPoints.put("00:14:d1:ea:06:95", loc);//ORANGE
		loc = new LinkedList<Integer>();
		loc.add(x, 7);
		loc.add(y, 8);
		loc.add(z, 0);
		//"00:14:d1:e9:f5:6a"
		accessPoints.put("00:14:d1:e9:f5:69", loc);//YELLOW
		loc = new LinkedList<Integer>();
		loc.add(x, 9);
		loc.add(y, 10);
		loc.add(z, 0);
		accessPoints.put("00:1a:70:d7:3b:bf", loc);//Linksys
		loc = new LinkedList<Integer>();
		loc.add(x, 11);
		loc.add(y, 12);
		loc.add(z, 0);
		accessPoints.put("00:24:01:75:c8:b9", loc);//DLink
	}
	
	/*
	 * Name : GetLocation()
	 * Description : 
	 * Parameters : String ssid 
	 * Returns : List<Integer>
	 */
	public List<Integer> GetLocation(String mac)
	{
		List<Integer> pos = accessPoints.get(mac);
		if(pos != null)
		{
			return pos;
		}
		
		return null;
	}
}
