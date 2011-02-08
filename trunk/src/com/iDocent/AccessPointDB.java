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
		loc.add(x, 23);
		loc.add(y, 26);
		loc.add(z, 0);
		accessPoints.put("00:1a:70:d7:3b:bf", loc);
		loc = new LinkedList<Integer>();
		loc.add(x, 27);
		loc.add(y, 2);
		loc.add(z, 0);
		accessPoints.put("00:1d:7e:70:36:bd", loc);
		loc = new LinkedList<Integer>();
		loc.add(x, 23);
		loc.add(y, 0);
		loc.add(z, 0);
		accessPoints.put("00:24:01:75:c8:b9", loc);
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
